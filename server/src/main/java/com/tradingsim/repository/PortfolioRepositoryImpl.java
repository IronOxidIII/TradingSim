package com.tradingsim.repository;

import com.tradingsim.common.dto.portfolio.PortfolioAssetDto;
import com.tradingsim.common.dto.portfolio.PortfolioDto;
import com.tradingsim.exception.NotFoundException;
import com.tradingsim.exception.ValidationException;
import com.tradingsim.model.Portfolio;
import com.tradingsim.model.PortfolioAsset;
import com.tradingsim.repository.base.AbstractInMemoryRepository;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class PortfolioRepositoryImpl
        extends AbstractInMemoryRepository<Portfolio>
        implements PortfolioRepository {

    @Override
    public Portfolio create(Portfolio portfolio) {
        validate(portfolio);
        if (portfolio.getId() > 0) {
            throw new ValidationException("New portfolio must not have an id");
        }
        return super.create(portfolio);
    }

    @Override
    public Portfolio update(Portfolio portfolio) {
        validate(portfolio);
        return super.update(portfolio);
    }

    @Override
    public Optional<Portfolio> findById(int id) {
        if (id <= 0) {
            throw new ValidationException("Invalid id");
        }
        return super.findById(id);
    }

    @Override
    public Optional<Portfolio> findByUserId(int userId) {
        if (userId <= 0) {
            throw new ValidationException("Invalid user id");
        }

        return storage.values().stream()
                .filter(portfolio -> portfolio.getUserId() == userId)
                .findFirst();
    }

    @Override
    public List<Portfolio> findAll() {
        return super.findAll();
    }

    @Override
    public void delete(int id) {
        if (id <= 0) {
            throw new ValidationException("Invalid id");
        }
        super.delete(id);
    }

    @Override
    public void deleteByUserId(int userId) {
        if (userId <= 0) {
            throw new ValidationException("Invalid user id");
        }

        Portfolio portfolio = findByUserId(userId)
                .orElseThrow(() ->
                        new NotFoundException(
                                "Portfolio for user id " + userId + " does not exist"
                        ));

        storage.remove(portfolio.getId());
    }

    private void validate(Portfolio portfolio) {
        if (portfolio == null) {
            throw new ValidationException("Portfolio must not be null");
        }

        if (portfolio.getUserId() <= 0) {
            throw new ValidationException("User id must be positive");
        }

        if (portfolio.getStartSum().compareTo(BigDecimal.ZERO) < 0) {
            throw new ValidationException("Start sum must not be negative");
        }

        if (portfolio.getTotalSum().compareTo(BigDecimal.ZERO) < 0) {
            throw new ValidationException("Total sum must not be negative");
        }

        if (portfolio.getCashBalance().compareTo(BigDecimal.ZERO) < 0) {
            throw new ValidationException("Cash balance must not be negative");
        }
    }

    public List<PortfolioDto> toPortfoliosDtoList() {
        var portfolios = super.findAll();
        List<PortfolioDto> result = new ArrayList<>();

        for (var portfolio : portfolios) {
            List<PortfolioAsset> portfolioAssets = portfolio.getPortfolioAssets();
            List<PortfolioAssetDto> portfolioAssetDtos = new ArrayList<>();
            for (var portfolioAsset : portfolioAssets) {
                portfolioAssetDtos.add(
                        new PortfolioAssetDto(
                                portfolioAsset.getAssetId(),
                                portfolioAsset.getAmount().toString()
                        )
                );
            }

            result.add(new PortfolioDto(
                    1,
                    10_000,
                    10_000,
                    portfolioAssetDtos));
        }

        return result;
    }
}
