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
                        new BigDecimal[]{
                                new BigDecimal("3200"),
                                new BigDecimal("3300"),
                                new BigDecimal("3400"),
                                new BigDecimal("3350"),
                                new BigDecimal("3450"),
                                new BigDecimal("3500"),
                                new BigDecimal("3480")
                        }
                );

            case SOL_USDT:
                return new TradingAsset(
                        SOL_USDT,
                        SOL_USDT,
                        new BigDecimal("140"),
                        new BigDecimal[]{
                                new BigDecimal("120"),
                                new BigDecimal("125"),
                                new BigDecimal("130"),
                                new BigDecimal("128"),
                                new BigDecimal("135"),
                                new BigDecimal("140"),
                                new BigDecimal("138")
                        }
                );

            case BNB_USDT:
                return new TradingAsset(
                        BNB_USDT,
                        BNB_USDT,
                        new BigDecimal("600"),
                        new BigDecimal[]{
                                new BigDecimal("560"),
                                new BigDecimal("575"),
                                new BigDecimal("590"),
                                new BigDecimal("580"),
                                new BigDecimal("595"),
                                new BigDecimal("600"),
                                new BigDecimal("592")
                        }
                );

            case XRP_USDT:
                return new TradingAsset(
                        XRP_USDT,
                        XRP_USDT,
                        new BigDecimal("0.62"),
                        new BigDecimal[]{
                                new BigDecimal("0.55"),
                                new BigDecimal("0.57"),
                                new BigDecimal("0.60"),
                                new BigDecimal("0.58"),
                                new BigDecimal("0.61"),
                                new BigDecimal("0.62"),
                                new BigDecimal("0.60")
                        }
                );

            case DOGE_USDT:
                return new TradingAsset(
                        DOGE_USDT,
                        DOGE_USDT,
                        new BigDecimal("0.15"),
                        new BigDecimal[]{
                                new BigDecimal("0.11"),
                                new BigDecimal("0.12"),
                                new BigDecimal("0.13"),
                                new BigDecimal("0.12"),
                                new BigDecimal("0.14"),
                                new BigDecimal("0.15"),
                                new BigDecimal("0.145")
                        }
                );

            case BTC_USDT:
            default:
                return new TradingAsset(
                        BTC_USDT,
                        BTC_USDT,
                        new BigDecimal("65000"),
                        new BigDecimal[]{
                                new BigDecimal("62000"),
                                new BigDecimal("62500"),
                                new BigDecimal("63000"),
                                new BigDecimal("62800"),
                                new BigDecimal("64000"),
                                new BigDecimal("65000"),
                                new BigDecimal("64500")
                        }
                );
        }
    }
}