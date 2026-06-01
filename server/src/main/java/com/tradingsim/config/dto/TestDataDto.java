package com.tradingsim.config.dto;

import com.tradingsim.common.dto.asset.AssetDto;
import com.tradingsim.common.dto.portfolio.PortfolioDto;
import com.tradingsim.common.dto.transaction.TransactionDto;
import com.tradingsim.common.dto.user.UserDto;

import java.util.List;

public class TestDataDto {

    private List<AssetDto> assets;
    private List<UserDto> users;
    private List<TransactionDto> transactions;
    private List<PortfolioDto> portfolios;

    public List<AssetDto> getAssets() {
        return assets;
    }

    public void setAssets(List<AssetDto> assets) {
        this.assets = assets;
    }

    public List<UserDto> getUsers() {
        return users;
    }

    public void setUsers(List<UserDto> users) {
        this.users = users;
    }

    public List<TransactionDto> getTransactions() {
        return transactions;
    }

    public void setTransactions(List<TransactionDto> transactions) {
        this.transactions = transactions;
    }

    public List<PortfolioDto> getPortfolios() {
        return portfolios;
    }

    public void setPortfolios(List<PortfolioDto> portfolios) {
        this.portfolios = portfolios;
    }
}