package com.tradingsim.client.data.repository.history;

import com.tradingsim.client.domain.model.Transaction;
import com.tradingsim.client.domain.model.TransactionType;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class InMemoryHistoryRepository implements HistoryRepository {

    @Override
    public List<Transaction> getTransactions() {
        List<Transaction> transactions = new ArrayList<>();

        transactions.add(new Transaction(
                TransactionType.BUY,
                new BigDecimal("0.25"),
                new BigDecimal("65000"),
                LocalDateTime.now()
        ));

        transactions.add(new Transaction(
                TransactionType.SELL,
                new BigDecimal("1.0"),
                new BigDecimal("3500"),
                LocalDateTime.now().minusHours(3)
        ));

        transactions.add(new Transaction(
                TransactionType.BUY,
                new BigDecimal("15.0"),
                new BigDecimal("140"),
                LocalDateTime.now().minusDays(1)
        ));

        return transactions;
    }
}