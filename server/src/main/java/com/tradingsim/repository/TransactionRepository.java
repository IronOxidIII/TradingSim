package com.tradingsim.repository;

import com.tradingsim.model.Transaction;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface TransactionRepository {
    Transaction create(Transaction transaction);
    Transaction update(Transaction transaction);
    Optional<Transaction> findById(int id);
    List<Transaction> findAll();
    List<Transaction> findByUserId(int userId);
    List<Transaction> findByAssetId(int assetId);
    List<Transaction> findBySumLessThan(BigDecimal sum);
    List<Transaction> findBySumBetween(BigDecimal min, BigDecimal max);
    List<Transaction> findBySumGreaterThan(BigDecimal sum);
    List<Transaction> findByPeriod(LocalDateTime from, LocalDateTime to);
    void delete(int id);
}
