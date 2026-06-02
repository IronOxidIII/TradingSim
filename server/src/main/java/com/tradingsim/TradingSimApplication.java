package com.tradingsim;

import com.tradingsim.initialize.StartUpInitializer;

import java.util.logging.Logger;

public class TradingSimApplication {

    private static final Logger log = Logger.getLogger(TradingSimApplication.class.getName());

    public static void main(String[] args) {
        StartUpInitializer startUpInitializer = new StartUpInitializer();
        startUpInitializer.Initialize();
    }
}
