# Software Testing & Automation (STA) Laboratory Suite

This repository contains completely independent, standalone, production-ready Java and Selenium WebDriver implementations for all 11 Software Testing and Automation (STA) lab experiments.

Every folder is **100% self-contained**:
- Has its own `pom.xml` with all necessary dependencies.
- Can be compiled and executed directly inside its own folder without relying on any parent or root configuration.
- Includes comprehensive documentation (IEEE 829 Test Plans, Test Case matrices, Defect reports, Performance benchmarks, Framework maintainability audits).
- Includes a dedicated `README.md` detailing the **Main Java File to Write in Lab Exam**, prerequisites, and how to run on any computer.

---

## 📋 Master Directory & Lab Index

| # | Directory Name | Lab Objective | Main Java File & Lab Manual (`EXP.md`) | Run Command |
|:-:|:---|:---|:---|:---|
| **1** | [`01_ecommerce_test_plan`](file:///d:/PROJECTS/stalab/01_ecommerce_test_plan) | **E-Commerce Test Plan:** Objectives, scope, environment, strategy, schedule, entry/exit criteria. | [`TestPlanApp.java`](file:///d:/PROJECTS/stalab/01_ecommerce_test_plan/src/main/java/com/sta/testplan/TestPlanApp.java)<br>👉 [Lab Manual: `EXP.md`](file:///d:/PROJECTS/stalab/01_ecommerce_test_plan/EXP.md) | `cd 01_ecommerce_test_plan`<br>`mvn clean test`<br>`mvn compile exec:java` |
| **2** | [`02_ecommerce_test_cases`](file:///d:/PROJECTS/stalab/02_ecommerce_test_cases) | **E-Commerce Test Cases:** Design & document test cases for Registration, Login, Search, Selection, Cart, Checkout, Payment, Order Confirmation. | [`TestCaseExecutor.java`](file:///d:/PROJECTS/stalab/02_ecommerce_test_cases/src/main/java/com/sta/testcases/TestCaseExecutor.java)<br>👉 [Lab Manual: `EXP.md`](file:///d:/PROJECTS/stalab/02_ecommerce_test_cases/EXP.md) | `cd 02_ecommerce_test_cases`<br>`mvn clean test`<br>`mvn compile exec:java` |
| **3** | [`03_ecommerce_defect_reporting`](file:///d:/PROJECTS/stalab/03_ecommerce_defect_reporting) | **Defect Identification & Reporting:** Execute test cases, detect defects, format Defect ID, summary, steps, severity, priority, expected vs actual. | [`DefectReporter.java`](file:///d:/PROJECTS/stalab/03_ecommerce_defect_reporting/src/main/java/com/sta/defectreport/DefectReporter.java)<br>👉 [Lab Manual: `EXP.md`](file:///d:/PROJECTS/stalab/03_ecommerce_defect_reporting/EXP.md) | `cd 03_ecommerce_defect_reporting`<br>`mvn clean test`<br>`mvn compile exec:java` |
| **4** | [`04_inventory_test_plan_and_cases`](file:///d:/PROJECTS/stalab/04_inventory_test_plan_and_cases) | **Inventory Control System:** Test plan and test cases for Product Addition, Stock Updates, Availability, Low-Stock Alerts, Reports. | [`InventoryApp.java`](file:///d:/PROJECTS/stalab/04_inventory_test_plan_and_cases/src/main/java/com/sta/inventory/InventoryApp.java)<br>👉 [Lab Manual: `EXP.md`](file:///d:/PROJECTS/stalab/04_inventory_test_plan_and_cases/EXP.md) | `cd 04_inventory_test_plan_and_cases`<br>`mvn clean test`<br>`mvn compile exec:java` |
| **5** | [`05_desktop_clientserver_testing`](file:///d:/PROJECTS/stalab/05_desktop_clientserver_testing) | **Client-Server / Desktop Testing:** Functional and usability defect analysis, race conditions, error prevention, heuristic violations. | [`ClientServerTestRunner.java`](file:///d:/PROJECTS/stalab/05_desktop_clientserver_testing/src/main/java/com/sta/clientserver/ClientServerTestRunner.java)<br>👉 [Lab Manual: `EXP.md`](file:///d:/PROJECTS/stalab/05_desktop_clientserver_testing/EXP.md) | `cd 05_desktop_clientserver_testing`<br>`mvn clean test`<br>`mvn compile exec:java` |
| **6** | [`06_performance_load_stress_testing`](file:///d:/PROJECTS/stalab/06_performance_load_stress_testing) | **Performance Load & Stress Testing:** Measure response time percentiles (p50, p90, p95), throughput (TPS), error rate, saturation breakpoint. | [`PerformanceApp.java`](file:///d:/PROJECTS/stalab/06_performance_load_stress_testing/src/main/java/com/sta/performance/PerformanceApp.java)<br>👉 [Lab Manual: `EXP.md`](file:///d:/PROJECTS/stalab/06_performance_load_stress_testing/EXP.md) | `cd 06_performance_load_stress_testing`<br>`mvn clean test`<br>`mvn compile exec:java` |
| **7** | [`07_selenium_webdriver_automation`](file:///d:/PROJECTS/stalab/07_selenium_webdriver_automation) | **Selenium WebDriver Automation:** Automate Login, Catalog Search, Product Selection, Cart Operations on real site (`saucedemo.com`). | [`EcommerceSeleniumScript.java`](file:///d:/PROJECTS/stalab/07_selenium_webdriver_automation/src/main/java/com/sta/selenium/EcommerceSeleniumScript.java)<br>👉 [Lab Manual: `EXP.md`](file:///d:/PROJECTS/stalab/07_selenium_webdriver_automation/EXP.md) | `cd 07_selenium_webdriver_automation`<br>`mvn clean test`<br>`mvn compile exec:java` |
| **8** | [`08_testng_selenium_integration`](file:///d:/PROJECTS/stalab/08_testng_selenium_integration) | **TestNG Integration:** Annotations (`@BeforeSuite`, `@Test`), Hard & Soft Assertions, Grouping (`smoke`, `regression`), Prioritization, Reports. | [`EcommerceTestNGSuite.java`](file:///d:/PROJECTS/stalab/08_testng_selenium_integration/src/test/java/com/sta/testng/EcommerceTestNGSuite.java)<br>👉 [Lab Manual: `EXP.md`](file:///d:/PROJECTS/stalab/08_testng_selenium_integration/EXP.md) | `cd 08_testng_selenium_integration`<br>`mvn clean test` |
| **9** | [`09_datadriven_framework_testng`](file:///d:/PROJECTS/stalab/09_datadriven_framework_testng) | **Data-Driven Automation Framework:** Read external CSV test data and execute parameterized tests with multiple datasets via `@DataProvider`. | [`DataDrivenLoginTest.java`](file:///d:/PROJECTS/stalab/09_datadriven_framework_testng/src/test/java/com/sta/datadriven/DataDrivenLoginTest.java)<br>👉 [Lab Manual: `EXP.md`](file:///d:/PROJECTS/stalab/09_datadriven_framework_testng/EXP.md) | `cd 09_datadriven_framework_testng`<br>`mvn clean test` |
| **10** | [`10_pom_framework_selenium_testng`](file:///d:/PROJECTS/stalab/10_pom_framework_selenium_testng) | **Page Object Model (POM) Framework:** Encapsulate Page Objects, locators, actions, and evaluate maintainability and flakiness reduction. | [`LoginPage.java`](file:///d:/PROJECTS/stalab/10_pom_framework_selenium_testng/src/main/java/com/sta/pom/pages/LoginPage.java)<br>[`EcommercePOMTest.java`](file:///d:/PROJECTS/stalab/10_pom_framework_selenium_testng/src/test/java/com/sta/pom/tests/EcommercePOMTest.java)<br>👉 [Lab Manual: `EXP.md`](file:///d:/PROJECTS/stalab/10_pom_framework_selenium_testng/EXP.md) | `cd 10_pom_framework_selenium_testng`<br>`mvn clean test` |
| **11** | [`11_bdd_cucumber_testng_framework`](file:///d:/PROJECTS/stalab/11_bdd_cucumber_testng_framework) | **BDD Framework (Cucumber + TestNG):** Gherkin feature files, Step Definitions, Scenario Outlines, Cucumber HTML/JSON execution reports. | Feature: `ecommerce_flows.feature`<br>Steps: [`EcommerceSteps.java`](file:///d:/PROJECTS/stalab/11_bdd_cucumber_testng_framework/src/test/java/com/sta/bdd/steps/EcommerceSteps.java)<br>👉 [Lab Manual: `EXP.md`](file:///d:/PROJECTS/stalab/11_bdd_cucumber_testng_framework/EXP.md) | `cd 11_bdd_cucumber_testng_framework`<br>`mvn clean test` |

---

## 🛠️ Global Prerequisites (Setting Up on Any New Computer)

To clone and run any of these projects on a brand-new computer (Windows, macOS, or Linux), ensure the following are installed:

### 1. Java Development Kit (JDK 17 or 21 LTS)
* **Check if installed:**
  ```bash
  java -version
  javac -version
  ```
* **Installation:**
  * **Windows:** Download and install JDK 17/21 from [Eclipse Adoptium](https://adoptium.net/). Ensure `JAVA_HOME` environment variable points to `C:\Program Files\Eclipse Adoptium\jdk-17...` and `%JAVA_HOME%\bin` is added to your system `PATH`.
  * **Ubuntu / Debian Linux:**
    ```bash
    sudo apt update && sudo apt install openjdk-17-jdk -y
    ```
  * **macOS:**
    ```bash
    brew install openjdk@17
    ```

### 2. Apache Maven (3.8 or higher)
* **Check if installed:**
  ```bash
  mvn -version
  ```
* **Installation:**
  * **Windows:** Download the binary zip from [Apache Maven](https://maven.apache.org/download.cgi), extract to `C:\Program Files\apache-maven`, and add `C:\Program Files\apache-maven\bin` to your system `PATH`.
  * **Ubuntu / Debian Linux:**
    ```bash
    sudo apt install maven -y
    ```
  * **macOS:**
    ```bash
    brew install maven
    ```

### 3. Google Chrome Browser (Latest Version)
* Used by the Selenium WebDriver test suites (Folders 07, 08, 09, 10, 11).
* **Automatic Driver Management:** All Selenium automation scripts use **Selenium 4.18.1** with built-in **SeleniumManager**. You do **not** need to manually download or set the path for `chromedriver.exe` — Selenium automatically detects your installed Chrome version and configures the corresponding driver binary automatically.
* All tests are configured to run in high-performance **headless mode** (`--headless=new`) by default, making them fully compatible with headless servers, Linux VMs, or CI/CD pipelines without needing a display monitor.

---

## 🚀 How to Run Any Lab Experiment

Each folder is completely standalone. Simply navigate (`cd`) into the desired folder and run Maven commands:

### Example: Running Experiment 04 (Inventory Control System)
```bash
cd 04_inventory_test_plan_and_cases
mvn clean test
mvn compile exec:java
```

### Example: Running Experiment 08 (TestNG & Selenium)
```bash
cd 08_testng_selenium_integration
mvn clean test
# To run only the Smoke test group:
mvn test -Dsurefire.suiteXmlFiles=src/test/resources/testng-smoke.xml
```

### Example: Running Experiment 11 (Cucumber BDD)
```bash
cd 11_bdd_cucumber_testng_framework
mvn clean test
```
Interactive HTML test reports will be generated in `target/cucumber-reports/cucumber.html`.
