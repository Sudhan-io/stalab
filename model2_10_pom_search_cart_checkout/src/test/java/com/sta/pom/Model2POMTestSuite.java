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

        // Instantiate Page Objects
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
