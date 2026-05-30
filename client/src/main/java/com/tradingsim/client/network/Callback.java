package com.tradingsim.client.network;

import android.os.Handler;

public interface Callback<T> {
    void onComplete(T result);
    void onError(Exception e);
}
