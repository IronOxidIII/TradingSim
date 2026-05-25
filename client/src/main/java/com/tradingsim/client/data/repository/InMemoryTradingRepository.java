package com.tradingsim.client.data.repository;

import com.tradingsim.client.data.local.TradingAssetsProvider;
import com.tradingsim.client.domain.model.TradingAsset;

import java.util.List;

public class InMemoryTradingRepository implements TradingRepository {

    @Override
    public List<String> getAssetSymbols() {
        return TradingAssetsProvider.getAssetSymbols();
    }

    @Override
    public TradingAsset getAssetBySymbol(String symbol) {
        return TradingAssetsProvider.getAssetBySymbol(symbol);
    }
}