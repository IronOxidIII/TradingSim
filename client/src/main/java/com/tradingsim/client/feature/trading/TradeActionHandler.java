package com.tradingsim.client.feature.trading;

import android.content.Context;
import android.widget.Toast;

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