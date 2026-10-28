# Experiment 08 (Model 2): TestNG Integration with Parallel Data Providers, Grouping & Assertions

## Target Website
- **URL**: `https://www.saucedemo.com` (Live real-world e-commerce application)

## Overview
Demonstrates full enterprise TestNG integration with Selenium WebDriver:
1. **Test Annotations**: `@BeforeClass`, `@AfterClass`, `@Test`, `@DataProvider`.
2. **Data Providers**: Parameterized `@DataProvider` feeding multiple distinct checkout customer profiles.
3. **Hard Assertions**: `Assert.assertEquals`, `Assert.assertTrue` for status validation.
4. **Method Dependencies**: Upstream dependency management using `dependsOnMethods = {"testLogin"}`.
5. **Grouping**: Categorization using `groups = {"smoke"}` and `groups = {"regression"}`.
6. **Automated Test Reporting**: Custom `ITestListener` (`CustomReportListener`) generating real-time start/pass/fail console and metrics reports.

## Main Java File to Write in Lab
- **`src/test/java/com/sta/testng/TestNGParallelSuite.java`**

## Prerequisites
- Java JDK 17 or higher
- Apache Maven 3.8+
- Google Chrome browser installed

## How to Set Up and Run on a New Computer
1. Open terminal inside this directory:
   ```bash
   cd model2_08_testng_parallel_dataproviders
   ```
2. Run TestNG suite via Maven in headless mode:
   ```bash
   mvn clean test
   ```
3. Run with **Visible Browser GUI Window**:
   ```bash
   mvn test -Dheadless=false
   ```
4. Run only the `smoke` group:
   ```bash
   mvn test -Dgroups=smoke
   ```

