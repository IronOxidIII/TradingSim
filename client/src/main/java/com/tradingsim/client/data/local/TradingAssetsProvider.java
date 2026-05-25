package com.tradingsim.client.data.local;

import com.tradingsim.client.domain.model.TradingAsset;

import java.util.Arrays;
import java.util.List;
import java.math.BigDecimal;

public class TradingAssetsProvider {
    public static final String BTC_USDT = "BTC/USDT";
    public static final String ETH_USDT = "ETH/USDT";
    public static final String SOL_USDT = "SOL/USDT";
    public static final String BNB_USDT = "BNB/USDT";
    public static final String XRP_USDT = "XRP/USDT";
    public static final String DOGE_USDT = "DOGE/USDT";
    private static final List<String> ASSET_SYMBOLS =
            Arrays.asList(
                    BTC_USDT,
                    ETH_USDT,
                    SOL_USDT,
                    BNB_USDT,
                    XRP_USDT,
                    DOGE_USDT
            );

    public List<String> getAssetSymbols() {
        return ASSET_SYMBOLS;
    }

    public TradingAsset getAssetBySymbol(String symbol) {
        switch (symbol) {
            case ETH_USDT:
                return new TradingAsset(
                        ETH_USDT,
                        ETH_USDT,
                        new BigDecimal("3500"),
                        new float[]{3200, 3300, 3400, 3350, 3450, 3500, 3480}
                );

            case SOL_USDT:
                return new TradingAsset(
                        SOL_USDT,
                        SOL_USDT,
                        new BigDecimal("140"),
                        new float[]{120, 125, 130, 128, 135, 140, 138}
                );

            case BNB_USDT:
                return new TradingAsset(
                        BNB_USDT,
                        BNB_USDT,
                        new BigDecimal("600"),
                        new float[]{560, 575, 590, 580, 595, 600, 592}
                );

            case XRP_USDT:
                return new TradingAsset(
                        XRP_USDT,
                        XRP_USDT,
                        new BigDecimal("0.62"),
                        new float[]{0.55f, 0.57f, 0.60f, 0.58f, 0.61f, 0.62f, 0.60f}
                );

            case DOGE_USDT:
                return new TradingAsset(
                        DOGE_USDT,
                        DOGE_USDT,
                        new BigDecimal("0.15"),
                        new float[]{0.11f, 0.12f, 0.13f, 0.12f, 0.14f, 0.15f, 0.145f}
                );

            case BTC_USDT:
            default:
                return new TradingAsset(
                        BTC_USDT,
                        BTC_USDT,
                        new BigDecimal("65000"),
                        new float[]{62000, 62500, 63000, 62800, 64000, 65000, 64500}
                );
        }
    }
}