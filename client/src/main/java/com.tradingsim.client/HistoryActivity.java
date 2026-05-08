package com.tradingsim.client;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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
        transactions.add(new Transaction("ПОКУПКА BTC", 0.25, 65000, "Сегодня"));
        transactions.add(new Transaction("ПРОДАЖА ETH", 1.0, 3500, "Сегодня"));
        transactions.add(new Transaction("ПОКУПКА SOL", 15.0, 140, "Вчера"));

        TransactionAdapter adapter = new TransactionAdapter(transactions);

        recyclerTransactions.setLayoutManager(new LinearLayoutManager(this));
        recyclerTransactions.setAdapter(adapter);
    }
}