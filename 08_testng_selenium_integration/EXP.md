# Experiment 8: TestNG & Selenium WebDriver Integration
> **Lab Exam Coverage:** Applicable for **Model 1 (Question 8)** and **Model 2 (Question 8)**.

## Aim
To integrate TestNG with Selenium WebDriver automation scripts to automate an actual e-commerce application (`https://www.saucedemo.com`), implement test annotations (`@BeforeSuite`, `@BeforeClass`, `@Test`, `@AfterClass`, `@AfterSuite`), hard and soft assertions, test method grouping (`smoke`, `regression`, `cart`), prioritization, and custom execution listener reporting.

---

## Algorithm / Procedure

1. **Configure TestNG Suite & Annotations:**
   - Define lifecycle hierarchy:
     - `@BeforeSuite` / `@AfterSuite`: Global framework and report environment setup.
     - `@BeforeClass` / `@AfterClass`: WebDriver instance instantiation and browser cleanup.
     - `@Test`: Test execution methods with attributes: `priority`, `groups`, and `dependsOnMethods`.
2. **Implement Hard and Soft Assertions:**
   - Use **Hard Assertions** (`Assert.assertEquals`) for critical validations where failure immediately aborts the test (e.g., verifying user login or item price).
   - Use **Soft Assertions** (`SoftAssert`) for multi-point UI verification without halting on the first failure (e.g., checking multiple product titles and sorting order), followed by `assertAll()`.
3. **Organize Tests into Prioritized Groups:**
   - Group tests into categories: `smoke`, `regression`, and `cart`.
   - Set linear dependency and prioritization (`priority = 1..5`):
     1. `testLogin` (`priority = 1`, groups: `smoke`, `regression`)
     2. `testProductCatalogDisplay` (`priority = 2`, group: `regression`)
     3. `testProductSelection` (`priority = 3`, group: `regression`)
     4. `testAddToCart` (`priority = 4`, groups: `smoke`, `regression`, `cart`)
     5. `testCartVerificationAndRemoval` (`priority = 5`, groups: `regression`, `cart`)
4. **Implement Custom Execution Listener:**
   - Implement `ITestListener` to intercept test lifecycle events (`onTestStart`, `onTestSuccess`, `onTestFailure`, `onFinish`) and print formatted execution logs.
5. **Execute via TestNG XML Suite:**
   - Run `testng.xml` using Maven Surefire plugin and verify that reports are generated in `target/surefire-reports`.

---

## Main Java Program

