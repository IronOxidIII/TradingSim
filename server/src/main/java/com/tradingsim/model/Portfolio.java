package com.tradingsim.model;

import com.tradingsim.config.MoneyConfig;
import com.tradingsim.exception.InsufficientFundsException;
import com.tradingsim.exception.ValidationException;
import com.tradingsim.repository.base.Identifiable;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class Portfolio implements Identifiable {

    private int id;
    private int userId;
    private BigDecimal startSum = BigDecimal.ZERO
            .setScale(MoneyConfig.MONEY_SCALE, MoneyConfig.ROUNDING_MODE);
    private BigDecimal totalSum = BigDecimal.ZERO
            .setScale(MoneyConfig.MONEY_SCALE, MoneyConfig.ROUNDING_MODE);
    private BigDecimal cashBalance = BigDecimal.ZERO
            .setScale(MoneyConfig.MONEY_SCALE, MoneyConfig.ROUNDING_MODE);
    private final List<PortfolioAsset> portfolioAssets = new ArrayList<>();

    public Portfolio() {}

    public Portfolio(
            int id,
            int userId,
            BigDecimal startSum,
            BigDecimal totalSum,
            BigDecimal cashBalance
    ) {
        this.id = id;
        this.userId = userId;
        this.startSum = startSum
                .setScale(MoneyConfig.MONEY_SCALE, MoneyConfig.ROUNDING_MODE);
        this.totalSum = totalSum
                .setScale(MoneyConfig.MONEY_SCALE, MoneyConfig.ROUNDING_MODE);
        this.cashBalance = cashBalance
                .setScale(MoneyConfig.MONEY_SCALE, MoneyConfig.ROUNDING_MODE);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public BigDecimal getStartSum() {
        return startSum;
    }

    public void setStartSum(BigDecimal startSum) {
        this.startSum = startSum.setScale(
                MoneyConfig.MONEY_SCALE,
                MoneyConfig.ROUNDING_MODE
        );
    }

    public BigDecimal getTotalSum() {
        return totalSum;
    }

    public void setTotalSum(BigDecimal totalSum) {
        this.totalSum = totalSum.setScale(
                MoneyConfig.MONEY_SCALE,
                MoneyConfig.ROUNDING_MODE
        );
    }

    public List<PortfolioAsset> getPortfolioAssets() {
        return portfolioAssets;
    }

    public BigDecimal getCashBalance() {
        return cashBalance;
    }

    public void setCashBalance(BigDecimal cashBalance) {
        this.cashBalance = cashBalance.setScale(
                MoneyConfig.MONEY_SCALE,
                MoneyConfig.ROUNDING_MODE
        );
    }

    public void buyAsset(
            int assetId, BigDecimal amount, BigDecimal price
    ) {

        BigDecimal totalCost = price.multiply(amount);

        if (cashBalance.compareTo(totalCost) < 0) {
            throw new InsufficientFundsException("Not enough cash");
        }

        cashBalance = cashBalance.subtract(totalCost);

        PortfolioAsset asset = findOrCreate(assetId);
        asset.setAmount(asset.getAmount().add(amount));
    }

    private PortfolioAsset findOrCreate(int assetId) {
        return portfolioAssets.stream()
                .filter(a -> a.getAssetId() == assetId)
                .findFirst()
                .orElseGet(() -> {
                    PortfolioAsset newAsset = new PortfolioAsset();
                    newAsset.setAssetId(assetId);
                    newAsset.setAmount(BigDecimal.ZERO);
                    portfolioAssets.add(newAsset);
                    return newAsset;
                });
    }

    public void sellAsset(
            int assetId, BigDecimal amount, BigDecimal price
    ) {
        PortfolioAsset asset = portfolioAssets.stream()
                .filter(a -> a.getAssetId() == assetId)
                .findFirst()
                .orElseThrow(() -> new ValidationException("Asset not found"));

        if (asset.getAmount().compareTo(amount) < 0) {
            throw new InsufficientFundsException("Not enough asset quantity");
        }

        BigDecimal tradeSum = price.multiply(amount)
                .setScale(MoneyConfig.MONEY_SCALE, MoneyConfig.ROUNDING_MODE);

        asset.setAmount(asset.getAmount().subtract(amount));
        cashBalance = cashBalance.add(tradeSum);

        if (asset.getAmount().compareTo(BigDecimal.ZERO) == 0) {
            portfolioAssets.remove(asset);
        }
    }
}
