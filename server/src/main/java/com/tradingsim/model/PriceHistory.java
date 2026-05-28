package com.tradingsim.model;

import com.tradingsim.config.MoneyConfig;
import com.tradingsim.repository.base.Identifiable;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class PriceHistory implements Identifiable {

    private int id;
    private int assetId;
    private LocalDateTime dateTime;
    private BigDecimal price;
    private int volume;

    public PriceHistory() {}

    public PriceHistory(
            int id,
            int assetId,
            LocalDateTime dateTime,
            BigDecimal price,
            int volume
    ) {
        this.id = id;
        this.assetId = assetId;
        this.dateTime = dateTime;
        this.price = price.setScale(
                MoneyConfig.MONEY_SCALE,
                MoneyConfig.ROUNDING_MODE
        );
        this.volume = volume;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getAssetId() {
        return assetId;
    }

    public void setAssetId(int assetId) {
        this.assetId = assetId;
    }

    public LocalDateTime getDateTime() {
        return dateTime;
    }

    public void setDateTime(LocalDateTime dateTime) {
        this.dateTime = dateTime;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price.setScale(
                MoneyConfig.MONEY_SCALE,
                MoneyConfig.ROUNDING_MODE
        );
    }

    public int getVolume() {
        return volume;
    }

    public void setVolume(int volume) {
        this.volume = volume;
    }
}
