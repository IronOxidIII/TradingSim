package com.tradingsim.repository;

import com.tradingsim.exception.ValidationException;
import com.tradingsim.model.PriceHistory;
import com.tradingsim.repository.base.AbstractInMemoryRepository;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

public class PriceHistoryRepositoryImpl
        extends AbstractInMemoryRepository<PriceHistory>
        implements PriceHistoryRepository {

    @Override
    public PriceHistory create(PriceHistory priceHistory) {
        validate(priceHistory);
        if (priceHistory.getId() > 0) {
            throw new ValidationException(
                    "Price history updates are not allowed"
            );
        }
        return super.create(priceHistory);
    }

    @Override
    public Optional<PriceHistory> findById(int id) {
        if (id <= 0) {
            throw new ValidationException("Invalid id");
        }
        return super.findById(id);
    }

    @Override
    public List<PriceHistory> findAll() {
        return super.findAll();
    }

    @Override
    public List<PriceHistory> findByAssetId(int assetId) {
        if (assetId <= 0) {
            throw new ValidationException("Invalid asset id");
        }

        return storage.values().stream()
                .filter(history -> history.getAssetId() == assetId)
                .sorted(Comparator.comparing(PriceHistory::getDateTime))
                .toList();
    }

    @Override
    public List<PriceHistory> findByPeriod(LocalDateTime from, LocalDateTime to) {
        if (from == null || to == null) {
            throw new ValidationException("Period must not be null");
        }

        if (from.isAfter(to)) {
            throw new ValidationException("From date must be before to date");
        }

        return storage.values().stream()
                .filter(history -> !history.getDateTime().isBefore(from))
                .filter(history -> !history.getDateTime().isAfter(to))
                .sorted(Comparator.comparing(PriceHistory::getDateTime))
                .toList();
    }

    @Override
    public List<PriceHistory> findByAssetIdAndPeriod(
            int assetId,
            LocalDateTime from,
            LocalDateTime to
    ) {
        if (assetId <= 0) {
            throw new ValidationException("Invalid asset id");
        }

        return findByPeriod(from, to).stream()
                .filter(history -> history.getAssetId() == assetId)
                .toList();
    }

    @Override
    public void delete(int id) {
        if (id <= 0) {
            throw new ValidationException("Invalid id");
        }
        super.delete(id);
    }

    private void validate(PriceHistory priceHistory) {
        if (priceHistory == null) {
            throw new ValidationException("Price history must not be null");
        }

        if (priceHistory.getAssetId() <= 0) {
            throw new ValidationException("Asset id must be positive");
        }

        if (priceHistory.getPrice() == null
                || priceHistory.getPrice().compareTo(BigDecimal.ZERO) <= 0) {
            throw new ValidationException("Price must be positive");
        }

        if (priceHistory.getVolume() < 0) {
            throw new ValidationException("Volume must not be negative");
        }

        if (priceHistory.getDateTime() == null) {
            throw new ValidationException("Date time must not be null");
        }
    }
}
