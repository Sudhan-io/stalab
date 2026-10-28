# Experiment 11: Behaviour-Driven Development (BDD) Framework using Cucumber, Selenium & TestNG

## Aim
To build a Behaviour-Driven Development (BDD) test automation framework using Cucumber, Selenium WebDriver, and TestNG, write feature files using human-readable Gherkin syntax (`Given`, `When`, `Then`, `And`), implement step definitions, execute positive and negative scenarios, and evaluate test results on a real e-commerce storefront (`https://www.saucedemo.com`).

---

## Algorithm / Procedure

1. **Write Gherkin Feature Specifications:**
   - Create `src/test/resources/features/ecommerce_flows.feature`.
   - Formulate business-readable scenarios using Gherkin keywords:
     - `Feature:` High-level business capability.
     - `Background:` Common prerequisite step (`Given the user navigates to the e-commerce storefront`).
     - `Scenario:` Single test flow (valid login, cart operations).
     - `Scenario Outline:` Parameterized template with `Examples` table (data-driven invalid login tests).
2. **Implement Cucumber Step Definitions (`EcommerceSteps`):**
   - Annotate Java methods with `@Given`, `@When`, `@Then`, and `@And` matching regex/cucumber expressions from the feature file.
   - Interact with the live browser using Selenium WebDriver and validate outcomes using TestNG assertions.
3. **Configure Lifecycle Hooks (`Hooks`):**
   - Implement `@Before` hook to initialize fresh browser sessions and log scenario start.
   - Implement `@After` hook to log pass/fail status and quit WebDriver instances.
4. **Implement TestNG Cucumber Runner (`CucumberRunnerTest`):**
   - Extend `AbstractTestNGCucumberTests` and annotate with `@CucumberOptions`:
     - `features`: Path to `.feature` files.
     - `glue`: Step definition package.
     - `plugin`: Output plugins (`pretty`, `html:target/cucumber-reports/report.html`, `json:target/cucumber-reports/report.json`).
5. **Execute and Review BDD Execution Reports:**
   - Run the suite via Maven `mvn test` and open the generated HTML report.

---

## Gherkin Feature File

### File: `src/test/resources/features/ecommerce_flows.feature`
```gherkin
Feature: E-Commerce Storefront Customer Journey Flows
  As a customer of the e-commerce store
  I want to be able to authenticate, browse products, and manage my shopping cart
  So that I can purchase items seamlessly

  Background:
    Given the user navigates to the e-commerce storefront

  @Smoke @Regression
  Scenario: Successful user login with valid credentials
    When the user enters username "standard_user" and password "secret_sauce"
    And clicks on the login button
    Then the user should be redirected to the product catalog page
    And the catalog should display available products

  @Negative @Regression
  Scenario Outline: Login failure with invalid credentials
    When the user enters username "<username>" and password "<password>"
    And clicks on the login button
    Then an error message containing "<expected_error>" should be displayed

    Examples:
      | username         | password     | expected_error                                |
      | locked_out_user  | secret_sauce | Sorry, this user has been locked out          |
      | invalid_user     | wrong_pass   | Username and password do not match any user   |
      |                  | secret_sauce | Username is required                          |

  @Cart @Regression
  Scenario: Adding product to cart and verifying cart contents
    Given the user is logged in with username "standard_user" and password "secret_sauce"
    When the user selects the product "Sauce Labs Backpack"
    And clicks the add to cart button
    Then the shopping cart badge count should be "1"
    When the user opens the shopping cart
    Then the cart should contain the item "Sauce Labs Backpack"
    When the user removes "Sauce Labs Backpack" from the cart
    Then the item should no longer be present in the cart
```

---

## Main Java Program

