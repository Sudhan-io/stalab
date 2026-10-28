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
