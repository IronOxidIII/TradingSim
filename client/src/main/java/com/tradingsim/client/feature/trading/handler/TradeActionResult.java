package com.tradingsim.client.feature.trading.handler;

public class TradeActionResult {

    private final boolean success;
    private final String message;

    public TradeActionResult(boolean success, String message) {
        this.success = success;
        this.message = message;
    }

    public boolean isSuccess() {
        return success;
    }

    public String getMessage() {
        return message;
    }
}