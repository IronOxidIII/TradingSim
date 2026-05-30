package com.tradingsim.handlers;

import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.tradingsim.common.dto.portfolio.PortfolioDto;
import com.tradingsim.common.dto.portfolio.PortfoliosResponseDto;
import com.tradingsim.repository.PortfolioRepositoryImpl;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.logging.Logger;

public class PortfoliosHandler implements HttpHandler {
    private final List<PortfolioDto> portfolios;

    private final Gson gson = new Gson();
    private static final Logger log = Logger.getLogger(PortfoliosHandler.class.getName());


    public PortfoliosHandler(
            PortfolioRepositoryImpl repository
    ) {
        this.portfolios = repository.toPortfoliosDtoList();
    }

    @Override
    public void handle(HttpExchange exchange)
            throws IOException {
        String json;
        try {
            PortfoliosResponseDto response =
                    new PortfoliosResponseDto();

            response.setPortfolios(portfolios);

            json = gson.toJson(response);

            exchange.getResponseHeaders().add(
                    "Content-Type",
                    "application/json"
            );
        } catch (Exception e) {
            log.warning("Error creating response: " + e);
            sendResponse(exchange, 500, "Internal error.");
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