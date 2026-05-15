package com.tradingsim.service.market;

import com.tradingsim.model.PriceHistory;

import java.math.BigDecimal;
import java.util.List;

public interface MarketService {

    BigDecimal getCurrentPrice(int assetId);

    BigDecimal updatePrice(int assetId);

    List<PriceHistory> getPriceHistory(int assetId);

    void refreshAllPrices();

    BigDecimal createInitialPrice(int assetId);
}