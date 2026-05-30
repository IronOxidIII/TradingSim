package com.tradingsim.initialize;

import com.sun.net.httpserver.HttpServer;
import com.tradingsim.handlers.AssetsHandler;
import com.tradingsim.handlers.PortfoliosHandler;
import com.tradingsim.repository.AssetRepositoryImpl;
import com.tradingsim.repository.PortfolioRepositoryImpl;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Logger;

public class HttpServerInitializer {

    private final AssetRepositoryImpl assetRepository;
    private final PortfolioRepositoryImpl portfolioRepository;
    private static HttpServer server = null;
    private static ExecutorService executor;

    private static final Logger log = Logger.getLogger(HttpServerInitializer.class.getName());

    public HttpServerInitializer(
            AssetRepositoryImpl assetRepository,
            PortfolioRepositoryImpl portfolioRepository
    ) {
        this.assetRepository = assetRepository;
        this.portfolioRepository = portfolioRepository;
    }

    public void Initialize() {
        executor = Executors.newFixedThreadPool(10);

        try {
            server = HttpServer.create(new InetSocketAddress(8888), 0);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        server.setExecutor(executor);

        server.createContext(
                "/Assets",
                new AssetsHandler(assetRepository)
        );

        server.createContext(
                "/Portfolios",
                new PortfoliosHandler(portfolioRepository)
        );

        server.start();
        log.info("Server started on port 8888");

        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            server.stop(1);
            executor.shutdown();
        }));
    }
}
