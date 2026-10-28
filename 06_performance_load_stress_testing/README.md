# Experiment 06: E-Commerce Application Performance Load & Stress Testing

## Question
> Evaluate the performance of an e-commerce application by conducting load and stress tests. Analyze response time, throughput, and error rate, and prepare a performance test report.

---

## 📝 Main Java File(s) to Write in Lab Exam / Practical
In your lab exam, you need to provide the performance test report and the multi-threaded load/stress testing engine:
* **Documentation File:** `PERFORMANCE_TEST_REPORT.md` (Contains the performance benchmark report analyzing Response Time percentiles p50-p99, Throughput in TPS, Error Rate %, and saturation breakpoint)
* **Main Java Class to Write:** [`src/main/java/com/sta/performance/LoadAndStressTester.java`](file:///d:/PROJECTS/stalab/06_performance_load_stress_testing/src/main/java/com/sta/performance/LoadAndStressTester.java)
  * **Class Name:** `com.sta.performance.LoadAndStressTester`
  * **Function:** Uses `java.net.http.HttpClient` with thread pools to fire concurrent virtual user requests, record latencies, and populate statistical metrics.
* **Benchmark Orchestrator & Report Generator:** [`src/main/java/com/sta/performance/PerformanceApp.java`](file:///d:/PROJECTS/stalab/06_performance_load_stress_testing/src/main/java/com/sta/performance/PerformanceApp.java)
* **Metrics Mathematical Engine:** [`src/main/java/com/sta/performance/PerformanceMetrics.java`](file:///d:/PROJECTS/stalab/06_performance_load_stress_testing/src/main/java/com/sta/performance/PerformanceMetrics.java)
* **Automated JUnit 5 Test Suite:** [`src/test/java/com/sta/performance/PerformanceTest.java`](file:///d:/PROJECTS/stalab/06_performance_load_stress_testing/src/test/java/com/sta/performance/PerformanceTest.java)

---

## Deliverables in this Directory
1. **[PERFORMANCE_TEST_REPORT.md](file:///d:/PROJECTS/stalab/06_performance_load_stress_testing/PERFORMANCE_TEST_REPORT.md)** - Comprehensive Performance Report comparing Load vs Stress testing, response time percentiles (p50, p90, p95, p99), throughput (TPS), error rate (%), and SLA analysis.
2. **`src/main/java/com/sta/performance/`** -
   - `PerformanceMetrics.java`: Statistical latency distribution, percentile, throughput, and error rate calculator.
   - `EcommerceMockServer.java`: Lightweight embedded HTTP server serving `/api/products` and `/api/checkout` with simulated connection pool saturation.
   - `LoadAndStressTester.java`: Concurrent HTTP virtual user load generator using `java.net.http.HttpClient`.
   - `PerformanceApp.java`: Main orchestrator running Load Test and Stress Test phases and generating the performance comparison report.
3. **`src/test/java/com/sta/performance/PerformanceTest.java`** - Automated JUnit 5 unit tests verifying percentile formulas, metrics mathematical consistency, and server connectivity.
4. **`pom.xml`** - Self-contained Maven configuration.

---

## Prerequisites (Setting Up on Any New Computer)
1. **Java JDK (17 or higher)** (`java -version`)
2. **Apache Maven (3.8 or higher)** (`mvn -version`)

---

## How to Build and Run

Open terminal/PowerShell inside this folder:
```bash
cd 06_performance_load_stress_testing
```

### 1. Run Performance Metrics Unit Tests
```bash
mvn clean test
```

### 2. Execute Load & Stress Testing Benchmark
```bash
mvn compile exec:java
```

---

## Expected Output
```text
==========================================================================================
 STA LAB - EXPERIMENT 6: E-COMMERCE PERFORMANCE LOAD & STRESS TEST HARNESS
==========================================================================================
Mock E-Commerce Backend Server running on port: 4337

>>> [PHASE 1] Executing Baseline Load Test (10 VUs, 100 Requests)...
>>> [PHASE 2] Executing System Stress Test (50 VUs, 250 Requests)...

==========================================================================================
                       E-COMMERCE PERFORMANCE AUDIT REPORT                                
==========================================================================================
Metric                           | Phase 1: Load Test       | Phase 2: Stress Test    
------------------------------------------------------------------------------------------
Concurrent Virtual Users (VU)    | 10                       | 50                      
Total Requests Executed          | 100                      | 250                     
Successful Responses (200 OK)    | 100                      | 35                      
Failed / Saturated (503/Err)     | 0                        | 215                     
Error Rate (%)                   | 0.00%                    | 86.00%                  
Throughput                       | 269.54 req/s             | 1012.15 req/s           
Average Latency (Mean)           | 36.80 ms                 | 42.71 ms                
Min Latency                      | 16 ms                    | 19 ms                   
Max Latency                      | 202 ms                   | 66 ms                   
90th Percentile (p90)            | 21 ms                    | 55 ms                   
95th Percentile (p95)            | 200 ms                   | 58 ms                   
99th Percentile (p99)            | 202 ms                   | 62 ms                   
------------------------------------------------------------------------------------------
SLA Compliance (< 1% Error Rate, p95 < 500ms):
  * Load Test SLA Status   : PASSED (Compliant)
  * Stress Test Degradation: Observed Saturation & Backpressure (Breakpoint reached at 50 VUs)
==========================================================================================
```
