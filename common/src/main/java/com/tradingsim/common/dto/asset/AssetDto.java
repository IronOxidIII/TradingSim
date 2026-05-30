package com.tradingsim.common.dto.asset;

import java.util.List;

public class AssetDto {

    private int id;
    private String name;
    private List<PriceHistoryDto> price_history;

    public AssetDto() {
    }

    public AssetDto(int id, String name, List<PriceHistoryDto> price_history) {
        this.id = id;
        this.name = name;
        this.price_history = price_history;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<PriceHistoryDto> getPrice_history() {
        return price_history;
    }

    public void setPrice_history(List<PriceHistoryDto> price_history) {
        this.price_history = price_history;
    }
}