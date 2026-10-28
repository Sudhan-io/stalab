# Experiment 11 (Model 2): BDD Automation Framework with Cucumber, TestNG & Scenario Outlines

## Target Website
- **URL**: `https://www.saucedemo.com` (Live real-world e-commerce application)

## Overview
Develops a complete Behaviour-Driven Development (BDD) framework using Selenium WebDriver, Cucumber, and TestNG:
1. **Gherkin Feature File**: `src/test/resources/features/ecommerce_shopping.feature` with:
   - `Scenario Outline` with `Examples` table validating multiple personas (valid user, locked-out user, wrong password).
   - End-to-end `Scenario` for product selection, cart management, and checkout.
2. **Cucumber Hooks**: `@Before` and `@After` in `DriverHooks.java` managing WebDriver lifecycle per scenario.
3. **Step Definitions**: `ShoppingSteps.java` implementing step logic with explicit waits and TestNG assertions.
4. **TestNG Runner**: `CucumberRunnerTest.java` extending `AbstractTestNGCucumberTests` generating HTML and JSON reports.

## Main Java File to Write in Lab
- **`src/test/java/com/sta/bdd/ShoppingSteps.java`**

## Prerequisites
- Java JDK 17 or higher
- Apache Maven 3.8+
- Google Chrome browser installed

## How to Set Up and Run on a New Computer
1. Open terminal inside this folder:
   ```bash
   cd model2_11_bdd_cucumber_scenario_outlines
   ```
2. Run BDD test scenarios in headless mode:
   ```bash
   mvn clean test
   ```
3. Run with **Visible Browser GUI Window**:
   ```bash
   mvn test -Dheadless=false
   ```
4. View generated Cucumber HTML report:
   Open `target/cucumber-reports/cucumber.html` in any web browser.

