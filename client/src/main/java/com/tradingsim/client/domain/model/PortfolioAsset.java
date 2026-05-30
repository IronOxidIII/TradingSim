package com.tradingsim.client.domain.model;

import java.math.BigDecimal;

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