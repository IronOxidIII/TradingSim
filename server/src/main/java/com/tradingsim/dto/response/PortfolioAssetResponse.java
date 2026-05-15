package com.tradingsim.dto.response;

import java.math.BigDecimal;

public record PortfolioAssetResponse(
        int assetId,
        BigDecimal amount
) {}
