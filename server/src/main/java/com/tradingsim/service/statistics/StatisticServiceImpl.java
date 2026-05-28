package com.tradingsim.service.statistics;

import com.tradingsim.config.MoneyConfig;
import com.tradingsim.exception.ConflictException;
import com.tradingsim.exception.InsufficientFundsException;
import com.tradingsim.exception.ValidationException;
import com.tradingsim.model.Portfolio;
import com.tradingsim.model.PortfolioAsset;
import com.tradingsim.model.Transaction;
import com.tradingsim.model.TransactionType;
import com.tradingsim.repository.TransactionRepository;
import com.tradingsim.service.market.MarketService;
import com.tradingsim.service.portfolio.PortfolioService;

import java.math.BigDecimal;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Deque;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class StatisticServiceImpl implements StatisticService {

    private final PortfolioService portfolioService;
    private final MarketService marketService;
    private final TransactionRepository transactionRepository;

    public StatisticServiceImpl(
            PortfolioService portfolioService,
            MarketService marketService,
            TransactionRepository transactionRepository
    ) {
        this.portfolioService = portfolioService;
        this.marketService = marketService;
        this.transactionRepository = transactionRepository;
    }

    public BigDecimal getPnL(int userId) {
        Portfolio portfolio = portfolioService.getPortfolioByUserId(userId);
        BigDecimal currentValue = getCurrentPortfolioValue(portfolio);
        return currentValue.subtract(portfolio.getStartSum());
    }

    public BigDecimal getRdi(int userId) {
        Portfolio portfolio = portfolioService.getPortfolioByUserId(userId);

        if (portfolio.getStartSum().compareTo(BigDecimal.ZERO) <= 0) {
            throw new ValidationException(
                    "Start sum must be positive to calculate RDI"
            );
        }

        BigDecimal pnl = getPnL(userId);
        return pnl
                .divide(
                        portfolio.getStartSum(),
                        MoneyConfig.MONEY_SCALE,
                        MoneyConfig.ROUNDING_MODE
                        )
                .multiply(BigDecimal.valueOf(100));
    }

    public BigDecimal getWinRate(int userId) {

        List<Transaction> transactions = getSortedTransactions(userId);
        List<BigDecimal> tradeReturns = calculateClosedTradeReturns(transactions);

        if (tradeReturns.isEmpty()) {
            return BigDecimal.ZERO;
        }

        long wins = tradeReturns.stream()
                .filter(r -> r.compareTo(BigDecimal.ZERO) > 0)
                .count();

        return BigDecimal.valueOf(wins)
                .multiply(BigDecimal.valueOf(100))
                .divide(
                        BigDecimal.valueOf(tradeReturns.size()),
                        MoneyConfig.MONEY_SCALE,
                        MoneyConfig.ROUNDING_MODE
                );
    }

    public BigDecimal getSharpeRatio(int userId) {

        List<BigDecimal> tradeReturns = calculateClosedTradeReturns(
                getSortedTransactions(userId)
        );

        if (tradeReturns.size() < 2) {
            return BigDecimal.ZERO;
        }

        List<Double> values = tradeReturns.stream()
                .map(BigDecimal::doubleValue)
                .toList();

        double mean = values.stream()
                .mapToDouble(Double::doubleValue)
                .average()
                .orElse(0.0);

        double variance = values.stream()
                .mapToDouble(r -> Math.pow(r - mean, 2))
                .sum() / (values.size() - 1);

        double stdDev = Math.sqrt(variance);

        if (stdDev == 0.0) {
            return BigDecimal.ZERO;
        }

        double sharpe = mean / stdDev;

        return BigDecimal.valueOf(sharpe)
                .setScale(
                        MoneyConfig.MONEY_SCALE,
                        MoneyConfig.ROUNDING_MODE
                );
    }

    public List<Transaction> getPastTransactions(int userId) {
        return getSortedTransactions(userId);
    }

    private BigDecimal getCurrentPortfolioValue(Portfolio portfolio) {
        BigDecimal total = portfolio.getCashBalance();

        for (PortfolioAsset portfolioAsset :
                portfolio.getPortfolioAssets()) {
            BigDecimal currentPrice = marketService.getCurrentPrice(
                    portfolioAsset.getAssetId()
            );

            total = total.add(currentPrice.multiply(portfolioAsset.getAmount()));
        }

        return total;
    }

    private List<Transaction> getSortedTransactions(int userId) {
        return transactionRepository.findByUserId(userId).stream()
                .sorted(Comparator.comparing(Transaction::getDateTime))
                .toList();
    }

    private List<BigDecimal> calculateClosedTradeReturns(
            List<Transaction> transactions
    ) {
        Map<Integer, Deque<PositionLot>> openLotsByAsset = new HashMap<>();
        List<BigDecimal> tradeReturns = new ArrayList<>();

        for (Transaction transaction : transactions) {
            if (transaction.getType() == null) {
                throw new ValidationException(
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

                if (result.costBasis.compareTo(BigDecimal.ZERO) > 0) {
                    tradeReturns.add(result.profit.divide(result.costBasis));
                }
            }
        }

        return tradeReturns;
    }

    private TradeResult closePosition(
            Map<Integer, Deque<PositionLot>> openLotsByAsset,
            int assetId,
            BigDecimal amount,
            BigDecimal sellPrice
    ) {

        Deque<PositionLot> lots = openLotsByAsset.get(assetId);

        if (lots == null || lots.isEmpty()) {
            throw new ConflictException(
                    "Sell transaction without open buy position for asset " + assetId
            );
        }

        BigDecimal remaining = amount;
        BigDecimal costBasis = BigDecimal.ZERO;

        while (remaining.compareTo(BigDecimal.ZERO) > 0) {

            PositionLot lot = lots.peekFirst();

            if (lot == null) {
                throw new InsufficientFundsException(
                        "Not enough open position for asset " + assetId
                );
            }

            BigDecimal used = remaining.min(lot.amount);

            costBasis = costBasis.add(
                    used.multiply(lot.price)
            );

            lot.amount = lot.amount.subtract(used);
            remaining = remaining.subtract(used);

            if (lot.amount.compareTo(BigDecimal.ZERO) == 0) {
                lots.removeFirst();
            }
        }

        BigDecimal proceeds = amount.multiply(sellPrice);
        BigDecimal profit = proceeds.subtract(costBasis);

        return new TradeResult(costBasis, proceeds, profit);
    }

    private static final class PositionLot {
        private BigDecimal amount;
        private final BigDecimal price;

        private PositionLot(BigDecimal amount, BigDecimal price) {
            this.amount = amount.setScale(
                    MoneyConfig.ASSET_SCALE,
                    MoneyConfig.ROUNDING_MODE
            );
            this.price = price.setScale(
                    MoneyConfig.MONEY_SCALE,
                    MoneyConfig.ROUNDING_MODE
            );
        }
    }

    private static final class TradeResult {
        private final BigDecimal costBasis;
        private final BigDecimal proceeds;
        private final BigDecimal profit;

        private TradeResult(
                BigDecimal costBasis,
                BigDecimal proceeds,
                BigDecimal profit
        ) {
            this.costBasis = costBasis.setScale(
                    MoneyConfig.MONEY_SCALE,
                    MoneyConfig.ROUNDING_MODE
            );
            this.proceeds = proceeds.setScale(
                    MoneyConfig.MONEY_SCALE,
                    MoneyConfig.ROUNDING_MODE
            );
            this.profit = profit.setScale(
                    MoneyConfig.MONEY_SCALE,
                    MoneyConfig.ROUNDING_MODE
            );
        }
    }
}
