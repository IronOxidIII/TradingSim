package com.tradingsim.client.network;


public interface Callback<T> {
    void onComplete(T result);
    void onError(Exception e);
}
