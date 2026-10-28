package com.sta.performance;

public class PerformanceApp {

    public static void main(String[] args) {
        System.out.println("==========================================================================================");
        System.out.println(" STA LAB - EXPERIMENT 6: E-COMMERCE PERFORMANCE LOAD & STRESS TEST HARNESS");
        System.out.println("==========================================================================================");

        EcommerceMockServer server = null;
        try {
            // Set capacity limit to 15 concurrent requests before saturation
            server = new EcommerceMockServer(0, 15);
            server.start();
            int port = server.getPort();
            String checkoutUrl = "http://localhost:" + port + "/api/checkout";
            System.out.println("Mock E-Commerce Backend Server running on port: " + port);

            // ==========================================
            // PHASE 1: Baseline Load Test (10 Virtual Users, 100 requests)
            // ==========================================
            System.out.println("\n>>> [PHASE 1] Executing Baseline Load Test (10 VUs, 100 Requests)...");
            PerformanceMetrics loadMetrics = LoadAndStressTester.executeTest("Load Test", checkoutUrl, 10, 100);

            // ==========================================
            // PHASE 2: High Concurrency Stress Test (50 Virtual Users, 250 requests)
            // ==========================================
            System.out.println(">>> [PHASE 2] Executing System Stress Test (50 VUs, 250 Requests)...");
            PerformanceMetrics stressMetrics = LoadAndStressTester.executeTest("Stress Test", checkoutUrl, 50, 250);

            // ==========================================
            // Performance Comparison Report
            // ==========================================
            printReport(loadMetrics, stressMetrics);

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (server != null) server.stop();
        }
        System.exit(0);
    }

    private static void printReport(PerformanceMetrics load, PerformanceMetrics stress) {
        System.out.println("\n==========================================================================================");
        System.out.println("                       E-COMMERCE PERFORMANCE AUDIT REPORT                                ");
        System.out.println("==========================================================================================");
        System.out.printf("%-32s | %-24s | %-24s%n", "Metric", "Phase 1: Load Test", "Phase 2: Stress Test");
        System.out.println("------------------------------------------------------------------------------------------");
        System.out.printf("%-32s | %-24d | %-24d%n", "Concurrent Virtual Users (VU)", load.getConcurrentUsers(), stress.getConcurrentUsers());
        System.out.printf("%-32s | %-24d | %-24d%n", "Total Requests Executed", load.getTotalRequests(), stress.getTotalRequests());
        System.out.printf("%-32s | %-24d | %-24d%n", "Successful Responses (200 OK)", load.getSuccessCount(), stress.getSuccessCount());
        System.out.printf("%-32s | %-24d | %-24d%n", "Failed / Saturated (503/Err)", load.getErrorCount(), stress.getErrorCount());
        System.out.printf("%-32s | %-23.2f%% | %-23.2f%%%n", "Error Rate (%)", load.getErrorRate(), stress.getErrorRate());
        System.out.printf("%-32s | %-20.2f req/s | %-20.2f req/s%n", "Throughput", load.getThroughput(), stress.getThroughput());
        System.out.printf("%-32s | %-21.2f ms | %-21.2f ms%n", "Average Latency (Mean)", load.getAverageResponseTime(), stress.getAverageResponseTime());
        System.out.printf("%-32s | %-21d ms | %-21d ms%n", "Min Latency", load.getMinResponseTime(), stress.getMinResponseTime());
        System.out.printf("%-32s | %-21d ms | %-21d ms%n", "Max Latency", load.getMaxResponseTime(), stress.getMaxResponseTime());
        System.out.printf("%-32s | %-21d ms | %-21d ms%n", "90th Percentile (p90)", load.getPercentile(90), stress.getPercentile(90));
        System.out.printf("%-32s | %-21d ms | %-21d ms%n", "95th Percentile (p95)", load.getPercentile(95), stress.getPercentile(95));
        System.out.printf("%-32s | %-21d ms | %-21d ms%n", "99th Percentile (p99)", load.getPercentile(99), stress.getPercentile(99));
        System.out.println("------------------------------------------------------------------------------------------");

        boolean slaPass = load.getErrorRate() < 1.0 && load.getPercentile(95) < 500;
        System.out.println("SLA Compliance (< 1% Error Rate, p95 < 500ms):");
        System.out.printf("  * Load Test SLA Status   : %s%n", slaPass ? "PASSED (Compliant)" : "FAILED");
        System.out.printf("  * Stress Test Degradation: %s (Breakpoint reached at %d VUs)%n",
            stress.getErrorRate() > 0 ? "Observed Saturation & Backpressure" : "Unsaturated", stress.getConcurrentUsers());
        System.out.println("==========================================================================================");
    }
}
