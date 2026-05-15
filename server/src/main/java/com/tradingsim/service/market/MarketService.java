package com.tradingsim.service.market;

import com.tradingsim.model.Asset;
import com.tradingsim.model.PriceHistory;
import com.tradingsim.repository.AssetRepository;
import com.tradingsim.repository.PriceHistoryRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public class MarketService {

    private static final double INITIAL_MIN_PRICE = 10.0;
    private static final double INITIAL_MAX_PRICE = 100.0;
    private static final double MAX_CHANGE_PERCENT = 0.05;

    private final AssetRepository assetRepository;
    private final PriceHistoryRepository priceHistoryRepository;

    public MarketService(
            AssetRepository assetRepository,
            PriceHistoryRepository priceHistoryRepository
    ) {
        this.assetRepository = assetRepository;
        this.priceHistoryRepository = priceHistoryRepository;
    }

    private PriceHistory createAndSavePriceHistory(int assetId, double price) {
        PriceHistory priceHistory = new PriceHistory();
        priceHistory.setAssetId(assetId);
        priceHistory.setDateTime(LocalDateTime.now());
        priceHistory.setPrice(price);
        priceHistory.setVolume(
                ThreadLocalRandom.current().nextInt(1, 10_000)
        );

        return priceHistoryRepository.save(priceHistory);
    }

    public synchronized double getCurrentPrice(int assetId) {
        validateAssetExists(assetId);

        List<PriceHistory> history =
                priceHistoryRepository.findByAssetId(assetId);

        if (history.isEmpty()) {
            return createInitialPrice(assetId);
        }

        return history.get(history.size() - 1).getPrice();
    }

    public synchronized double updatePrice(int assetId) {
        validateAssetExists(assetId);

        double currentPrice = getCurrentPrice(assetId);

        double changeFactor = ThreadLocalRandom.current()
                .nextDouble(
                        1.0 - MAX_CHANGE_PERCENT,
                        1.0 + MAX_CHANGE_PERCENT
                );

        double newPrice = roundPrice(currentPrice * changeFactor);

        if (newPrice <= 0) {
            newPrice = INITIAL_MIN_PRICE;
        }

        createAndSavePriceHistory(assetId, newPrice);

        return newPrice;
    }

    public List<PriceHistory> getPriceHistory(int assetId) {
        validateAssetExists(assetId);
        return priceHistoryRepository.findByAssetId(assetId);
    }

    public synchronized void refreshAllPrices() {
        for (Asset asset : assetRepository.findAll()) {
            updatePrice(asset.getId());
        }
    }

    public double createInitialPrice(int assetId) {
        double initialPrice = roundPrice(
                ThreadLocalRandom.current().nextDouble(
                        INITIAL_MIN_PRICE, INITIAL_MAX_PRICE
                )
        );

        createAndSavePriceHistory(assetId, initialPrice);

        return initialPrice;
    }

    private void validateAssetExists(int assetId) {
        if (assetId <= 0) {
            throw new IllegalArgumentException("Asset id must be positive");
        }

        if (assetRepository.findById(assetId).isEmpty()) {
            throw new IllegalArgumentException("Asset does not exist");
        }
    }

    private double roundPrice(double price) {
        return Math.round(price * 100.0) / 100.0;
    }
}
