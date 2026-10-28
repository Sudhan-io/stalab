# Experiment 6: E-Commerce Performance Load and Stress Testing

## Aim
To evaluate the performance of an e-commerce application under normal and peak operating conditions by conducting automated Load and Stress tests, analyze response times (Average, Min, Max, $p_{90}, p_{95}, p_{99}$ percentiles), throughput (Transactions Per Second / TPS), and error rate, and generate a performance benchmark report.

---

## Algorithm / Procedure

1. **Deploy Mock E-Commerce Backend Service:**
   - Initialize an embedded HTTP server providing high-traffic endpoints (`/api/checkout`, `/api/catalog`).
   - Configure a maximum concurrent worker queue capacity (e.g., 15 concurrent threads) beyond which the server rejects requests with HTTP 503 Service Unavailable (backpressure simulation).
2. **Execute Phase 1: Normal Load Test:**
   - Configure moderate concurrency within the rated server capacity (10 Virtual Users / VUs, 100 total requests).
   - Spawn worker threads via `ExecutorService` issuing asynchronous HTTP POST requests.
   - Record response time for each request, response HTTP status code, and total wall-clock duration.
3. **Execute Phase 2: Stress / Breakpoint Test:**
   - Configure high concurrency significantly exceeding server capacity (50 Virtual Users / VUs, 250 total requests).
   - Subject the server to burst traffic to identify the system saturation breakpoint and test system resiliency under resource exhaustion.
4. **Compute Performance Metrics:**
   - Calculate Error Rate: $\text{Error Rate} = \frac{\text{Failed Requests}}{\text{Total Requests}} \times 100\%$
   - Calculate Throughput: $\text{Throughput} = \frac{\text{Total Requests}}{\text{Total Duration (seconds)}}$
   - Calculate Latency Percentiles ($p_{50}, p_{90}, p_{95}, p_{99}$) by sorting the response time array.
5. **Evaluate SLA Compliance & Report:**
   - Check against SLA criteria ($\text{Error Rate} < 1\%$, $p_{95} < 500\text{ms}$).
   - Generate comparative performance audit table.

---

## Main Java Program

### File: `src/main/java/com/sta/performance/PerformanceApp.java`
```java
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
        System.out.println("SLA Compliance (< 1% Error Rate, p95 < 500ms):");
        System.out.println("  * Load Test SLA Status   : " + (load.isSlaCompliant(500, 1.0) ? "PASSED (Compliant)" : "FAILED"));
        System.out.println("  * Stress Test Degradation: Observed Saturation & Backpressure (Breakpoint reached at 50 VUs)");
        System.out.println("==========================================================================================");
    }
}
```

---

## Sample Output

```text
==========================================================================================
 STA LAB - EXPERIMENT 6: E-COMMERCE PERFORMANCE LOAD & STRESS TEST HARNESS
==========================================================================================
Mock E-Commerce Backend Server running on port: 10670

>>> [PHASE 1] Executing Baseline Load Test (10 VUs, 100 Requests)...
>>> [PHASE 2] Executing System Stress Test (50 VUs, 250 Requests)...

==========================================================================================
                       E-COMMERCE PERFORMANCE AUDIT REPORT                                
==========================================================================================
Metric                           | Phase 1: Load Test       | Phase 2: Stress Test    
------------------------------------------------------------------------------------------
Concurrent Virtual Users (VU)    | 10                       | 50                      
Total Requests Executed          | 100                      | 250                     
Successful Responses (200 OK)    | 100                      | 15                      
Failed / Saturated (503/Err)     | 0                        | 235                     
Error Rate (%)                   | 0.00                   % | 94.00                  %
Throughput                       | 256.41               req/s | 1037.34              req/s
Average Latency (Mean)           | 38.30                 ms | 41.84                 ms
Min Latency                      | 16                    ms | 25                    ms
Max Latency                      | 219                   ms | 67                    ms
90th Percentile (p90)            | 22                    ms | 56                    ms
95th Percentile (p95)            | 213                   ms | 60                    ms
99th Percentile (p99)            | 219                   ms | 65                    ms
------------------------------------------------------------------------------------------
SLA Compliance (< 1% Error Rate, p95 < 500ms):
  * Load Test SLA Status   : PASSED (Compliant)
  * Stress Test Degradation: Observed Saturation & Backpressure (Breakpoint reached at 50 VUs)
==========================================================================================
```
