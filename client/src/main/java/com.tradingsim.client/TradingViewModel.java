package com.tradingsim.client;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class TradingViewModel extends ViewModel {

    private final MutableLiveData<String> selectedAsset =
            new MutableLiveData<>(TradingAssetsProvider.BTC_USDT);

    private final MutableLiveData<String> amount =
            new MutableLiveData<>("");

    private final MutableLiveData<String> leverage =
            new MutableLiveData<>("1");

    public LiveData<String> getSelectedAsset() {
        return selectedAsset;
    }

    public LiveData<String> getAmount() {
        return amount;
    }

    public LiveData<String> getLeverage() {
        return leverage;
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