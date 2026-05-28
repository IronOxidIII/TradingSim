package com.tradingsim.dto.request;

import java.math.BigDecimal;

public record BuyAssetRequest(
        int assetId,
        BigDecimal amount
) {}
