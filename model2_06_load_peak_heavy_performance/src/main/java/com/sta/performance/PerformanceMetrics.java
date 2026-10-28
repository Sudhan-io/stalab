package com.sta.performance;

import java.util.*;

public class PerformanceMetrics {

    private final String stageName;
    private final int virtualUsers;
    private final int totalRequests;
    private final int successfulRequests;
    private final int failedRequests;
    private final double durationSeconds;
    private final double throughputTps;
    private final double minLatencyMs;
    private final double meanLatencyMs;
    private final double maxLatencyMs;
    private final double p95LatencyMs;
    private final double failureRatePercent;
    private final long memoryUsedMb;
    private final int activeThreadCount;

    public PerformanceMetrics(String stageName, int virtualUsers, int totalRequests, int successfulRequests,
                              int failedRequests, double durationSeconds, List<Long> latencies,
                              long memoryUsedMb, int activeThreadCount) {
        this.stageName = stageName;
        this.virtualUsers = virtualUsers;
        this.totalRequests = totalRequests;
        this.successfulRequests = successfulRequests;
        this.failedRequests = failedRequests;
        this.durationSeconds = durationSeconds;
        this.throughputTps = durationSeconds > 0 ? (totalRequests / durationSeconds) : 0;
        this.failureRatePercent = totalRequests > 0 ? ((double) failedRequests / totalRequests) * 100.0 : 0.0;
        this.memoryUsedMb = memoryUsedMb;
        this.activeThreadCount = activeThreadCount;

        if (latencies.isEmpty()) {
            this.minLatencyMs = 0;
            this.meanLatencyMs = 0;
            this.maxLatencyMs = 0;
            this.p95LatencyMs = 0;
        } else {
            List<Long> sorted = new ArrayList<>(latencies);
            Collections.sort(sorted);
            this.minLatencyMs = sorted.get(0);
            this.maxLatencyMs = sorted.get(sorted.size() - 1);
            double sum = 0;
            for (long l : sorted) sum += l;
            this.meanLatencyMs = sum / sorted.size();

            int p95Index = (int) Math.ceil(0.95 * sorted.size()) - 1;
            this.p95LatencyMs = sorted.get(Math.max(0, p95Index));
        }
    }

    public String getStageName() { return stageName; }
    public int getVirtualUsers() { return virtualUsers; }
    public int getTotalRequests() { return totalRequests; }
    public int getSuccessfulRequests() { return successfulRequests; }
    public int getFailedRequests() { return failedRequests; }
    public double getThroughputTps() { return throughputTps; }
    public double getMinLatencyMs() { return minLatencyMs; }
    public double getMeanLatencyMs() { return meanLatencyMs; }
    public double getMaxLatencyMs() { return maxLatencyMs; }
    public double getP95LatencyMs() { return p95LatencyMs; }
    public double getFailureRatePercent() { return failureRatePercent; }
    public long getMemoryUsedMb() { return memoryUsedMb; }
    public int getActiveThreadCount() { return activeThreadCount; }
}
