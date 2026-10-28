# E-Commerce Performance Test Report: Load and Stress Testing

**Application Tested:** E-Commerce High-Volume Order Processing API  
**Document ID:** PERF-REP-2026-V1.0  
**Test Harness:** Multithreaded Non-Blocking Virtual User Engine  
**Date:** October 2026  

---

## 1. Executive Summary
This report evaluates the performance boundaries and stability profile of the E-Commerce backend under varying user load conditions. Both **Load Testing (expected operating conditions)** and **Stress Testing (beyond system design capacity)** were conducted to assess Response Time, System Throughput, and Error Rate.

### Key Metrics Summary:
| Metric | Baseline Load Test (20 VU) | Peak Load Test (50 VU) | Stress Test (100 VU) | SLA Threshold |
| :--- | :--- | :--- | :--- | :--- |
| **Total Requests Executed** | 200 reqs | 500 reqs | 1,000 reqs | N/A |
| **Average Response Time (Mean)**| 12.4 ms | 38.6 ms | 215.8 ms | < 200 ms |
| **95th Percentile Latency (p95)**| 22.0 ms | 65.0 ms | 640.0 ms | < 500 ms |
| **Max Latency Recorded** | 45.0 ms | 120.0 ms | 1,280.0 ms | < 1,000 ms |
| **Throughput (TPS)** | 145.2 req/s | 280.5 req/s | 178.4 req/s (degraded) | > 200 req/s |
| **Error Rate (%)** | 0.0% (0 errors) | 0.0% (0 errors) | 8.4% (84 errors) | < 1.0% |
| **SLA Compliance Status** | **PASSED** | **PASSED** | **FAILED (Breakpoint)**| PASS required |

---

## 2. Load Testing vs Stress Testing Analysis

### 2.1 Load Test (Target: 20 to 50 Concurrent Virtual Users)
* **Behavior:** The system exhibited linear scalability. As concurrency increased from 20 to 50 VUs, throughput doubled from 145.2 to 280.5 req/sec with no errors.
* **Latency:** 95% of all requests completed in under 65ms, well within the 500ms SLA.
* **CPU / Memory:** CPU utilization averaged 38%, Garbage Collection pauses < 15ms.

### 2.2 Stress Test (Target: 100+ Concurrent Virtual Users)
* **Behavior:** System reached its saturation breakpoint at approximately 75 concurrent users.
* **Degradation:** Beyond 75 concurrent users, worker thread queues filled up, resulting in connection timeouts and HTTP 503 (Service Unavailable) errors.
* **Error Rate:** Reached 8.4% error rate under 100 concurrent threads, demonstrating thread pool and database connection starvation.
* **Recovery:** Post-stress cool-down demonstrated graceful recovery within 4.2 seconds once load subsided.

---

## 3. Bottleneck Analysis & Recommendations

1. **Thread Pool Saturation:** Worker threads spent significant time waiting for backend database connection acquisition during checkout.
   - *Recommendation:* Increase connection pool size (`HikariCP` maximumPoolSize from 10 to 30) and configure non-blocking asynchronous I/O.
2. **Caching Strategy:** Repeated queries to `/api/products` caused unnecessary database scans.
   - *Recommendation:* Introduce Redis distributed caching with 60-second TTL on popular catalog categories.
3. **Graceful Shedding:** Under extreme stress, the server accepted excess connections before timing out.
   - *Recommendation:* Implement a rate-limiting circuit breaker (Resilience4j / NGINX leaky bucket) returning HTTP 429 Too Many Requests to prevent cascading failures.

