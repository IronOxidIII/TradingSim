package com.tradingsim.service.statistics;

import com.tradingsim.model.Portfolio;
import com.tradingsim.model.PortfolioAsset;
import com.tradingsim.model.Transaction;
import com.tradingsim.model.TransactionType;
import com.tradingsim.repository.TransactionRepository;
import com.tradingsim.service.market.MarketService;
import com.tradingsim.service.portfolio.PortfolioService;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Deque;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class StatisticService {

    private final PortfolioService portfolioService;
    private final MarketService marketService;
    private final TransactionRepository transactionRepository;

    public StatisticService(
            PortfolioService portfolioService,
            MarketService marketService,
            TransactionRepository transactionRepository
    ) {
        this.portfolioService = portfolioService;
        this.marketService = marketService;
        this.transactionRepository = transactionRepository;
    }

    public double getPnL(int userId) {
        Portfolio portfolio = portfolioService.getPortfolioByUserId(userId);
        double currentValue = getCurrentPortfolioValue(portfolio);
        return currentValue - portfolio.getStartSum();
    }

    public double getRdi(int userId) {
        Portfolio portfolio = portfolioService.getPortfolioByUserId(userId);

        if (portfolio.getStartSum() <= 0) {
            throw new IllegalStateException(
                    "Start sum must be positive to calculate RDI"
            );
        }

        double pnl = getPnL(userId);
        return (pnl / portfolio.getStartSum()) * 100.0;
    }

    public double getWinRate(int userId) {
        List<Transaction> transactions = getSortedTransactions(userId);

        List<Double> tradeReturns = calculateClosedTradeReturns(transactions);

        if (tradeReturns.isEmpty()) {
            return 0.0;
        }

        long wins = tradeReturns.stream()
                .filter(r -> r > 0)
                .count();

        return (wins * 100.0) / tradeReturns.size();
    }

    public double getSharpeRatio(int userId) {
        List<Transaction> transactions = getSortedTransactions(userId);

        List<Double> tradeReturns = calculateClosedTradeReturns(transactions);

        if (tradeReturns.size() < 2) {
            return 0.0;
        }

        double mean = tradeReturns.stream()
                .mapToDouble(Double::doubleValue)
                .average()
                .orElse(0.0);

        double variance = tradeReturns.stream()
                .mapToDouble(r -> Math.pow(r - mean, 2))
                .sum() / (tradeReturns.size() - 1);

        double stdDev = Math.sqrt(variance);

        if (stdDev == 0.0) {
            return 0.0;
        }

        return mean / stdDev;
    }

    public List<Transaction> getPastTransactions(int userId) {
        return getSortedTransactions(userId);
    }

    private double getCurrentPortfolioValue(Portfolio portfolio) {
        double total = portfolio.getCashBalance();

        for (PortfolioAsset portfolioAsset :
                portfolio.getPortfolioAssets()) {
            double currentPrice = marketService.getCurrentPrice(
                    portfolioAsset.getAssetId()
            );

            total += currentPrice * portfolioAsset.getAmount();
        }

        return total;
    }

    private List<Transaction> getSortedTransactions(int userId) {
        return transactionRepository.findByUserId(userId).stream()
                .sorted(Comparator.comparing(Transaction::getDateTime))
                .toList();
    }

    private List<Double> calculateClosedTradeReturns(
            List<Transaction> transactions
    ) {
        Map<Integer, Deque<PositionLot>> openLotsByAsset = new HashMap<>();
        List<Double> tradeReturns = new ArrayList<>();

        for (Transaction transaction : transactions) {
            if (transaction.getType() == null) {
                throw new IllegalStateException(
                        "Transaction type is missing for transaction id " +
                                transaction.getId()
                );
            }

            if (transaction.getType() == TransactionType.BUY) {
                openLotsByAsset
                        .computeIfAbsent(
                                transaction.getAssetId(),
                                k -> new ArrayDeque<>())
                        .addLast(new PositionLot(
                                transaction.getAmount(),
                                transaction.getAssetPrice()
                        ));
            } else if (transaction.getType() == TransactionType.SELL) {
                TradeResult result = closePosition(
                        openLotsByAsset,
                        transaction.getAssetId(),
                        transaction.getAmount(),
                        transaction.getAssetPrice()
                );

                if (result.costBasis > 0) {
                    tradeReturns.add(result.profit / result.costBasis);
                }
            }
        }

        return tradeReturns;
    }

    private TradeResult closePosition(
            Map<Integer, Deque<PositionLot>> openLotsByAsset,
            int assetId,
            double amount,
            double sellPrice
    ) {
        Deque<PositionLot> lots = openLotsByAsset.get(assetId);

        if (lots == null || lots.isEmpty()) {
            throw new IllegalStateException(
                    "Sell transaction without open buy position for asset " + assetId
            );
        }

        double remaining = amount;
        double costBasis = 0.0;
        final double EPS = 1e-9;

        while (remaining > EPS) {
            PositionLot lot = lots.peekFirst();

            if (lot == null) {
                throw new IllegalStateException(
                        "Not enough open position for asset " + assetId
                );
            }

            double used = Math.min(remaining, lot.amount);
            costBasis += used * lot.price;
            lot.amount -= used;
            remaining -= used;

            if (lot.amount <= EPS) {
                lots.removeFirst();
            }
        }

        double proceeds = amount * sellPrice;
        double profit = proceeds - costBasis;

        return new TradeResult(costBasis, proceeds, profit);
    }

    private static final class PositionLot {
        private double amount;
        private final double price;

        private PositionLot(double amount, double price) {
            this.amount = amount;
            this.price = price;
        }
    }

    private static final class TradeResult {
        private final double costBasis;
        private final double proceeds;
        private final double profit;

        private TradeResult(
                double costBasis,
                double proceeds,
                double profit
        ) {
            this.costBasis = costBasis;
            this.proceeds = proceeds;
            this.profit = profit;
        }
    }
}