### File: `src/test/java/com/sta/testng/EcommerceTestNGSuite.java`
```java
package com.sta.testng;

import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.*;
import org.testng.asserts.SoftAssert;

import java.time.Duration;
import java.util.List;

public class EcommerceTestNGSuite {

    private WebDriver driver;
    private WebDriverWait wait;
    private static final String APP_URL = "https://www.saucedemo.com";

    // 1. Suite Lifecycle Annotations
    @BeforeSuite(alwaysRun = true)
    public void beforeSuite() {
        System.out.println("[@BeforeSuite] Global Test Automation Framework Initialized.");
        System.out.println("[@BeforeSuite] Target URL: " + APP_URL);
    }

    @BeforeTest(alwaysRun = true)
    public void beforeTest() {
        System.out.println("[@BeforeTest] Preparing Test Configuration.");
    }

    @BeforeClass(alwaysRun = true)
    public void beforeClass() {
        System.out.println("[@BeforeClass] Launching Chrome Browser Instance.");
        ChromeOptions options = new ChromeOptions();
        boolean headless = Boolean.parseBoolean(System.getProperty("headless", "true"));
        if (headless) {
            options.addArguments("--headless=new");
        }
        options.addArguments("--no-sandbox");
        options.addArguments("--disable-dev-shm-usage");
        options.addArguments("--disable-gpu");
        options.addArguments("--window-size=1920,1080");
        options.addArguments("--remote-allow-origins=*");

        driver = new ChromeDriver(options);
        wait = new WebDriverWait(driver, Duration.ofSeconds(10));
    }

    @BeforeMethod(alwaysRun = true)
    public void beforeMethod() {
        // Method pre-condition hook
    }

    // 2. Test Execution Methods with Prioritization and Grouping

    @Test(priority = 1, groups = {"smoke", "regression"}, description = "Automate valid user authentication")
    public void testLogin() {
        driver.get(APP_URL);

        WebElement userField = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("user-name")));
        WebElement passField = driver.findElement(By.id("password"));
        WebElement loginBtn = driver.findElement(By.id("login-button"));

        userField.clear();
        userField.sendKeys("standard_user");
        passField.clear();
        passField.sendKeys("secret_sauce");
        loginBtn.click();

        wait.until(ExpectedConditions.urlContains("inventory"));

        // Hard Assertion: Verify landing on product inventory page
        Assert.assertTrue(driver.getCurrentUrl().contains("inventory"), "User is not redirected to inventory page!");
    }

    @Test(priority = 2, groups = {"regression"}, dependsOnMethods = {"testLogin"}, description = "Verify product catalog display and soft assertions on items")
    public void testProductCatalogDisplay() {
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.className("inventory_item_name")));

        List<WebElement> productNames = driver.findElements(By.className("inventory_item_name"));
        List<WebElement> productPrices = driver.findElements(By.className("inventory_item_price"));

        // Soft Assertions
        SoftAssert softAssert = new SoftAssert();
        softAssert.assertTrue(productNames.size() >= 4, "Expected at least 4 catalog items, found: " + productNames.size());
        softAssert.assertTrue(productPrices.size() >= 4, "Expected at least 4 price labels, found: " + productPrices.size());

        boolean hasBackpack = productNames.stream().anyMatch(e -> e.getText().contains("Sauce Labs Backpack"));
        softAssert.assertTrue(hasBackpack, "Sauce Labs Backpack was not found in catalog list!");

        softAssert.assertAll();
    }

    @Test(priority = 3, groups = {"regression"}, dependsOnMethods = {"testProductCatalogDisplay"}, description = "Select product to navigate to Product Detail Page")
    public void testProductSelection() {
        WebElement backpackLink = driver.findElement(By.xpath("//div[text()='Sauce Labs Backpack']"));
        backpackLink.click();

        wait.until(ExpectedConditions.urlContains("inventory-item"));

        WebElement detailName = wait.until(ExpectedConditions.visibilityOfElementLocated(By.className("inventory_details_name")));
        WebElement detailPrice = driver.findElement(By.className("inventory_details_price"));

        // Hard Assertions
        Assert.assertEquals(detailName.getText(), "Sauce Labs Backpack", "Product detail title mismatch");
        Assert.assertEquals(detailPrice.getText(), "$29.99", "Product price mismatch");
    }

    @Test(priority = 4, groups = {"cart", "smoke", "regression"}, dependsOnMethods = {"testProductSelection"}, description = "Add item to cart and verify cart badge counter increment")
    public void testAddToCart() {
        WebElement addBtn = wait.until(ExpectedConditions.presenceOfElementLocated(
            By.xpath("//button[contains(@id, 'add-to-cart') or text()='Add to cart' or contains(text(), 'Add to cart')]")));
        try {
            addBtn.click();
        } catch (Exception e) {
            ((JavascriptExecutor) driver).executeScript("arguments[0].click();", addBtn);
        }

        WebElement cartBadge = wait.until(ExpectedConditions.visibilityOfElementLocated(By.className("shopping_cart_badge")));

        // Hard Assertion: Cart badge should reflect 1 item
        Assert.assertEquals(cartBadge.getText(), "1", "Cart badge should increment to 1");
    }

    @Test(priority = 5, groups = {"cart", "regression"}, dependsOnMethods = {"testAddToCart"}, description = "Open cart, verify item presence, and perform removal")
    public void testCartVerificationAndRemoval() {
        WebElement cartLink = wait.until(ExpectedConditions.presenceOfElementLocated(By.className("shopping_cart_link")));
        try {
            cartLink.click();
        } catch (Exception e) {
            ((JavascriptExecutor) driver).executeScript("arguments[0].click();", cartLink);
        }

        wait.until(ExpectedConditions.urlContains("cart"));

        WebElement cartItem = wait.until(ExpectedConditions.visibilityOfElementLocated(By.className("inventory_item_name")));
        Assert.assertEquals(cartItem.getText(), "Sauce Labs Backpack", "Cart item title mismatch");

        // Remove item
        WebElement removeBtn = wait.until(ExpectedConditions.presenceOfElementLocated(
            By.xpath("//button[contains(@id, 'remove') or text()='Remove' or contains(text(), 'Remove')]")));
        try {
            removeBtn.click();
        } catch (Exception e) {
            ((JavascriptExecutor) driver).executeScript("arguments[0].click();", removeBtn);
        }

        // Verify badge is removed when cart is empty
        List<WebElement> badges = driver.findElements(By.className("shopping_cart_badge"));
        Assert.assertTrue(badges.isEmpty(), "Cart badge should disappear when cart is empty");
    }

    // 3. Teardown Lifecycle Annotations

    @AfterMethod(alwaysRun = true)
    public void afterMethod() {
        // Method post-condition hook
    }

    @AfterClass(alwaysRun = true)
    public void afterClass() {
        System.out.println("[@AfterClass] Closing Browser WebDriver Instance.");
        if (driver != null) {
            driver.quit();
        }
    }

    @AfterTest(alwaysRun = true)
    public void afterTest() {
        System.out.println("[@AfterTest] Test Run Finished.");
    }

    @AfterSuite(alwaysRun = true)
    public void afterSuite() {
        System.out.println("[@AfterSuite] TestNG Execution Suite Complete. Reports generated in target/surefire-reports.");
    }
}
```