### 1. Step Definitions: `src/test/java/com/sta/bdd/steps/EcommerceSteps.java`
```java
package com.sta.bdd.steps;

import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;

import java.time.Duration;
import java.util.List;

public class EcommerceSteps {

    private WebDriver getDriver() {
        return DriverManager.getDriver();
    }

    private WebDriverWait getWait() {
        return new WebDriverWait(getDriver(), Duration.ofSeconds(10));
    }

    private static final String APP_URL = "https://www.saucedemo.com";

    @Given("the user navigates to the e-commerce storefront")
    public void userNavigatesToStorefront() {
        getDriver().get(APP_URL);
    }

    @When("the user enters username {string} and password {string}")
    public void userEntersCredentials(String username, String password) {
        WebElement userField = getWait().until(ExpectedConditions.visibilityOfElementLocated(By.id("user-name")));
        WebElement passField = getDriver().findElement(By.id("password"));

        userField.clear();
        if (!username.isEmpty()) {
            userField.sendKeys(username);
        }

        passField.clear();
        if (!password.isEmpty()) {
            passField.sendKeys(password);
        }
    }

    @And("clicks on the login button")
    public void clicksLoginButton() {
        WebElement loginBtn = getDriver().findElement(By.id("login-button"));
        loginBtn.click();
    }

    @Then("the user should be redirected to the product catalog page")
    public void verifyCatalogRedirect() {
        getWait().until(ExpectedConditions.urlContains("inventory"));
        Assert.assertTrue(getDriver().getCurrentUrl().contains("inventory"), "URL should contain 'inventory'");
    }

    @And("the catalog should display available products")
    public void verifyCatalogItems() {
        List<WebElement> items = getWait().until(ExpectedConditions.visibilityOfAllElementsLocatedBy(By.className("inventory_item_name")));
        Assert.assertTrue(items.size() >= 4, "Catalog should have at least 4 products");
    }

    @Given("the user is logged in with username {string} and password {string}")
    public void userIsLoggedIn(String user, String pass) {
        userEntersCredentials(user, pass);
        clicksLoginButton();
        verifyCatalogRedirect();
    }

    @When("the user selects the product {string}")
    public void selectProduct(String productName) {
        WebElement item = getWait().until(ExpectedConditions.elementToBeClickable(
            By.xpath("//div[text()='" + productName + "']")));
        item.click();
    }

    @And("clicks the add to cart button")
    public void clickAddToCart() {
        WebElement btn = getWait().until(ExpectedConditions.presenceOfElementLocated(
            By.xpath("//button[contains(@id, 'add-to-cart') or text()='Add to cart' or contains(text(), 'Add to cart')]")));
        try {
            btn.click();
        } catch (Exception e) {
            ((JavascriptExecutor) getDriver()).executeScript("arguments[0].click();", btn);
        }
    }

    @Then("the shopping cart badge count should be {string}")
    public void verifyCartBadge(String count) {
        WebElement badge = getWait().until(ExpectedConditions.visibilityOfElementLocated(By.className("shopping_cart_badge")));
        Assert.assertEquals(badge.getText(), count);
    }

    @When("the user opens the shopping cart")
    public void openShoppingCart() {
        WebElement link = getWait().until(ExpectedConditions.presenceOfElementLocated(By.className("shopping_cart_link")));
        try {
            link.click();
        } catch (Exception e) {
            ((JavascriptExecutor) getDriver()).executeScript("arguments[0].click();", link);
        }
    }

    @Then("the cart should contain the item {string}")
    public void verifyCartItem(String productName) {
        WebElement item = getWait().until(ExpectedConditions.visibilityOfElementLocated(By.className("inventory_item_name")));
        Assert.assertEquals(item.getText(), productName);
    }

    @When("the user removes {string} from the cart")
    public void removeItemFromCart(String productName) {
        WebElement removeBtn = getWait().until(ExpectedConditions.presenceOfElementLocated(
            By.xpath("//button[contains(@id, 'remove') or text()='Remove' or contains(text(), 'Remove')]")));
        try {
            removeBtn.click();
        } catch (Exception e) {
            ((JavascriptExecutor) getDriver()).executeScript("arguments[0].click();", removeBtn);
        }
    }

    @Then("the item should no longer be present in the cart")
    public void verifyItemRemoved() {
        List<WebElement> items = getDriver().findElements(By.className("cart_item"));
        Assert.assertTrue(items.isEmpty(), "Cart item should have been removed");
    }

    @Then("an error message containing {string} should be displayed")
    public void verifyErrorMessage(String expectedSubstr) {
        WebElement errorBox = getWait().until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("h3[data-test='error']")));
        Assert.assertTrue(errorBox.getText().contains(expectedSubstr),
            "Expected error message containing: " + expectedSubstr + " but was: " + errorBox.getText());
    }
}
```

