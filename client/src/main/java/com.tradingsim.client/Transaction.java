package com.tradingsim.client;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class Transaction {

    private final String type;
    private final BigDecimal amount;
    private final BigDecimal price;
    private final LocalDateTime dateTime;

    public Transaction(
            String type,
            BigDecimal amount,
            BigDecimal price,
            LocalDateTime dateTime
    ) {
        this.type = type;
        this.amount = amount;
        this.price = price;
        this.dateTime = dateTime;
    }

    public String getType() {
        return type;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public LocalDateTime getDateTime() {
        return dateTime;
    }

    public BigDecimal getTotal() {
        return amount.multiply(price);
    }
}