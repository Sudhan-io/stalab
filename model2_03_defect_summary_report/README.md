# Experiment 03 (Model 2): E-Commerce Defect Execution & Summary Report

## Overview
Executes designed test cases against an e-commerce shopping application to identify defects, evaluate actual results against expected results, classify defects by **Severity** and **Priority**, and generate a comprehensive **Defect Summary Report**.

## Main Java File to Write in Lab
- **`src/main/java/com/sta/defect/DefectSummaryReporter.java`**

## Prerequisites
- Java JDK 17 or higher
- Apache Maven 3.8+

## How to Set Up and Run on a New Computer
1. Open terminal inside this folder:
   ```bash
   cd model2_03_defect_summary_report
   ```
2. Compile and run all tests:
   ```bash
   mvn clean test
   ```
3. Run the Defect Summary Reporter directly:
   ```bash
   mvn exec:java
   ```
   Or run the compiled jar:
   ```bash
   mvn clean package
   java -cp target/model2_03_defect_summary_report-1.0.0.jar com.sta.defect.DefectSummaryReporter
   ```