### 2. Test Runner: `src/test/java/com/sta/bdd/runner/CucumberRunnerTest.java`
```java
package com.sta.bdd.runner;

import io.cucumber.testng.AbstractTestNGCucumberTests;
import io.cucumber.testng.CucumberOptions;
import org.testng.annotations.DataProvider;

@CucumberOptions(
    features = "src/test/resources/features",
    glue = {"com.sta.bdd.steps"},
    plugin = {
        "pretty",
        "html:target/cucumber-reports/cucumber-report.html",
        "json:target/cucumber-reports/cucumber-report.json"
    },
    monochrome = true
)
public class CucumberRunnerTest extends AbstractTestNGCucumberTests {

    @Override
    @DataProvider(parallel = false)
    public Object[][] scenarios() {
        return super.scenarios();
    }
}
```

---

## Sample Output (Live Website: SauceDemo.com)

```text
@Smoke @Regression
Scenario: Successful user login with valid credentials
===============================================================================
 [CUCUMBER SCENARIO START] Successful user login with valid credentials
===============================================================================
  Given the user navigates to the e-commerce storefront
  When the user enters username "standard_user" and password "secret_sauce"
  And clicks on the login button
  Then the user should be redirected to the product catalog page
  And the catalog should display available products
 [CUCUMBER SCENARIO END] Status: PASSED

@Negative @Regression
Scenario Outline: Login failure with invalid credentials
===============================================================================
 [CUCUMBER SCENARIO START] Login failure with invalid credentials
===============================================================================
  Given the user navigates to the e-commerce storefront
  When the user enters username "locked_out_user" and password "secret_sauce"
  And clicks on the login button
  Then an error message containing "Sorry, this user has been locked out" should be displayed
 [CUCUMBER SCENARIO END] Status: PASSED

@Negative @Regression
Scenario Outline: Login failure with invalid credentials
===============================================================================
 [CUCUMBER SCENARIO START] Login failure with invalid credentials
===============================================================================
  Given the user navigates to the e-commerce storefront
  When the user enters username "invalid_user" and password "wrong_pass"
  And clicks on the login button
  Then an error message containing "Username and password do not match any user" should be displayed
 [CUCUMBER SCENARIO END] Status: PASSED

@Negative @Regression
Scenario Outline: Login failure with invalid credentials
===============================================================================
 [CUCUMBER SCENARIO START] Login failure with invalid credentials
===============================================================================
  Given the user navigates to the e-commerce storefront
  When the user enters username "" and password "secret_sauce"
  And clicks on the login button
  Then an error message containing "Username is required" should be displayed
 [CUCUMBER SCENARIO END] Status: PASSED

@Cart @Regression
Scenario: Adding product to cart and verifying cart contents
===============================================================================
 [CUCUMBER SCENARIO START] Adding product to cart and verifying cart contents
===============================================================================
  Given the user navigates to the e-commerce storefront
  Given the user is logged in with username "standard_user" and password "secret_sauce"
  When the user selects the product "Sauce Labs Backpack"
  And clicks the add to cart button
  Then the shopping cart badge count should be "1"
  When the user opens the shopping cart
  Then the cart should contain the item "Sauce Labs Backpack"
  When the user removes "Sauce Labs Backpack" from the cart
  Then the item should no longer be present in the cart
 [CUCUMBER SCENARIO END] Status: PASSED

Results:
Tests run: 5, Failures: 0, Errors: 0, Skipped: 0
```
