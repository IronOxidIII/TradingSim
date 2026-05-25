package com.tradingsim.client.feature.trading;

import android.content.Context;

public interface TradeActionProcessor {

    void handle(
            Context context,
            String type,
            String asset,
            String amount,
            String leverage
    );
}