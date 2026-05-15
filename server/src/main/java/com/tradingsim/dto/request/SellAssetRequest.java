package com.tradingsim.dto.request;

import java.math.BigDecimal;

public record SellAssetRequest(
        int assetId,
        BigDecimal amount
) {}
