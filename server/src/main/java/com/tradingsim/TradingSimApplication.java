package com.tradingsim;

import com.tradingsim.initialize.ConfigInitializer;
import com.tradingsim.initialize.HttpServerInitializer;
import com.tradingsim.initialize.StartUpInitializer;

import java.util.logging.Logger;

public class TradingSimApplication {

    private static final Logger log = Logger.getLogger(TradingSimApplication.class.getName());

    public static void main(String[] args) {
        StartUpInitializer startUpInitializer = new StartUpInitializer();
        ConfigInitializer configInitializer = new ConfigInitializer();
        configInitializer.initializeTestData(
                StartUpInitializer.assetRepository,
                StartUpInitializer.portfolioRepository,
                StartUpInitializer.priceHistoryRepository,
                StartUpInitializer.transactionRepository,
                StartUpInitializer.userRepository
        );

        HttpServerInitializer httpServerInitializer = new HttpServerInitializer(
                StartUpInitializer.assetRepository,
                StartUpInitializer.portfolioRepository,
                configInitializer.getPort()
        );
        httpServerInitializer.Initialize();
    }
}
