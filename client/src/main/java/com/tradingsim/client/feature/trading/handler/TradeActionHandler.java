package com.tradingsim.client.feature.trading.handler;

import android.content.Context;
import android.widget.Toast;

import com.tradingsim.client.feature.trading.service.TradeActionService;

public class TradeActionHandler implements TradeActionProcessor {

    private final TradeActionService service =
            new TradeActionService();

    @Override
    public void handle(
            Context context,
            String type,
            String asset,
            String amount,
            String leverage
    ) {
        TradeActionResult result = service.process(
                type,
                asset,
                amount,
                leverage
        );

        Toast.makeText(
                context,
                result.getMessage(),
                result.isSuccess() ? Toast.LENGTH_LONG : Toast.LENGTH_SHORT
        ).show();
    }
}