package com.tradingsim.client.feature.trading;

import com.github.mikephil.charting.charts.LineChart;
import com.tradingsim.client.domain.model.TradingAsset;

public interface ChartRenderer {

    void render(
            LineChart lineChart,
            TradingAsset asset
    );
}