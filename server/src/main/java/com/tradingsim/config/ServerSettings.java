package com.tradingsim.config;

import com.tradingsim.initialize.ConfigInitializer;

public class ServerSettings {
    public int port;

    public ServerSettings() {
        ConfigInitializer configInitializer = new ConfigInitializer();
        this.port = configInitializer.getPort();
    }

    public int getPort() {
        return this.port;
    }
}
