package com.tradingsim.initialize;
import com.tradingsim.service.market.MarketService;
import com.tradingsim.service.market.MarketServiceImpl;
import com.tradingsim.service.portfolio.PortfolioService;
import com.tradingsim.service.portfolio.PortfolioServiceImpl;
import com.tradingsim.service.statistics.StatisticService;
import com.tradingsim.service.statistics.StatisticServiceImpl;
import com.tradingsim.service.trading.TradingService;
import com.tradingsim.service.trading.TradingServiceImpl;

import java.util.logging.Logger;

public class StartUpInitializer {

    private static final Logger log = Logger.getLogger(StartUpInitializer.class.getName());

    private ApplicationContext applicationContext;

    public void Initialize() {
        log.info("Initializing server components.");
        applicationContext = new ApplicationContext();

        log.info("Initializing services...");
        MarketService marketService = new MarketServiceImpl(
                applicationContext.getAssetRepository(),
                applicationContext.getPriceHistoryRepository()
        );

        PortfolioService portfolioService = new PortfolioServiceImpl(
                applicationContext.getPortfolioRepository(),
                applicationContext.getAssetRepository(),
                applicationContext.getPriceHistoryRepository(),
                applicationContext.getUserRepository()
        );

        TradingService tradingService = new TradingServiceImpl(
                portfolioService,
                marketService,
                applicationContext.getPortfolioRepository(),
                applicationContext.getTransactionRepository()
        );

        StatisticService statisticService = new StatisticServiceImpl(
                portfolioService,
                marketService,
                applicationContext.getTransactionRepository()
        );

        log.info("Services initialized.");
        log.info("Starting Server.");

        ConfigInitializer configInitializer = new ConfigInitializer();

        configInitializer.initializeTestData(
                applicationContext.getAssetRepository(),
                applicationContext.getPortfolioRepository(),
                applicationContext.getPriceHistoryRepository(),
                applicationContext.getTransactionRepository(),
                applicationContext.getUserRepository()
        );

        HttpServerInitializer httpServerInitializer = new HttpServerInitializer(
                applicationContext.getAssetRepository(),
                applicationContext.getPriceHistoryRepository(),
                applicationContext.getPortfolioRepository(),
                configInitializer.getPort()
        );
        httpServerInitializer.Initialize();
    }
}
