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

    // 1. Grouping: "smoke"
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

    // 2. Dependencies: depends on testLogin, Grouping: "regression"
    @Test(priority = 2, dependsOnMethods = {"testLogin"}, groups = {"regression"})
    public void testProductCatalogSorting() {
        WebElement sortDropdown = wait.until(ExpectedConditions.elementToBeClickable(By.className("product_sort_container")));
        Select select = new Select(sortDropdown);
        select.selectByValue("lohi");

        WebElement firstItemPrice = wait.until(ExpectedConditions.visibilityOfElementLocated(By.className("inventory_item_price")));
        Assert.assertEquals(firstItemPrice.getText().trim(), "$7.99", "First item in Low-to-High sort should be $7.99");
    }

    // 3. Dependencies: depends on testProductCatalogSorting, Grouping: "smoke", "regression"
    @Test(priority = 3, dependsOnMethods = {"testProductCatalogSorting"}, groups = {"smoke", "regression"})
    public void testAddToCart() {
        WebElement addBtn = wait.until(ExpectedConditions.presenceOfElementLocated(By.id("add-to-cart-sauce-labs-onesie")));
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView({block: 'center'}); arguments[0].click();", addBtn);

        WebElement badge = wait.until(ExpectedConditions.visibilityOfElementLocated(By.className("shopping_cart_badge")));
        Assert.assertEquals(badge.getText().trim(), "1", "Cart badge should display 1 item");
    }

    // 4. Data Provider for Parameterized Checkout Testing
    @DataProvider(name = "checkoutCustomerProvider")
    public Object[][] provideCustomerData() {
        return new Object[][]{
                {"Alice", "Johnson", "94107"},
                {"Bob", "Smith", "10001"}
        };
    }

    // 5. Test with DataProvider, Dependencies, and Assertions
    @Test(priority = 4, dependsOnMethods = {"testAddToCart"}, dataProvider = "checkoutCustomerProvider", groups = {"regression"})
    public void testCheckoutWithDataProvider(String firstName, String lastName, String zipCode) {
        // Open Cart
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

        // Click cancel to return and allow next data provider iteration
        WebElement cancelBtn = driver.findElement(By.id("cancel"));
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView({block: 'center'}); arguments[0].click();", cancelBtn);
        wait.until(ExpectedConditions.urlContains("/inventory.html"));
    }
}
