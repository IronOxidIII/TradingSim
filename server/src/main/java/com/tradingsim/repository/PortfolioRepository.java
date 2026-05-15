package com.tradingsim.repository;

import com.tradingsim.model.Portfolio;

import java.util.List;
import java.util.Optional;

public interface PortfolioRepository {
    Portfolio create(Portfolio portfolio);
    Portfolio update(Portfolio portfolio);
    Optional<Portfolio> findById(int id);
    Optional<Portfolio> findByUserId(int userId);
    List<Portfolio> findAll();
    void delete(int id);
    void deleteByUserId(int userId);
}
