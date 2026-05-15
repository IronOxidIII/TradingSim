package com.tradingsim.model;

import java.util.ArrayList;
import java.util.List;

public class Portfolio {

    private int id;
    private int userId;
    private double startSum;
    private double totalSum;
    private double cashBalance;
    private final List<PortfolioAsset> portfolioAssets = new ArrayList<>();

    public Portfolio() {}

    public Portfolio(
            int id,
            int userId,
            double startSum,
            double totalSum,
            double cashBalance
    ) {
        this.id = id;
        this.userId = userId;
        this.startSum = startSum;
        this.totalSum = totalSum;
        this.cashBalance = cashBalance;
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

    public double getStartSum() {
        return startSum;
    }

    public void setStartSum(double startSum) {
        this.startSum = startSum;
    }

    public double getTotalSum() {
        return totalSum;
    }

    public void setTotalSum(double totalSum) {
        this.totalSum = totalSum;
    }

    public List<PortfolioAsset> getPortfolioAssets() {
        return portfolioAssets;
    }

    public double getCashBalance() {
        return cashBalance;
    }

    public void setCashBalance(double cashBalance) {
        this.cashBalance = cashBalance;
    }
}
