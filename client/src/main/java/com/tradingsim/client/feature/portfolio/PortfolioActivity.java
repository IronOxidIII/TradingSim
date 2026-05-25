package com.tradingsim.client.feature.portfolio;

import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.tradingsim.client.R;
import com.tradingsim.client.data.repository.InMemoryPortfolioRepository;
import com.tradingsim.client.data.repository.PortfolioRepository;
import com.tradingsim.client.domain.model.PortfolioAsset;

import java.math.BigDecimal;
import java.util.ArrayList;

public class PortfolioActivity extends AppCompatActivity {

    private final PortfolioRepository repository =
            new InMemoryPortfolioRepository();

    private TextView tvPortfolioValue;
    private RecyclerView recyclerPortfolio;
    private ArrayList<PortfolioAsset> assets;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_portfolio);

        tvPortfolioValue = findViewById(R.id.tvPortfolioValue);
        recyclerPortfolio = findViewById(R.id.recyclerPortfolio);

        assets = new ArrayList<>(repository.getPortfolioAssets());

        BigDecimal totalValue = BigDecimal.ZERO;

        for (PortfolioAsset asset : assets) {
            totalValue = totalValue.add(asset.getTotalValue());
        }

        tvPortfolioValue.setText(
                getString(
                        R.string.portfolio_value,
                        totalValue.toString()
                )
        );

        PortfolioAdapter adapter = new PortfolioAdapter(assets);
        recyclerPortfolio.setLayoutManager(new LinearLayoutManager(this));
        recyclerPortfolio.setAdapter(adapter);
    }
}