package com.tradingsim.common.dto.dto.portfolio;

public class PortfolioAssetDto {

    private int asset_id;
    private String amount;

    public PortfolioAssetDto() {
    }

    public PortfolioAssetDto(int asset_id, String amount) {
        this.asset_id = asset_id;
        this.amount = amount;
    }

    public int getAsset_id() {
        return asset_id;
    }

    public void setAsset_id(int asset_id) {
        this.asset_id = asset_id;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }
}