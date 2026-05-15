package com.tradingsim.service.trading;

import com.tradingsim.exception.InsufficientFundsException;
import com.tradingsim.exception.NotFoundException;
import com.tradingsim.exception.TradingException;
import com.tradingsim.exception.ValidationException;
import com.tradingsim.model.Portfolio;
import com.tradingsim.model.PortfolioAsset;
import com.tradingsim.repository.AssetRepository;
import com.tradingsim.repository.UserRepository;
import com.tradingsim.service.portfolio.PortfolioService;

import java.math.BigDecimal;

public class TradeValidationService {

    private final PortfolioService portfolioService;
    private final UserRepository userRepository;
    private final AssetRepository assetRepository;

    public TradeValidationService(
            PortfolioService portfolioService,
            UserRepository userRepository,
            AssetRepository assetRepository
    ) {
        this.portfolioService = portfolioService;
        this.userRepository = userRepository;
        this.assetRepository = assetRepository;
    }

    private void validateCommon(
            int userId,
            int assetId,
            BigDecimal amount
    ) {
        if (userId <= 0) {
            throw new ValidationException(
                    "User id must be positive"
            );
        }

        if (assetId <= 0) {
            throw new ValidationException(
                    "Asset id must be positive"
            );
        }

        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new ValidationException(
                    "Amount must be positive"
            );
        }

        if (userRepository.findById(userId).isEmpty()) {
            throw new NotFoundException(
                    "User does not exist"
            );
        }

        if (assetRepository.findById(assetId).isEmpty()) {
            throw new NotFoundException(
                    "Asset does not exist"
            );
        }
    }

    public void validateBuy(
            int userId,
            int assetId,
            BigDecimal amount,
            BigDecimal totalCost
    ) {
        validateCommon(userId, assetId, amount);

        if (totalCost.compareTo(BigDecimal.ZERO) <= 0) {
            throw new ValidationException(
                    "Total cost must be positive"
            );
        }

        Portfolio portfolio = portfolioService.getPortfolioByUserId(userId);

        if (portfolio.getCashBalance().compareTo(totalCost) < 0) {
            throw new InsufficientFundsException("Not enough cash");
        }
    }

    public void validateSell(
            int userId,
            int assetId,
            BigDecimal amount
    ) {
        validateCommon(userId, assetId, amount);

        Portfolio portfolio = portfolioService.getPortfolioByUserId(userId);

        PortfolioAsset portfolioAsset = portfolio.getPortfolioAssets()
                .stream()
                .filter(pa -> pa.getAssetId() == assetId)
                .findFirst()
                .orElseThrow(() ->
                        new NotFoundException(
                                "Asset is not in portfolio"
                        )
                );

        if (portfolioAsset.getAmount().compareTo(amount) < 0) {
            throw new ValidationException(
                    "Not enough asset quantity"
            );
        }
    }
}
