package com.tradingsim.client;

import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class PortfolioActivity extends AppCompatActivity {

    private TextView tvPortfolioValue;
    private RecyclerView recyclerPortfolio;
    private ArrayList<PortfolioAsset> assets;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_portfolio);

        tvPortfolioValue = findViewById(R.id.tvPortfolioValue);
        recyclerPortfolio = findViewById(R.id.recyclerPortfolio);

        assets = new ArrayList<>();
        assets.add(new PortfolioAsset("BTC", 0.25, 65000));
        assets.add(new PortfolioAsset("ETH", 2.0, 3500));
        assets.add(new PortfolioAsset("SOL", 15.0, 140));

        double totalValue = 0;
        for (PortfolioAsset asset : assets) {
            totalValue += asset.getTotalValue();
        }

        tvPortfolioValue.setText("Стоимость портфеля: " + totalValue + " USDT");

        PortfolioAdapter adapter = new PortfolioAdapter(assets);
        recyclerPortfolio.setLayoutManager(new LinearLayoutManager(this));
        recyclerPortfolio.setAdapter(adapter);
    }
}