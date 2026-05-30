package com.tradingsim.common.dto.dto.asset;

import java.time.LocalDateTime;

public class PriceHistoryDto {

    private LocalDateTime time;
    private String price;
    private int volume;

    public PriceHistoryDto() {
    }

    public PriceHistoryDto(LocalDateTime time, String price, int volume) {
        this.time = time;
        this.price = price;
        this.volume = volume;
    }

    public LocalDateTime getTime() {
        return time;
    }

    public void setTime(LocalDateTime time) {
        this.time = time;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public int getVolume() {
        return volume;
    }

    public void setVolume(int volume) {
        this.volume = volume;
    }
}
