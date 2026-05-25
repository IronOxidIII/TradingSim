package com.tradingsim.client.data.repository;

import com.tradingsim.client.domain.model.Transaction;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class InMemoryHistoryRepository implements HistoryRepository {

    @Override
    public List<Transaction> getTransactions() {
        List<Transaction> transactions = new ArrayList<>();

        transactions.add(new Transaction(
                "ПОКУПКА BTC",
                new BigDecimal("0.25"),
                new BigDecimal("65000"),
                LocalDateTime.now()
        ));

        transactions.add(new Transaction(
                "ПРОДАЖА ETH",
                new BigDecimal("1.0"),
                new BigDecimal("3500"),
                LocalDateTime.now().minusHours(3)
        ));

        transactions.add(new Transaction(
                "ПОКУПКА SOL",
                new BigDecimal("15.0"),
                new BigDecimal("140"),
                LocalDateTime.now().minusDays(1)
        ));

        return transactions;
    }
}