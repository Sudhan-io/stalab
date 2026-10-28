# Software Testing & Automation (STA) Laboratory Suite

This repository contains completely independent, standalone, production-ready Java and Selenium WebDriver implementations for all Software Testing and Automation (STA) lab experiments across **Model 1** and **Model 2** curricula.

Every folder is **100% self-contained**:
- Has its own `pom.xml` with all necessary dependencies.
- Can be compiled and executed directly inside its own folder without relying on any parent or root configuration.
- Includes comprehensive documentation (IEEE 829 Test Plans, Test Case matrices, Defect reports, Performance benchmarks, Framework maintainability audits).
- Includes a dedicated `README.md` detailing the **Main Java File to Write in Lab Exam**, prerequisites, and how to run on any computer.
- Includes a dedicated `EXP.md` containing **Aim**, **Algorithm / Procedure**, **Main Java Program**, and verified **Sample Output**.

---

## 📋 Model 1 Lab Experiments Directory Index

| Exp # | Directory Name | Lab Objective | Main Java File & Lab Manual (`EXP.md`) | Run Command |
|:---:|:---|:---|:---|:---|
| **01** | [`01_ecommerce_test_plan`](file:///d:/PROJECTS/stalab/01_ecommerce_test_plan) | **E-Commerce Test Plan:** Objectives, scope, environment, strategy, schedule, entry/exit criteria. | [`TestPlanApp.java`](file:///d:/PROJECTS/stalab/01_ecommerce_test_plan/src/main/java/com/sta/testplan/TestPlanApp.java)<br>👉 [Lab Manual: `EXP.md`](file:///d:/PROJECTS/stalab/01_ecommerce_test_plan/EXP.md) | `cd 01_ecommerce_test_plan`<br>`mvn clean test`<br>`mvn compile exec:java` |
| **02** | [`02_ecommerce_test_cases`](file:///d:/PROJECTS/stalab/02_ecommerce_test_cases) | **E-Commerce Test Cases:** Design & document test cases for Registration, Login, Search, Selection, Cart, Checkout, Payment, Order Confirmation. | [`TestCaseExecutor.java`](file:///d:/PROJECTS/stalab/02_ecommerce_test_cases/src/main/java/com/sta/testcases/TestCaseExecutor.java)<br>👉 [Lab Manual: `EXP.md`](file:///d:/PROJECTS/stalab/02_ecommerce_test_cases/EXP.md) | `cd 02_ecommerce_test_cases`<br>`mvn clean test`<br>`mvn compile exec:java` |
| **03** | [`03_ecommerce_defect_reporting`](file:///d:/PROJECTS/stalab/03_ecommerce_defect_reporting) | **Defect Identification & Reporting:** Execute test cases, detect defects, format Defect ID, summary, steps, severity, priority, expected vs actual. | [`DefectReporter.java`](file:///d:/PROJECTS/stalab/03_ecommerce_defect_reporting/src/main/java/com/sta/defectreport/DefectReporter.java)<br>👉 [Lab Manual: `EXP.md`](file:///d:/PROJECTS/stalab/03_ecommerce_defect_reporting/EXP.md) | `cd 03_ecommerce_defect_reporting`<br>`mvn clean test`<br>`mvn compile exec:java` |
| **04** | [`04_inventory_test_plan_and_cases`](file:///d:/PROJECTS/stalab/04_inventory_test_plan_and_cases) | **Inventory Control System:** Test plan and test cases for Product Addition, Stock Updates, Availability, Low-Stock Alerts, Reports. | [`InventoryApp.java`](file:///d:/PROJECTS/stalab/04_inventory_test_plan_and_cases/src/main/java/com/sta/inventory/InventoryApp.java)<br>👉 [Lab Manual: `EXP.md`](file:///d:/PROJECTS/stalab/04_inventory_test_plan_and_cases/EXP.md) | `cd 04_inventory_test_plan_and_cases`<br>`mvn clean test`<br>`mvn compile exec:java` |
| **05** | [`05_desktop_clientserver_testing`](file:///d:/PROJECTS/stalab/05_desktop_clientserver_testing) | **Client-Server / Desktop Testing:** Functional and usability defect analysis, race conditions, error prevention, heuristic violations. | [`ClientServerTestRunner.java`](file:///d:/PROJECTS/stalab/05_desktop_clientserver_testing/src/main/java/com/sta/clientserver/ClientServerTestRunner.java)<br>👉 [Lab Manual: `EXP.md`](file:///d:/PROJECTS/stalab/05_desktop_clientserver_testing/EXP.md) | `cd 05_desktop_clientserver_testing`<br>`mvn clean test`<br>`mvn compile exec:java` |
| **06** | [`06_performance_load_stress_testing`](file:///d:/PROJECTS/stalab/06_performance_load_stress_testing) | **Performance Load & Stress Testing:** Measure response time percentiles (p50, p90, p95), throughput (TPS), error rate, saturation breakpoint. | [`PerformanceApp.java`](file:///d:/PROJECTS/stalab/06_performance_load_stress_testing/src/main/java/com/sta/performance/PerformanceApp.java)<br>👉 [Lab Manual: `EXP.md`](file:///d:/PROJECTS/stalab/06_performance_load_stress_testing/EXP.md) | `cd 06_performance_load_stress_testing`<br>`mvn clean test`<br>`mvn compile exec:java` |
| **07** | [`07_selenium_webdriver_automation`](file:///d:/PROJECTS/stalab/07_selenium_webdriver_automation) | **Selenium WebDriver Automation:** Automate Login, Catalog Search, Product Selection, Cart Operations on real site (`saucedemo.com`). | [`EcommerceSeleniumScript.java`](file:///d:/PROJECTS/stalab/07_selenium_webdriver_automation/src/main/java/com/sta/selenium/EcommerceSeleniumScript.java)<br>👉 [Lab Manual: `EXP.md`](file:///d:/PROJECTS/stalab/07_selenium_webdriver_automation/EXP.md) | `cd 07_selenium_webdriver_automation`<br>`mvn clean test`<br>`mvn compile exec:java` |
| **08** | [`08_testng_selenium_integration`](file:///d:/PROJECTS/stalab/08_testng_selenium_integration) | **TestNG Integration:** Annotations (`@BeforeSuite`, `@Test`), Hard & Soft Assertions, Grouping (`smoke`, `regression`), Prioritization, Reports. | [`EcommerceTestNGSuite.java`](file:///d:/PROJECTS/stalab/08_testng_selenium_integration/src/test/java/com/sta/testng/EcommerceTestNGSuite.java)<br>👉 [Lab Manual: `EXP.md`](file:///d:/PROJECTS/stalab/08_testng_selenium_integration/EXP.md) | `cd 08_testng_selenium_integration`<br>`mvn clean test` |
| **09** | [`09_datadriven_framework_testng`](file:///d:/PROJECTS/stalab/09_datadriven_framework_testng) | **Data-Driven Automation Framework:** Read external CSV test data and execute parameterized tests with multiple datasets via `@DataProvider`. | [`DataDrivenLoginTest.java`](file:///d:/PROJECTS/stalab/09_datadriven_framework_testng/src/test/java/com/sta/datadriven/DataDrivenLoginTest.java)<br>👉 [Lab Manual: `EXP.md`](file:///d:/PROJECTS/stalab/09_datadriven_framework_testng/EXP.md) | `cd 09_datadriven_framework_testng`<br>`mvn clean test` |
| **10** | [`10_pom_framework_selenium_testng`](file:///d:/PROJECTS/stalab/10_pom_framework_selenium_testng) | **Page Object Model (POM) Framework:** Encapsulate Page Objects, locators, actions, and evaluate maintainability and flakiness reduction. | [`LoginPage.java`](file:///d:/PROJECTS/stalab/10_pom_framework_selenium_testng/src/main/java/com/sta/pom/pages/LoginPage.java)<br>[`EcommercePOMTest.java`](file:///d:/PROJECTS/stalab/10_pom_framework_selenium_testng/src/test/java/com/sta/pom/tests/EcommercePOMTest.java)<br>👉 [Lab Manual: `EXP.md`](file:///d:/PROJECTS/stalab/10_pom_framework_selenium_testng/EXP.md) | `cd 10_pom_framework_selenium_testng`<br>`mvn clean test` |
| **11** | [`11_bdd_cucumber_testng_framework`](file:///d:/PROJECTS/stalab/11_bdd_cucumber_testng_framework) | **BDD Framework (Cucumber + TestNG):** Gherkin feature files, Step Definitions, Scenario Outlines, Cucumber HTML/JSON execution reports. | Feature: `ecommerce_flows.feature`<br>Steps: [`EcommerceSteps.java`](file:///d:/PROJECTS/stalab/11_bdd_cucumber_testng_framework/src/test/java/com/sta/bdd/steps/EcommerceSteps.java)<br>👉 [Lab Manual: `EXP.md`](file:///d:/PROJECTS/stalab/11_bdd_cucumber_testng_framework/EXP.md) | `cd 11_bdd_cucumber_testng_framework`<br>`mvn clean test` |

---

## 📋 Model 2 Lab Experiments Directory Index

| Exp # | Directory Name | Lab Objective | Main Java File & Lab Manual (`EXP.md`) | Run Command |
|:---:|:---|:---|:---|:---|
| **01** | [`model2_01_shopping_test_plan`](file:///d:/PROJECTS/stalab/model2_01_shopping_test_plan) | **Online Shopping Comprehensive Test Plan:** Browsing, Auth, Wishlist, Order Placement, Payment, Order Tracking. | [`ShoppingTestPlanApp.java`](file:///d:/PROJECTS/stalab/model2_01_shopping_test_plan/src/main/java/com/sta/testplan/ShoppingTestPlanApp.java)<br>👉 [Lab Manual: `EXP.md`](file:///d:/PROJECTS/stalab/model2_01_shopping_test_plan/EXP.md) | `cd model2_01_shopping_test_plan`<br>`mvn clean test`<br>`mvn exec:java` |
| **02** | [`model2_02_ep_bva_test_cases`](file:///d:/PROJECTS/stalab/model2_02_ep_bva_test_cases) | **Equivalence Partitioning & Boundary Value Analysis:** Valid/invalid inputs for login, quantity, search fields, payment card & CVV. | [`EpBvaTestRunner.java`](file:///d:/PROJECTS/stalab/model2_02_ep_bva_test_cases/src/main/java/com/sta/blackbox/EpBvaTestRunner.java)<br>👉 [Lab Manual: `EXP.md`](file:///d:/PROJECTS/stalab/model2_02_ep_bva_test_cases/EXP.md) | `cd model2_02_ep_bva_test_cases`<br>`mvn clean test`<br>`mvn exec:java` |
| **03** | [`model2_03_defect_summary_report`](file:///d:/PROJECTS/stalab/model2_03_defect_summary_report) | **Defect Summary & Severity/Priority Classification:** Execute test cases, evaluate actual vs expected, classify defects, build defect report. | [`DefectSummaryReporter.java`](file:///d:/PROJECTS/stalab/model2_03_defect_summary_report/src/main/java/com/sta/defect/DefectSummaryReporter.java)<br>👉 [Lab Manual: `EXP.md`](file:///d:/PROJECTS/stalab/model2_03_defect_summary_report/EXP.md) | `cd model2_03_defect_summary_report`<br>`mvn clean test`<br>`mvn exec:java` |
| **04** | [`model2_04_warehouse_inventory`](file:///d:/PROJECTS/stalab/model2_04_warehouse_inventory) | **Warehouse Inventory System:** Test plan and test cases for Stock Receipt (GRN), Stock Issue, Inventory Adjustment, Suppliers, Reconciliation. | [`WarehouseInventoryApp.java`](file:///d:/PROJECTS/stalab/model2_04_warehouse_inventory/src/main/java/com/sta/warehouse/WarehouseInventoryApp.java)<br>👉 [Lab Manual: `EXP.md`](file:///d:/PROJECTS/stalab/model2_04_warehouse_inventory/EXP.md) | `cd model2_04_warehouse_inventory`<br>`mvn clean test`<br>`mvn exec:java` |
| **05** | [`model2_05_clientserver_input_validation`](file:///d:/PROJECTS/stalab/model2_05_clientserver_input_validation) | **Client-Server Functional, Input & Error Testing:** Multi-threaded socket tests, boundary overflow, malformed packets, defect evidence. | [`ClientServerTestSuite.java`](file:///d:/PROJECTS/stalab/model2_05_clientserver_input_validation/src/main/java/com/sta/clientserver/ClientServerTestSuite.java)<br>👉 [Lab Manual: `EXP.md`](file:///d:/PROJECTS/stalab/model2_05_clientserver_input_validation/EXP.md) | `cd model2_05_clientserver_input_validation`<br>`mvn clean test`<br>`mvn exec:java` |
| **06** | [`model2_06_load_peak_heavy_performance`](file:///d:/PROJECTS/stalab/model2_06_load_peak_heavy_performance) | **Normal, Peak & Heavy-Load Testing:** Concurrency tiers (10, 30, 75 VUs), latency percentiles, throughput (TPS), heap/thread utilization. | [`PerformanceReportApp.java`](file:///d:/PROJECTS/stalab/model2_06_load_peak_heavy_performance/src/main/java/com/sta/performance/PerformanceReportApp.java)<br>👉 [Lab Manual: `EXP.md`](file:///d:/PROJECTS/stalab/model2_06_load_peak_heavy_performance/EXP.md) | `cd model2_06_load_peak_heavy_performance`<br>`mvn clean test`<br>`mvn exec:java` |
| **07** | [`model2_07_selenium_filtering_sorting_checkout`](file:///d:/PROJECTS/stalab/model2_07_selenium_filtering_sorting_checkout) | **Selenium Filtering, Sorting & Checkout on Live App:** Real website (`saucedemo.com`), Low-to-High price sort, Z-to-A name sort, cart and checkout. | [`SortingCheckoutAutomation.java`](file:///d:/PROJECTS/stalab/model2_07_selenium_filtering_sorting_checkout/src/main/java/com/sta/selenium/SortingCheckoutAutomation.java)<br>👉 [Lab Manual: `EXP.md`](file:///d:/PROJECTS/stalab/model2_07_selenium_filtering_sorting_checkout/EXP.md) | `cd model2_07_selenium_filtering_sorting_checkout`<br>`mvn clean test`<br>`mvn test -Dheadless=false` |
| **08** | [`model2_08_testng_parallel_dataproviders`](file:///d:/PROJECTS/stalab/model2_08_testng_parallel_dataproviders) | **TestNG Parallel Data Providers, Grouping & Reports:** Data providers, method dependencies, smoke/regression groups, custom `ITestListener`. | [`TestNGParallelSuite.java`](file:///d:/PROJECTS/stalab/model2_08_testng_parallel_dataproviders/src/test/java/com/sta/testng/TestNGParallelSuite.java)<br>👉 [Lab Manual: `EXP.md`](file:///d:/PROJECTS/stalab/model2_08_testng_parallel_dataproviders/EXP.md) | `cd model2_08_testng_parallel_dataproviders`<br>`mvn clean test`<br>`mvn test -Dheadless=false` |
| **09** | [`model2_09_datadriven_login_multiple_datasets`](file:///d:/PROJECTS/stalab/model2_09_datadriven_login_multiple_datasets) | **Data-Driven Login with Multiple External Datasets:** External CSV data source, positive and negative login personas on live site. | [`DataDrivenLoginSuite.java`](file:///d:/PROJECTS/stalab/model2_09_datadriven_login_multiple_datasets/src/test/java/com/sta/datadriven/DataDrivenLoginSuite.java)<br>👉 [Lab Manual: `EXP.md`](file:///d:/PROJECTS/stalab/model2_09_datadriven_login_multiple_datasets/EXP.md) | `cd model2_09_datadriven_login_multiple_datasets`<br>`mvn clean test`<br>`mvn test -Dheadless=false` |
| **10** | [`model2_10_pom_search_cart_checkout`](file:///d:/PROJECTS/stalab/model2_10_pom_search_cart_checkout) | **Page Object Model (POM) for Search, Cart & Checkout:** Decoupled Page Objects, maintainability and reusability evaluation report. | [`Model2POMTestSuite.java`](file:///d:/PROJECTS/stalab/model2_10_pom_search_cart_checkout/src/test/java/com/sta/pom/Model2POMTestSuite.java)<br>👉 [Lab Manual: `EXP.md`](file:///d:/PROJECTS/stalab/model2_10_pom_search_cart_checkout/EXP.md) | `cd model2_10_pom_search_cart_checkout`<br>`mvn clean test`<br>`mvn test -Dheadless=false` |
| **11** | [`model2_11_bdd_cucumber_scenario_outlines`](file:///d:/PROJECTS/stalab/model2_11_bdd_cucumber_scenario_outlines) | **BDD Cucumber Framework with Scenario Outlines & Hooks:** Gherkin feature files, `@Before`/`@After` hooks, Scenario Outline tables, HTML reports. | Feature: `ecommerce_shopping.feature`<br>Steps: [`ShoppingSteps.java`](file:///d:/PROJECTS/stalab/model2_11_bdd_cucumber_scenario_outlines/src/test/java/com/sta/bdd/ShoppingSteps.java)<br>👉 [Lab Manual: `EXP.md`](file:///d:/PROJECTS/stalab/model2_11_bdd_cucumber_scenario_outlines/EXP.md) | `cd model2_11_bdd_cucumber_scenario_outlines`<br>`mvn clean test`<br>`mvn test -Dheadless=false` |

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
* Used by the Selenium WebDriver test suites (Folders 07-11, and model2_07 to model2_11).
* **Automatic Driver Management:** All Selenium automation scripts use built-in **SeleniumManager**. You do **not** need to manually download or set the path for `chromedriver.exe` — Selenium automatically detects your installed Chrome version and configures the corresponding driver binary automatically.
* All tests are configured to run in high-performance **headless mode** (`--headless=new`) by default, making them fully compatible with headless servers, Linux VMs, or CI/CD pipelines without needing a display monitor.
* To run with a **Visible Chrome Browser Window**, append `-Dheadless=false` to your maven command:
  ```bash
  mvn test -Dheadless=false
  ```
