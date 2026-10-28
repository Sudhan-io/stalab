# Experiment 08 (Model 2)

## Aim
To integrate Selenium WebDriver scripts with TestNG to implement data providers, assertions, dependencies, grouping, parallel execution, and automated test reports.

## Algorithm / Procedure
1. Create a TestNG test suite configured with `@BeforeClass` to initialize ChromeDriver with options for visible or headless execution.
2. Implement **Grouping** by tagging test methods with `groups = {"smoke"}` and `groups = {"regression"}`.
3. Define the primary login test (`testLogin`) and assert successful redirect to `/inventory.html`.
4. Establish **Method Dependencies** on downstream tests using `dependsOnMethods = {"testLogin"}` so that catalog and checkout tests only run if authentication succeeds.
5. Implement a `@DataProvider` (`checkoutCustomerProvider`) returning multiple arrays of test input parameters (First Name, Last Name, Zip Code).
6. Execute parameterized checkout tests using the data provider and validate cart totals using TestNG `Assert` statements.
7. Implement a custom TestNG `ITestListener` (`CustomReportListener`) to capture lifecycle events (`onTestStart`, `onTestSuccess`, `onTestFailure`, `onFinish`) and produce formatted automated execution reports.
8. Configure `testng.xml` to attach listeners, declare test classes, and manage parallel execution settings.

## Main Java Program
```java
package com.sta.testng;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.*;

import java.time.Duration;

public class TestNGParallelSuite {

    private WebDriver driver;
    private WebDriverWait wait;

    @BeforeClass(alwaysRun = true)
    public void setupBrowser() {
        ChromeOptions options = new ChromeOptions();
        boolean headless = Boolean.parseBoolean(System.getProperty("headless", "true"));
        if (headless) {
            options.addArguments("--headless=new");
        }
        options.addArguments("--window-size=1920,1080");
        options.addArguments("--disable-gpu");
        options.addArguments("--no-sandbox");
        options.addArguments("--disable-dev-shm-usage");
        options.addArguments("--remote-allow-origins=*");

        driver = new ChromeDriver(options);
        wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        driver.get("https://www.saucedemo.com");
    }

    @AfterClass(alwaysRun = true)
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }

    @Test(priority = 1, groups = {"smoke"})
    public void testLogin() {
        WebElement userField = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("user-name")));
        userField.clear();
        userField.sendKeys("standard_user");

        WebElement passField = driver.findElement(By.id("password"));
        passField.clear();
        passField.sendKeys("secret_sauce");

        driver.findElement(By.id("login-button")).click();

        wait.until(ExpectedConditions.urlContains("/inventory.html"));
        Assert.assertTrue(driver.getCurrentUrl().contains("/inventory.html"), "User should be redirected to inventory page after successful login");
    }

    @Test(priority = 2, dependsOnMethods = {"testLogin"}, groups = {"regression"})
    public void testProductCatalogSorting() {
        WebElement sortDropdown = wait.until(ExpectedConditions.elementToBeClickable(By.className("product_sort_container")));
        Select select = new Select(sortDropdown);
        select.selectByValue("lohi");

        WebElement firstItemPrice = wait.until(ExpectedConditions.visibilityOfElementLocated(By.className("inventory_item_price")));
        Assert.assertEquals(firstItemPrice.getText().trim(), "$7.99", "First item in Low-to-High sort should be $7.99");
    }

    @Test(priority = 3, dependsOnMethods = {"testProductCatalogSorting"}, groups = {"smoke", "regression"})
    public void testAddToCart() {
        WebElement addBtn = wait.until(ExpectedConditions.presenceOfElementLocated(By.id("add-to-cart-sauce-labs-onesie")));
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView({block: 'center'}); arguments[0].click();", addBtn);

        WebElement badge = wait.until(ExpectedConditions.visibilityOfElementLocated(By.className("shopping_cart_badge")));
        Assert.assertEquals(badge.getText().trim(), "1", "Cart badge should display 1 item");
    }

    @DataProvider(name = "checkoutCustomerProvider")
    public Object[][] provideCustomerData() {
        return new Object[][]{
                {"Alice", "Johnson", "94107"},
                {"Bob", "Smith", "10001"}
        };
    }

    @Test(priority = 4, dependsOnMethods = {"testAddToCart"}, dataProvider = "checkoutCustomerProvider", groups = {"regression"})
    public void testCheckoutWithDataProvider(String firstName, String lastName, String zipCode) {
        WebElement cartLink = driver.findElement(By.className("shopping_cart_link"));
        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", cartLink);

        wait.until(ExpectedConditions.urlContains("/cart.html"));
        WebElement checkoutBtn = wait.until(ExpectedConditions.presenceOfElementLocated(By.id("checkout")));
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView({block: 'center'}); arguments[0].click();", checkoutBtn);

        wait.until(ExpectedConditions.urlContains("/checkout-step-one.html"));
        WebElement firstEl = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("first-name")));
        firstEl.clear();
        firstEl.sendKeys(firstName);

        WebElement lastEl = driver.findElement(By.id("last-name"));
        lastEl.clear();
        lastEl.sendKeys(lastName);

        WebElement zipEl = driver.findElement(By.id("postal-code"));
        zipEl.clear();
        zipEl.sendKeys(zipCode);

        WebElement continueBtn = driver.findElement(By.id("continue"));
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView({block: 'center'}); arguments[0].click();", continueBtn);

        wait.until(ExpectedConditions.urlContains("/checkout-step-two.html"));
        WebElement summaryEl = wait.until(ExpectedConditions.visibilityOfElementLocated(By.className("summary_total_label")));
        Assert.assertTrue(summaryEl.getText().contains("$"), "Summary total must display dollar amount");

        WebElement cancelBtn = driver.findElement(By.id("cancel"));
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView({block: 'center'}); arguments[0].click();", cancelBtn);
        wait.until(ExpectedConditions.urlContains("/inventory.html"));
    }
}
```

## Sample Output
```text
==========================================================================================
                     TESTNG AUTOMATED TEST EXECUTION REPORT START                         
Suite Name: EcommerceParallelDataProvidersSuite
Test Run  : SauceDemoAutomationTests
==========================================================================================
  [RUNNING] testLogin                           (Groups: smoke)
  [PASS]    testLogin                           (Time: 356 ms)
  [RUNNING] testProductCatalogSorting           (Groups: regression)
  [PASS]    testProductCatalogSorting           (Time: 158 ms)
  [RUNNING] testAddToCart                       (Groups: regression, smoke)
  [PASS]    testAddToCart                       (Time: 48 ms)
  [RUNNING] testCheckoutWithDataProvider        (Groups: regression)
  [PASS]    testCheckoutWithDataProvider        (Time: 376 ms)
  [RUNNING] testCheckoutWithDataProvider        (Groups: regression)
  [PASS]    testCheckoutWithDataProvider        (Time: 340 ms)
------------------------------------------------------------------------------------------
                         TESTNG AUTOMATION SUMMARY METRICS                                
Total Tests Planned  : 4
Tests Passed         : 5
Tests Failed         : 0
Tests Skipped        : 0
==========================================================================================
```
