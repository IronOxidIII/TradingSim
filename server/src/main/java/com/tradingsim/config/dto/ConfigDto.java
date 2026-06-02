package com.tradingsim.config.dto;

public class ConfigDto {

    private int port;
    private TestDataDto testData;

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public TestDataDto getTestData() {
        return testData;
    }

    public void setTestData(TestDataDto testData) {
        this.testData = testData;
    }
}