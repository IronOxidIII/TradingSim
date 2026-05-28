package com.tradingsim.client.feature.trading.ui;

import android.os.Bundle;
import android.text.Editable;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.tradingsim.client.core.ui.SimpleTextWatcher;
import com.tradingsim.client.data.repository.trading.InMemoryTradingRepository;
import com.tradingsim.client.data.repository.trading.TradingRepository;
import com.github.mikephil.charting.charts.LineChart;
import com.tradingsim.client.R;
import com.tradingsim.client.domain.model.TradingAsset;
import com.tradingsim.client.feature.trading.viewmodel.TradingViewModel;
import com.tradingsim.client.feature.trading.viewmodel.TradingViewModelFactory;
import com.tradingsim.client.feature.trading.adapter.AssetAdapter;
import com.tradingsim.client.feature.trading.chart.ChartRenderer;
import com.tradingsim.client.feature.trading.chart.TradingChartRenderer;
import com.tradingsim.client.feature.trading.handler.TradeActionHandler;
import com.tradingsim.client.feature.trading.handler.TradeActionProcessor;

public class TradingActivity extends AppCompatActivity {

    private TradingViewModel viewModel;
    private TextView tvSelectedAsset;
    private TextView tvAssetPrice;
    private RecyclerView recyclerAssets;
    private EditText etAmount;
    private EditText etLeverage;
    private Button btnBuy;
    private Button btnSell;
    private LineChart lineChart;

    private final ChartRenderer chartRenderer =
            new TradingChartRenderer();
    private final TradeActionProcessor tradeActionHandler =
            new TradeActionHandler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trading);

        TradingRepository repository =
                new InMemoryTradingRepository();

        TradingViewModelFactory factory =
                new TradingViewModelFactory(repository);

        viewModel = new ViewModelProvider(
                this,
                factory
        ).get(TradingViewModel.class);

        bindViews();
        setupAssetList();
        setupListeners();
        setupTextWatchers();
        observeViewModel();
    }

    private void bindViews() {
        tvSelectedAsset = findViewById(R.id.tvSelectedAsset);
        tvAssetPrice = findViewById(R.id.tvAssetPrice);
        recyclerAssets = findViewById(R.id.recyclerAssets);
        etAmount = findViewById(R.id.etAmount);
        etLeverage = findViewById(R.id.etLeverage);
        btnBuy = findViewById(R.id.btnBuy);
        btnSell = findViewById(R.id.btnSell);
        lineChart = findViewById(R.id.lineChart);
    }

    private void setupAssetList() {
        AssetAdapter adapter = new AssetAdapter(
                viewModel.getAssetSymbols(),
                asset -> viewModel.selectAsset(asset)
        );

        recyclerAssets.setLayoutManager(
                new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        );

        recyclerAssets.setAdapter(adapter);
    }

    private void setupListeners() {
        btnBuy.setOnClickListener(
                v -> handleTradeAction(getString(R.string.buy_operation))
        );

        btnSell.setOnClickListener(
                v -> handleTradeAction(getString(R.string.sell_operation))
        );
    }

    private void setupTextWatchers() {

        etAmount.addTextChangedListener(
                new SimpleTextWatcher() {
                    @Override
                    public void afterTextChanged(Editable s) {
                        viewModel.setAmount(s.toString());
                    }
                }
        );

        etLeverage.addTextChangedListener(
                new SimpleTextWatcher() {
                    @Override
                    public void afterTextChanged(Editable s) {
                        viewModel.setLeverage(s.toString());
                    }
                }
        );
    }

    private void observeViewModel() {

        viewModel.getSelectedAsset().observe(this, assetSymbol -> {
            if (assetSymbol != null) {
                updateAsset(assetSymbol);
            }
        });

        viewModel.getAmount().observe(this, value -> {
            if (!etAmount.getText().toString().equals(value)) {
                etAmount.setText(value);
            }
        });

        viewModel.getLeverage().observe(this, value -> {
            if (!etLeverage.getText().toString().equals(value)) {
                etLeverage.setText(value);
            }
        });
    }

    private void updateAsset(String assetSymbol) {
        TradingAsset asset = viewModel.getAssetBySymbol(assetSymbol);

        tvSelectedAsset.setText(asset.getDisplaySymbol());

        tvAssetPrice.setText(
                getString(
                        R.string.trading_asset_price,
                        asset.getPrice().toString()
                )
        );

        chartRenderer.render(lineChart, asset);
    }

    private void handleTradeAction(String type) {

        String selectedAsset = viewModel.getSelectedAsset().getValue();

        if (selectedAsset == null) {
            return;
        }

        tradeActionHandler.handle(
                this,
                type,
                selectedAsset,
                viewModel.getAmount().getValue(),
                viewModel.getLeverage().getValue()
        );
    }
}