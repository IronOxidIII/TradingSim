package com.tradingsim.client.domain.model;

public class TradingAsset {

    private final String symbol;
    private final String displaySymbol;
    private final String priceText;
    private final float[] chartPrices;

    public TradingAsset(
            String symbol,
            String displaySymbol,
            String priceText,
            float[] chartPrices
    ) {
        this.symbol = symbol;
        this.displaySymbol = displaySymbol;
        this.priceText = priceText;
        this.chartPrices = chartPrices;
    }

    public String getSymbol() {
        return symbol;
    }

    public String getDisplaySymbol() {
        return displaySymbol;
    }

    public String getPriceText() {
        return priceText;
    }

    public float[] getChartPrices() {
        return chartPrices;
    }
}