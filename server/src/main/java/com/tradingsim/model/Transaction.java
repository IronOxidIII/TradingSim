package com.tradingsim.model;

import com.tradingsim.config.MoneyConfig;
import com.tradingsim.repository.base.Identifiable;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class Transaction implements Identifiable {

    private int id;
    private int userId;
    private int assetId;
    private BigDecimal sum;
    private BigDecimal assetPrice;
    private BigDecimal amount;
    private LocalDateTime dateTime;
    private TransactionType type;

    public Transaction() {}

    public Transaction(
            int id,
            int userId,
            int assetId,
            BigDecimal sum,
            BigDecimal assetPrice,
            BigDecimal amount,
            LocalDateTime dateTime,
            TransactionType type
    ) {
        this.id = id;
        this.userId = userId;
        this.assetId = assetId;
        this.sum = sum
                .setScale(MoneyConfig.MONEY_SCALE, MoneyConfig.ROUNDING_MODE);
        this.assetPrice = assetPrice
                .setScale(MoneyConfig.MONEY_SCALE, MoneyConfig.ROUNDING_MODE);
        this.amount = amount
                .setScale(MoneyConfig.ASSET_SCALE, MoneyConfig.ROUNDING_MODE);
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

    public BigDecimal getSum() {
        return sum;
    }

    public void setSum(BigDecimal sum) {
        this.sum = sum.setScale(
                MoneyConfig.MONEY_SCALE,
                MoneyConfig.ROUNDING_MODE
        );
    }

    public BigDecimal getAssetPrice() {
        return assetPrice;
    }

    public void setAssetPrice(BigDecimal assetPrice) {
        this.assetPrice = assetPrice.setScale(
                MoneyConfig.MONEY_SCALE,
                MoneyConfig.ROUNDING_MODE
        );
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
