package com.tradingsim.service.trading;

import java.math.BigDecimal;

public interface TradingService {

    void buyAsset(int userId, int assetId, BigDecimal amount);

    void sellAsset(int userId, int assetId, BigDecimal amount);
}