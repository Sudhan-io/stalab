package com.sta.performance;

import java.util.*;
import java.util.concurrent.ConcurrentLinkedQueue;

public class PerformanceMetrics {
    private final String testPhase;
    private final int concurrentUsers;
    private final ConcurrentLinkedQueue<Long> responseTimesMs = new ConcurrentLinkedQueue<>();
    private final ConcurrentLinkedQueue<Boolean> successFlags = new ConcurrentLinkedQueue<>();
    private long startTimeMs;
    private long endTimeMs;

    public PerformanceMetrics(String testPhase, int concurrentUsers) {
        this.testPhase = testPhase;
        this.concurrentUsers = concurrentUsers;
    }

    public void start() {
        this.startTimeMs = System.currentTimeMillis();
    }

    public void stop() {
        this.endTimeMs = System.currentTimeMillis();
    }

    public void recordSample(long latencyMs, boolean success) {
        responseTimesMs.add(latencyMs);
        successFlags.add(success);
    }

    public int getTotalRequests() {
        return responseTimesMs.size();
    }

    public int getSuccessCount() {
        return (int) successFlags.stream().filter(b -> b).count();
    }

    public int getErrorCount() {
        return getTotalRequests() - getSuccessCount();
    }

    public double getErrorRate() {
        if (getTotalRequests() == 0) return 0.0;
        return (getErrorCount() * 100.0) / getTotalRequests();
    }

    public double getThroughput() {
        long durationMs = Math.max(1, (endTimeMs - startTimeMs));
        return (getTotalRequests() * 1000.0) / durationMs;
    }

    public double getAverageResponseTime() {
        if (responseTimesMs.isEmpty()) return 0.0;
        return responseTimesMs.stream().mapToLong(Long::longValue).average().orElse(0.0);
    }

    public long getMinResponseTime() {
        return responseTimesMs.stream().mapToLong(Long::longValue).min().orElse(0);
    }

    public long getMaxResponseTime() {
        return responseTimesMs.stream().mapToLong(Long::longValue).max().orElse(0);
    }

    public long getPercentile(double percentile) {
        if (responseTimesMs.isEmpty()) return 0;
        List<Long> sorted = new ArrayList<>(responseTimesMs);
        Collections.sort(sorted);
        int index = (int) Math.ceil((percentile / 100.0) * sorted.size()) - 1;
        index = Math.max(0, Math.min(index, sorted.size() - 1));
        return sorted.get(index);
    }

    public String getTestPhase() { return testPhase; }
    public int getConcurrentUsers() { return concurrentUsers; }
    public long getDurationMs() { return Math.max(1, (endTimeMs - startTimeMs)); }
}

