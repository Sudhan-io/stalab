# Experiment 01: E-Commerce Web and Mobile Application Test Plan

## Question
> Develop a test plan for testing an e-commerce web or mobile application, such as Amazon. Specify the testing objectives, scope, test environment, resources, test strategy, schedule, and entry and exit criteria.

---

## 📝 Main Java File(s) to Write in Lab Exam / Practical
In your lab exam, you need to provide the formal IEEE 829 test plan document and the gate validator code:
* **Documentation File:** `TEST_PLAN.md` (Full IEEE 829 specification covering Scope, Environment, Schedule, Resources, Entry/Exit criteria)
* **Main Java Class to Write:** [`src/main/java/com/sta/testplan/TestPlanValidator.java`](file:///d:/PROJECTS/stalab/01_ecommerce_test_plan/src/main/java/com/sta/testplan/TestPlanValidator.java)
  * **Class Name:** `com.sta.testplan.TestPlanValidator`
  * **Execution Runner:** [`src/main/java/com/sta/testplan/TestPlanApp.java`](file:///d:/PROJECTS/stalab/01_ecommerce_test_plan/src/main/java/com/sta/testplan/TestPlanApp.java) (`main()` method that executes gate validation checks)
* **Automated Test Suite:** [`src/test/java/com/sta/testplan/TestPlanCriteriaTest.java`](file:///d:/PROJECTS/stalab/01_ecommerce_test_plan/src/test/java/com/sta/testplan/TestPlanCriteriaTest.java)

---

## Deliverables in this Directory
1. **[TEST_PLAN.md](file:///d:/PROJECTS/stalab/01_ecommerce_test_plan/TEST_PLAN.md)** - Full IEEE 829 Standard Test Plan Specification Document.
2. **`src/main/java/com/sta/testplan/`** -
   - `EcommerceTestPlanModel.java`: Models IEEE 829 test plan data structures.
   - `TestPlanValidator.java`: Evaluates entry/exit criteria gates and milestone schedules.
   - `TestPlanApp.java`: Main executable reporting test plan status.
3. **`src/test/java/com/sta/testplan/TestPlanCriteriaTest.java`** - Automated JUnit 5 unit tests.
4. **`pom.xml`** - Self-contained Maven configuration for standalone execution.

---

## Prerequisites (Setting Up on Any New Computer)
1. **Java JDK (17 or higher)**
   - Verify: `java -version`
   - *Windows:* Download JDK 17/21 from [Adoptium](https://adoptium.net/) and add `%JAVA_HOME%\bin` to PATH.
   - *Linux (Ubuntu/Debian):* `sudo apt update && sudo apt install openjdk-17-jdk -y`
   - *macOS:* `brew install openjdk@17`
2. **Apache Maven (3.8 or higher)**
   - Verify: `mvn -version`
   - *Windows:* Download zip from [Maven](https://maven.apache.org/download.cgi), extract to `C:\Program Files\apache-maven`, and add `bin` to PATH.
   - *Linux:* `sudo apt install maven -y`
   - *macOS:* `brew install maven`

---

## How to Build and Run

Open terminal/PowerShell inside this folder:
```bash
cd 01_ecommerce_test_plan
```

### 1. Run Unit Tests (Validates Entry & Exit Criteria)
```bash
mvn clean test
```

### 2. Execute Test Plan Quality Gate Evaluator
```bash
mvn compile exec:java
```

---

## Expected Output
```text
===============================================================================
 STA LAB - EXPERIMENT 1: E-COMMERCE TEST PLAN RUNNER & GATE VALIDATOR
===============================================================================
Plan ID       : TP-ECOM-2026-V1.0
System Under Test: Amazon-Like Multi-Channel E-Commerce Application
Total Schedule: 30 Days

--- 1. IN-SCOPE MODULES ---
  1. User Registration & Authentication (OTP, SSO)
  2. Product Catalog, Search & Faceted Filtering
  3. Product Detail View & Stock Status
  4. Cart Management & Promo Code Calculations
  5. Checkout Delivery Address & Time Slot
  6. Payment Gateway Integration (Cards, UPI, COD)
  7. Order Confirmation, Invoicing & Notifications

--- 5. ENTRY CRITERIA EVALUATION ---
  Status: READY TO COMMENCE TESTING (4/4 criteria satisfied)
   * [PASS] SRS and UI Wireframes baselined -> Signed off by Product Owner
   * [PASS] Staging QA environment deployed and operational -> AWS staging healthy
   * [PASS] Smoke testing suite executed 100% pass -> Build verified
   * [PASS] Test catalog data & user credentials populated -> 10k SKUs seeded

--- 6. EXIT / SIGN-OFF CRITERIA EVALUATION ---
  Status: READY FOR PRODUCTION RELEASE SIGN-OFF (5/5 criteria satisfied)
   * [PASS] 100% of planned test cases executed -> All 150 test cases run
   * [PASS] >= 98% overall test pass percentage -> Achieved 98.7% pass rate
   * [PASS] Zero open Critical (P1) or Major (P2) defects -> 0 P1/P2 remaining
===============================================================================
```
