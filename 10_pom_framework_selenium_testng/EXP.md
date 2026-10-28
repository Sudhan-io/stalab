# Experiment 10: Page Object Model (POM) Automation Framework

## Aim
To develop a robust, maintainable UI automation framework using Selenium WebDriver, TestNG, and the Page Object Model (POM) design pattern, automate User Login, Product Search, Product Selection, and Cart Operations against a real e-commerce store (`https://www.saucedemo.com`), and evaluate the framework's maintainability.

---

## Algorithm / Procedure

1. **Establish Base Page Layer (`BasePage`):**
   - Create an abstract `BasePage` encapsulating common WebDriver instances and explicit wait wrapper methods (`click`, `type`, `getText`, `isElementVisible`).
2. **Encapsulate Web Pages into Page Objects:**
   - **`LoginPage`:** Holds private `By` locators for `#user-name`, `#password`, `#login-button`, and error containers. Exposes `loginAs(user, pass)` returning the subsequent `ProductsPage`.
   - **`ProductsPage`:** Holds locators for catalog items, product cards, inventory names, and cart badge. Exposes methods to get product titles, select a product (returning `ProductDetailPage`), and add an item to the cart.
   - **`ProductDetailPage`:** Holds locators for product title, price, and add-to-cart buttons.
   - **`CartPage`:** Holds locators for cart items and checkout controls. Exposes methods to verify item presence and remove items.
3. **Implement Decoupled TestNG Test Suite (`EcommercePOMTest`):**
   - Inherit from `BaseTest` for setup and teardown lifecycles.
   - Write tests that interact strictly with high-level page methods, hiding DOM locators and WebDriver mechanics completely from the test logic.
4. **Evaluate Framework Maintainability:**
   - If an element locator changes in the UI, update the locator in exactly **one** place (inside its Page Object) with zero modifications required across test classes.

---

## Main Java Program

### 1. Page Object: `src/main/java/com/sta/pom/pages/LoginPage.java`
```java
package com.sta.pom.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class LoginPage extends BasePage {

    // Encapsulated UI Locators
    private final By usernameInput = By.id("user-name");
    private final By passwordInput = By.id("password");
    private final By loginButton = By.id("login-button");
    private final By errorMessageContainer = By.cssSelector("h3[data-test='error']");

    public LoginPage(WebDriver driver) {
        super(driver);
    }

    public ProductsPage loginAs(String username, String password) {
        type(usernameInput, username);
        type(passwordInput, password);
        click(loginButton);
        return new ProductsPage(driver);
    }

    public String getErrorMessage() {
        return getText(errorMessageContainer);
    }
}
```

### 2. Test Suite: `src/test/java/com/sta/pom/tests/EcommercePOMTest.java`
```java
package com.sta.pom.tests;

import com.sta.pom.pages.*;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.List;

public class EcommercePOMTest extends BaseTest {

    @Test(priority = 1, description = "POM Test: Automate valid user authentication")
    public void testValidLogin() {
        System.out.println(">>> [POM TEST 1] Executing Valid Login Test...");
        LoginPage loginPage = new LoginPage(driver);
        ProductsPage productsPage = loginPage.loginAs("standard_user", "secret_sauce");

        Assert.assertEquals(productsPage.getPageTitle(), "Products", "Page title should be 'Products'");
        System.out.println("  * [PASS] Successfully logged in using LoginPage object.");
    }

    @Test(priority = 2, description = "POM Test: Automate product search & catalog verification")
    public void testProductCatalogAndSearch() {
        System.out.println(">>> [POM TEST 2] Executing Product Catalog Verification...");
        LoginPage loginPage = new LoginPage(driver);
        ProductsPage productsPage = loginPage.loginAs("standard_user", "secret_sauce");

        List<String> titles = productsPage.getAllProductTitles();
        System.out.println("  * Found " + titles.size() + " products in catalog.");
        Assert.assertTrue(titles.size() >= 4, "Catalog should have at least 4 products");
        Assert.assertTrue(titles.contains("Sauce Labs Backpack"), "Catalog must contain 'Sauce Labs Backpack'");
    }

    @Test(priority = 3, description = "POM Test: Automate product selection and details verification")
    public void testProductSelectionDetails() {
        System.out.println(">>> [POM TEST 3] Executing Product Selection Test...");
        LoginPage loginPage = new LoginPage(driver);
        ProductsPage productsPage = loginPage.loginAs("standard_user", "secret_sauce");

        ProductDetailPage detailPage = productsPage.selectProduct("Sauce Labs Backpack");
        Assert.assertEquals(detailPage.getProductTitle(), "Sauce Labs Backpack");
        Assert.assertEquals(detailPage.getProductPrice(), "$29.99");
        System.out.println("  * [PASS] Verified details via ProductDetailPage object: " + detailPage.getProductTitle() + " ($29.99)");
    }

    @Test(priority = 4, description = "POM Test: Automate cart management operations")
    public void testCartOperations() {
        System.out.println(">>> [POM TEST 4] Executing Cart Operations Test...");
        LoginPage loginPage = new LoginPage(driver);
        ProductsPage productsPage = loginPage.loginAs("standard_user", "secret_sauce");

        // Add to cart from products page
        productsPage.addProductToCart("Sauce Labs Backpack");
        Assert.assertEquals(productsPage.getCartBadgeCount(), 1, "Badge count should be 1");
        System.out.println("  * [PASS] Cart badge incremented to 1.");

        // Open cart and verify item presence
        CartPage cartPage = productsPage.openCart();
        Assert.assertTrue(cartPage.isItemInCart("Sauce Labs Backpack"), "Backpack must be present in cart");
        System.out.println("  * [PASS] Verified 'Sauce Labs Backpack' in CartPage.");

        // Remove item from cart
        cartPage.removeItem("Sauce Labs Backpack");
        Assert.assertFalse(cartPage.isItemInCart("Sauce Labs Backpack"), "Backpack should be removed");
        System.out.println("  * [PASS] Removed item from CartPage successfully.");
    }

    @Test(priority = 5, description = "POM Test: Verify invalid credentials display error message")
    public void testInvalidLoginErrorMessage() {
        System.out.println(">>> [POM TEST 5] Executing Negative Login Validation...");
        LoginPage loginPage = new LoginPage(driver);
        loginPage.loginAs("invalid_user", "wrong_pass");

        String error = loginPage.getErrorMessage();
        System.out.println("  * [PASS] Captured expected error: " + error);
        Assert.assertTrue(error.contains("Username and password do not match"), "Expected mismatch error message");
    }
}
```

---

## Sample Output (Live Website: SauceDemo.com)

```text
>>> [POM TEST 1] Executing Valid Login Test...
  * [PASS] Successfully logged in using LoginPage object.

>>> [POM TEST 2] Executing Product Catalog Verification...
  * Found 6 products in catalog.

>>> [POM TEST 3] Executing Product Selection Test...
  * [PASS] Verified details via ProductDetailPage object: Sauce Labs Backpack ($29.99)

>>> [POM TEST 4] Executing Cart Operations Test...
  * [PASS] Cart badge incremented to 1.
  * [PASS] Verified 'Sauce Labs Backpack' in CartPage.
  * [PASS] Removed item from CartPage successfully.

>>> [POM TEST 5] Executing Negative Login Validation...
  * [PASS] Captured expected error: Epic sadface: Username and password do not match any user in this service

Results:
Tests run: 5, Failures: 0, Errors: 0, Skipped: 0
```
