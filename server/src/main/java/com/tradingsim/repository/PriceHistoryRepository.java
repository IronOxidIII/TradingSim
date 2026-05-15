package com.tradingsim.repository;

import com.tradingsim.model.PriceHistory;

import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

public class PriceHistoryRepository {

    private final Map<Integer, PriceHistory> priceHistories =
            new ConcurrentHashMap<>();
    private final AtomicInteger idGenerator = new AtomicInteger(1);

    public synchronized PriceHistory save(PriceHistory priceHistory) {
        if (priceHistory == null) {
            throw new IllegalArgumentException(
                    "Price history must not be null"
            );
        }

        if (priceHistory.getAssetId() <= 0) {
            throw new IllegalArgumentException(
                    "Asset id must be positive"
            );
        }

        if (priceHistory.getPrice() <= 0) {
            throw new IllegalArgumentException(
                    "Price must be positive"
            );
        }

        if (priceHistory.getVolume() < 0) {
            throw new IllegalArgumentException(
                    "Volume must not be negative"
            );
        }

        if (priceHistory.getDateTime() == null) {
            throw new IllegalArgumentException(
                    "Date time must not be null"
            );
        }

        if (priceHistory.getId() != 0) {
            throw new IllegalArgumentException(
                    "Price history updates are not allowed"
            );
        }

        int newId;

        do {
            newId = idGenerator.getAndIncrement();
        } while (priceHistories.containsKey(newId));

        priceHistory.setId(newId);

        priceHistories.put(priceHistory.getId(), priceHistory);
        return priceHistory;
    }

    public Optional<PriceHistory> findById(int id) {
        if (id <= 0) {
            throw new IllegalArgumentException("Invalid id");
        }

        return Optional.ofNullable(priceHistories.get(id));
    }

    public List<PriceHistory> findAll() {
        return new ArrayList<>(priceHistories.values());
    }

    public List<PriceHistory> findByAssetId(int assetId) {
        if (assetId <= 0) {
            throw new IllegalArgumentException("Invalid asset id");
        }

        return priceHistories.values().stream()
                .filter(history -> history.getAssetId() == assetId)
                .sorted(Comparator.comparing(PriceHistory::getDateTime))
                .toList();
    }

    public List<PriceHistory> findByPeriod(
            LocalDateTime from,
            LocalDateTime to
    ) {
        if (from == null || to == null) {
            throw new IllegalArgumentException("Period must not be null");
        }

        if (from.isAfter(to)) {
            throw new IllegalArgumentException(
                    "From date must be before to date"
            );
        }
        return priceHistories.values().stream()
                .filter(history -> !history.getDateTime().isBefore(from))
                .filter(history -> !history.getDateTime().isAfter(to))
                .sorted(Comparator.comparing(PriceHistory::getDateTime))
                .toList();
    }

    public List<PriceHistory> findByAssetIdAndPeriod(
            int assetId,
            LocalDateTime from,
            LocalDateTime to
    ) {
        if (assetId <= 0) {
            throw new IllegalArgumentException(
                    "Invalid asset id"
            );
        }

        return findByPeriod(from, to).stream()
                .filter(history -> history.getAssetId() == assetId)
                .toList();
    }

    public synchronized void delete(int id) {
        if (id <= 0) {
            throw new IllegalArgumentException("Invalid id");
        }

        if (!priceHistories.containsKey(id)) {
            throw new IllegalArgumentException(
                    "Price history with id " + id + " does not exist"
            );
        }

        priceHistories.remove(id);
    }
}
