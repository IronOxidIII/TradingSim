package com.tradingsim.client.domain.model;

import android.os.Build;

import com.tradingsim.client.network.dto.asset.AssetDto;
import com.tradingsim.client.network.dto.portfolio.PortfolioAssetDto;

import java.math.BigDecimal;
import java.util.List;

public class PortfolioAsset {

    private final String symbol;
    private final BigDecimal amount;
    private final BigDecimal price;

    public PortfolioAsset(
            String symbol,
            BigDecimal amount,
            BigDecimal price
    ) {
        this.symbol = symbol;
        this.amount = amount;
        this.price = price;
    }

    public String getSymbol() {
        return symbol;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public BigDecimal getTotalValue() {
        return amount.multiply(price);
    }
}