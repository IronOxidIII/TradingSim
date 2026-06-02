package com.tradingsim.client;

import com.tradingsim.client.feature.main.MainActivity;

import timber.log.Timber;

public class Application extends android.app.Application {

    @Override
    public void onCreate() {
        super.onCreate();

        Timber.plant(new Timber.DebugTree());
        Timber.i("Application is starting");

    }
}
