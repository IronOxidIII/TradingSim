package com.tradingsim.client.feature.trading.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.tradingsim.client.data.repository.trading.TradingRepository;
import com.tradingsim.client.domain.model.TradingAsset;

import java.util.List;

public class TradingViewModel extends ViewModel {

    private final MutableLiveData<String> selectedAsset =
            new MutableLiveData<>("BTC/USDT");

    private final MutableLiveData<String> amount =
            new MutableLiveData<>("");

    private final MutableLiveData<String> leverage =
            new MutableLiveData<>("1");

    private final TradingRepository repository;

    public TradingViewModel(TradingRepository repository) {
        this.repository = repository;
    }

    public LiveData<String> getSelectedAsset() {
        return selectedAsset;
    }

    public LiveData<String> getAmount() {
        return amount;
    }

    public LiveData<String> getLeverage() {
        return leverage;
    }

    public List<String> getAssetSymbols() {
        return repository.getAssetSymbols();
    }

    public TradingAsset getAssetBySymbol(String symbol) {
        return repository.getAssetBySymbol(symbol);
    }

    public void selectAsset(String asset) {
        selectedAsset.setValue(asset);
    }

    public void setAmount(String value) {
        amount.setValue(value);
    }

    public void setLeverage(String value) {
        leverage.setValue(value);
    }
}