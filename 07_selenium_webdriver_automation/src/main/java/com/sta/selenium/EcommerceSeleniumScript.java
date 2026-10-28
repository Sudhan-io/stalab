package com.sta.selenium;

import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.time.Duration;
import java.util.List;

public class EcommerceSeleniumScript {

    public static WebDriver createDriver() {
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
        return new ChromeDriver(options);
    }

    public static String resolveTargetUrl() {
        // First try live SauceDemo
        try {
            HttpClient client = HttpClient.newBuilder().connectTimeout(Duration.ofSeconds(2)).build();
            HttpRequest req = HttpRequest.newBuilder().uri(URI.create("https://www.saucedemo.com")).GET().build();
            HttpResponse<Void> resp = client.send(req, HttpResponse.BodyHandlers.discarding());
            if (resp.statusCode() == 200) {
                return "https://www.saucedemo.com";
            }
        } catch (Exception ignored) {}

        // Fallback to local mock HTML
        File localFile = new File("src/main/resources/mock_store/index.html");
        if (localFile.exists()) {
            return localFile.toURI().toString();
        }
        return "https://www.saucedemo.com";
    }

    public static void takeScreenshot(WebDriver driver, String filename) {
        try {
            File src = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
            File dest = new File("target/screenshots/" + filename);
            dest.getParentFile().mkdirs();
            Files.copy(src.toPath(), dest.toPath(), StandardCopyOption.REPLACE_EXISTING);
            System.out.println("  [SCREENSHOT SAVED] -> " + dest.getAbsolutePath());
        } catch (IOException e) {
            System.err.println("Failed to save screenshot: " + e.getMessage());
        }
    }

    public static void main(String[] args) {
        System.out.println("===============================================================================");
        System.out.println(" STA LAB - EXPERIMENT 7: SELENIUM WEBDRIVER E-COMMERCE AUTOMATION SCRIPT");
        System.out.println("===============================================================================");

        WebDriver driver = null;
        try {
            driver = createDriver();
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
            String targetUrl = resolveTargetUrl();
            boolean isLiveSauceDemo = targetUrl.contains("saucedemo.com");

            System.out.println("Target E-Commerce Application URL: " + targetUrl);
            driver.get(targetUrl);
            takeScreenshot(driver, "01_login_page.png");

            // ==========================================
            // STEP 1: AUTOMATE LOGIN
            // ==========================================
            System.out.println("\n[STEP 1] Automating User Login...");
            WebElement userField = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("user-name")));
            WebElement passField = driver.findElement(By.id("password"));
            WebElement loginBtn = driver.findElement(By.id("login-button"));

            userField.clear();
            userField.sendKeys("standard_user");
            passField.clear();
            passField.sendKeys("secret_sauce");
            takeScreenshot(driver, "02_credentials_entered.png");
            loginBtn.click();

            // Verify landing on inventory
            wait.until(ExpectedConditions.urlContains("inventory"));
            System.out.println("  * Login Successful: Redirected to Product Catalog.");
            takeScreenshot(driver, "03_inventory_page.png");

            // ==========================================
            // STEP 2: AUTOMATE PRODUCT SEARCH & VERIFICATION
            // ==========================================
            System.out.println("\n[STEP 2] Verifying Product Catalog & Search Filtering...");
            List<WebElement> productItems = wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(By.className("inventory_item_name")));
            System.out.println("  * Total Products Displayed in Catalog: " + productItems.size());
            for (WebElement item : productItems) {
                System.out.println("    - Found: " + item.getText());
            }

            // ==========================================
            // STEP 3: AUTOMATE PRODUCT SELECTION
            // ==========================================
            System.out.println("\n[STEP 3] Selecting Product 'Sauce Labs Backpack' to View Details...");
            WebElement backpackLink = driver.findElement(By.xpath("//div[text()='Sauce Labs Backpack']"));
            backpackLink.click();

            // Verify Product Detail Page
            wait.until(ExpectedConditions.visibilityOfElementLocated(By.className("inventory_details_name")));
            String detailName = driver.findElement(By.className("inventory_details_name")).getText();
            String detailPrice = driver.findElement(By.className("inventory_details_price")).getText();
            System.out.println("  * Product Detail Loaded: " + detailName + " | Price: " + detailPrice);
            takeScreenshot(driver, "04_product_detail.png");

            // ==========================================
            // STEP 4: AUTOMATE CART MANAGEMENT
            // ==========================================
            System.out.println("\n[STEP 4] Automating Cart Operations...");
            WebElement addToCartBtn = driver.findElement(By.xpath("//button[contains(@id, 'add-to-cart') or text()='Add to cart']"));
            addToCartBtn.click();
            System.out.println("  * Clicked 'Add to cart'.");

            // Verify Cart Badge Counter
            WebElement cartBadge = wait.until(ExpectedConditions.visibilityOfElementLocated(By.className("shopping_cart_badge")));
            System.out.println("  * Verified Cart Badge Count: " + cartBadge.getText());

            // Navigate to Cart
            WebElement cartLink = driver.findElement(By.className("shopping_cart_link"));
            cartLink.click();
            wait.until(ExpectedConditions.urlContains("cart"));
            takeScreenshot(driver, "05_cart_page.png");

            // Verify item in cart
            WebElement cartItemName = driver.findElement(By.className("inventory_item_name"));
            System.out.println("  * Verified Item in Cart: " + cartItemName.getText());

            // Remove item from cart
            WebElement removeBtn = driver.findElement(By.xpath("//button[contains(@id, 'remove') or text()='Remove']"));
            removeBtn.click();
            System.out.println("  * Removed item from Cart.");
            takeScreenshot(driver, "06_item_removed.png");

            System.out.println("\n===============================================================================");
            System.out.println(" ALL SELENIUM E-COMMERCE AUTOMATION STEPS EXECUTED SUCCESSFULLY!");
            System.out.println("===============================================================================");

        } catch (Exception e) {
            System.err.println("Automation Execution Exception: " + e.getMessage());
            e.printStackTrace();
        } finally {
            if (driver != null) {
                driver.quit();
            }
        }
        System.exit(0);
    }
}
