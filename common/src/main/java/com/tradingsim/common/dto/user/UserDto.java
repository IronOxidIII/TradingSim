package com.tradingsim.common.dto.user;

import com.tradingsim.common.dto.portfolio.PortfolioDto;

public class UserDto {

    private int id;
    private String username;
    private PortfolioDto portfolio;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public PortfolioDto getPortfolio() {
        return portfolio;
    }

    public void setPortfolio(PortfolioDto portfolio) {
        this.portfolio = portfolio;
    }
}