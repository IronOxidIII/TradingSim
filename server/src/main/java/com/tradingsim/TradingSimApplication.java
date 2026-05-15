package com.tradingsim;

import com.tradingsim.model.*;
import com.tradingsim.repository.*;
import com.tradingsim.service.market.MarketService;
import com.tradingsim.service.portfolio.PortfolioService;
import com.tradingsim.service.statistics.StatisticService;
import com.tradingsim.service.trading.TradeValidationService;
import com.tradingsim.service.trading.TradingService;

public class TradingSimApplication {
    public static void main(String[] args) {
        UserRepository userRepository = new UserRepository();
        AssetRepository assetRepository = new AssetRepository();
        PortfolioRepository portfolioRepository = new PortfolioRepository();
        PriceHistoryRepository priceHistoryRepository = new PriceHistoryRepository();
        TransactionRepository transactionRepository = new TransactionRepository();

        MarketService marketService = new MarketService(
                assetRepository,
                priceHistoryRepository
        );

        PortfolioService portfolioService = new PortfolioService(
                portfolioRepository,
                assetRepository,
                priceHistoryRepository,
                userRepository
        );

        TradeValidationService tradeValidationService = new TradeValidationService(
                portfolioService,
                userRepository,
                assetRepository
        );

        TradingService tradingService = new TradingService(
                portfolioService,
                marketService,
                tradeValidationService,
                transactionRepository
        );

        StatisticService statisticService = new StatisticService(
                portfolioService,
                marketService,
                transactionRepository
        );

        User user = new User();
        user.setId(1);
        user.setUsername("Test User");
        userRepository.save(user);

        Asset btc = new Asset();
        btc.setId(1);
        btc.setName("BTC");
        assetRepository.save(btc);

        Asset eth = new Asset();
        eth.setId(2);
        eth.setName("ETH");
        assetRepository.save(eth);

        marketService.createInitialPrice(1);
        marketService.createInitialPrice(2);

        portfolioService.createPortfolio(1, 10_000);

        tradingService.buyAsset(1, 1, 2);
        tradingService.buyAsset(1, 2, 5);

        marketService.refreshAllPrices();

        tradingService.sellAsset(1, 1, 1);

        marketService.refreshAllPrices();

        tradingService.sellAsset(1, 1, 1);

        System.out.println("PNL: " + statisticService.getPnL(1));
        System.out.println("RDI: " + statisticService.getRdi(1));
        System.out.println("WinRate: " + statisticService.getWinRate(1));
        System.out.println("Sharpe: " + statisticService.getSharpeRatio(1));

        System.out.println("Done.");
    }
}
