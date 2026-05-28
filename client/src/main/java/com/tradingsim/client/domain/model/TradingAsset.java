package com.tradingsim.client.domain.model;

import java.math.BigDecimal;

public class TradingAsset {

    private final String symbol;
    private final String displaySymbol;
    private final BigDecimal price;
    private final BigDecimal[] chartPrices;

    public TradingAsset(
            String symbol,
            String displaySymbol,
            BigDecimal price,
            BigDecimal[] chartPrices
    ) {
        this.symbol = symbol;
        this.displaySymbol = displaySymbol;
        this.price = price;
        this.chartPrices = chartPrices;
    }

    public String getSymbol() {
        return symbol;
    }

    public String getDisplaySymbol() {
        return displaySymbol;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public BigDecimal[] getChartPrices() {
        return chartPrices;
    }
}