# Experiment 11 (Model 2)

## Aim
To develop a BDD automation framework using Selenium WebDriver, TestNG, and Cucumber for an e-commerce application, implement feature files, step definitions, hooks, and scenario outlines, and evaluate the execution results.

## Algorithm / Procedure
1. Create a Gherkin feature file (`ecommerce_shopping.feature`) defining:
   - A `Scenario Outline` with `Examples` table testing parameterized authentication scenarios.
   - An end-to-end `Scenario` testing product additions and checkout completion.
2. Implement Cucumber `@Before` and `@After` hooks in `DriverHooks.java` to instantiate ChromeDriver (with support for headless and visible browser window) and ensure teardown after each scenario.
3. Implement Step Definitions in `ShoppingSteps.java` using `@Given`, `@When`, `@Then`, and `@And` annotations.
4. Interact with real DOM elements on `https://www.saucedemo.com` using `WebDriverWait` and `JavascriptExecutor` for reliable clicks.
5. Validate outcomes using TestNG assertions (`Assert.assertTrue`, `Assert.assertEquals`).
6. Configure `CucumberRunnerTest` extending `AbstractTestNGCucumberTests` to bridge Cucumber with the TestNG runner and produce HTML reports.
7. Execute tests via Maven and evaluate step execution logs and output HTML reports.

## Gherkin Feature File
```gherkin
Feature: E-Commerce Shopping Journey and Authentication

  Scenario Outline: User Authentication with Different Personas
    Given the customer is on the SauceDemo login page
    When user enters credentials "<username>" and "<password>"
    And user clicks the login button
    Then login outcome should be "<outcome>" with expected text "<expectedText>"

    Examples:
      | username        | password       | outcome | expectedText                         |
      | standard_user   | secret_sauce   | SUCCESS | /inventory.html                      |
      | locked_out_user | secret_sauce   | ERROR   | Sorry, this user has been locked out |
      | standard_user   | wrong_pwd_99   | ERROR   | Username and password do not match   |

  Scenario: End-to-End Product Selection and Checkout
    Given the user is logged in as "standard_user" with "secret_sauce"
    When user adds "sauce-labs-backpack" and "sauce-labs-bike-light" to the cart
    And user proceeds through checkout with "Diana" "Prince" "90210"
    Then order confirmation message should be "Thank you for your order!"
```

## Main Java Program (Step Definitions)
```java
package com.sta.bdd;

import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;

import java.time.Duration;

public class ShoppingSteps {

    private final WebDriver driver = DriverHooks.getDriver();
    private final WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));

    @Given("the customer is on the SauceDemo login page")
    public void navigateToLoginPage() {
        driver.get("https://www.saucedemo.com");
    }

    @When("user enters credentials {string} and {string}")
    public void enterCredentials(String username, String password) {
        WebElement userField = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("user-name")));
        userField.clear();
        userField.sendKeys(username);

        WebElement passField = driver.findElement(By.id("password"));
        passField.clear();
        passField.sendKeys(password);
    }

    @And("user clicks the login button")
    public void clickLogin() {
        driver.findElement(By.id("login-button")).click();
    }

    @Then("login outcome should be {string} with expected text {string}")
    public void verifyLoginOutcome(String outcome, String expectedText) {
        if ("SUCCESS".equalsIgnoreCase(outcome)) {
            wait.until(ExpectedConditions.urlContains(expectedText));
            Assert.assertTrue(driver.getCurrentUrl().contains(expectedText),
                    "Expected landing page containing " + expectedText);
        } else {
            WebElement errorEl = wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("[data-test='error']")));
            Assert.assertTrue(errorEl.getText().contains(expectedText),
                    "Expected error text to contain: " + expectedText + ", but got: " + errorEl.getText());
        }
    }

    @Given("the user is logged in as {string} with {string}")
    public void userIsLoggedIn(String user, String pass) {
        driver.get("https://www.saucedemo.com");
        enterCredentials(user, pass);
        clickLogin();
        wait.until(ExpectedConditions.urlContains("/inventory.html"));
    }

    @When("user adds {string} and {string} to the cart")
    public void addProductsToCart(String item1, String item2) {
        WebElement btn1 = wait.until(ExpectedConditions.presenceOfElementLocated(By.id("add-to-cart-" + item1)));
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView({block: 'center'}); arguments[0].click();", btn1);

        WebElement btn2 = wait.until(ExpectedConditions.presenceOfElementLocated(By.id("add-to-cart-" + item2)));
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView({block: 'center'}); arguments[0].click();", btn2);

        WebElement badge = wait.until(ExpectedConditions.visibilityOfElementLocated(By.className("shopping_cart_badge")));
        Assert.assertEquals(badge.getText().trim(), "2", "Cart badge should display 2 items");
    }

    @And("user proceeds through checkout with {string} {string} {string}")
    public void proceedThroughCheckout(String first, String last, String zip) {
        WebElement cartLink = driver.findElement(By.className("shopping_cart_link"));
        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", cartLink);

        wait.until(ExpectedConditions.urlContains("/cart.html"));
        WebElement checkoutBtn = wait.until(ExpectedConditions.presenceOfElementLocated(By.id("checkout")));
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView({block: 'center'}); arguments[0].click();", checkoutBtn);

        wait.until(ExpectedConditions.urlContains("/checkout-step-one.html"));
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("first-name"))).sendKeys(first);
        driver.findElement(By.id("last-name")).sendKeys(last);
        driver.findElement(By.id("postal-code")).sendKeys(zip);

        WebElement continueBtn = driver.findElement(By.id("continue"));
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView({block: 'center'}); arguments[0].click();", continueBtn);

        wait.until(ExpectedConditions.urlContains("/checkout-step-two.html"));
        WebElement finishBtn = wait.until(ExpectedConditions.presenceOfElementLocated(By.id("finish")));
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView({block: 'center'}); arguments[0].click();", finishBtn);
    }

    @Then("order confirmation message should be {string}")
    public void verifyOrderConfirmation(String expectedMessage) {
        wait.until(ExpectedConditions.urlContains("/checkout-complete.html"));
        WebElement header = wait.until(ExpectedConditions.visibilityOfElementLocated(By.className("complete-header")));
        Assert.assertEquals(header.getText().trim(), expectedMessage, "Complete header should match expected message");
    }
}
```

