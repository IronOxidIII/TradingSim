package com.tradingsim.client;

import android.app.Application;

import timber.log.Timber;

public class TradingSimApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        Timber.plant(new Timber.DebugTree());
        Timber.i("Application is starting");
    }
}
