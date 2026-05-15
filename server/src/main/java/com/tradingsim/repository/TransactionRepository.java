package com.tradingsim.repository;

import com.tradingsim.model.Transaction;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

public class TransactionRepository {

    private final Map<Integer, Transaction> transactions =
            new ConcurrentHashMap<>();
    private final AtomicInteger idGenerator = new AtomicInteger(1);

    public synchronized Transaction save(Transaction transaction) {
        if (transaction == null) {
            throw new IllegalArgumentException(
                    "Transaction must not be null"
            );
        }

        if (transaction.getType() == null) {
            throw new IllegalArgumentException(
                    "Transaction type must not be null"
            );
        }

        if (transaction.getUserId() <= 0) {
            throw new IllegalArgumentException(
                    "User id must be positive"
            );
        }

        if (transaction.getAssetId() <= 0) {
            throw new IllegalArgumentException(
                    "Asset id must be positive"
            );
        }

        if (transaction.getSum() <= 0) {
            throw new IllegalArgumentException(
                    "Sum must be positive"
            );
        }

        if (transaction.getAmount() <= 0) {
            throw new IllegalArgumentException(
                    "Amount must be positive"
            );
        }

        if (transaction.getAssetPrice() <= 0) {
            throw new IllegalArgumentException(
                    "Asset price must be positive"
            );
        }

        if (transaction.getDateTime() == null) {
            throw new IllegalArgumentException(
                    "Date time must not be null"
            );
        }

        if (transaction.getId() == 0) {
            int newId;

            do {
                newId = idGenerator.getAndIncrement();
            } while (transactions.containsKey(newId));

            transaction.setId(newId);
        } else {
            if (!transactions.containsKey(transaction.getId())) {
                throw new IllegalArgumentException(
                        "Transaction with id " +
                                transaction.getId() +
                                " does not exist"
                );
            }
        }

        transactions.put(transaction.getId(), transaction);
        return transaction;
    }

    public Optional<Transaction> findById(int id) {
        if (id <= 0) {
            throw new IllegalArgumentException("Invalid id");
        }

        return Optional.ofNullable(transactions.get(id));
    }

    public List<Transaction> findAll() {
        return new ArrayList<>(transactions.values());
    }

    public List<Transaction> findByUserId(int userId) {
        if (userId <= 0) {
            throw new IllegalArgumentException("Invalid user id");
        }

        return transactions.values().stream()
                .filter(transaction -> transaction.getUserId() == userId)
                .toList();
    }

    public List<Transaction> findByAssetId(int assetId) {
        if (assetId <= 0) {
            throw new IllegalArgumentException("Invalid asset id");
        }

        return transactions.values().stream()
                .filter(transaction -> transaction.getAssetId() == assetId)
                .toList();
    }

    public List<Transaction> findBySumLess(double sum) {
        if (sum < 0) {
            throw new IllegalArgumentException("Sum must not be negative");
        }

        return transactions.values().stream()
                .filter(t -> t.getSum() < sum)
                .toList();
    }

    public List<Transaction> findBySumMore(double sum) {
        if (sum < 0) {
            throw new IllegalArgumentException("Sum must not be negative");
        }

        return transactions.values().stream()
                .filter(t -> t.getSum() > sum)
                .toList();
    }

    public synchronized void delete(int id) {
        if (id <= 0) {
            throw new IllegalArgumentException("Invalid id");
        }

        if (!transactions.containsKey(id)) {
            throw new IllegalArgumentException(
                    "Transaction with id " + id + " does not exist"
            );
        }

        transactions.remove(id);
    }
}
