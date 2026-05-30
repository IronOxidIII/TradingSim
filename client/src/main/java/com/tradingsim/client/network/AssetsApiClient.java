package com.tradingsim.client.network;

import com.google.gson.Gson;
import com.tradingsim.common.dto.asset.AssetsResponseDto;

import java.util.HashMap;
import java.util.Map;

import timber.log.Timber;

public class AssetsApiClient {

    private static final Gson gson = new Gson();

    public static void sendGetAssetsRequest(
            String serverHost,
            int serverPort,
            Integer id,
            String name,
            Integer page,
            Integer perPage,
            AssetsResponseCallback callback
    ) {
        Map<String, String> params = new HashMap<>();

        if (id != null) {
            params.put("id", id.toString());
        }

        if (name != null) {
            params.put("name", name);
        }

        if (page != null) {
            params.put("name", name);
        }

        if (perPage != null) {
            params.put("perPage", perPage.toString());
        }

        HttpClient.get(
                "http://" + serverHost + ":" + serverPort + "/Assets",
                params,
                new HttpClient.HttpCallback() {
                    @Override
                    public void onSuccess(String response) {
                        Timber.i("Starting converting response of /Assets...");
                        AssetsResponseDto assetsResponseDto;
                        try {
                            assetsResponseDto =
                                    gson.fromJson(response, AssetsResponseDto.class);
                        } catch (Exception e) {
                            Timber.e(e, "Error converting response of /Assets");
                            callback.onError(e);
                            return;
                        }

                        Timber.i("Successfully converted response of /Assets");

                        callback.onSuccess(assetsResponseDto);
                    }

                    @Override
                    public void onError(Exception e) {
                        Timber.e(e, "Received bad response of /Assets.");
                        callback.onError(e);
                    }
                });
    }

    public interface AssetsResponseCallback {
        void onSuccess(AssetsResponseDto assetsResponseDto);
        void onError(Exception e);
    }
}
