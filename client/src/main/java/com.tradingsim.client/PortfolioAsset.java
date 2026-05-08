package com.tradingsim.client;

public class PortfolioAsset {

    private String symbol;
    private double amount;
    private double price;

    public PortfolioAsset(String symbol, double amount, double price) {
        this.symbol = symbol;
        this.amount = amount;
        this.price = price;
    }

    public String getSymbol() {
        return symbol;
    }

    public double getAmount() {
        return amount;
    }

    public double getPrice() {
        return price;
    }

    public double getTotalValue() {
        return amount * price;
    }
}