package com.tradingsim.client;

import android.graphics.Color;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;

import java.util.ArrayList;
import java.util.Arrays;

public class TradingActivity extends AppCompatActivity {

    private TextView tvSelectedAsset;
    private TextView tvAssetPrice;
    private RecyclerView recyclerAssets;
    private EditText etAmount;
    private EditText etLeverage;
    private Button btnBuy;
    private Button btnSell;
    private LineChart lineChart;

    private String currentAsset = "BTC/USDT";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trading);

        tvSelectedAsset = findViewById(R.id.tvSelectedAsset);
        tvAssetPrice = findViewById(R.id.tvAssetPrice);
        recyclerAssets = findViewById(R.id.recyclerAssets);
        etAmount = findViewById(R.id.etAmount);
        etLeverage = findViewById(R.id.etLeverage);
        btnBuy = findViewById(R.id.btnBuy);
        btnSell = findViewById(R.id.btnSell);
        lineChart = findViewById(R.id.lineChart);

        setupAssetList();
        updateAsset("BTC/USDT");

        btnBuy.setOnClickListener(v -> makeTrade("покупка"));
        btnSell.setOnClickListener(v -> makeTrade("продажа"));
    }

    private void setupAssetList() {
        ArrayList<String> assets = new ArrayList<>(
                Arrays.asList("BTC", "ETH", "SOL", "BNB", "XRP", "DOGE")
        );

        AssetAdapter adapter = new AssetAdapter(assets, asset -> updateAsset(asset));

        recyclerAssets.setLayoutManager(
                new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        );

        recyclerAssets.setAdapter(adapter);
    }

    private void updateAsset(String asset) {
        currentAsset = asset;
        tvSelectedAsset.setText(asset);

        if (asset.equals("BTC/USDT")) {
            tvAssetPrice.setText("Цена: 65 000 USDT");
            setupChart(asset, 62000, 62500, 63000, 62800, 64000, 65000, 64500);
        } else if (asset.equals("ETH/USDT")) {
            tvAssetPrice.setText("Цена: 3 500 USDT");
            setupChart(asset, 3200, 3300, 3400, 3350, 3450, 3500, 3480);
        } else if (asset.equals("SOL/USDT")) {
            tvAssetPrice.setText("Цена: 140 USDT");
            setupChart(asset, 120, 125, 130, 128, 135, 140, 138);
        } else if (asset.equals("BNB/USDT")) {
            tvAssetPrice.setText("Цена: 600 USDT");
            setupChart(asset, 560, 575, 590, 580, 595, 600, 592);
        } else if (asset.equals("XRP/USDT")) {
            tvAssetPrice.setText("Цена: 0.62 USDT");
            setupChart(asset, 0.55f, 0.57f, 0.60f, 0.58f, 0.61f, 0.62f, 0.60f);
        } else if (asset.equals("DOGE/USDT")) {
            tvAssetPrice.setText("Цена: 0.15 USDT");
            setupChart(asset, 0.11f, 0.12f, 0.13f, 0.12f, 0.14f, 0.15f, 0.145f);
        }
    }

    private void setupChart(String label, float p1, float p2, float p3, float p4, float p5, float p6, float p7) {
        ArrayList<Entry> entries = new ArrayList<>();

        entries.add(new Entry(0, p1));
        entries.add(new Entry(1, p2));
        entries.add(new Entry(2, p3));
        entries.add(new Entry(3, p4));
        entries.add(new Entry(4, p5));
        entries.add(new Entry(5, p6));
        entries.add(new Entry(6, p7));

        LineDataSet dataSet = new LineDataSet(entries, label);
        dataSet.setColor(Color.GREEN);
        dataSet.setValueTextColor(Color.WHITE);
        dataSet.setLineWidth(3f);
        dataSet.setCircleColor(Color.GREEN);

        LineData lineData = new LineData(dataSet);
        lineChart.setData(lineData);

        Description description = new Description();
        description.setText("График цены");
        lineChart.setDescription(description);

        lineChart.invalidate();
    }

    private void makeTrade(String type) {
        String amount = etAmount.getText().toString();
        String leverage = etLeverage.getText().toString();

        if (amount.isEmpty()) {
            Toast.makeText(this, "Введите количество", Toast.LENGTH_SHORT).show();
            return;
        }

        if (leverage.isEmpty()) {
            leverage = "1";
        }

        Toast.makeText(
                this,
                "Операция: " + type +
                        ", актив: " + currentAsset +
                        ", количество: " + amount +
                        ", плечо: x" + leverage,
                Toast.LENGTH_LONG
        ).show();
    }
}