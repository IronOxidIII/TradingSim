package com.tradingsim.client.network;


public interface Callback<T> {
    void onSuccess(T result);
    void onError(Exception e);
}
