package com.tradingsim.repository;

import com.tradingsim.model.Portfolio;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

public class PortfolioRepository {

    private final Map<Integer, Portfolio> portfolios =
            new ConcurrentHashMap<>();
    private final AtomicInteger idGenerator = new AtomicInteger(1);

    public synchronized Portfolio save(Portfolio portfolio) {
        if (portfolio == null) {
            throw new IllegalArgumentException(
                    "Portfolio must not be null"
            );
        }

        if (portfolio.getUserId() <= 0) {
            throw new IllegalArgumentException(
                    "User id must be positive"
            );
        }

        if (portfolio.getStartSum() < 0) {
            throw new IllegalArgumentException(
                    "Start sum must not be negative"
            );
        }

        if (portfolio.getTotalSum() < 0) {
            throw new IllegalArgumentException(
                    "Total sum must not be negative"
            );
        }

        if (portfolio.getCashBalance() < 0) {
            throw new IllegalArgumentException(
                    "Cash balance must not be negative"
            );
        }

        if (portfolio.getId() == 0) {
            int newId;

            do {
                newId = idGenerator.getAndIncrement();
            } while (portfolios.containsKey(newId));

            portfolio.setId(newId);
        } else {
            if (!portfolios.containsKey(portfolio.getId())) {
                throw new IllegalArgumentException(
                        "Portfolio with id " +
                                portfolio.getId() +
                                " does not exist"
                );
            }
        }
        portfolios.put(portfolio.getId(), portfolio);
        return portfolio;
    }

    public Optional<Portfolio> findById(int id) {
        if (id <= 0) {
            throw new IllegalArgumentException("Invalid id");
        }

        return Optional.ofNullable(portfolios.get(id));
    }

    public Optional<Portfolio> findByUserId(int userId) {
        if (userId <= 0) {
            throw new IllegalArgumentException("Invalid user id");
        }

        return portfolios.values().stream()
                .filter(portfolio -> portfolio.getUserId() == userId)
                .findFirst();
    }

    public List<Portfolio> findAll() {
        return new ArrayList<>(portfolios.values());
    }

    public synchronized void delete(int id) {
        if (id <= 0) {
            throw new IllegalArgumentException("Invalid id");
        }

        if (!portfolios.containsKey(id)) {
            throw new IllegalArgumentException(
                    "Portfolio with id " + id + " does not exist"
            );
        }

        portfolios.remove(id);
    }

    public synchronized void deleteByUserId(int userId) {
        if (userId <= 0) {
            throw new IllegalArgumentException("Invalid user id");
        }

        Portfolio portfolio = findByUserId(userId)
                .orElseThrow(() ->
                        new IllegalArgumentException(
                                "Portfolio for user id " +
                                        userId + " does not exist"
                        ));

        portfolios.remove(portfolio.getId());
    }
}
