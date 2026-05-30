package com.tradingsim.common.dto.dto.asset;


import java.util.List;

public class AssetsResponseDto {

    private List<AssetDto> assets;

    public AssetsResponseDto() {
    }

    public List<AssetDto> getAssets() {
        return assets;
    }

    public void setAssets(List<AssetDto> assets) {
        this.assets = assets;
    }
}