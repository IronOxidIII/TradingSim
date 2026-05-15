package com.tradingsim.client;

import android.graphics.Color;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;

import java.util.ArrayList;

public class TradingChartRenderer {

    private static final String CHART_DESCRIPTION = "График цены";

    public void render(LineChart lineChart, TradingAsset asset) {

        ArrayList<Entry> entries = new ArrayList<>();

        float[] prices = asset.getChartPrices();

        for (int i = 0; i < prices.length; i++) {
            entries.add(new Entry(i, prices[i]));
        }

        LineDataSet dataSet = new LineDataSet(entries, asset.getSymbol());

        dataSet.setColor(Color.GREEN);
        dataSet.setValueTextColor(Color.WHITE);
        dataSet.setLineWidth(3f);
        dataSet.setCircleColor(Color.GREEN);

        LineData lineData = new LineData(dataSet);

        lineChart.setData(lineData);

        Description description = new Description();
        description.setText(CHART_DESCRIPTION);

        lineChart.setDescription(description);

        lineChart.invalidate();
    }
}