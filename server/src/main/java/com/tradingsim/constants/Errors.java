package com.tradingsim.constants;

public class Errors {
    public static final String ErrorCreatingResponse = "Error creating response";

    public static String getErrorMessage(String message, String error) {
        return "%s: %s".formatted(message, error);
    }
}
