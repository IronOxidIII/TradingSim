package com.tradingsim.config;

import java.math.RoundingMode;

public class MoneyConfig {
    private MoneyConfig() {}
    public static final int MONEY_SCALE = 2;
    public static final int ASSET_SCALE = 8;
    public static final RoundingMode ROUNDING_MODE = RoundingMode.HALF_UP;
}
