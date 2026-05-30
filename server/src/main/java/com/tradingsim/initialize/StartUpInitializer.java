package com.tradingsim.initialize;

import com.tradingsim.model.Asset;
import com.tradingsim.model.User;
import com.tradingsim.repository.AssetRepository;
import com.tradingsim.repository.AssetRepositoryImpl;
import com.tradingsim.repository.PortfolioRepository;
import com.tradingsim.repository.PortfolioRepositoryImpl;
import com.tradingsim.repository.PriceHistoryRepository;
import com.tradingsim.repository.PriceHistoryRepositoryImpl;
import com.tradingsim.repository.TransactionRepository;
import com.tradingsim.repository.TransactionRepositoryImpl;
import com.tradingsim.repository.UserRepository;
import com.tradingsim.repository.UserRepositoryImpl;
import com.tradingsim.service.market.MarketService;
import com.tradingsim.service.market.MarketServiceImpl;
import com.tradingsim.service.portfolio.PortfolioService;
import com.tradingsim.service.portfolio.PortfolioServiceImpl;
import com.tradingsim.service.statistics.StatisticService;
import com.tradingsim.service.statistics.StatisticServiceImpl;
import com.tradingsim.service.trading.TradingService;
import com.tradingsim.service.trading.TradingServiceImpl;

import java.math.BigDecimal;
import java.util.logging.Logger;

public class StartUpInitializer {

    private UserRepository userRepository;
    private AssetRepositoryImpl assetRepository;
    private PortfolioRepositoryImpl portfolioRepository;
    private PriceHistoryRepository priceHistoryRepository;
    private TransactionRepository transactionRepository;

    private static final Logger log = Logger.getLogger(StartUpInitializer.class.getName());

    public UserRepository getUserRepository() {
        return this.userRepository;
    }

    public void setUserRepository(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public AssetRepositoryImpl getAssetRepository() {
        return assetRepository;
    }

    public void setAssetRepository(AssetRepositoryImpl assetRepository) {
        this.assetRepository = assetRepository;
    }

    public PortfolioRepositoryImpl getPortfolioRepository() {
        return portfolioRepository;
    }

    public void setPortfolioRepository(PortfolioRepositoryImpl portfolioRepository) {
        this.portfolioRepository = portfolioRepository;
    }

    public PriceHistoryRepository getPriceHistoryRepository() {
        return priceHistoryRepository;
    }

    public void setPriceHistoryRepository(PriceHistoryRepository priceHistoryRepository) {
        this.priceHistoryRepository = priceHistoryRepository;
    }

    public TransactionRepository getTransactionRepository() {
        return transactionRepository;
    }

    public void setTransactionRepository(TransactionRepository transactionRepository) {
        this.transactionRepository = transactionRepository;
    }

    public void Initialize() {
        userRepository = new UserRepositoryImpl();
        assetRepository = new AssetRepositoryImpl();
        portfolioRepository = new PortfolioRepositoryImpl();
        priceHistoryRepository = new PriceHistoryRepositoryImpl();
        transactionRepository = new TransactionRepositoryImpl();

        MarketService marketService = new MarketServiceImpl(
                assetRepository,
                priceHistoryRepository
        );

        PortfolioService portfolioService = new PortfolioServiceImpl(
                portfolioRepository,
                assetRepository,
                priceHistoryRepository,
                userRepository
        );

        TradingService tradingService = new TradingServiceImpl(
                portfolioService,
                marketService,
                portfolioRepository,
                transactionRepository
        );

        StatisticService statisticService = new StatisticServiceImpl(
                portfolioService,
                marketService,
                transactionRepository
        );

        User user = new User();
        user.setUsername("Test User");
        userRepository.create(user);

        Asset btc = new Asset();
        btc.setName("BTC");
        assetRepository.create(btc);

        Asset eth = new Asset();
        eth.setName("ETH");
        assetRepository.create(eth);

        marketService.createInitialPrice(1);
        marketService.createInitialPrice(2);

        portfolioService.createPortfolio(1, BigDecimal.valueOf(10_000));

        tradingService.buyAsset(1, 1, BigDecimal.valueOf(2));
        tradingService.buyAsset(1, 2, BigDecimal.valueOf(5));

        marketService.refreshAllPrices();

        //tradingService.sellAsset(1, 1, BigDecimal.valueOf(1));

        marketService.refreshAllPrices();

        tradingService.sellAsset(1, 1, BigDecimal.valueOf(1));

        log.info(() -> "PNL: " + statisticService.getPnL(1));
        log.info(() -> "RDI: " + statisticService.getRdi(1));

        log.info("Done.");
    }
}
