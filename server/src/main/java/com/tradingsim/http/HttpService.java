package com.tradingsim.http;

import com.sun.net.httpserver.HttpExchange;

import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.logging.Logger;

public class HttpService {

    private static final Logger log = Logger.getLogger(HttpService.class.getName());

    public static void sendResponse(
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

    public static void sendInternalErrorResponse(HttpExchange exchange) {
        sendResponse(exchange, 500, "Internal error.");
    }
}
