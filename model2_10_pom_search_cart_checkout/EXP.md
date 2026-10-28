# Experiment 10 (Model 2)

## Aim
To construct a Page Object Model framework using Selenium WebDriver and TestNG for an e-commerce application, automate the product search, cart, and checkout modules, and evaluate the framework's reusability and maintainability.

## Algorithm / Procedure
1. Create a `BasePage` abstract class defining shared `WebDriver` and `WebDriverWait` instances, along with robust interaction helpers (`click`, `type`, `getText`).
2. Construct dedicated Page Object classes:
   - `LoginPage`: Encapsulates username, password, and submit interactions.
   - `InventoryPage`: Encapsulates product filtering, category sorting, item addition to cart, and cart badge count retrieval.
   - `CartPage`: Encapsulates verification of cart line items and transition to checkout.
   - `CheckoutPage`: Encapsulates customer shipping details entry and order summary totals validation.
   - `CheckoutCompletePage`: Encapsulates order placement confirmation banner verification.
3. Design a TestNG test suite (`Model2POMTestSuite`) using `@BeforeClass`, `@AfterClass`, and `@Test(dependsOnMethods = ...)` annotations to drive user journey flows sequentially.
4. Execute tests against `https://www.saucedemo.com`.
5. Evaluate framework maintainability: Document how locator updates in POM classes decouple test logic from UI changes, minimizing maintenance overhead and eliminating code duplication.

## Main Java Program
```java
package com.sta.pom;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.testng.Assert;
import org.testng.annotations.*;

import java.util.List;

public class Model2POMTestSuite {

    private WebDriver driver;
    private LoginPage loginPage;
    private InventoryPage inventoryPage;
    private CartPage cartPage;
    private CheckoutPage checkoutPage;
    private CheckoutCompletePage completePage;

    @BeforeClass(alwaysRun = true)
    public void setup() {
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

        loginPage = new LoginPage(driver);
        inventoryPage = new InventoryPage(driver);
        cartPage = new CartPage(driver);
        checkoutPage = new CheckoutPage(driver);
        completePage = new CheckoutCompletePage(driver);
    }

    @AfterClass(alwaysRun = true)
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }

    @Test(priority = 1)
    public void testAuthenticationViaPOM() {
        loginPage.open("https://www.saucedemo.com");
        loginPage.login("standard_user", "secret_sauce");
        Assert.assertTrue(inventoryPage.isLoaded(), "Inventory page should load after login");
    }

    @Test(priority = 2, dependsOnMethods = {"testAuthenticationViaPOM"})
    public void testProductSearchAndFilteringViaPOM() {
        List<String> backpackResults = inventoryPage.searchOrFilterProducts("Backpack");
        Assert.assertFalse(backpackResults.isEmpty(), "Should find at least 1 product matching 'Backpack'");
        Assert.assertTrue(backpackResults.get(0).contains("Backpack"));
        System.out.println("  POM Filter Result: " + backpackResults);
    }

    @Test(priority = 3, dependsOnMethods = {"testProductSearchAndFilteringViaPOM"})
    public void testCartOperationsViaPOM() {
        inventoryPage.addItemToCart("sauce-labs-backpack");
        inventoryPage.addItemToCart("sauce-labs-bike-light");
        Assert.assertEquals(inventoryPage.getCartBadgeCount(), 2, "Cart badge count should equal 2");

        inventoryPage.navigateToCart();
        Assert.assertTrue(cartPage.isLoaded(), "Cart page should load");

        List<String> items = cartPage.getItemNames();
        Assert.assertEquals(items.size(), 2, "Cart should contain 2 items");
        System.out.println("  POM Cart Items: " + items);
    }

    @Test(priority = 4, dependsOnMethods = {"testCartOperationsViaPOM"})
    public void testCheckoutWorkflowViaPOM() {
        cartPage.clickCheckout();
        checkoutPage.enterCustomerInfo("Sarah", "Connor", "90210");

        String total = checkoutPage.getSummaryTotal();
        Assert.assertTrue(total.contains("$"), "Total price label should be present");
        System.out.println("  POM Order Summary Total: " + total);

        checkoutPage.clickFinish();
        Assert.assertTrue(completePage.isLoaded(), "Complete checkout page should load");

        String confirmation = completePage.getConfirmationMessage();
        Assert.assertTrue(confirmation.toLowerCase().contains("thank you"),
                "Confirmation banner should say 'Thank you for your order!'");
        System.out.println("  POM Final Confirmation: " + confirmation);
    }
}
```

## Sample Output
```text
  POM Filter Result: [Sauce Labs Backpack]
  POM Cart Items: [Sauce Labs Backpack, Sauce Labs Bike Light]
  POM Order Summary Total: Total: $43.18
  POM Final Confirmation: Thank you for your order!
Tests run: 4, Failures: 0, Errors: 0, Skipped: 0
BUILD SUCCESS
```

