# Experiment 02: E-Commerce Application Test Case Design & Execution

## Question
> Design and document appropriate test cases for an e-commerce application covering user registration, login, product search, product selection, cart management, checkout, payment, and order confirmation.

---

## 📝 Main Java File(s) to Write in Lab Exam / Practical
In your lab exam, you need to provide the complete test cases document and the automated test case runner:
* **Documentation File:** `TEST_CASES.md` (Contains the tabular test cases covering all 8 mandatory modules: Registration, Login, Search, Selection, Cart, Checkout, Payment, Order Confirmation)
* **Main Java Class to Write:** [`src/main/java/com/sta/testcases/TestCaseExecutor.java`](file:///d:/PROJECTS/stalab/02_ecommerce_test_cases/src/main/java/com/sta/testcases/TestCaseExecutor.java)
  * **Class Name:** `com.sta.testcases.TestCaseExecutor`
  * **Function:** Populates the test cases array, triggers business rules verification, compares Expected vs Actual, and outputs the test execution pass/fail matrix.
* **Underlying Business Validation Engine:** [`src/main/java/com/sta/testcases/EcommerceBusinessEngine.java`](file:///d:/PROJECTS/stalab/02_ecommerce_test_cases/src/main/java/com/sta/testcases/EcommerceBusinessEngine.java)
* **Automated JUnit 5 Test Suite:** [`src/test/java/com/sta/testcases/EcommerceTestCasesSuiteTest.java`](file:///d:/PROJECTS/stalab/02_ecommerce_test_cases/src/test/java/com/sta/testcases/EcommerceTestCasesSuiteTest.java)

---

## Deliverables in this Directory
1. **[TEST_CASES.md](file:///d:/PROJECTS/stalab/02_ecommerce_test_cases/TEST_CASES.md)** - Tabular test case specification covering all 8 mandatory modules.
2. **`src/main/java/com/sta/testcases/`** -
   - `TestCase.java`: Standard test case data model.
   - `EcommerceBusinessEngine.java`: Business logic and validation rules for all 8 modules.
   - `TestCaseExecutor.java`: Automated test runner executing test cases and outputting metrics.
3. **`src/test/java/com/sta/testcases/EcommerceTestCasesSuiteTest.java`** - JUnit 5 test suite.
4. **`pom.xml`** - Self-contained Maven configuration.

---

## Prerequisites (Setting Up on Any New Computer)
1. **Java JDK (17 or higher)** (`java -version`)
2. **Apache Maven (3.8 or higher)** (`mvn -version`)

---

## How to Build and Run

Open terminal/PowerShell inside this folder:
```bash
cd 02_ecommerce_test_cases
```

### 1. Run Automated Test Verification
```bash
mvn clean test
```

### 2. Run Test Cases Execution Engine
```bash
mvn compile exec:java
```

---

## Expected Output
```text
==========================================================================================
 STA LAB - EXPERIMENT 2: E-COMMERCE TEST CASE SUITE AUTOMATED EXECUTION
==========================================================================================
TC ID       | Module           | Scenario                         | Status  
------------------------------------------------------------------------------------------
TC_REG_01   | Registration     | Valid Registration               | [PASS]  
TC_REG_02   | Registration     | Duplicate Email Rejection        | [PASS]  
TC_REG_03   | Registration     | Weak Password Validation         | [PASS]  
TC_LOG_01   | Login            | Valid Login                      | [PASS]  
TC_LOG_02   | Login            | Invalid Password                 | [PASS]  
TC_LOG_03   | Login            | 5 Failed Attempts Lockout        | [PASS]  
TC_SRC_01   | Search           | Exact Product Search             | [PASS]  
TC_SRC_02   | Search           | No Results Found                 | [PASS]  
TC_SEL_01   | Product Selection| In-Stock Product Selection       | [PASS]  
TC_SEL_02   | Product Selection| Out-of-Stock Badge Check         | [PASS]  
TC_CRT_01   | Cart Management  | Add Item to Cart                 | [PASS]  
TC_CRT_02   | Cart Management  | Max Item Limit Check             | [PASS]  
TC_CHK_01   | Checkout         | Valid PIN Code Validation        | [PASS]  
TC_CHK_02   | Checkout         | Invalid PIN Code Validation      | [PASS]  
TC_PAY_01   | Payment          | Valid Credit Card Payment        | [PASS]  
TC_PAY_02   | Payment          | COD Order Amount Limit Check     | [PASS]  
TC_ORD_01   | Order Confirmation| Order Confirmation & Stock Decrement | [PASS]  
------------------------------------------------------------------------------------------
Total Test Cases: 17 | Passed: 17 | Failed: 0 | Pass Rate: 100.0%
==========================================================================================
```
