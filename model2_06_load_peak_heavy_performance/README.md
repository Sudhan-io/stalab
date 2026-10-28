# Experiment 06 (Model 2): Normal, Peak & Heavy-Load Performance Testing

## Overview
Conducts structured performance engineering across three load tiers:
1. **Normal Load** (10 concurrent virtual users / baseline traffic)
2. **Peak Load** (30 concurrent virtual users / expected high promotion traffic)
3. **Heavy Load** (75 concurrent virtual users / stress threshold testing)

Analyzes key performance indicators:
- **Response Time**: Mean, P95 percentile, and Maximum latency
- **Throughput**: Transactions per second (TPS)
- **Resource Utilization**: JVM Heap memory consumption (MB) & live active threads
- **Failure Rate**: Percentage of rejected or failed transactions

## Main Java File to Write in Lab
- **`src/main/java/com/sta/performance/PerformanceReportApp.java`**

## Prerequisites
- Java JDK 17 or higher
- Apache Maven 3.8+

## How to Set Up and Run on a New Computer
1. Open terminal inside this folder:
   ```bash
   cd model2_06_load_peak_heavy_performance
   ```
2. Run test verification:
   ```bash
   mvn clean test
   ```
3. Execute the full load test suite across all 3 tiers:
   ```bash
   mvn exec:java
   ```
   Or package into jar and run:
   ```bash
   mvn clean package
   java -cp target/model2_06_load_peak_heavy_performance-1.0.0.jar com.sta.performance.PerformanceReportApp
   ```

