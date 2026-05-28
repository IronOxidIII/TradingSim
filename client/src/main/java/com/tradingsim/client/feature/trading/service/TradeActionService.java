package com.tradingsim.client.feature.trading.service;

import com.tradingsim.client.feature.trading.handler.TradeActionResult;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
public class TradeActionService {
    private static final String DEFAULT_LEVERAGE = "1";
    private static final String EMPTY_AMOUNT_MESSAGE = "Введите количество";
    private static final String INVALID_AMOUNT_MESSAGE = "Некорректное количество";
    private static final String POSITIVE_AMOUNT_MESSAGE = "Количество должно быть больше 0";
    private static final String INVALID_LEVERAGE_MESSAGE = "Некорректное плечо";
    private static final String AVAILABLE_LEVERAGE_MESSAGE = "Доступное плечо: 1, 2, 5, 10";
    private static final List<Integer> AVAILABLE_LEVERAGES =
            Arrays.asList(1, 2, 5, 10);
    public TradeActionResult process(
            String type,
            String asset,
            String amount,
            String leverage
    ) {
        if (amount == null || amount.isEmpty()) {
            return new TradeActionResult(false, EMPTY_AMOUNT_MESSAGE);
        }

        BigDecimal amountValue;

        try {
            amountValue = new BigDecimal(amount);
        } catch (NumberFormatException e) {
            return new TradeActionResult(false, INVALID_AMOUNT_MESSAGE);
        }

        if (amountValue.compareTo(BigDecimal.ZERO) <= 0) {
            return new TradeActionResult(false, POSITIVE_AMOUNT_MESSAGE);
        }

        if (leverage == null || leverage.isEmpty()) {
            leverage = DEFAULT_LEVERAGE;
        }

        int leverageValue;

        try {
            leverageValue = Integer.parseInt(leverage);
        } catch (NumberFormatException e) {
            return new TradeActionResult(false, INVALID_LEVERAGE_MESSAGE);
        }

        if (!AVAILABLE_LEVERAGES.contains(leverageValue)) {
            return new TradeActionResult(false, AVAILABLE_LEVERAGE_MESSAGE);
        }

        return new TradeActionResult(
                true,
                "Операция: " + type +
                        ", актив: " + asset +
                        ", количество: " + amount +
                        ", плечо: x" + leverage
        );
    }
}