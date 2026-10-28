package com.sta.performance;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import java.io.IOException;
import static org.junit.jupiter.api.Assertions.*;

public class PerformanceLoadTest {

    private static PerformanceTargetServer server;

    @BeforeAll
    public static void start() throws IOException {
        server = new PerformanceTargetServer(0);
        server.start();
    }

    @AfterAll
    public static void stop() {
        if (server != null) server.stop();
    }

    @Test
    public void testPerformanceStagesExecution() {
        PerformanceMetrics normal = PerformanceReportApp.executeLoadStage("Normal", 5, 20, server.getPort());
        assertNotNull(normal);
        assertEquals(20, normal.getTotalRequests());
        assertTrue(normal.getThroughputTps() > 0, "Throughput must be positive");
        assertTrue(normal.getMeanLatencyMs() > 0, "Mean latency must be positive");
        assertTrue(normal.getMemoryUsedMb() > 0, "Memory used must be tracked");
    }
}

