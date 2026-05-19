package com.tradingsim.client;

import android.os.Bundle;

import java.time.LocalDateTime;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.math.BigDecimal;
import java.util.ArrayList;

public class HistoryActivity extends AppCompatActivity {

    private RecyclerView recyclerTransactions;
    private ArrayList<Transaction> transactions;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);

        recyclerTransactions = findViewById(R.id.recyclerTransactions);

        transactions = new ArrayList<>();

        transactions.add(
                new Transaction(
                        "ПОКУПКА BTC",
                        new BigDecimal("0.25"),
                        new BigDecimal("65000"),
                        LocalDateTime.now()
                )
        );

        transactions.add(
                new Transaction(
                        "ПРОДАЖА ETH",
                        new BigDecimal("1.0"),
                        new BigDecimal("3500"),
                        LocalDateTime.now().minusHours(3)
                )
        );

        transactions.add(
                new Transaction(
                        "ПОКУПКА SOL",
                        new BigDecimal("15.0"),
                        new BigDecimal("140"),
                        LocalDateTime.now().minusDays(1)
                )
        );

        TransactionAdapter adapter = new TransactionAdapter(transactions);

        recyclerTransactions.setLayoutManager(new LinearLayoutManager(this));
        recyclerTransactions.setAdapter(adapter);
    }
}