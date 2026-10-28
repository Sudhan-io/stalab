# Experiment 03: E-Commerce Defect Identification and Reporting

## Question
> Test an e-commerce application by executing the designed test cases. Identify the defects and prepare a defect report containing the defect ID, description, steps to reproduce, severity, priority, and expected and actual results.

---

## 📝 Main Java File(s) to Write in Lab Exam / Practical
In your lab exam, you need to provide the formal defect report document and the automated defect tracking engine:
* **Documentation File:** `DEFECT_REPORT.md` (Formal defect table with Defect ID, Description, Steps to Reproduce, Severity: Critical/Major/Moderate/Minor, Priority: P1-P4, Expected Result, Actual Result, Status)
* **Main Java Class to Write:** [`src/main/java/com/sta/defectreport/DefectReporter.java`](file:///d:/PROJECTS/stalab/03_ecommerce_defect_reporting/src/main/java/com/sta/defectreport/DefectReporter.java)
  * **Class Name:** `com.sta.defectreport.DefectReporter`
  * **Function:** Runs test scenarios against the target e-commerce app, captures defects, logs reproduction steps, calculates severity/priority counts, and prints the defect matrix.
* **Target Application Under Test:** [`src/main/java/com/sta/defectreport/BuggyEcommerceApp.java`](file:///d:/PROJECTS/stalab/03_ecommerce_defect_reporting/src/main/java/com/sta/defectreport/BuggyEcommerceApp.java)
* **Automated JUnit 5 Test Suite:** [`src/test/java/com/sta/defectreport/DefectReportTest.java`](file:///d:/PROJECTS/stalab/03_ecommerce_defect_reporting/src/test/java/com/sta/defectreport/DefectReportTest.java)

---

## Deliverables in this Directory
1. **[DEFECT_REPORT.md](file:///d:/PROJECTS/stalab/03_ecommerce_defect_reporting/DEFECT_REPORT.md)** - Full IEEE Standard Defect Report document with Defect ID, Description, Steps to Reproduce, Severity, Priority, Expected and Actual Results.
2. **`src/main/java/com/sta/defectreport/`** -
   - `Defect.java`: Standard defect entity.
   - `BuggyEcommerceApp.java`: Target application exhibiting defects.
   - `DefectReporter.java`: Automated test runner that detects failures and produces structured defect reports.
3. **`src/test/java/com/sta/defectreport/DefectReportTest.java`** - JUnit 5 tests asserting defect categorization and tracking integrity.
4. **`pom.xml`** - Self-contained Maven configuration.

---

## Prerequisites (Setting Up on Any New Computer)
1. **Java JDK (17 or higher)** (`java -version`)
2. **Apache Maven (3.8 or higher)** (`mvn -version`)

---

## How to Build and Run

Open terminal/PowerShell inside this folder:
```bash
cd 03_ecommerce_defect_reporting
```

### 1. Run Automated Defect Verification Tests
```bash
mvn clean test
```

### 2. Execute Defect Report Generator
```bash
mvn compile exec:java
```

---

## Expected Output
```text
==========================================================================================
 STA LAB - EXPERIMENT 3: DEFECT IDENTIFICATION AND FORMAL REPORT GENERATOR
==========================================================================================
--- DEFECT SUMMARY METRICS ---
Total Defects Identified: 5
  * Critical (P1) : 2
  * Major (P2)    : 1
  * Moderate (P3) : 1
  * Minor (P4)    : 1

--- DETAILED DEFECT LOG ---
------------------------------------------------------------------------------------------
Defect ID  : DEF_ECOM_001 | Severity: Critical | Priority: P1 | Status: OPEN
Description: Order confirmed despite payment gateway decline
Steps      : 1. Add item to cart. 2. Proceed to payment. 3. Enter declined card. 4. Click 'Pay Now'.
Expected   : System must display 'Payment Declined' and reject order creation
Actual     : Order ID generated and marked 'Confirmed' despite payment status DECLINED
------------------------------------------------------------------------------------------
Defect ID  : DEF_ECOM_002 | Severity: Critical | Priority: P1 | Status: OPEN
...
==========================================================================================
```
