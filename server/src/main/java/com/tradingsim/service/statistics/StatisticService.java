package com.tradingsim.service.statistics;

import com.tradingsim.model.Transaction;

import java.math.BigDecimal;
import java.util.List;

public interface StatisticService {

    BigDecimal getPnL(int userId);

    BigDecimal getRdi(int userId);

    BigDecimal getWinRate(int userId);

    BigDecimal getSharpeRatio(int userId);

    List<Transaction> getPastTransactions(int userId);
}