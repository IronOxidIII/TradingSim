package com.tradingsim.client.domain.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class Transaction {

    private final TransactionType type;
    private final BigDecimal amount;
    private final BigDecimal price;
    private final LocalDateTime dateTime;

    public Transaction(
            TransactionType type,
            BigDecimal amount,
            BigDecimal price,
            LocalDateTime dateTime
    ) {
        this.type = type;
        this.amount = amount;
        this.price = price;
        this.dateTime = dateTime;
    }

    public TransactionType getType() {
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