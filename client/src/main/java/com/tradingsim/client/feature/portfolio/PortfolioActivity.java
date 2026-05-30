package com.tradingsim.client.feature.portfolio;

import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.tradingsim.client.R;
import com.tradingsim.client.data.repository.portfolio.NetworkPortfolioRepository;
import com.tradingsim.client.data.repository.portfolio.PortfolioRepository;
import com.tradingsim.client.domain.model.PortfolioAsset;
import com.tradingsim.client.network.Callback;

import java.math.BigDecimal;
import java.util.List;

import timber.log.Timber;

public class PortfolioActivity extends AppCompatActivity {
    private TextView tvPortfolioValue;
    private RecyclerView recyclerPortfolio;

    private NetworkPortfolioRepository repository = new NetworkPortfolioRepository();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_portfolio);

        tvPortfolioValue = findViewById(R.id.tvPortfolioValue);
        recyclerPortfolio = findViewById(R.id.recyclerPortfolio);

        PortfolioAdapter adapter = new PortfolioAdapter();
        recyclerPortfolio.setLayoutManager(new LinearLayoutManager(this));
        recyclerPortfolio.setAdapter(adapter);

        new Thread(() -> {
            repository.getPortfolioAssetsWithCallback(
                    new Callback<>() {
                @Override
                public void onComplete(List<PortfolioAsset> result) {
                    runOnUiThread(() -> {
                        adapter.submitList(result);

                        BigDecimal totalValue = BigDecimal.ZERO;
                        for (var asset : result) {
                            totalValue = totalValue.add(asset.getTotalValue());
                        }

                        tvPortfolioValue.setText(
                                getString(
                                        R.string.portfolio_value,
                                        totalValue.toString()
                                )
                        );
                    });
                }

                @Override
                public void onError(Exception e) {
                    Timber.i("Failed updating assets.");
                }
            });
        }).start();
    }
}