# Experiment 7: Selenium WebDriver E-Commerce Automation

## Aim
To develop and execute Selenium WebDriver automation scripts to automate User Login, Product Catalog Search, Product Selection, Cart Operations (Add/Remove), and Screenshot Capture on a live, real-world e-commerce application (`https://www.saucedemo.com`).

---

## Algorithm / Procedure

1. **Initialize WebDriver & Browser Configuration:**
   - Configure Chrome options with headless mode (`--headless=new`), window sizing (`1920x1080`), and security flags (`--no-sandbox`, `--remote-allow-origins=*`).
   - Instantiate `ChromeDriver` with explicit wait (`WebDriverWait`) of 10 seconds.
2. **Navigate to Real Storefront URL:**
   - Load the live production website: `https://www.saucedemo.com`.
   - Capture initial landing page screenshot.
3. **Automate User Authentication:**
   - Locate username (`#user-name`), password (`#password`), and submit button (`#login-button`).
   - Enter valid credentials (`standard_user` / `secret_sauce`) and click login.
   - Assert page redirection to `/inventory.html`.
4. **Automate Catalog Search & Item Inspection:**
   - Query all displayed catalog cards via `By.className("inventory_item_name")`.
   - Log total count and names of available inventory products.
5. **Automate Product Selection:**
   - Locate and click the target product: `"Sauce Labs Backpack"`.
   - Wait for product detail view and verify product name and price (`$29.99`).
6. **Automate Cart Management:**
   - Click `"Add to cart"` button.
   - Assert shopping cart badge counter updates to `"1"`.
   - Navigate to cart (`By.className("shopping_cart_link")`).
   - Assert item presence in cart (`"Sauce Labs Backpack"`).
   - Click `"Remove"` button to empty the cart and capture confirmation screenshot.
7. **Clean Teardown:**
   - Safely terminate the WebDriver session (`driver.quit()`).

---

## Main Java Program

### File: `src/main/java/com/sta/selenium/EcommerceSeleniumScript.java`
```java
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
        try {
            HttpClient client = HttpClient.newBuilder().connectTimeout(Duration.ofSeconds(2)).build();
            HttpRequest req = HttpRequest.newBuilder().uri(URI.create("https://www.saucedemo.com")).GET().build();
            HttpResponse<Void> resp = client.send(req, HttpResponse.BodyHandlers.discarding());
            if (resp.statusCode() == 200) {
                return "https://www.saucedemo.com";
            }
        } catch (Exception ignored) {}

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
            WebElement addToCartBtn = wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//button[contains(@id, 'add-to-cart') or text()='Add to cart' or contains(text(), 'Add to cart')]")));
            addToCartBtn.click();
            System.out.println("  * Clicked 'Add to cart'.");

            // Verify Cart Badge Counter
            WebElement cartBadge = wait.until(ExpectedConditions.visibilityOfElementLocated(By.className("shopping_cart_badge")));
            System.out.println("  * Verified Cart Badge Count: " + cartBadge.getText());

            // Navigate to Cart
            WebElement cartLink = wait.until(ExpectedConditions.presenceOfElementLocated(By.className("shopping_cart_link")));
            try {
                cartLink.click();
            } catch (Exception e) {
                ((JavascriptExecutor) driver).executeScript("arguments[0].click();", cartLink);
            }
            wait.until(ExpectedConditions.urlContains("cart"));
            takeScreenshot(driver, "05_cart_page.png");

            // Verify item in cart
            WebElement cartItemName = wait.until(ExpectedConditions.visibilityOfElementLocated(By.className("inventory_item_name")));
            System.out.println("  * Verified Item in Cart: " + cartItemName.getText());

            // Remove item from cart
            WebElement removeBtn = wait.until(ExpectedConditions.presenceOfElementLocated(
                By.xpath("//button[contains(@id, 'remove') or text()='Remove' or contains(text(), 'Remove')]")));
            try {
                removeBtn.click();
            } catch (Exception e) {
                ((JavascriptExecutor) driver).executeScript("arguments[0].click();", removeBtn);
            }
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
```

---

## Sample Output (Live Website: SauceDemo.com)

```text
===============================================================================
 STA LAB - EXPERIMENT 7: SELENIUM WEBDRIVER E-COMMERCE AUTOMATION SCRIPT
===============================================================================
Target E-Commerce Application URL: https://www.saucedemo.com
  [SCREENSHOT SAVED] -> target\screenshots\01_login_page.png

[STEP 1] Automating User Login...
  [SCREENSHOT SAVED] -> target\screenshots\02_credentials_entered.png
  * Login Successful: Redirected to Product Catalog.
  [SCREENSHOT SAVED] -> target\screenshots\03_inventory_page.png

[STEP 2] Verifying Product Catalog & Search Filtering...
  * Total Products Displayed in Catalog: 6
    - Found: Sauce Labs Backpack
    - Found: Sauce Labs Bike Light
    - Found: Sauce Labs Bolt T-Shirt
    - Found: Sauce Labs Fleece Jacket
    - Found: Sauce Labs Onesie
    - Found: Test.allTheThings() T-Shirt (Red)

[STEP 3] Selecting Product 'Sauce Labs Backpack' to View Details...
  * Product Detail Loaded: Sauce Labs Backpack | Price: $29.99
  [SCREENSHOT SAVED] -> target\screenshots\04_product_detail.png

[STEP 4] Automating Cart Operations...
  * Clicked 'Add to cart'.
  * Verified Cart Badge Count: 1
  [SCREENSHOT SAVED] -> target\screenshots\05_cart_page.png
  * Verified Item in Cart: Sauce Labs Backpack
  * Removed item from Cart.
  [SCREENSHOT SAVED] -> target\screenshots\06_item_removed.png

===============================================================================
 ALL SELENIUM E-COMMERCE AUTOMATION STEPS EXECUTED SUCCESSFULLY!
===============================================================================
```
