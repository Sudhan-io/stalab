package com.sta.selenium;

import org.junit.jupiter.api.*;
import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

import static org.junit.jupiter.api.Assertions.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class EcommerceSeleniumTest {

    private static WebDriver driver;
    private static WebDriverWait wait;
    private static String targetUrl;

    @BeforeAll
    public static void setUp() {
        driver = EcommerceSeleniumScript.createDriver();
        wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        targetUrl = EcommerceSeleniumScript.resolveTargetUrl();
        driver.get(targetUrl);
    }

    @AfterAll
    public static void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }

    @Test
    @Order(1)
    @DisplayName("Verify valid user login to e-commerce store")
    public void testLogin() {
        WebElement userField = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("user-name")));
        WebElement passField = driver.findElement(By.id("password"));
        WebElement loginBtn = driver.findElement(By.id("login-button"));

        userField.clear();
        userField.sendKeys("standard_user");
        passField.clear();
        passField.sendKeys("secret_sauce");
        loginBtn.click();

        wait.until(ExpectedConditions.urlContains("inventory"));
        assertTrue(driver.getCurrentUrl().contains("inventory"), "URL should contain 'inventory'");
    }

    @Test
    @Order(2)
    @DisplayName("Verify product catalog is loaded with items")
    public void testProductCatalog() {
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.className("inventory_item_name")));
        var items = driver.findElements(By.className("inventory_item_name"));
        assertFalse(items.isEmpty(), "Catalog should list products");
    }

    @Test
    @Order(3)
    @DisplayName("Verify product selection and Product Detail Page navigation")
    public void testProductSelection() {
        WebElement backpack = driver.findElement(By.xpath("//div[text()='Sauce Labs Backpack']"));
        backpack.click();

        WebElement title = wait.until(ExpectedConditions.visibilityOfElementLocated(By.className("inventory_details_name")));
        assertEquals("Sauce Labs Backpack", title.getText());
    }

    @Test
    @Order(4)
    @DisplayName("Verify cart addition and badge counter increment")
    public void testCartOperations() {
        WebElement addBtn = driver.findElement(By.xpath("//button[contains(@id, 'add-to-cart') or text()='Add to cart']"));
        addBtn.click();

        WebElement badge = wait.until(ExpectedConditions.visibilityOfElementLocated(By.className("shopping_cart_badge")));
        assertEquals("1", badge.getText());

        WebElement cartLink = driver.findElement(By.className("shopping_cart_link"));
        cartLink.click();

        WebElement itemInCart = wait.until(ExpectedConditions.visibilityOfElementLocated(By.className("inventory_item_name")));
        assertEquals("Sauce Labs Backpack", itemInCart.getText());

        WebElement removeBtn = driver.findElement(By.xpath("//button[contains(@id, 'remove') or text()='Remove']"));
        removeBtn.click();
    }
}

