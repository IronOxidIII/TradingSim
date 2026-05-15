package com.tradingsim.service.trading;

import com.tradingsim.model.Portfolio;
import com.tradingsim.model.PortfolioAsset;
import com.tradingsim.repository.AssetRepository;
import com.tradingsim.repository.UserRepository;
import com.tradingsim.service.portfolio.PortfolioService;

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
            double amount
    ) {
        if (userId <= 0) {
            throw new IllegalArgumentException(
                    "User id must be positive"
            );
        }

        if (assetId <= 0) {
            throw new IllegalArgumentException(
                    "Asset id must be positive"
            );
        }

        if (amount <= 0) {
            throw new IllegalArgumentException(
                    "Amount must be positive"
            );
        }

        if (userRepository.findById(userId).isEmpty()) {
            throw new IllegalArgumentException(
                    "User does not exist"
            );
        }

        if (assetRepository.findById(assetId).isEmpty()) {
            throw new IllegalArgumentException(
                    "Asset does not exist"
            );
        }
    }

    public void validateBuy(
            int userId,
            int assetId,
            double amount,
            double totalCost
    ) {
        validateCommon(userId, assetId, amount);

        if (totalCost <= 0) {
            throw new IllegalArgumentException(
                    "Total cost must be positive"
            );
        }

        Portfolio portfolio = portfolioService.getPortfolioByUserId(userId);

        if (portfolio.getCashBalance() < totalCost) {
            throw new IllegalArgumentException("Not enough cash");
        }
    }

    public void validateSell(
            int userId,
            int assetId,
            double amount
    ) {
        validateCommon(userId, assetId, amount);

        Portfolio portfolio = portfolioService.getPortfolioByUserId(userId);

        PortfolioAsset portfolioAsset = portfolio.getPortfolioAssets()
                .stream()
                .filter(pa -> pa.getAssetId() == assetId)
                .findFirst()
                .orElseThrow(() ->
                        new IllegalArgumentException(
                                "Asset is not in portfolio"
                        )
                );

        if (portfolioAsset.getAmount() < amount) {
            throw new IllegalArgumentException(
                    "Not enough asset quantity"
            );
        }
    }
}
