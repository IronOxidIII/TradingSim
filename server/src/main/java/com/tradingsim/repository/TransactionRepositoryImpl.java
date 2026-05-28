package com.tradingsim.repository;

import com.tradingsim.exception.ValidationException;
import com.tradingsim.model.Transaction;
import com.tradingsim.repository.base.AbstractInMemoryRepository;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public class TransactionRepositoryImpl
        extends AbstractInMemoryRepository<Transaction>
        implements TransactionRepository {

    @Override
    public Transaction create(Transaction transaction) {
        validate(transaction);
        if (transaction.getId() > 0) {
            throw new ValidationException(
                    "New transaction must not have an id"
            );
        }
        return super.create(transaction);
    }

    @Override
    public Transaction update(Transaction transaction) {
        validate(transaction);
        return super.update(transaction);
    }

    @Override
    public Optional<Transaction> findById(int id) {
        if (id <= 0) {
            throw new ValidationException("Invalid id");
        }
        return super.findById(id);
    }

    @Override
    public List<Transaction> findAll() {
        return super.findAll();
    }

    @Override
    public List<Transaction> findByUserId(int userId) {
        if (userId <= 0) {
            throw new ValidationException("Invalid user id");
        }

        return storage.values().stream()
                .filter(t -> t.getUserId() == userId)
                .toList();
    }

    @Override
    public List<Transaction> findByAssetId(int assetId) {
        if (assetId <= 0) {
            throw new ValidationException("Invalid asset id");
        }

        return storage.values().stream()
                .filter(t -> t.getAssetId() == assetId)
                .toList();
    }

    @Override
    public List<Transaction> findBySumLessThan(BigDecimal sum) {
        if (sum == null || sum.compareTo(BigDecimal.ZERO) < 0) {
            throw new ValidationException("Sum must not be negative");
        }

        return storage.values().stream()
                .filter(t -> t.getSum().compareTo(sum) < 0)
                .toList();
    }

    @Override
    public List<Transaction> findBySumGreaterThan(BigDecimal sum) {
        if (sum == null || sum.compareTo(BigDecimal.ZERO) < 0) {
            throw new ValidationException("Sum must not be negative");
        }

        return storage.values().stream()
                .filter(t -> t.getSum().compareTo(sum) > 0)
                .toList();
    }

    @Override
    public List<Transaction> findBySumBetween(BigDecimal min, BigDecimal max) {

        if (min == null || max == null) {
            throw new ValidationException("Bounds must not be null");
        }

        if (min.compareTo(BigDecimal.ZERO) < 0 || max.compareTo(BigDecimal.ZERO) < 0) {
            throw new ValidationException("Sum bounds must not be negative");
        }

        if (min.compareTo(max) > 0) {
            throw new ValidationException("Min must be <= max");
        }

        return storage.values().stream()
                .filter(t -> {
                    BigDecimal sum = t.getSum();
                    return sum.compareTo(min) >= 0 && sum.compareTo(max) <= 0;
                })
                .toList();
    }

    @Override
    public List<Transaction> findByPeriod(LocalDateTime from, LocalDateTime to) {
        if (from == null || to == null) {
            throw new ValidationException("Period must not be null");
        }

        if (from.isAfter(to)) {
            throw new ValidationException("From date must be before to date");
        }

        return storage.values().stream()
                .filter(t -> !t.getDateTime().isBefore(from))
                .filter(t -> !t.getDateTime().isAfter(to))
                .toList();
    }

    @Override
    public void delete(int id) {
        if (id <= 0) {
            throw new ValidationException("Invalid id");
        }
        super.delete(id);
    }

    private void validate(Transaction transaction) {
        if (transaction == null) {
            throw new ValidationException("Transaction must not be null");
        }

        if (transaction.getType() == null) {
            throw new ValidationException("Transaction type must not be null");
        }

        if (transaction.getUserId() <= 0) {
            throw new ValidationException("User id must be positive");
        }

        if (transaction.getAssetId() <= 0) {
            throw new ValidationException("Asset id must be positive");
        }

        if (transaction.getSum() == null
                || transaction.getSum().compareTo(BigDecimal.ZERO) <= 0) {
            throw new ValidationException("Sum must be positive");
        }

        if (transaction.getAmount() == null
                || transaction.getAmount().compareTo(BigDecimal.ZERO) <= 0) {
            throw new ValidationException("Amount must be positive");
        }

        if (transaction.getAssetPrice() == null
                || transaction.getAssetPrice().compareTo(BigDecimal.ZERO) <= 0) {
            throw new ValidationException("Asset price must be positive");
        }

        if (transaction.getDateTime() == null) {
            throw new ValidationException("Date time must not be null");
        }
    }
}
