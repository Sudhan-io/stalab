package com.sta.performance;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

public class EcommerceMockServer {
    private final HttpServer server;
    private final int port;
    private final AtomicInteger activeConnections = new AtomicInteger(0);
    private final int concurrencyLimit;
    private final java.util.concurrent.ExecutorService serverExecutor = java.util.concurrent.Executors.newCachedThreadPool();

    public EcommerceMockServer(int port, int concurrencyLimit) throws IOException {
        this.port = port;
        this.concurrencyLimit = concurrencyLimit;
        this.server = HttpServer.create(new InetSocketAddress(port), 0);
        this.server.setExecutor(serverExecutor);

        this.server.createContext("/api/products", new ProductsHandler());
        this.server.createContext("/api/checkout", new CheckoutHandler());
    }

    public void start() {
        this.server.start();
    }

    public void stop() {
        this.server.stop(0);
        this.serverExecutor.shutdownNow();
    }

    public int getPort() {
        return server.getAddress().getPort();
    }

    private class ProductsHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            try { Thread.sleep(5); } catch (InterruptedException ignored) {} // 5ms catalog latency
            String response = "{\"status\":\"ok\",\"products\":[\"iPhone 15\",\"MacBook Pro\",\"Sony WH-1000\"]}";
            sendResponse(exchange, 200, response);
        }
    }

    private class CheckoutHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            int current = activeConnections.incrementAndGet();
            try {
                // If current load exceeds server concurrency limit, return HTTP 503 (Stress breakpoint)
                if (current > concurrencyLimit) {
                    try { Thread.sleep(30); } catch (InterruptedException ignored) {}
                    String errorResp = "{\"error\":\"503 Service Unavailable: Concurrency saturation\"}";
                    sendResponse(exchange, 503, errorResp);
                    return;
                }

                // Normal simulated checkout database commit time (15ms)
                try { Thread.sleep(15); } catch (InterruptedException ignored) {}
                String response = "{\"status\":\"success\",\"orderId\":\"ORD-PERF-8812\"}";
                sendResponse(exchange, 200, response);
            } finally {
                activeConnections.decrementAndGet();
            }
        }
    }

    private void sendResponse(HttpExchange exchange, int statusCode, String response) throws IOException {
        byte[] bytes = response.getBytes(StandardCharsets.UTF_8);
        exchange.getResponseHeaders().set("Content-Type", "application/json");
        exchange.sendResponseHeaders(statusCode, bytes.length);
        try (OutputStream os = exchange.getResponseBody()) {
            os.write(bytes);
        }
    }
}
