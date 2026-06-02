package com.tradingsim.client.api.assets;

import com.tradingsim.client.network.Callback;
import com.tradingsim.client.network.HttpClient;
import com.tradingsim.common.dto.asset.AssetsResponseDto;

import java.util.HashMap;
import java.util.Map;

public class AssetsApiClient {

    public static void sendGetAssetsRequest(
            String serverHost,
            int serverPort,
            GetAssetsRequest request,
            Callback<AssetsResponseDto> callback
    ) {
        Map<String, String> params = mapStringParams(request);

        HttpClient.get(
                "http://" + serverHost + ":" + serverPort + "/Assets",
                params,
                AssetsResponseDto.class,
                callback
        );
    }

    private static Map<String, String> mapStringParams(GetAssetsRequest request) {
        Map<String, String> params = new HashMap<>();

        if (request.getId() != null) {
            params.put("id", request.getId().toString());
        }

        if (request.getName() != null) {
            params.put("name", request.getName());
        }

        if (request.getPage() != null) {
            params.put("page", request.getPage().toString());
        }

        if (request.getPerPage() != null) {
            params.put("perPage", request.getPerPage().toString());
        }

        return params;
    }
}
