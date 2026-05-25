package com.tradingsim.client.data.repository;

import com.tradingsim.client.domain.model.TradingAsset;

import java.util.List;

public interface TradingRepository {

    List<String> getAssetSymbols();

    TradingAsset getAssetBySymbol(String symbol);
}