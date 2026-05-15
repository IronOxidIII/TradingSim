package com.tradingsim.model;

import java.time.LocalDateTime;

public class Transaction {

    private int id;
    private int userId;
    private int assetId;
    private double sum;
    private double assetPrice;
    private double amount;
    private LocalDateTime dateTime;
    private TransactionType type;

    public Transaction() {}

    public Transaction(
            int id,
            int userId,
            int assetId,
            double sum,
            double assetPrice,
            double amount,
            LocalDateTime dateTime,
            TransactionType type
    ) {
        this.id = id;
        this.userId = userId;
        this.assetId = assetId;
        this.sum = sum;
        this.assetPrice = assetPrice;
        this.amount = amount;
        this.dateTime = dateTime;
        this.type = type;
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

    public int getAssetId() {
        return assetId;
    }

    public void setAssetId(int assetId) {
        this.assetId = assetId;
    }

    public double getSum() {
        return sum;
    }

    public void setSum(double sum) {
        this.sum = sum;
    }

    public double getAssetPrice() {
        return assetPrice;
    }

    public void setAssetPrice(double assetPrice) {
        this.assetPrice = assetPrice;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public LocalDateTime getDateTime() {
        return dateTime;
    }

    public void setDateTime(LocalDateTime dateTime) {
        this.dateTime = dateTime;
    }

    public TransactionType getType() {
        return type;
    }

    public void setType(TransactionType type) {
        this.type = type;
    }
}
