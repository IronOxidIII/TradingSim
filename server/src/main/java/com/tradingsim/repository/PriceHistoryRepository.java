package com.tradingsim.repository;

import com.tradingsim.model.PriceHistory;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface PriceHistoryRepository {
    PriceHistory create(PriceHistory priceHistory);
    Optional<PriceHistory> findById(int id);
    List<PriceHistory> findAll();
    List<PriceHistory> findByAssetId(int assetId);
    List<PriceHistory> findByPeriod(LocalDateTime from, LocalDateTime to);
    List<PriceHistory> findByAssetIdAndPeriod(int assetId, LocalDateTime from, LocalDateTime to);
    void delete(int id);
}
