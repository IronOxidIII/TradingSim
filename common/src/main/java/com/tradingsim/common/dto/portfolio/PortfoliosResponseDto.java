package com.tradingsim.common.dto.portfolio;

import java.util.List;

public class PortfoliosResponseDto {

    private List<PortfolioDto> portfolios;

    public PortfoliosResponseDto() {
    }

    public List<PortfolioDto> getPortfolios() {
        return portfolios;
    }

    public void setPortfolios(List<PortfolioDto> portfolios) {
        this.portfolios = portfolios;
    }
}