---

## Sample Output (Live Website: SauceDemo.com)

```text
===============================================================================
 [TESTNG LISTENER] STARTING TEST SUITE: E-Commerce End-to-End Regression Tests
===============================================================================
[@BeforeTest] Preparing Test Configuration.
[@BeforeClass] Launching Chrome Browser Instance.
  --> [STARTED] EcommerceTestNGSuite#testLogin() | Priority: 1 | Groups: [regression, smoke]
  <-- [PASSED ] EcommerceTestNGSuite#testLogin() in 1585 ms
  --> [STARTED] EcommerceTestNGSuite#testProductCatalogDisplay() | Priority: 2 | Groups: [regression]
  <-- [PASSED ] EcommerceTestNGSuite#testProductCatalogDisplay() in 164 ms
  --> [STARTED] EcommerceTestNGSuite#testProductSelection() | Priority: 3 | Groups: [regression]
  <-- [PASSED ] EcommerceTestNGSuite#testProductSelection() in 176 ms
  --> [STARTED] EcommerceTestNGSuite#testAddToCart() | Priority: 4 | Groups: [regression, smoke, cart]
  <-- [PASSED ] EcommerceTestNGSuite#testAddToCart() in 123 ms
  --> [STARTED] EcommerceTestNGSuite#testCartVerificationAndRemoval() | Priority: 5 | Groups: [regression, cart]
  <-- [PASSED ] EcommerceTestNGSuite#testCartVerificationAndRemoval() in 156 ms
[@AfterClass] Closing Browser WebDriver Instance.
[@AfterTest] Test Run Finished.
===============================================================================
 [TESTNG LISTENER] SUITE FINISHED: Passed: 5 | Failed: 0 | Skipped: 0
===============================================================================
[@AfterSuite] TestNG Execution Suite Complete. Reports generated in target/surefire-reports.

Results:
Tests run: 5, Failures: 0, Errors: 0, Skipped: 0
```

