package com.tradingsim.handlers;

import static com.tradingsim.http.HttpService.sendResponse;

import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.tradingsim.common.dto.asset.AssetDto;
import com.tradingsim.common.dto.asset.AssetsResponseDto;
import com.tradingsim.common.dto.asset.PriceHistoryDto;
import com.tradingsim.constants.Errors;
import com.tradingsim.model.Asset;
import com.tradingsim.model.PriceHistory;
import com.tradingsim.repository.AssetRepository;
import com.tradingsim.repository.PriceHistoryRepository;

import java.util.*;
import java.util.logging.Logger;

public class AssetsHandler implements HttpHandler {
    private final AssetRepository assetRepository;
    private final PriceHistoryRepository priceHistoryRepository;

    private final Gson gson = new Gson();
    private static final Logger log = Logger.getLogger(AssetsHandler.class.getName());

    public AssetsHandler(
            AssetRepository assetRepository,
            PriceHistoryRepository priceHistoryRepository) {
        this.assetRepository = assetRepository;
        this.priceHistoryRepository = priceHistoryRepository;
    }

    @Override
    public void handle(HttpExchange exchange) {
        log.info("Got request: " + exchange.getRequestHeaders().toString());
        String json;
        try {
            AssetsResponseDto response =
                    new AssetsResponseDto();

            response.setAssets(convertAssetsToDtos(assetRepository.findAll()));

            json = gson.toJson(response);
            log.info("Json response: " + json);
            exchange.getResponseHeaders().add(
                    "Content-Type",
                    "application/json"
            );
        } catch (Exception e) {
            log.warning(Errors.getErrorMessage(Errors.ErrorCreatingResponse, e.getMessage()));
            sendResponse(exchange, 500, "Internal Error.");
            return;
        }

        sendResponse(exchange, 200, json);
    }

    private List<AssetDto> convertAssetsToDtos(List<Asset> assets) {
        List<AssetDto> result = new ArrayList<>();
        for (var asset : assets) {
            List<PriceHistoryDto> priceHistoriesDto = new ArrayList<>(convertPriceHistoriesToDtos(
                    priceHistoryRepository.findByAssetId(asset.getId())
            ));

            result.add(new AssetDto(
                    asset.getId(),
                    asset.getName(),
                    priceHistoriesDto));
        }

        return result;
    }

    private List<PriceHistoryDto> convertPriceHistoriesToDtos(
            List<PriceHistory> priceHistories) {
        List<PriceHistoryDto> result = new ArrayList<>();
        for (var priceHistory: priceHistories) {
            result.add(
                    new PriceHistoryDto(
                            priceHistory.getDateTime().toString(),
                            priceHistory.getPrice().toString(),
                            priceHistory.getVolume()
                    )
            );
        }

        return result;
    }
}