# Experiment 08: TestNG and Selenium WebDriver Integration

## Question
> Integrate TestNG with Selenium automation scripts. Implement test annotations, assertions, grouping, prioritization, and test execution reporting.

---

## 📝 Main Java File(s) to Write in Lab Exam / Practical
In your lab exam, this is the core TestNG integration suite and custom listener you write:
* **Main Java Class to Write:** [`src/test/java/com/sta/testng/EcommerceTestNGSuite.java`](file:///d:/PROJECTS/stalab/08_testng_selenium_integration/src/test/java/com/sta/testng/EcommerceTestNGSuite.java)
  * **Class Name:** `com.sta.testng.EcommerceTestNGSuite`
  * **TestNG Annotations Implemented:**
    * `@BeforeSuite`, `@BeforeTest`, `@BeforeClass`, `@BeforeMethod`
    * `@Test(priority = ..., groups = {...}, dependsOnMethods = {...})`
    * `@AfterMethod`, `@AfterClass`, `@AfterTest`, `@AfterSuite`
  * **Assertions Implemented:**
    * Hard Assertions: `Assert.assertEquals()`, `Assert.assertTrue()`
    * Soft Assertions: `SoftAssert softAssert = new SoftAssert(); ... softAssert.assertAll();`
  * **Grouping Implemented:** `groups = {"smoke", "regression"}`, `groups = {"cart"}`
  * **Prioritization Implemented:** `priority = 1`, `priority = 2`, `priority = 3`, `priority = 4`, `priority = 5`
* **Custom Execution Listener:** [`src/main/java/com/sta/testng/listener/CustomTestNGListener.java`](file:///d:/PROJECTS/stalab/08_testng_selenium_integration/src/main/java/com/sta/testng/listener/CustomTestNGListener.java) (implements `org.testng.ITestListener`)
* **XML Suite Configurations:**
  - `src/test/resources/testng.xml` (Full test suite)
  - `src/test/resources/testng-smoke.xml` (Runs only `@Test(groups={"smoke"})`)

---

## Deliverables in this Directory
1. **`src/test/java/com/sta/testng/EcommerceTestNGSuite.java`** - Comprehensive TestNG automation suite.
2. **`src/main/java/com/sta/testng/listener/CustomTestNGListener.java`** - Custom listener reporting execution durations and summaries.
3. **`src/test/resources/testng.xml`** - Master TestNG execution suite.
4. **`src/test/resources/testng-smoke.xml`** - Smoke test group runner.
5. **`pom.xml`** - Self-contained Maven configuration.

---

## Prerequisites (Setting Up on Any New Computer)
1. **Java JDK (17 or higher)** (`java -version`)
2. **Apache Maven (3.8 or higher)** (`mvn -version`)
3. **Google Chrome Browser** (Latest version). Driver managed automatically by SeleniumManager.

---

## How to Build and Run

Open terminal/PowerShell inside this folder:
```bash
cd 08_testng_selenium_integration
```

### 1. Run Complete TestNG Automation Suite (with Custom Listener & Reports)
```bash
mvn clean test
```

### 2. Run Only the 'Smoke' Test Group
```bash
mvn test -Dsurefire.suiteXmlFiles=src/test/resources/testng-smoke.xml
```

---

## Viewing Test Reports
After running `mvn clean test`, TestNG generates reports in:
- HTML Dashboard: `target/surefire-reports/index.html`
- Emailable HTML Report: `target/surefire-reports/emailable-report.html`

---

## Expected Output
```text
[@BeforeSuite] Global Test Automation Framework Initialized.
[@BeforeClass] Launching Headless Chrome Browser Instance.
===============================================================================
 [TESTNG LISTENER] STARTING TEST SUITE: E-Commerce End-to-End Regression Tests
===============================================================================
  --> [STARTED] EcommerceTestNGSuite#testLogin() | Priority: 1 | Groups: [smoke, regression]
  <-- [PASSED ] EcommerceTestNGSuite#testLogin() in 1283 ms
  --> [STARTED] EcommerceTestNGSuite#testProductCatalogDisplay() | Priority: 2 | Groups: [regression]
  <-- [PASSED ] EcommerceTestNGSuite#testProductCatalogDisplay() in 109 ms
  --> [STARTED] EcommerceTestNGSuite#testProductSelection() | Priority: 3 | Groups: [regression]
  <-- [PASSED ] EcommerceTestNGSuite#testProductSelection() in 629 ms
  --> [STARTED] EcommerceTestNGSuite#testAddToCart() | Priority: 4 | Groups: [cart, smoke, regression]
  <-- [PASSED ] EcommerceTestNGSuite#testAddToCart() in 110 ms
  --> [STARTED] EcommerceTestNGSuite#testCartVerificationAndRemoval() | Priority: 5 | Groups: [cart, regression]
  <-- [PASSED ] EcommerceTestNGSuite#testCartVerificationAndRemoval() in 110 ms
===============================================================================
 [TESTNG LISTENER] SUITE FINISHED: Passed: 5 | Failed: 0 | Skipped: 0
===============================================================================
[@AfterClass] Closing Browser WebDriver Instance.
[@AfterSuite] TestNG Execution Suite Complete. Reports generated in target/surefire-reports.
```
