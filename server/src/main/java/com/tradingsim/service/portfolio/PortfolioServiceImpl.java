package com.tradingsim.service.portfolio;

import com.tradingsim.config.MoneyConfig;
import com.tradingsim.exception.ConflictException;
import com.tradingsim.exception.InsufficientFundsException;
import com.tradingsim.exception.NotFoundException;
import com.tradingsim.exception.ValidationException;
import com.tradingsim.model.Asset;
import com.tradingsim.model.Portfolio;
import com.tradingsim.model.PortfolioAsset;
import com.tradingsim.model.PriceHistory;
import com.tradingsim.repository.AssetRepository;
import com.tradingsim.repository.PortfolioRepository;
import com.tradingsim.repository.PriceHistoryRepository;
import com.tradingsim.repository.UserRepository;

import java.math.BigDecimal;
import java.util.Comparator;
import java.util.List;

public class PortfolioServiceImpl implements PortfolioService {

    private final PortfolioRepository portfolioRepository;
    private final AssetRepository assetRepository;
    private final PriceHistoryRepository priceHistoryRepository;
    private final UserRepository userRepository;

    public PortfolioServiceImpl(
            PortfolioRepository portfolioRepository,
            AssetRepository assetRepository,
            PriceHistoryRepository priceHistoryRepository,
            UserRepository userRepository
    ) {
        this.portfolioRepository = portfolioRepository;
        this.assetRepository = assetRepository;
        this.priceHistoryRepository = priceHistoryRepository;
        this.userRepository = userRepository;
    }

    @Override
    public Portfolio createPortfolio(int userId, BigDecimal startSum) {
        if (userId <= 0) {
            throw new ValidationException("User id must be positive");
        }

        if (userRepository.findById(userId).isEmpty()) {
            throw new NotFoundException(
                    "User does not exist"
            );
        }

        if (startSum == null || startSum.compareTo(BigDecimal.ZERO) < 0) {
            throw new ValidationException(
                    "Start sum must not be negative"
            );
        }

        if (portfolioRepository.findByUserId(userId).isPresent()) {
            throw new ConflictException(
                    "User already has portfolio"
            );
        }

        Portfolio portfolio = new Portfolio();
        portfolio.setUserId(userId);
        portfolio.setStartSum(startSum);
        portfolio.setTotalSum(startSum);
        portfolio.setCashBalance(startSum);

        return portfolioRepository.create(portfolio);
    }

    @Override
    public Portfolio save(Portfolio portfolio) {
        return portfolioRepository.update(portfolio);
    }

    @Override
    public Portfolio getPortfolioByUserId(int userId) {
        return portfolioRepository.findByUserId(userId)
                .orElseThrow(() ->
                        new NotFoundException(
                                "Portfolio for user id " +
                                        userId +
                                        " does not exist"
                        ));
    }

    @Override
    public List<PortfolioAsset> getPortfolioAssets(int userId) {
        return List.copyOf(
                getPortfolioByUserId(userId).getPortfolioAssets()
        );
    }

    @Override
    public synchronized void addAsset(
            int userId,
            int assetId,
            BigDecimal amount
    ) {
        if (amount == null || amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new ValidationException(
                    "Amount must be positive"
            );
        }

        Portfolio portfolio = getPortfolioByUserId(userId);

        Asset asset = assetRepository.findById(assetId)
                .orElseThrow(() ->
                        new NotFoundException(
                                "Asset with id " +
                                        assetId +
                                        " does not exist"
                        ));

        PortfolioAsset existingAsset = portfolio.getPortfolioAssets()
                .stream()
                .filter(pa -> pa.getAssetId() == asset.getId())
                .findFirst()
                .orElse(null);

        if (existingAsset != null) {
            existingAsset.setAmount(
                    existingAsset.getAmount().add(amount)
            );
        } else {
            PortfolioAsset portfolioAsset = new PortfolioAsset();
            portfolioAsset.setAssetId(assetId);
            portfolioAsset.setAmount(amount);

            portfolio.getPortfolioAssets()
                    .add(portfolioAsset);
        }

        recalculateTotalSum(portfolio);
        portfolioRepository.update(portfolio);
    }

    @Override
    public synchronized void removeAsset(
            int userId,
            int assetId,
            BigDecimal amount
    ) {
        if (amount == null || amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new ValidationException(
                    "Amount must be positive"
            );
        }

        Portfolio portfolio = getPortfolioByUserId(userId);

        PortfolioAsset existingAsset = portfolio.getPortfolioAssets()
                .stream()
                .filter(pa -> pa.getAssetId() == assetId)
                .findFirst()
                .orElseThrow(() ->
                        new NotFoundException(
                                "Asset not found in portfolio"
                        ));

        if (existingAsset.getAmount().compareTo(amount) < 0) {
            throw new InsufficientFundsException(
                    "Not enough assets to remove"
            );
        }

        existingAsset.setAmount(existingAsset.getAmount().subtract(amount));

        if (existingAsset.getAmount().compareTo(BigDecimal.ZERO) <= 0) {
            portfolio.getPortfolioAssets().remove(existingAsset);
        }

        recalculateTotalSum(portfolio);
        portfolioRepository.update(portfolio);
    }

    @Override
    public synchronized void decreaseCash(int userId, BigDecimal amount) {
        if (amount == null || amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new ValidationException(
                    "Amount must be positive"
            );
        }

        Portfolio portfolio = getPortfolioByUserId(userId);

        if (portfolio.getCashBalance().compareTo(amount) < 0) {
            throw new InsufficientFundsException(
                    "Not enough cash balance"
            );
        }

        portfolio.setCashBalance(
                portfolio.getCashBalance().subtract(amount)
        );
        portfolioRepository.update(portfolio);
    }

    @Override
    public synchronized void increaseCash(int userId, BigDecimal amount) {
        if (amount == null || amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new ValidationException("Amount must be positive");
        }

        Portfolio portfolio = getPortfolioByUserId(userId);

        portfolio.setCashBalance(portfolio.getCashBalance().add(amount));

        portfolioRepository.update(portfolio);
    }

    private void recalculateTotalSum(
            Portfolio portfolio
    ) {
        BigDecimal total = portfolio.getCashBalance();

        for (PortfolioAsset portfolioAsset :
                portfolio.getPortfolioAssets()) {
            List<PriceHistory> history =
                    priceHistoryRepository.findByAssetId(
                            portfolioAsset.getAssetId()
                    );

            if (history.isEmpty()) {
                continue;
            }

            PriceHistory latestPrice = history.stream()
                    .max(Comparator.comparing(PriceHistory::getDateTime))
                    .orElseThrow(() ->
                            new NotFoundException(
                                    "No price history for asset"
                            ));

            BigDecimal assetValue = latestPrice.getPrice().multiply(portfolioAsset.getAmount())
                    .setScale(
                            MoneyConfig.MONEY_SCALE,
                            MoneyConfig.ROUNDING_MODE
                    );

            total = total.add(assetValue);
        }

        portfolio.setTotalSum(total);
    }
}
