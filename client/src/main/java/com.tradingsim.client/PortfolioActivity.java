package com.tradingsim.client;

import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.math.BigDecimal;
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
        assets.add(new PortfolioAsset("BTC", new BigDecimal("0.25"), new BigDecimal("65000")));
        assets.add(new PortfolioAsset("ETH", new BigDecimal("2.0"), new BigDecimal("3500")));
        assets.add(new PortfolioAsset("SOL", new BigDecimal("15.0"), new BigDecimal("140")));

        BigDecimal totalValue = BigDecimal.ZERO;

        for (PortfolioAsset asset : assets) {
            totalValue = totalValue.add(asset.getTotalValue());
        }

        tvPortfolioValue.setText("Стоимость портфеля: " + totalValue + " USDT");

        PortfolioAdapter adapter = new PortfolioAdapter(assets);
        recyclerPortfolio.setLayoutManager(new LinearLayoutManager(this));
        recyclerPortfolio.setAdapter(adapter);
    }
}