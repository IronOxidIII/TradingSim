package com.tradingsim.service.portfolio;

import com.tradingsim.model.Portfolio;
import com.tradingsim.model.PortfolioAsset;

import java.math.BigDecimal;
import java.util.List;

public interface PortfolioService {

    Portfolio createPortfolio(int userId, BigDecimal startSum);

    Portfolio save(Portfolio portfolio);

    Portfolio getPortfolioByUserId(int userId);

    List<PortfolioAsset> getPortfolioAssets(int userId);

    void addAsset(int userId, int assetId, BigDecimal amount);

    void removeAsset(int userId, int assetId, BigDecimal amount);

    void decreaseCash(int userId, BigDecimal amount);

    void increaseCash(int userId, BigDecimal amount);
}