## Sample Output
```text
Scenario Outline: User Authentication with Different Personas                 # src/test/resources/features/ecommerce_shopping.feature:11
  Given the customer is on the SauceDemo login page                           # com.sta.bdd.ShoppingSteps.navigateToLoginPage()
  When user enters credentials "standard_user" and "secret_sauce"             # com.sta.bdd.ShoppingSteps.enterCredentials(java.lang.String,java.lang.String)
  And user clicks the login button                                            # com.sta.bdd.ShoppingSteps.clickLogin()
  Then login outcome should be "SUCCESS" with expected text "/inventory.html" # com.sta.bdd.ShoppingSteps.verifyLoginOutcome(java.lang.String,java.lang.String)

Scenario Outline: User Authentication with Different Personas                                    # src/test/resources/features/ecommerce_shopping.feature:12
  Given the customer is on the SauceDemo login page                                              # com.sta.bdd.ShoppingSteps.navigateToLoginPage()
  When user enters credentials "locked_out_user" and "secret_sauce"                              # com.sta.bdd.ShoppingSteps.enterCredentials(java.lang.String,java.lang.String)
  And user clicks the login button                                                               # com.sta.bdd.ShoppingSteps.clickLogin()
  Then login outcome should be "ERROR" with expected text "Sorry, this user has been locked out" # com.sta.bdd.ShoppingSteps.verifyLoginOutcome(java.lang.String,java.lang.String)

Scenario Outline: User Authentication with Different Personas                                  # src/test/resources/features/ecommerce_shopping.feature:13
  Given the customer is on the SauceDemo login page                                            # com.sta.bdd.ShoppingSteps.navigateToLoginPage()
  When user enters credentials "standard_user" and "wrong_pwd_99"                              # com.sta.bdd.ShoppingSteps.enterCredentials(java.lang.String,java.lang.String)
  And user clicks the login button                                                             # com.sta.bdd.ShoppingSteps.clickLogin()
  Then login outcome should be "ERROR" with expected text "Username and password do not match" # com.sta.bdd.ShoppingSteps.verifyLoginOutcome(java.lang.String,java.lang.String)

Scenario: End-to-End Product Selection and Checkout                            # src/test/resources/features/ecommerce_shopping.feature:15
  Given the user is logged in as "standard_user" with "secret_sauce"           # com.sta.bdd.ShoppingSteps.userIsLoggedIn(java.lang.String,java.lang.String)
  When user adds "sauce-labs-backpack" and "sauce-labs-bike-light" to the cart # com.sta.bdd.ShoppingSteps.addProductsToCart(java.lang.String,java.lang.String)
  And user proceeds through checkout with "Diana" "Prince" "90210"             # com.sta.bdd.ShoppingSteps.proceedThroughCheckout(java.lang.String,java.lang.String,java.lang.String)
  Then order confirmation message should be "Thank you for your order!"        # com.sta.bdd.ShoppingSteps.verifyOrderConfirmation(java.lang.String)

Tests run: 4, Failures: 0, Errors: 0, Skipped: 0
BUILD SUCCESS
```

