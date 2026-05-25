package com.tradingsim.client.feature.trading;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.tradingsim.client.data.repository.TradingRepository;

public class TradingViewModelFactory
        implements ViewModelProvider.Factory {

    private final TradingRepository repository;

    public TradingViewModelFactory(
            TradingRepository repository
    ) {
        this.repository = repository;
    }

    @NonNull
    @Override
    @SuppressWarnings("unchecked")
    public <T extends ViewModel> T create(
            @NonNull Class<T> modelClass
    ) {
        return (T) new TradingViewModel(repository);
    }
}