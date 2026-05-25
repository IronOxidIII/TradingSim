package com.tradingsim.client.data.repository;

import com.tradingsim.client.domain.model.PortfolioAsset;

import java.util.List;

public interface PortfolioRepository {

    List<PortfolioAsset> getPortfolioAssets();
}