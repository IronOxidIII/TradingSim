package com.tradingsim.client.data.repository.history;

import com.tradingsim.client.domain.model.Transaction;

import java.util.List;

public interface HistoryRepository {

    List<Transaction> getTransactions();
}