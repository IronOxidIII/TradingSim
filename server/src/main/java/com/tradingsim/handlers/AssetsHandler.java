package com.tradingsim.handlers;

import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.tradingsim.common.dto.dto.asset.AssetDto;
import com.tradingsim.common.dto.dto.asset.AssetsResponseDto;
import com.tradingsim.repository.AssetRepositoryImpl;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.logging.Logger;

public class AssetsHandler implements HttpHandler {
    private final List<AssetDto> assets;

    private final Gson gson = new Gson();
    private static final Logger log = Logger.getLogger(AssetsHandler.class.getName());

    public AssetsHandler(AssetRepositoryImpl assets) {
        this.assets = assets.toAssetDtoList();
    }

    @Override
    public void handle(HttpExchange exchange)
            throws IOException {
        log.info("Got request: " + exchange.getRequestHeaders().toString());
        String json;
        try {
            AssetsResponseDto response =
                    new AssetsResponseDto();

            response.setAssets(assets);

             json = gson.toJson(response);
            log.info("Json response: " + json);
            exchange.getResponseHeaders().add(
                    "Content-Type",
                    "application/json"
            );
        } catch (Exception e) {
            log.warning("Error creating response: " + e.getMessage());
            sendResponse(exchange, 500, "Internal Error.");
            return;
        }

        sendResponse(exchange, 200, json);
    }

    private void sendResponse(
            HttpExchange exchange,
            int code,
            String body
    ) {
        try {
            byte[] bytes =
                    body.getBytes(
                            StandardCharsets.UTF_8
                    );

            exchange.sendResponseHeaders(
                    code,
                    bytes.length
            );

            OutputStream os =
                    exchange.getResponseBody();

            os.write(bytes);
            log.info("Wrote response with code: %d".formatted(code));
            os.close();
        } catch (Exception e) {
            log.warning("Error sending response: " + e.getMessage());
        }
    }
}