package com.tradingsim.initialize;

import com.google.gson.Gson;
import com.google.gson.JsonParseException;
import com.tradingsim.common.dto.asset.AssetDto;
import com.tradingsim.common.dto.portfolio.PortfolioDto;
import com.tradingsim.config.dto.ConfigDto;
import com.tradingsim.config.dto.TestDataDto;
import com.tradingsim.model.Asset;
import com.tradingsim.model.Portfolio;
import com.tradingsim.model.PriceHistory;
import com.tradingsim.model.Transaction;
import com.tradingsim.model.TransactionType;
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

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.logging.Logger;

public class ConfigInitializer {
    private Gson gson = new Gson();
    private static ConfigDto data;
    InputStream stream =
            getClass()
                    .getClassLoader()
                    .getResourceAsStream("config.json");

    Logger logger = Logger.getLogger(ConfigInitializer.class.getName());

    public ConfigInitializer() {
        logger.info("Config initializing.");
        if (data != null) {
            // Конфиг уже прочитан.
            logger.warning("Detected try to reread config.");
            return;
        }

        if (stream == null) {
            logger.info("Config.json not found.");

            logger.info("Setting port = 8888.");
            data = new ConfigDto();
            data.setPort(8888);

            return;
        }

        try (Reader reader = new InputStreamReader(stream)) {
            data = gson.fromJson(reader, ConfigDto.class);
        } catch (JsonParseException e) {
            logger.warning("Failure reading json: " + e.getMessage());
        } catch (IOException e) {
            logger.warning("Error reading config: " + e.getMessage());
        } finally {
            if (data == null) {
                logger.info("Setting port = 8888.");
                data = new ConfigDto();
                data.setPort(8888);
            }
        }
    }

    public int getPort() {
        return data.getPort();
    }

    /// <remark>Ids doesn't matter because of repository realization.</remark>
    public void initializeTestData(
            AssetRepository assetRepository,
            PortfolioRepository portfolioRepository,
            PriceHistoryRepository priceHistoryRepository,
            TransactionRepository transactionRepository,
            UserRepository userRepository) {
        logger.info("Initializing Test Data...");

        TestDataDto testDataDto = data.getTestData();
        if (testDataDto == null) {
            logger.warning("TestData is empty.");
            return;
        }

        for (var assetDto: testDataDto.getAssets()) {
            var asset = new Asset();
            asset.setName(assetDto.getName());
            assetRepository.create(asset);

            var priceHistory = new PriceHistory();
            var addedAsset = assetRepository.findByName(asset.getName());
            if (addedAsset.isPresent()) {
                for (var priceHistoryDto: assetDto.getPrice_history()) {
                    priceHistory.setAssetId(addedAsset.get().getId());
                    priceHistory.setPrice(new BigDecimal(priceHistoryDto.getPrice()));
                    priceHistory.setVolume(priceHistoryDto.getVolume());
                    priceHistory.setDateTime(LocalDateTime.parse(priceHistoryDto.getTime()));

                    priceHistoryRepository.create(priceHistory);
                }
            }
        }

        for (var userDto: testDataDto.getUsers()) {
            User user = new User();
            user.setUsername(userDto.getUsername());
            userRepository.create(user);
        }

        for (var portfolioDto: testDataDto.getPortfolios()) {
            Portfolio portfolio = new Portfolio();
            portfolio.setUserId(portfolioDto.getUser_id());
            portfolio.setStartSum(new BigDecimal(portfolioDto.getStart_sum()));
            portfolio.setTotalSum(new BigDecimal(portfolioDto.getTotal_sum()));
            // No such thing in api( Misarchitectured.
            portfolio.setCashBalance(new BigDecimal("1000000000000"));

            for (var portfolioAssetDto: portfolioDto.getPortfolio_assets()) {
                portfolio.buyAsset(
                        portfolioAssetDto.getAsset_id(),
                        new BigDecimal(portfolioAssetDto.getAmount()),
                        priceHistoryRepository.
                                findByAssetId(portfolioAssetDto.getAsset_id())
                                .getLast()
                                .getPrice());
            }

            portfolioRepository.create(portfolio);
        }

        for (var transactionDto: testDataDto.getTransactions()) {
            var transaction = new Transaction();
            transaction.setAssetId(transactionDto.getAsset_id());
            transaction.setAmount(new BigDecimal(transactionDto.getAmount()));
            transaction.setUserId(transactionDto.getUser_id());
            transaction.setAssetPrice(new BigDecimal(transactionDto.getAsset_price()));
            transaction.setDateTime(LocalDateTime.parse(transactionDto.getDatetime()));
            transaction.setSum(new BigDecimal(transactionDto.getSum()));
            transaction.setType(TransactionType.SELL);

            transactionRepository.create(transaction);
        }

        logger.info("Successfully read config.");
    }
}
