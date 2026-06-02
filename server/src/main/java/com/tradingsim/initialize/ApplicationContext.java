package com.tradingsim.initialize;

import com.tradingsim.repository.AssetRepositoryImpl;
import com.tradingsim.repository.PortfolioRepositoryImpl;
import com.tradingsim.repository.PriceHistoryRepository;
import com.tradingsim.repository.PriceHistoryRepositoryImpl;
import com.tradingsim.repository.TransactionRepository;
import com.tradingsim.repository.TransactionRepositoryImpl;
import com.tradingsim.repository.UserRepository;
import com.tradingsim.repository.UserRepositoryImpl;

public class ApplicationContext {
    private final UserRepository userRepository;
    private final AssetRepositoryImpl assetRepository;
    private final PortfolioRepositoryImpl portfolioRepository;
    private final PriceHistoryRepository priceHistoryRepository;
    private final TransactionRepository transactionRepository;

    public ApplicationContext() {
        userRepository = new UserRepositoryImpl();
        assetRepository = new AssetRepositoryImpl();
        portfolioRepository = new PortfolioRepositoryImpl();
        priceHistoryRepository = new PriceHistoryRepositoryImpl();
        transactionRepository = new TransactionRepositoryImpl();
    }

    public UserRepository getUserRepository() {
        return userRepository;
    }

    public AssetRepositoryImpl getAssetRepository() {
        return assetRepository;
    }

    public PortfolioRepositoryImpl getPortfolioRepository() {
        return portfolioRepository;
    }

    public PriceHistoryRepository getPriceHistoryRepository() {
        return priceHistoryRepository;
    }

    public TransactionRepository getTransactionRepository() {
        return transactionRepository;
    }
}
