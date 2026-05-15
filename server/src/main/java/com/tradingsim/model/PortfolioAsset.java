package com.tradingsim.model;

public class PortfolioAsset {

    private int id;
    private int portfolioId;
    private int assetId;
    private double amount;

    public PortfolioAsset() {}

    public PortfolioAsset(
            int id,
            int portfolioId,
            int assetId,
            double amount
    ) {
        this.id = id;
        this.portfolioId = portfolioId;
        this.assetId = assetId;
        this.amount = amount;
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

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }
}
