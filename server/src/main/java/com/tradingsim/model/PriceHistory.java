package com.tradingsim.model;

import java.time.LocalDateTime;

public class PriceHistory {

    private int id;
    private int assetId;
    private LocalDateTime dateTime;
    private double price;
    private int volume;

    public PriceHistory() {}

    public PriceHistory(
            int id,
            int assetId,
            LocalDateTime dateTime,
            double price,
            int volume
    ) {
        this.id = id;
        this.assetId = assetId;
        this.dateTime = dateTime;
        this.price = price;
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

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public int getVolume() {
        return volume;
    }

    public void setVolume(int volume) {
        this.volume = volume;
    }
}
