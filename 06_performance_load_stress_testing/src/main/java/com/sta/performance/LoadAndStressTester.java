package com.sta.performance;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.concurrent.*;

public class LoadAndStressTester {

    public static PerformanceMetrics executeTest(String phaseName, String targetUrl, int virtualUsers, int totalRequests) {
        PerformanceMetrics metrics = new PerformanceMetrics(phaseName, virtualUsers);
        ExecutorService executor = Executors.newFixedThreadPool(virtualUsers);
        HttpClient client = HttpClient.newBuilder()
            .connectTimeout(Duration.ofSeconds(3))
            .build();

        metrics.start();

        CountDownLatch latch = new CountDownLatch(totalRequests);

        for (int i = 0; i < totalRequests; i++) {
            executor.submit(() -> {
                long start = System.currentTimeMillis();
                boolean success = false;
                try {
                    HttpRequest request = HttpRequest.newBuilder()
                        .uri(URI.create(targetUrl))
                        .timeout(Duration.ofSeconds(2))
                        .GET()
                        .build();

                    HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
                    if (response.statusCode() == 200) {
                        success = true;
                    }
                } catch (Exception e) {
                    success = false;
                } finally {
                    long latency = System.currentTimeMillis() - start;
                    metrics.recordSample(latency, success);
                    latch.countDown();
                }
            });
        }

        try {
            latch.await(30, TimeUnit.SECONDS);
        } catch (InterruptedException ignored) {}

        metrics.stop();
        executor.shutdownNow();

        return metrics;
    }
}

