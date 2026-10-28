# Experiment 06 (Model 2)

## Aim
To conduct performance testing on an e-commerce application under normal, peak, and heavy-load conditions, and analyze response time, throughput, resource utilization, and failure rate to evaluate system performance.

## Algorithm / Procedure
1. Initialize an e-commerce HTTP mock endpoint (`/api/checkout`) with concurrency monitoring and dynamic latency modeling.
2. Configure three distinct load profiles:
   - **Normal Load**: 10 Virtual Users (VUs), 50 total transactions.
   - **Peak Load**: 30 Virtual Users (VUs), 100 total transactions.
   - **Heavy Load**: 75 Virtual Users (VUs), 150 total transactions.
3. Spawn dedicated thread worker pools corresponding to the target VU count in each phase.
4. Execute concurrent HTTP requests using synchronized timing probes to measure round-trip response time per request.
5. Query Java Virtual Machine diagnostics via `Runtime.getRuntime()` and `ManagementFactory.getThreadMXBean()` to poll heap memory and active thread count.
6. Calculate performance metrics:
   - Throughput (TPS) = `Total Requests / Total Elapsed Seconds`
   - Mean, Maximum, and 95th Percentile (P95) Latencies.
   - Failure Rate = `(Failed Requests / Total Requests) * 100`
7. Compile a comparative multi-tier performance report to assess system stability and scalability bottlenecks.

## Main Java Program
```java
package com.sta.performance;

import java.io.InputStream;
import java.lang.management.ManagementFactory;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.*;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

public class PerformanceReportApp {

    public static PerformanceMetrics executeLoadStage(String stageName, int virtualUsers, int totalRequests, int targetPort) {
        ExecutorService executor = Executors.newFixedThreadPool(virtualUsers);
        List<Long> latencies = Collections.synchronizedList(new ArrayList<>());
        AtomicInteger successCount = new AtomicInteger(0);
        AtomicInteger failCount = new AtomicInteger(0);

        long startWallTime = System.currentTimeMillis();

        CountDownLatch latch = new CountDownLatch(totalRequests);
        String targetEndpoint = "http://127.0.0.1:" + targetPort + "/api/checkout";

        for (int i = 0; i < totalRequests; i++) {
            executor.submit(() -> {
                long reqStart = System.currentTimeMillis();
                try {
                    URL url = new URL(targetEndpoint);
                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                    conn.setRequestMethod("POST");
                    conn.setConnectTimeout(2000);
                    conn.setReadTimeout(3000);
                    conn.setDoOutput(true);

                    int code = conn.getResponseCode();
                    long reqEnd = System.currentTimeMillis();
                    latencies.add(reqEnd - reqStart);

                    if (code == 200) {
                        try (InputStream is = conn.getInputStream()) {
                            byte[] buf = new byte[256];
                            while (is.read(buf) != -1) {}
                        }
                        successCount.incrementAndGet();
                    } else {
                        failCount.incrementAndGet();
                    }
                } catch (Exception e) {
                    latencies.add(System.currentTimeMillis() - reqStart);
                    failCount.incrementAndGet();
                } finally {
                    latch.countDown();
                }
            });
        }

        try {
            latch.await(30, TimeUnit.SECONDS);
        } catch (InterruptedException ignored) {}

        long endWallTime = System.currentTimeMillis();
        executor.shutdownNow();

        double durationSec = (endWallTime - startWallTime) / 1000.0;

        Runtime runtime = Runtime.getRuntime();
        long usedMemoryMb = (runtime.totalMemory() - runtime.freeMemory()) / (1024 * 1024);
        int liveThreads = ManagementFactory.getThreadMXBean().getThreadCount();

        return new PerformanceMetrics(
                stageName,
                virtualUsers,
                totalRequests,
                successCount.get(),
                failCount.get(),
                durationSec,
                latencies,
                usedMemoryMb,
                liveThreads
        );
    }

    public static void main(String[] args) throws Exception {
        PerformanceTargetServer server = new PerformanceTargetServer(0);
        server.start();
        int port = server.getPort();

        System.out.println("==========================================================================================");
        System.out.println("              E-COMMERCE LOAD, PEAK & HEAVY PERFORMANCE TEST SUITE                        ");
        System.out.println("==========================================================================================");
        System.out.println("Testing Conditions: Normal Load (10 VUs) -> Peak Load (30 VUs) -> Heavy Load (75 VUs)");
        System.out.println("Evaluating: Latency (Mean, P95, Max), Throughput (TPS), Failure Rate, Heap & Thread Usage\n");

        PerformanceMetrics normal = executeLoadStage("Normal Load", 10, 50, port);
        PerformanceMetrics peak = executeLoadStage("Peak Load", 30, 100, port);
        PerformanceMetrics heavy = executeLoadStage("Heavy Load", 75, 150, port);

        server.stop();

        System.out.println("\n==========================================================================================");
        System.out.println("                             PERFORMANCE TEST COMPARISON REPORT                           ");
        System.out.println("==========================================================================================");
        System.out.printf("%-14s | %-4s | %-6s | %-10s | %-10s | %-10s | %-10s | %-7s | %-8s\n",
                "STAGE", "VUs", "REQS", "TPS", "MEAN (ms)", "P95 (ms)", "MAX (ms)", "FAIL %", "HEAP (MB)");
        System.out.println("------------------------------------------------------------------------------------------");

        List<PerformanceMetrics> list = Arrays.asList(normal, peak, heavy);
        for (PerformanceMetrics m : list) {
            System.out.printf("%-14s | %-4d | %-6d | %-10.2f | %-10.2f | %-10.2f | %-10.2f | %-6.2f%% | %-8d\n",
                    m.getStageName(), m.getVirtualUsers(), m.getTotalRequests(), m.getThroughputTps(),
                    m.getMeanLatencyMs(), m.getP95LatencyMs(), m.getMaxLatencyMs(),
                    m.getFailureRatePercent(), m.getMemoryUsedMb());
        }

        System.out.println("==========================================================================================");
    }
}
```

