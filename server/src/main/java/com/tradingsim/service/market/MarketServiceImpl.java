package com.tradingsim.service.market;

import com.tradingsim.config.MoneyConfig;
import com.tradingsim.exception.NotFoundException;
import com.tradingsim.exception.ValidationException;
import com.tradingsim.model.Asset;
import com.tradingsim.model.PriceHistory;
import com.tradingsim.repository.AssetRepository;
import com.tradingsim.repository.PriceHistoryRepository;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ThreadLocalRandom;

public class MarketServiceImpl implements MarketService {

    private static final BigDecimal INITIAL_MIN_PRICE =
            BigDecimal.valueOf(10.0);
    private static final BigDecimal INITIAL_MAX_PRICE =
            BigDecimal.valueOf(100.0);
    private static final BigDecimal MAX_CHANGE_PERCENT =
            BigDecimal.valueOf(0.05);

    private final AssetRepository assetRepository;
    private final PriceHistoryRepository priceHistoryRepository;
    private final ConcurrentHashMap<Integer, Object> assetLocks = new ConcurrentHashMap<>();

    private Object getLock(int assetId) {
        return assetLocks.computeIfAbsent(assetId, id -> new Object());
    }

    public MarketServiceImpl(
            AssetRepository assetRepository,
            PriceHistoryRepository priceHistoryRepository
    ) {
        this.assetRepository = assetRepository;
        this.priceHistoryRepository = priceHistoryRepository;
    }

    private PriceHistory createAndSavePriceHistory(int assetId, BigDecimal price) {
        PriceHistory priceHistory = new PriceHistory();
        priceHistory.setAssetId(assetId);
        priceHistory.setDateTime(LocalDateTime.now());
        priceHistory.setPrice(price);
        priceHistory.setVolume(
                ThreadLocalRandom.current().nextInt(1, 10_000)
        );

        return priceHistoryRepository.create(priceHistory);
    }

    public BigDecimal getCurrentPrice(int assetId) {
        validateAssetExists(assetId);

        synchronized (getLock(assetId)) {
            return getCurrentPriceInternal(assetId);
        }
    }

    private BigDecimal getCurrentPriceInternal(int assetId) {
        List<PriceHistory> history =
                priceHistoryRepository.findByAssetId(assetId);

        if (history.isEmpty()) {
            return createInitialPriceInternal(assetId);
        }

        return history.get(history.size() - 1).getPrice();
    }

    public BigDecimal updatePrice(int assetId) {
        validateAssetExists(assetId);

        synchronized (getLock(assetId)) {
            BigDecimal currentPrice = getCurrentPriceInternal(assetId);

            BigDecimal minFactor = BigDecimal.ONE.subtract(MAX_CHANGE_PERCENT);
            BigDecimal maxFactor = BigDecimal.ONE.add(MAX_CHANGE_PERCENT);

            BigDecimal randomFactor = minFactor.add(
                    BigDecimal.valueOf(ThreadLocalRandom.current().nextDouble())
                            .multiply(maxFactor.subtract(minFactor))
            );

            BigDecimal newPrice = currentPrice
                    .multiply(randomFactor)
                    .setScale(
                            MoneyConfig.MONEY_SCALE,
                            MoneyConfig.ROUNDING_MODE
                    );

            if (newPrice.compareTo(BigDecimal.ZERO) <= 0) {
                newPrice = INITIAL_MIN_PRICE.setScale(
                        MoneyConfig.MONEY_SCALE,
                        MoneyConfig.ROUNDING_MODE
                );
            }

            createAndSavePriceHistory(assetId, newPrice);

            return newPrice;
        }
    }

    public List<PriceHistory> getPriceHistory(int assetId) {
        validateAssetExists(assetId);
        return priceHistoryRepository.findByAssetId(assetId);
    }

    public void refreshAllPrices() {
        for (Asset asset : assetRepository.findAll()) {
            updatePrice(asset.getId());
        }
    }

    public BigDecimal createInitialPrice(int assetId) {

        synchronized (getLock(assetId)) {
            return createInitialPriceInternal(assetId);
        }
    }

    public BigDecimal createInitialPriceInternal(int assetId) {
        BigDecimal range = INITIAL_MAX_PRICE.subtract(INITIAL_MIN_PRICE);

        BigDecimal initialPrice = INITIAL_MIN_PRICE.add(
                BigDecimal.valueOf(ThreadLocalRandom.current().nextDouble())
                        .multiply(range)
        ).setScale(
                MoneyConfig.MONEY_SCALE,
                MoneyConfig.ROUNDING_MODE
        );

        createAndSavePriceHistory(assetId, initialPrice);

        return initialPrice;
    }

    private void validateAssetExists(int assetId) {
        if (assetId <= 0) {
            throw new ValidationException("Asset id must be positive");
        }

        if (assetRepository.findById(assetId).isEmpty()) {
            throw new NotFoundException("Asset does not exist");
        }
    }
}
