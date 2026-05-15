package com.tradingsim.service.portfolio;

import com.tradingsim.model.Portfolio;
import com.tradingsim.model.Asset;
import com.tradingsim.model.PortfolioAsset;
import com.tradingsim.model.PriceHistory;
import com.tradingsim.model.User;
import com.tradingsim.repository.PortfolioRepository;
import com.tradingsim.repository.AssetRepository;
import com.tradingsim.repository.PriceHistoryRepository;
import com.tradingsim.repository.UserRepository;

import java.util.Comparator;
import java.util.List;

public class PortfolioService {

    private final PortfolioRepository portfolioRepository;
    private final AssetRepository assetRepository;
    private final PriceHistoryRepository priceHistoryRepository;
    private final UserRepository userRepository;

    public PortfolioService(
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

    public Portfolio createPortfolio(int userId, int startSum) {
        if (userId <= 0) {
            throw new IllegalArgumentException("User id must be positive");
        }

        if (userRepository.findById(userId).isEmpty()) {
            throw new IllegalArgumentException(
                    "User does not exist"
            );
        }

        if (startSum < 0) {
            throw new IllegalArgumentException(
                    "Start sum must not be negative"
            );
        }

        if (portfolioRepository.findByUserId(userId).isPresent()) {
            throw new IllegalArgumentException(
                    "User already has portfolio"
            );
        }

        Portfolio portfolio = new Portfolio();
        portfolio.setUserId(userId);
        portfolio.setStartSum(startSum);
        portfolio.setTotalSum(startSum);
        portfolio.setCashBalance(startSum);

        return portfolioRepository.save(portfolio);
    }

    public Portfolio getPortfolioByUserId(int userId) {
        return portfolioRepository.findByUserId(userId)
                .orElseThrow(() ->
                        new IllegalArgumentException(
                                "Portfolio for user id " +
                                        userId +
                                        " does not exist"
                        ));
    }

    public List<PortfolioAsset> getPortfolioAssets(int userId) {
        return List.copyOf(
                getPortfolioByUserId(userId).getPortfolioAssets()
        );
    }

    public synchronized void addAsset(
            int userId,
            int assetId,
            double amount
    ) {
        if (amount <= 0) {
            throw new IllegalArgumentException(
                    "Amount must be positive"
            );
        }

        Portfolio portfolio = getPortfolioByUserId(userId);

        Asset asset = assetRepository.findById(assetId)
                .orElseThrow(() ->
                        new IllegalArgumentException(
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
                    existingAsset.getAmount() + amount
            );
        } else {
            PortfolioAsset portfolioAsset = new PortfolioAsset();
            portfolioAsset.setAssetId(assetId);
            portfolioAsset.setAmount(amount);

            portfolio.getPortfolioAssets()
                    .add(portfolioAsset);
        }

        recalculateTotalSum(portfolio);
        portfolioRepository.save(portfolio);
    }

    public synchronized void removeAsset(
            int userId,
            int assetId,
            double amount
    ) {
        if (amount <= 0) {
            throw new IllegalArgumentException(
                    "Amount must be positive"
            );
        }

        Portfolio portfolio = getPortfolioByUserId(userId);

        PortfolioAsset existingAsset = portfolio.getPortfolioAssets()
                .stream()
                .filter(pa -> pa.getAssetId() == assetId)
                .findFirst()
                .orElseThrow(() ->
                        new IllegalArgumentException(
                                "Asset not found in portfolio"
                        ));

        if (existingAsset.getAmount() < amount) {
            throw new IllegalArgumentException(
                    "Not enough assets to remove"
            );
        }

        existingAsset.setAmount(existingAsset.getAmount() - amount);

        if (existingAsset.getAmount() <= 0) {
            portfolio.getPortfolioAssets().remove(existingAsset);
        }

        recalculateTotalSum(portfolio);
        portfolioRepository.save(portfolio);
    }

    public synchronized void decreaseCash(int userId, double amount) {
        if (amount <= 0) {
            throw new IllegalArgumentException(
                    "Amount must be positive"
            );
        }

        Portfolio portfolio = getPortfolioByUserId(userId);

        if (portfolio.getCashBalance() < amount) {
            throw new IllegalArgumentException(
                    "Not enough cash balance"
            );
        }

        portfolio.setCashBalance(portfolio.getCashBalance() - amount);
        portfolioRepository.save(portfolio);
    }

    public synchronized void increaseCash(int userId, double amount) {
        if (amount <= 0) {
            throw new IllegalArgumentException("Amount must be positive");
        }

        Portfolio portfolio = getPortfolioByUserId(userId);

        portfolio.setCashBalance(portfolio.getCashBalance() + amount);

        portfolioRepository.save(portfolio);
    }

    private void recalculateTotalSum(
            Portfolio portfolio
    ) {
        double total = portfolio.getCashBalance();

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
                            new IllegalStateException(
                                    "No price history for asset"
                            ));

            total += latestPrice.getPrice() * portfolioAsset.getAmount();
        }

        portfolio.setTotalSum(total);
    }
}
