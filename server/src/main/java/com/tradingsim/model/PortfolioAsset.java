package com.tradingsim.model;

import com.tradingsim.common.dto.dto.portfolio.PortfolioAssetDto;
import com.tradingsim.config.MoneyConfig;

import java.math.BigDecimal;
import java.util.List;

public class PortfolioAsset {

    private int id;
    private int portfolioId;
    private int assetId;
    private BigDecimal amount;

    public PortfolioAsset() {}

    public PortfolioAsset(
            int id,
            int portfolioId,
            int assetId,
            BigDecimal amount
    ) {
        this.id = id;
        this.portfolioId = portfolioId;
        this.assetId = assetId;
        this.amount = amount.setScale(
                MoneyConfig.ASSET_SCALE,
                MoneyConfig.ROUNDING_MODE
        );
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getPortfolioId() {
        return portfolioId;
    }

    public void setPortfolioId(int portfolioId) {
        this.portfolioId = portfolioId;
    }

    public int getAssetId() {
        return assetId;
    }

    public void setAssetId(int assetId) {
        this.assetId = assetId;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount.setScale(
                MoneyConfig.ASSET_SCALE,
                MoneyConfig.ROUNDING_MODE
        );
    }
}
