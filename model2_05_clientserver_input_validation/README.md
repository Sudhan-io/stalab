# Experiment 05 (Model 2): Client-Server Functional, Input-Validation & Error-Handling Testing

## Overview
Executes comprehensive automated testing against a multi-threaded TCP Client-Server application across three core pillars:
1. **Functional Testing** (Ping healthcheck, Admin authentication, Order creation, Authorization verification)
2. **Input-Validation Testing** (Negative numbers, alphanumeric string injections, numeric boundary and overflow limits)
3. **Error-Handling Testing** (Empty payloads, unsupported verbs, graceful handling of uncaught exceptions and connection drops)

Evaluates actual responses against expected responses and records defects with captured technical evidence.

## Main Java File to Write in Lab
- **`src/main/java/com/sta/clientserver/ClientServerTestSuite.java`**

## Prerequisites
- Java JDK 17 or higher
- Apache Maven 3.8+

## How to Set Up and Run on a New Computer
1. Open terminal inside this folder:
   ```bash
   cd model2_05_clientserver_input_validation
   ```
2. Run test assertions:
   ```bash
   mvn clean test
   ```
3. Run the interactive client-server test suite and inspect the defect evidence report:
   ```bash
   mvn exec:java
   ```
   Or package into jar and run:
   ```bash
   mvn clean package
   java -cp target/model2_05_clientserver_input_validation-1.0.0.jar com.sta.clientserver.ClientServerTestSuite
   ```

