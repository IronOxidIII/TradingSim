package com.tradingsim.client.network;

import java.util.Map;

import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import timber.log.Timber;

public class HttpClient {

    private static final OkHttpClient client = new OkHttpClient();

    public static <T> void get(
            String url,
            Map<String, String> params,
            Class<T> responseClass,
            Callback<T> callback) {
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

        client.newCall(request).enqueue(new NetworkResponseCallback<T>(
                url,
                responseClass,
                callback
        ));
    }
}