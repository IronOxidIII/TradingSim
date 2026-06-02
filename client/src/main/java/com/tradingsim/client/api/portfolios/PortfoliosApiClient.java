package com.tradingsim.client.api.portfolios;

import com.tradingsim.client.network.Callback;
import com.tradingsim.client.network.HttpClient;
import com.tradingsim.common.dto.portfolio.PortfoliosResponseDto;

import java.util.HashMap;
import java.util.Map;

import timber.log.Timber;

public class PortfoliosApiClient {

    public PortfoliosApiClient() {
        Timber.plant();
    }

    public static void sendGetPortfolioRequest(
            String serverHost,
            int serverPort,
            GetPortfoliosRequest request,
            Callback<PortfoliosResponseDto> callback
    ) {
        Map<String, String> params = mapStringParams(request);

        HttpClient.get(
                "http://" + serverHost + ":" + serverPort + "/Portfolios",
                params,
                PortfoliosResponseDto.class,
                callback
        );
    }

    private static Map<String, String> mapStringParams(GetPortfoliosRequest request) {
        Map<String, String> params = new HashMap<>();

        if (request.getId() != null) {
            params.put("id", request.getId().toString());
        }

        if (request.getStartSum() != null) {
            params.put("startSum", request.getStartSum().toString());
        }

        if (request.getTotalSumLess() != null) {
            params.put("totalSumLess", request.getTotalSumLess().toString());
        }

        if (request.getTotalSumMore() != null) {
            params.put("totalSumMore", request.getTotalSumMore().toString());
        }

        return params;
    }
}