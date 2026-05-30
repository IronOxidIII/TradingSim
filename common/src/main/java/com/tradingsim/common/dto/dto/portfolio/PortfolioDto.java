package com.tradingsim.common.dto.dto.portfolio;

import java.util.List;

public class PortfolioDto {

    private int user_id;
    private int start_sum;
    private double total_sum;

    private List<PortfolioAssetDto> portfolio_assets;

    public PortfolioDto() {
    }

    public PortfolioDto(
            int user_id,
            int start_sum,
            double total_sum,
            List<PortfolioAssetDto> portfolio_assets
    ) {
        this.user_id = user_id;
        this.start_sum = start_sum;
        this.total_sum = total_sum;
        this.portfolio_assets = portfolio_assets;
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public int getStart_sum() {
        return start_sum;
    }

    public void setStart_sum(int start_sum) {
        this.start_sum = start_sum;
    }

    public double getTotal_sum() {
        return total_sum;
    }

    public void setTotal_sum(double total_sum) {
        this.total_sum = total_sum;
    }

    public List<PortfolioAssetDto> getPortfolio_assets() {
        return portfolio_assets;
    }

    public void setPortfolio_assets(
            List<PortfolioAssetDto> portfolio_assets
    ) {
        this.portfolio_assets = portfolio_assets;
    }
}