package com.tradingsim.client;

import com.tradingsim.client.feature.main.MainActivity;

import timber.log.Timber;

public class Application extends android.app.Application {

    @Override
    public void onCreate() {
        Timber.plant();
        Timber.i("Application is starting");

        super.onCreate();
        new MainActivity();
    }
}
