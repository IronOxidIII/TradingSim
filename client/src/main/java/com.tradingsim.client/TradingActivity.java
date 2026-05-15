package com.tradingsim.client;

import androidx.lifecycle.ViewModelProvider;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.github.mikephil.charting.charts.LineChart;

public class TradingActivity extends AppCompatActivity {

    private TradingViewModel viewModel;
    private static final String BUY_OPERATION = "покупка";
    private static final String SELL_OPERATION = "продажа";

    private TextView tvSelectedAsset;
    private TextView tvAssetPrice;
    private RecyclerView recyclerAssets;
    private EditText etAmount;
    private EditText etLeverage;
    private Button btnBuy;
    private Button btnSell;
    private LineChart lineChart;

    private String currentAsset = TradingAssetsProvider.BTC_USDT;

    private final TradingChartRenderer chartRenderer = new TradingChartRenderer();
    private final TradeActionHandler tradeActionHandler = new TradeActionHandler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trading);
        viewModel = new ViewModelProvider(this).get(TradingViewModel.class);

        bindViews();
        setupAssetList();
        setupListeners();
        viewModel.getSelectedAsset().observe(this, this::updateAsset);
        viewModel.getAmount().observe(this, value -> {
            etAmount.setText(value);
        });

        viewModel.getLeverage().observe(this, value -> {
            etLeverage.setText(value);
        });
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
                TradingAssetsProvider.getAssetSymbols(),
                asset -> viewModel.selectAsset(asset)
        );

        recyclerAssets.setLayoutManager(
                new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        );

        recyclerAssets.setAdapter(adapter);
    }

    private void setupListeners() {
        btnBuy.setOnClickListener(v -> handleTradeAction(BUY_OPERATION));
        btnSell.setOnClickListener(v -> handleTradeAction(SELL_OPERATION));
    }

    private void updateAsset(String assetSymbol) {
        TradingAsset asset = TradingAssetsProvider.getAssetBySymbol(assetSymbol);

        currentAsset = asset.getSymbol();
        tvSelectedAsset.setText(asset.getDisplaySymbol());
        tvAssetPrice.setText(asset.getPriceText());
        chartRenderer.render(lineChart, asset);
    }

    private void handleTradeAction(String type) {
        viewModel.setAmount(etAmount.getText().toString());
        viewModel.setLeverage(etLeverage.getText().toString());
        tradeActionHandler.handle(
                this,
                type,
                currentAsset,
                etAmount.getText().toString(),
                etLeverage.getText().toString()
        );
    }
}