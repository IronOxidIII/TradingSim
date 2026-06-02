package com.tradingsim.handlers;

import static com.tradingsim.http.HttpService.sendInternalErrorResponse;
import static com.tradingsim.http.HttpService.sendResponse;

import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.tradingsim.common.dto.portfolio.PortfolioAssetDto;
import com.tradingsim.common.dto.portfolio.PortfolioDto;
import com.tradingsim.common.dto.portfolio.PortfoliosResponseDto;
import com.tradingsim.constants.Errors;
import com.tradingsim.model.Portfolio;
import com.tradingsim.model.PortfolioAsset;
import com.tradingsim.repository.PortfolioRepository;

import java.util.*;
import java.util.logging.Logger;

public class PortfoliosHandler implements HttpHandler {
    private final PortfolioRepository portfolioRepository;

    private final Gson gson = new Gson();
    private static final Logger log = Logger.getLogger(PortfoliosHandler.class.getName());


    public PortfoliosHandler(
            PortfolioRepository portfolioRepository
    ) {
        this.portfolioRepository = portfolioRepository;
    }

    @Override
    public void handle(HttpExchange exchange) {
        String json;
        try {
            PortfoliosResponseDto response =
                    new PortfoliosResponseDto();

            response.setPortfolios(convertPortfoliosToDtos(portfolioRepository.findAll()));

            json = gson.toJson(response);
            log.info("Json response: " + json);
            exchange.getResponseHeaders().add(
                    "Content-Type",
                    "application/json"
            );
        } catch (Exception e) {
            log.warning("%s: %s".formatted(Errors.ErrorCreatingResponse, e.getMessage()));
            sendInternalErrorResponse(exchange);
            return;
        }

        sendResponse(exchange, 200, json);
    }

    private List<PortfolioDto> convertPortfoliosToDtos(List<Portfolio> portfolios) {
        List<PortfolioDto> result = new ArrayList<>();

        for (var portfolio : portfolios) {
            List<PortfolioAsset> portfolioAssets = portfolio.getPortfolioAssets();
            List<PortfolioAssetDto> portfolioAssetDtos = new ArrayList<>();

            for (var portfolioAsset : portfolioAssets) {
                portfolioAssetDtos.add(
                        new PortfolioAssetDto(
                                portfolioAsset.getAssetId(),
                                portfolioAsset.getAmount().toString()
                        )
                );
            }

            result.add(new PortfolioDto(
                    portfolio.getUserId(),
                    portfolio.getStartSum().intValue(),
                    portfolio.getTotalSum().toString(),
                    portfolioAssetDtos));
        }

        return result;
    }
}