package com.tradingsim.client.network;

import java.io.IOException;
import java.io.InputStream;
import java.util.Map;
import java.util.logging.Logger;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import timber.log.Timber;

public class HttpClient {

    private static final OkHttpClient client = new OkHttpClient();

    public static void get(String url, Map<String, String> params, HttpCallback callback) {
        HttpUrl.Builder builder;
        try {
            builder = HttpUrl.parse(url).newBuilder();
        } catch (NullPointerException e) {
            Timber.e(e);
            throw e;
        }

        for (Map.Entry<String, String> entry : params.entrySet()) {
            builder.addQueryParameter(entry.getKey(), entry.getValue());
        }

        Request request = new Request.Builder()
                .url(builder.build())
                .get()
                .build();

        Timber.i("Created request: headers = %s", request);

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                callback.onError(e);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                Timber.i("Received response: %s", response);
                callback.onSuccess(response.body().string());
            }
        });
    }

    public interface HttpCallback {
        void onSuccess(String response);
        void onError(Exception e);
    }
}