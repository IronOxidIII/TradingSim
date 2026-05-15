package com.tradingsim;

import com.tradingsim.model.*;
import com.tradingsim.repository.*;
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

public class TradingSimApplication {

    private static final Logger log = Logger.getLogger(TradingSimApplication.class.getName());

    public static void main(String[] args) {
        UserRepository userRepository = new UserRepositoryImpl();
        AssetRepository assetRepository = new AssetRepositoryImpl();
        PortfolioRepository portfolioRepository = new PortfolioRepositoryImpl();
        PriceHistoryRepository priceHistoryRepository =
                new PriceHistoryRepositoryImpl();
        TransactionRepository transactionRepository =
                new TransactionRepositoryImpl();

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

        tradingService.sellAsset(1, 1, BigDecimal.valueOf(1));

        marketService.refreshAllPrices();

        tradingService.sellAsset(1, 1, BigDecimal.valueOf(1));

        log.info(() -> "PNL: " + statisticService.getPnL(1));
        log.info(() -> "RDI: " + statisticService.getRdi(1));
        log.info(() -> "WinRate: " + statisticService.getWinRate(1));
        log.info(() -> "Sharpe: " + statisticService.getSharpeRatio(1));

        log.info("Done.");
    }
}
