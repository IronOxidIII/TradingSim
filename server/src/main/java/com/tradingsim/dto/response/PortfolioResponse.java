package com.tradingsim.dto.response;

import java.math.BigDecimal;
import java.util.List;

public record PortfolioResponse(
        int id,
        int userId,
        BigDecimal cashBalance,
        BigDecimal totalValue,
        List<PortfolioAssetResponse> assets
) {}
