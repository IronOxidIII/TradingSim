package com.tradingsim.service.trading;

import com.tradingsim.config.MoneyConfig;
import com.tradingsim.exception.ValidationException;
import com.tradingsim.model.Portfolio;
import com.tradingsim.model.Transaction;
import com.tradingsim.model.TransactionType;
import com.tradingsim.repository.TransactionRepository;
import com.tradingsim.repository.PortfolioRepository;
import com.tradingsim.service.market.MarketService;
import com.tradingsim.service.portfolio.PortfolioService;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.concurrent.ConcurrentHashMap;

public class TradingServiceImpl implements TradingService {
    private final PortfolioService portfolioService;
    private final MarketService marketService;
    private final PortfolioRepository portfolioRepository;
    private final TransactionRepository transactionRepository;

    private final ConcurrentHashMap<Integer, Object> userLocks = new ConcurrentHashMap<>();

    private Object getLock(int userId) {
        return userLocks.computeIfAbsent(userId, id -> new Object());
    }

    public TradingServiceImpl(
            PortfolioService portfolioService,
            MarketService marketService,
            PortfolioRepository portfolioRepository,
            TransactionRepository transactionRepository
    ) {
        this.portfolioService = portfolioService;
        this.marketService = marketService;
        this.portfolioRepository = portfolioRepository;
        this.transactionRepository = transactionRepository;
    }

    private void saveTransaction(
            int userId,
            int assetId,
            BigDecimal assetPrice,
            BigDecimal amount,
            TransactionType type
    ) {
        BigDecimal sum = assetPrice
                .multiply(amount)
                .setScale(
                        MoneyConfig.MONEY_SCALE,
                        MoneyConfig.ROUNDING_MODE
                );

        Transaction transaction = new Transaction();

        transaction.setUserId(userId);
        transaction.setAssetId(assetId);
        transaction.setSum(sum);
        transaction.setAssetPrice(assetPrice);
        transaction.setAmount(amount);
        transaction.setDateTime(LocalDateTime.now());
        transaction.setType(type);

        transactionRepository.create(transaction);
    }

    @Override
    public void buyAsset(int userId, int assetId, BigDecimal amount) {

        if (amount == null
                || amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new ValidationException("Amount must be positive");
        }

        synchronized (getLock(userId)) {

            BigDecimal price = marketService.getCurrentPrice(assetId);

            Portfolio portfolio = portfolioService.getPortfolioByUserId(userId);
            portfolio.buyAsset(assetId, amount, price);

            portfolioRepository.update(portfolio);
            saveTransaction(userId, assetId, price, amount, TransactionType.BUY);
        }
    }

    @Override
    public void sellAsset(int userId, int assetId, BigDecimal amount) {

        if (amount == null
                || amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new ValidationException("Amount must be positive");
        }

        synchronized (getLock(userId)) {

            BigDecimal price = marketService.getCurrentPrice(assetId);

            Portfolio portfolio = portfolioService.getPortfolioByUserId(userId);
            portfolio.sellAsset(assetId, amount, price);

            portfolioRepository.update(portfolio);
            saveTransaction(userId, assetId, price, amount, TransactionType.SELL);
        }
    }
}
