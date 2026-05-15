package com.tradingsim.service.trading;

import com.tradingsim.model.Transaction;
import com.tradingsim.model.TransactionType;
import com.tradingsim.service.portfolio.PortfolioService;
import com.tradingsim.service.market.MarketService;
import com.tradingsim.repository.TransactionRepository;

import java.time.LocalDateTime;

public class TradingService {

    private final PortfolioService portfolioService;
    private final MarketService marketService;
    private final TradeValidationService tradeValidationService;
    private final TransactionRepository transactionRepository;

    public TradingService(
            PortfolioService portfolioService,
            MarketService marketService,
            TradeValidationService tradeValidationService,
            TransactionRepository transactionRepository
    ) {
        this.portfolioService = portfolioService;
        this.marketService = marketService;
        this.tradeValidationService = tradeValidationService;
        this.transactionRepository = transactionRepository;
    }

    private void saveTransaction(
            int userId,
            int assetId,
            double assetPrice,
            double amount,
            TransactionType type
    ) {
        double tradePrice = amount * assetPrice;

        Transaction transaction = new Transaction();

        transaction.setUserId(userId);
        transaction.setAssetId(assetId);
        transaction.setSum(tradePrice);
        transaction.setAssetPrice(assetPrice);
        transaction.setAmount(amount);
        transaction.setDateTime(LocalDateTime.now());
        transaction.setType(type);

        transactionRepository.save(transaction);
    }

    public synchronized void buyAsset(
            int userId,
            int assetId,
            double amount
    ) {
        if (amount <= 0) {
            throw new IllegalArgumentException(
                    "Amount must be positive"
            );
        }

        double currentPrice = marketService.getCurrentPrice(assetId);
        double totalCost = currentPrice * amount;

        tradeValidationService.validateBuy(
                userId,
                assetId,
                amount,
                totalCost
        );

        portfolioService.decreaseCash(userId, totalCost);
        portfolioService.addAsset(userId,assetId,amount);

        saveTransaction(
                userId,
                assetId,
                currentPrice,
                amount,
                TransactionType.BUY
        );
    }

    public synchronized void sellAsset(
            int userId,
            int assetId,
            double amount
    ) {
        if (amount <= 0) {
            throw new IllegalArgumentException(
                    "Amount must be positive"
            );
        }

        double currentPrice = marketService.getCurrentPrice(assetId);
        double totalCost = currentPrice * amount;

        tradeValidationService.validateSell(
                userId,
                assetId,
                amount
        );

        portfolioService.increaseCash(userId, totalCost);
        portfolioService.removeAsset(userId, assetId, amount);

        saveTransaction(
                userId,
                assetId,
                currentPrice,
                amount,
                TransactionType.SELL
        );
    }
}
