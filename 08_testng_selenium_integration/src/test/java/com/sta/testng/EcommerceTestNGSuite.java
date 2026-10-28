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
        System.out.println("[@BeforeClass] Launching Headless Chrome Browser Instance.");
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--headless=new");
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

        // Hard Assertion: Verify landing page URL
        Assert.assertTrue(driver.getCurrentUrl().contains("inventory"), "URL must contain 'inventory'");

        // Hard Assertion: Verify inventory container is visible
        WebElement inventoryContainer = driver.findElement(By.id("inventory_container"));
        Assert.assertNotNull(inventoryContainer, "Inventory container must be rendered");
    }

    @Test(priority = 2, groups = {"regression"}, dependsOnMethods = {"testLogin"}, description = "Verify product catalog display and pricing attributes using SoftAssert")
    public void testProductCatalogDisplay() {
        List<WebElement> items = wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(By.className("inventory_item")));

        // TestNG Soft Assertion: Validate multiple conditions without immediate stoppage
        SoftAssert softAssert = new SoftAssert();
        softAssert.assertTrue(items.size() >= 4, "Catalog should have at least 4 items");

        WebElement firstTitle = items.get(0).findElement(By.className("inventory_item_name"));
        softAssert.assertFalse(firstTitle.getText().isEmpty(), "Item title should not be blank");

        WebElement firstPrice = items.get(0).findElement(By.className("inventory_item_price"));
        softAssert.assertTrue(firstPrice.getText().startsWith("$"), "Price should contain dollar currency symbol");

        softAssert.assertAll(); // Evaluates all soft assertions
    }

    @Test(priority = 3, groups = {"regression"}, dependsOnMethods = {"testLogin"}, description = "Verify single product selection and details")
    public void testProductSelection() {
        WebElement backpackTitle = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//div[text()='Sauce Labs Backpack']")));
        backpackTitle.click();

        WebElement detailName = wait.until(ExpectedConditions.visibilityOfElementLocated(By.className("inventory_details_name")));
        WebElement detailPrice = driver.findElement(By.className("inventory_details_price"));

        // Hard Assertions
        Assert.assertEquals(detailName.getText(), "Sauce Labs Backpack", "Product detail title mismatch");
        Assert.assertEquals(detailPrice.getText(), "$29.99", "Product price mismatch");
    }

    @Test(priority = 4, groups = {"cart", "smoke", "regression"}, dependsOnMethods = {"testProductSelection"}, description = "Add item to cart and verify cart badge counter increment")
    public void testAddToCart() {
        WebElement addBtn = wait.until(ExpectedConditions.elementToBeClickable(
            By.xpath("//button[contains(@id, 'add-to-cart') or text()='Add to cart']")));
        addBtn.click();

        WebElement cartBadge = wait.until(ExpectedConditions.visibilityOfElementLocated(By.className("shopping_cart_badge")));

        // Hard Assertion: Cart badge should reflect 1 item
        Assert.assertEquals(cartBadge.getText(), "1", "Cart badge should increment to 1");
    }

    @Test(priority = 5, groups = {"cart", "regression"}, dependsOnMethods = {"testAddToCart"}, description = "Open cart, verify item presence, and perform removal")
    public void testCartVerificationAndRemoval() {
        WebElement cartLink = driver.findElement(By.className("shopping_cart_link"));
        cartLink.click();

        wait.until(ExpectedConditions.urlContains("cart"));

        WebElement cartItem = wait.until(ExpectedConditions.visibilityOfElementLocated(By.className("inventory_item_name")));
        Assert.assertEquals(cartItem.getText(), "Sauce Labs Backpack", "Cart item title mismatch");

        // Remove item
        WebElement removeBtn = driver.findElement(By.xpath("//button[contains(@id, 'remove') or text()='Remove']"));
        removeBtn.click();

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

