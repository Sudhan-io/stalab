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

