package com.tradingsim.client.data.repository;

import com.tradingsim.client.domain.model.PortfolioAsset;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class InMemoryPortfolioRepository implements PortfolioRepository {

    @Override
    public List<PortfolioAsset> getPortfolioAssets() {
        List<PortfolioAsset> assets = new ArrayList<>();

        assets.add(new PortfolioAsset("BTC", new BigDecimal("0.25"), new BigDecimal("65000")));
        assets.add(new PortfolioAsset("ETH", new BigDecimal("2.0"), new BigDecimal("3500")));
        assets.add(new PortfolioAsset("SOL", new BigDecimal("15.0"), new BigDecimal("140")));

        return assets;
    }
}