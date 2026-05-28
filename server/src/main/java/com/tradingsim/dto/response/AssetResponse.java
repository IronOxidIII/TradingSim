package com.tradingsim.dto.response;

import java.math.BigDecimal;

public record AssetResponse(
        int id,
        String name,
        BigDecimal currentPrice
) {}
