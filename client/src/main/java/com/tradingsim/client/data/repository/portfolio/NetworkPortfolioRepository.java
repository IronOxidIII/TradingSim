package com.tradingsim.client.data.repository.portfolio;

import android.os.Build;

import com.tradingsim.client.domain.model.PortfolioAsset;
import com.tradingsim.client.network.AssetsApiClient;
import com.tradingsim.client.network.Callback;
import com.tradingsim.client.network.PortfoliosApiClient;
import com.tradingsim.common.dto.dto.asset.AssetDto;
import com.tradingsim.common.dto.dto.asset.AssetsResponseDto;
import com.tradingsim.common.dto.dto.portfolio.PortfolioAssetDto;
import com.tradingsim.common.dto.dto.portfolio.PortfolioDto;
import com.tradingsim.common.dto.dto.portfolio.PortfoliosResponseDto;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import timber.log.Timber;

public class NetworkPortfolioRepository implements PortfolioRepository {
    private List<AssetDto> assetDtos;
    private List<PortfolioDto> portfolioDtos;
    private List<PortfolioAsset> portfolioAssets;

    private boolean isAssetsReady = false;
    private boolean isPortfoliosReady = false;

    @Override
    public List<PortfolioAsset> getPortfolioAssets() {
        return this.portfolioAssets;
    }

    public void getPortfolioAssetsWithCallback(
            Callback<List<PortfolioAsset>> callback
    ) {
        getAssets(callback);
        getPortfolios(callback);
    }

    private void processPortfolioAssets(Callback<List<PortfolioAsset>> callback) {
        if (!isAssetsReady || !isPortfoliosReady) {
            return;
        }

        Timber.i("Trying to get assets from response.");
        List<PortfolioAsset> result = new ArrayList<>();

        var portfolio = portfolioDtos.get(0);
        for (var portfolioAssetDto : portfolio.getPortfolio_assets()) {
            result.add(makePortfolioAsset(portfolioAssetDto));
        }

        this.portfolioAssets = result;
        callback.onComplete(result);
    }

    private void getAssets(Callback<List<PortfolioAsset>> callback) {
        AssetsApiClient.sendGetAssetsRequest(
                "10.0.2.2",
                8888,
                null,
                null,
                null,
                null,
                new AssetsApiClient.AssetsResponseCallback() {
                    @Override
                    public void onSuccess(AssetsResponseDto assetsResponseDto) {
                        assetDtos = assetsResponseDto.getAssets();
                        isAssetsReady = true;
                        processPortfolioAssets(callback);
                    }

                    @Override
                    public void onError(Exception e) {
                        Timber.e(e);
                    }
                }
        );
    }

    private void getPortfolios(Callback<List<PortfolioAsset>> callback) {
        PortfoliosApiClient.getPortfolios(
                "10.0.2.2",
                8888,
                null,
                null,
                null,
                null,
                new PortfoliosApiClient.PortfoliosResponseCallback() {
                    @Override
                    public void onSuccess(PortfoliosResponseDto portfoliosResponseDto) {
                        portfolioDtos = portfoliosResponseDto.getPortfolios();
                        isPortfoliosReady = true;
                        processPortfolioAssets(callback);
                    }

                    @Override
                    public void onError(Exception e) {
                        Timber.e(e);
                    }
                }
        );
    }

    private PortfolioAsset makePortfolioAsset(PortfolioAssetDto portfolioAssetDto) {
        String symbol = null;
        for (AssetDto a : assetDtos) {
            if (a.getId() == portfolioAssetDto.getAsset_id()) {
                symbol = a.getName();
                break;
            }
        }

        BigDecimal amount = new BigDecimal(portfolioAssetDto.getAmount());

        BigDecimal price = new BigDecimal("0");
        for (var a : assetDtos) {
            if (a.getId() == portfolioAssetDto.getAsset_id()) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.VANILLA_ICE_CREAM) {
                    price = new BigDecimal(a.getPrice_history().getLast().getPrice());
                }
                break;
            }
        }

        return new PortfolioAsset(symbol, amount, price);
    }
}
