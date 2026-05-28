package com.tradingsim.dto.request;

import java.math.BigDecimal;

public record CreateAssetRequest(
        String name,
        BigDecimal initialPrice
) {}
