# Experiment 11: Behaviour-Driven Development (BDD) Framework Using Cucumber, Selenium & TestNG

## Question
> Build a Behaviour-Driven Development (BDD) framework using Selenium WebDriver, TestNG, and Cucumber. Write feature files using Gherkin syntax, implement step definitions, execute scenarios, and evaluate the test results.

---

## 📝 Main Files to Write in Lab Exam / Practical
In your lab exam, this is the BDD stack you write:
* **Documentation File:** `BDD_EVALUATION.md` (Comprehensive BDD evaluation report analyzing living documentation, Gherkin syntax, and Cucumber execution metrics)
* **Gherkin Feature File:** [`src/test/resources/features/ecommerce_flows.feature`](file:///d:/PROJECTS/stalab/11_bdd_cucumber_testng_framework/src/test/resources/features/ecommerce_flows.feature)
  * Implements `Feature:`, `Background:`, `@Smoke`, `@Regression`, `Scenario:`, and `Scenario Outline:` with `Examples:` table.
* **Step Definitions Java Class:** [`src/test/java/com/sta/bdd/steps/EcommerceSteps.java`](file:///d:/PROJECTS/stalab/11_bdd_cucumber_testng_framework/src/test/java/com/sta/bdd/steps/EcommerceSteps.java)
  * **Class Name:** `com.sta.bdd.steps.EcommerceSteps`
  * **Function:** Maps `@Given`, `@When`, `@Then`, `@And` annotations to Selenium WebDriver browser actions and TestNG assertions.
* **Cucumber TestNG Runner:** [`src/test/java/com/sta/bdd/runner/CucumberRunnerTest.java`](file:///d:/PROJECTS/stalab/11_bdd_cucumber_testng_framework/src/test/java/com/sta/bdd/runner/CucumberRunnerTest.java)
  * **Class Name:** `com.sta.bdd.runner.CucumberRunnerTest` (extends `AbstractTestNGCucumberTests` with `@CucumberOptions`)
* **Hooks Class:** [`src/test/java/com/sta/bdd/steps/Hooks.java`](file:///d:/PROJECTS/stalab/11_bdd_cucumber_testng_framework/src/test/java/com/sta/bdd/steps/Hooks.java) (`@Before`, `@After` scenario hooks)

---

## Deliverables in this Directory
1. **[BDD_EVALUATION.md](file:///d:/PROJECTS/stalab/11_bdd_cucumber_testng_framework/BDD_EVALUATION.md)** - Full architectural evaluation report on BDD.
2. **Gherkin Feature Files (`src/test/resources/features/`)** -
   - `ecommerce_flows.feature`: Positive login, data-driven negative login with Examples, and cart management scenarios.
3. **Step Definitions & Hooks (`src/test/java/com/sta/bdd/steps/`)** -
   - `EcommerceSteps.java`: Maps Gherkin expressions to Selenium actions.
   - `Hooks.java`: Scenario lifecycle hooks.
   - `DriverManager.java`: Thread-safe WebDriver factory.
4. **Cucumber TestNG Runner (`src/test/java/com/sta/bdd/runner/`)** -
   - `CucumberRunnerTest.java`: Extends `AbstractTestNGCucumberTests` configured with HTML and JSON reporting plugins.
5. **`pom.xml`** - Self-contained Maven configuration with Cucumber 7.x, Selenium 4, and TestNG.

---

## Prerequisites (Setting Up on Any New Computer)
1. **Java JDK (17 or higher)** (`java -version`)
2. **Apache Maven (3.8 or higher)** (`mvn -version`)
3. **Google Chrome Browser** (Latest version). Driver managed automatically.

---

## How to Build and Run

Open terminal/PowerShell inside this folder:
```bash
cd 11_bdd_cucumber_testng_framework
```

### Run All Cucumber BDD Scenarios via Maven
```bash
mvn clean test
```

---

## Viewing Cucumber Test Reports
After test execution, comprehensive visual reports are created in:
- HTML Interactive Report: `target/cucumber-reports/cucumber.html`
- JSON Metrics Report: `target/cucumber-reports/cucumber.json`

Open HTML report in browser:
```powershell
Start-Process target/cucumber-reports/cucumber.html
```

---

## Expected Output
```text
Scenario: Successful user login with valid credentials                     # src/test/resources/features/ecommerce_flows.feature:9
  Given the user navigates to the e-commerce storefront                   # com.sta.bdd.steps.EcommerceSteps.userNavigatesToStorefront()
  When the user enters username "standard_user" and password "secret_sauce" # com.sta.bdd.steps.EcommerceSteps.userEntersCredentials(java.lang.String,java.lang.String)
  And clicks on the login button                                          # com.sta.bdd.steps.EcommerceSteps.clicksLoginButton()
  Then the user should be redirected to the product catalog page          # com.sta.bdd.steps.EcommerceSteps.verifyCatalogRedirect()
  And the catalog should display available products                       # com.sta.bdd.steps.EcommerceSteps.verifyCatalogItems()

Scenario Outline: Login failure with invalid credentials                  # src/test/resources/features/ecommerce_flows.feature:22
  ...
Scenario: Adding product to cart and verifying cart contents              # src/test/resources/features/ecommerce_flows.feature:26
  ...
5 Scenarios (5 passed)
28 Steps (28 passed)
[INFO] Tests run: 5, Failures: 0, Errors: 0, Skipped: 0
[INFO] BUILD SUCCESS
```
