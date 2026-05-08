package com.tradingsim.client;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private Button btnPortfolio;
    private Button btnTrading;
    private Button btnHistory;
    private Button btnCharts;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnPortfolio = findViewById(R.id.btnPortfolio);
        btnTrading = findViewById(R.id.btnTrading);
        btnHistory = findViewById(R.id.btnHistory);
        btnPortfolio.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, PortfolioActivity.class);
            startActivity(intent);
        });

        btnTrading.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, TradingActivity.class);
            startActivity(intent);
        });

        btnHistory.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, HistoryActivity.class);
            startActivity(intent);
        });
    }
}