package com.tradingsim.dto.response;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record TransactionResponse(
        int id,
        int assetId,
        BigDecimal amount,
        BigDecimal price,
        BigDecimal sum,
        String type,
        LocalDateTime dateTime
) {}
