package com.sta.performance;

import com.sun.net.httpserver.*;
import java.io.*;
import java.net.*;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

public class PerformanceTargetServer {

    private final HttpServer server;
    private final int port;
    private final AtomicInteger activeConnections = new AtomicInteger(0);

    public PerformanceTargetServer(int port) throws IOException {
        this.port = port;
        this.server = HttpServer.create(new InetSocketAddress("127.0.0.1", port), 0);

        // Simulated e-commerce checkout endpoint with realistic latency backpressure
        server.createContext("/api/checkout", exchange -> {
            int currentLoad = activeConnections.incrementAndGet();
            try {
                // Introduce synthetic latency proportional to current concurrent load
                // When load exceeds 40 active threads, server introduces jitter & occasional 503
                long delay = 5 + (currentLoad * 2L);
                if (currentLoad > 60) {
                    delay += 25; // Saturation spike
                }

                try {
                    Thread.sleep(Math.min(delay, 200));
                } catch (InterruptedException ignored) {}

                if (currentLoad > 70 && Math.random() < 0.15) {
                    // Under heavy load condition, 15% rate limit / 503 Service Unavailable
                    byte[] err = "{\"status\":\"ERROR\",\"message\":\"Server overloaded\"}".getBytes();
                    exchange.sendResponseHeaders(503, err.length);
                    try (OutputStream os = exchange.getResponseBody()) {
                        os.write(err);
                    }
                } else {
                    byte[] response = "{\"status\":\"SUCCESS\",\"orderId\":\"ORD-PERF-99\"}".getBytes();
                    exchange.sendResponseHeaders(200, response.length);
                    try (OutputStream os = exchange.getResponseBody()) {
                        os.write(response);
                    }
                }
            } finally {
                activeConnections.decrementAndGet();
            }
        });

        // Use custom thread pool
        server.setExecutor(Executors.newFixedThreadPool(50));
    }

    public void start() {
        server.start();
    }

    public void stop() {
        server.stop(0);
    }

    public int getPort() {
        return server.getAddress().getPort();
    }
}

