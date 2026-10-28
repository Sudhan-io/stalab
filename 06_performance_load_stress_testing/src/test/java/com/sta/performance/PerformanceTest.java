package com.sta.performance;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class PerformanceTest {

    @Test
    @DisplayName("Verify PerformanceMetrics percentile and throughput computations")
    public void testMetricsCalculation() {
        PerformanceMetrics metrics = new PerformanceMetrics("Unit Test", 5);
        metrics.start();

        // Feed 10 samples: 10, 20, 30, 40, 50, 60, 70, 80, 90, 100
        for (int i = 1; i <= 10; i++) {
            metrics.recordSample(i * 10, true);
        }
        // Feed 2 error samples
        metrics.recordSample(120, false);
        metrics.recordSample(130, false);

        metrics.stop();

        assertEquals(12, metrics.getTotalRequests());
        assertEquals(10, metrics.getSuccessCount());
        assertEquals(2, metrics.getErrorCount());
        assertEquals(16.666, metrics.getErrorRate(), 0.01);
        assertEquals(10, metrics.getMinResponseTime());
        assertEquals(130, metrics.getMaxResponseTime());
        assertTrue(metrics.getThroughput() > 0);
        assertEquals(90, metrics.getPercentile(75));
    }

    @Test
    @DisplayName("Verify Mock Server responds under normal load")
    public void testServerResponse() throws Exception {
        EcommerceMockServer server = new EcommerceMockServer(0, 10);
        server.start();
        try {
            String url = "http://localhost:" + server.getPort() + "/api/products";
            PerformanceMetrics m = LoadAndStressTester.executeTest("Quick Test", url, 2, 10);
            assertEquals(10, m.getTotalRequests());
            assertEquals(10, m.getSuccessCount());
            assertEquals(0.0, m.getErrorRate(), 0.001);
        } finally {
            server.stop();
        }
    }
}

