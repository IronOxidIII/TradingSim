package com.tradingsim.client.network;

import com.google.gson.Gson;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Response;
import timber.log.Timber;

public class NetworkResponseCallback<T>
        implements okhttp3.Callback {

    private static final Gson gson = new Gson();

    private final Class<T> responseClass;
    private final Callback<T> callback;
    private final String url;

    public NetworkResponseCallback(
            String url,
            Class<T> responseClass,
            Callback<T> callback
    ) {
        this.url = url;
        this.responseClass = responseClass;
        this.callback = callback;
    }

    @Override
    public void onResponse(
            Call call,
            Response response
    ) throws IOException {
        String body = response.body().string();
        Timber.d("Response from %s: %s", url, body);
        try {
            T result = gson.fromJson(body, responseClass);
            callback.onSuccess(result);
        } catch (Exception e) {
            Timber.e(e, "Failed to deserialize %s", url);
            callback.onError(e);
        }
    }

    @Override
    public void onFailure(
            Call call,
            IOException e
    ) {
        Timber.e(e);
        callback.onError(e);
    }
}