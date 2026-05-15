package com.tradingsim.client;

import android.content.Context;
import android.widget.Toast;
import java.math.BigDecimal;

public class TradeActionHandler {

    private static final String DEFAULT_LEVERAGE = "1";
    private static final String EMPTY_AMOUNT_MESSAGE = "Введите количество";

    public void handle(
            Context context,
            String type,
            String asset,
            String amount,
            String leverage
    ) {

        if (amount.isEmpty()) {
            Toast.makeText(
                    context,
                    EMPTY_AMOUNT_MESSAGE,
                    Toast.LENGTH_SHORT
            ).show();

            return;
        }

        BigDecimal amountValue;

        try {
            amountValue = new BigDecimal(amount);
        } catch (NumberFormatException e) {
            Toast.makeText(
                    context,
                    "Некорректное количество",
                    Toast.LENGTH_SHORT
            ).show();

            return;
        }

        if (amountValue.compareTo(BigDecimal.ZERO) <= 0){
            Toast.makeText(
                    context,
                    "Количество должно быть больше 0",
                    Toast.LENGTH_SHORT
            ).show();

            return;
        }

        if (leverage.isEmpty()) {
            leverage = DEFAULT_LEVERAGE;
        }

        int leverageValue;

        try {
            leverageValue = Integer.parseInt(leverage);
        } catch (NumberFormatException e) {
            Toast.makeText(
                    context,
                    "Некорректное плечо",
                    Toast.LENGTH_SHORT
            ).show();

            return;
        }

        if (
                leverageValue != 1 &&
                        leverageValue != 2 &&
                        leverageValue != 5 &&
                        leverageValue != 10
        ) {
            Toast.makeText(
                    context,
                    "Доступное плечо: 1, 2, 5, 10",
                    Toast.LENGTH_SHORT
            ).show();

            return;
        }

        Toast.makeText(
                context,
                "Операция: " + type +
                        ", актив: " + asset +
                        ", количество: " + amount +
                        ", плечо: x" + leverage,
                Toast.LENGTH_LONG
        ).show();
    }
}