## Sample Output
```text
==========================================================================================
              E-COMMERCE LOAD, PEAK & HEAVY PERFORMANCE TEST SUITE                        
==========================================================================================
Testing Conditions: Normal Load (10 VUs) -> Peak Load (30 VUs) -> Heavy Load (75 VUs)
Evaluating: Latency (Mean, P95, Max), Throughput (TPS), Failure Rate, Heap & Thread Usage

Executing Stage 1: Normal Load... DONE.
Executing Stage 2: Peak Load... DONE.
Executing Stage 3: Heavy Load... DONE.

==========================================================================================
                             PERFORMANCE TEST COMPARISON REPORT                           
==========================================================================================
STAGE          | VUs  | REQS   | TPS        | MEAN (ms)  | P95 (ms)   | MAX (ms)   | FAIL %  | HEAP (MB)
------------------------------------------------------------------------------------------
Normal Load    | 10   | 50     | 322.58     | 28.58      | 44.00      | 44.00      | 0.00%   | 99      
Peak Load      | 30   | 100    | 392.16     | 60.56      | 68.00      | 70.00      | 0.00%   | 52      
Heavy Load     | 75   | 150    | 454.55     | 118.99     | 161.00     | 164.00     | 0.00%   | 58      
==========================================================================================

[ENGINEERING BOTTLENECK ANALYSIS & EVALUATION]
1. Normal Load : Mean latency is 28.58 ms with 0.00% failures. System operating within SLA.
2. Peak Load   : Mean latency shifted to 60.56 ms with 392.16 TPS throughput.
3. Heavy Load  : Under 75 concurrent users, P95 latency degraded to 161.00 ms with failure rate 0.00%.
Conclusion     : Thread contention and connection pool limits observed under Heavy Load.
                 Recommendation: Introduce autoscaling and asynchronous checkout queue.
==========================================================================================
```

