package com.tradingsim.client.data.repository.trading;

import com.tradingsim.client.data.local.TradingAssetsProvider;
import com.tradingsim.client.domain.model.TradingAsset;

import java.util.List;

public class InMemoryTradingRepository
        implements TradingRepository {

    private final TradingAssetsProvider provider =
            new TradingAssetsProvider();

    @Override
    public List<String> getAssetSymbols() {
        return provider.getAssetSymbols();
    }

    @Override
    public TradingAsset getAssetBySymbol(String symbol) {
        return provider.getAssetBySymbol(symbol);
    }
}