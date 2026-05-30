package com.tradingsim.client.network;

import com.google.gson.Gson;
import com.tradingsim.common.dto.portfolio.PortfoliosResponseDto;

import java.util.HashMap;
import java.util.Map;

import timber.log.Timber;

public class PortfoliosApiClient {

    private static final Gson gson = new Gson();

    public static void getPortfolios(
            String serverHost,
            int serverPort,
            Integer id,
            Integer startSum,
            Double totalSumLess,
            Double totalSumMore,
            PortfoliosResponseCallback callback
    ) {
        Map<String, String> params = new HashMap<>();

        if (id != null) {
            params.put("id", id.toString());
        }

        if (startSum != null) {
            params.put("startSum", startSum.toString());
        }

        if (totalSumLess != null) {
            params.put("totalSumLess", totalSumLess.toString());
        }

        if (totalSumMore != null) {
            params.put("totalSumMore", totalSumMore.toString());
        }

        HttpClient.get(
                "http://" + serverHost + ":" + serverPort + "/Portfolios",
                params,
                new HttpClient.HttpCallback() {
                    @Override
                    public void onSuccess(String response) {
                        Timber.d("Received response: %s", response);
                        PortfoliosResponseDto portfoliosResponseDto;
                        try {
                            portfoliosResponseDto =
                                    gson.fromJson(response, PortfoliosResponseDto.class);
                        } catch (Exception e) {
                            Timber.e(e, "Error converting response of /Portfolios");
                            callback.onError(e);
                            return;
                        }

                        Timber.i("Successfully converted response of /Portfolios");

                        callback.onSuccess(portfoliosResponseDto);
                    }

                    @Override
                    public void onError(Exception e) {
                        Timber.e(e);
                    }
                });
    }

    public interface PortfoliosResponseCallback {
        void onSuccess(PortfoliosResponseDto portfoliosResponseDto);
        void onError(Exception e);
    }
}