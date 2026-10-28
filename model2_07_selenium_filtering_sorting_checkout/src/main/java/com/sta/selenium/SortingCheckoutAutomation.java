package com.sta.selenium;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class SortingCheckoutAutomation {

    public static WebDriver createDriver() {
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
        return new ChromeDriver(options);
    }

    public static boolean runAutomation(WebDriver driver) {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        try {
            System.out.println("[STEP 1] Navigating to real e-commerce target: https://www.saucedemo.com");
            driver.get("https://www.saucedemo.com");

            // Step 1: User Login
            System.out.println("[STEP 2] Performing User Login Authentication...");
            WebElement userField = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("user-name")));
            userField.sendKeys("standard_user");
            driver.findElement(By.id("password")).sendKeys("secret_sauce");
            driver.findElement(By.id("login-button")).click();

            wait.until(ExpectedConditions.urlContains("/inventory.html"));
            System.out.println("  PASS: Authenticated successfully. Current URL: " + driver.getCurrentUrl());

            // Step 2: Product Sorting - Price (Low to High)
            System.out.println("[STEP 3] Testing Product Sorting: Price (Low to High)...");
            WebElement sortSelectEl = wait.until(ExpectedConditions.elementToBeClickable(By.className("product_sort_container")));
            Select sortDropdown = new Select(sortSelectEl);
            sortDropdown.selectByValue("lohi");

            // Wait for sort update
            Thread.sleep(1000);
            List<WebElement> priceElements = driver.findElements(By.className("inventory_item_price"));
            List<Double> actualPrices = new ArrayList<>();
            for (WebElement el : priceElements) {
                String text = el.getText().replace("$", "").trim();
                actualPrices.add(Double.parseDouble(text));
            }
            System.out.println("  Extracted catalog prices (Low to High): " + actualPrices);

            for (int i = 0; i < actualPrices.size() - 1; i++) {
                if (actualPrices.get(i) > actualPrices.get(i + 1)) {
                    throw new AssertionError("Price sorting failed: " + actualPrices.get(i) + " > " + actualPrices.get(i + 1));
                }
            }
            System.out.println("  PASS: Verified Price (Low to High) sorting assertion.");

            // Step 3: Product Sorting - Name (Z to A)
            System.out.println("[STEP 4] Testing Product Sorting: Name (Z to A)...");
            sortSelectEl = driver.findElement(By.className("product_sort_container"));
            new Select(sortSelectEl).selectByValue("za");
            Thread.sleep(1000);

            List<WebElement> nameElements = driver.findElements(By.className("inventory_item_name"));
            List<String> actualNames = new ArrayList<>();
            for (WebElement el : nameElements) {
                actualNames.add(el.getText());
            }
            System.out.println("  First 2 items (Z to A): " + actualNames.subList(0, Math.min(2, actualNames.size())));

            List<String> sortedExpectedNames = new ArrayList<>(actualNames);
            sortedExpectedNames.sort(Collections.reverseOrder());
            if (!actualNames.equals(sortedExpectedNames)) {
                throw new AssertionError("Name sorting (Z to A) mismatch!");
            }
            System.out.println("  PASS: Verified Name (Z to A) sorting assertion.");

            // Step 4: Product Selection & Cart Operations
            System.out.println("[STEP 5] Selecting Products & Managing Cart...");
            WebElement addBackpack = wait.until(ExpectedConditions.presenceOfElementLocated(By.id("add-to-cart-sauce-labs-backpack")));
            ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView({block: 'center'});", addBackpack);
            Thread.sleep(300);
            ((JavascriptExecutor) driver).executeScript("arguments[0].click();", addBackpack);

            WebElement addBikeLight = wait.until(ExpectedConditions.presenceOfElementLocated(By.id("add-to-cart-sauce-labs-bike-light")));
            ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView({block: 'center'});", addBikeLight);
            Thread.sleep(300);
            ((JavascriptExecutor) driver).executeScript("arguments[0].click();", addBikeLight);

            WebElement badge = wait.until(ExpectedConditions.visibilityOfElementLocated(By.className("shopping_cart_badge")));
            String badgeCount = badge.getText();
            System.out.println("  Cart Badge Count: " + badgeCount);
            if (!"2".equals(badgeCount)) {
                throw new AssertionError("Expected 2 items in cart badge, but found: " + badgeCount);
            }
            System.out.println("  PASS: Cart count assertion validated.");

            // Step 5: Checkout Operations
            System.out.println("[STEP 6] Opening Cart and Proceeding to Checkout...");
            WebElement cartLink = driver.findElement(By.className("shopping_cart_link"));
            ((JavascriptExecutor) driver).executeScript("arguments[0].click();", cartLink);

            wait.until(ExpectedConditions.urlContains("/cart.html"));
            WebElement checkoutBtn = wait.until(ExpectedConditions.presenceOfElementLocated(By.id("checkout")));
            ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView({block: 'center'}); arguments[0].click();", checkoutBtn);

            System.out.println("[STEP 7] Submitting Customer Checkout Information...");
            wait.until(ExpectedConditions.urlContains("/checkout-step-one.html"));
            driver.findElement(By.id("first-name")).sendKeys("Jane");
            driver.findElement(By.id("last-name")).sendKeys("Doe");
            driver.findElement(By.id("postal-code")).sendKeys("90210");
            WebElement continueBtn = driver.findElement(By.id("continue"));
            ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView({block: 'center'}); arguments[0].click();", continueBtn);

            System.out.println("[STEP 8] Validating Order Summary & Finishing Checkout...");
            wait.until(ExpectedConditions.urlContains("/checkout-step-two.html"));
            WebElement summaryTotal = wait.until(ExpectedConditions.visibilityOfElementLocated(By.className("summary_total_label")));
            System.out.println("  Order " + summaryTotal.getText());

            WebElement finishBtn = wait.until(ExpectedConditions.presenceOfElementLocated(By.id("finish")));
            ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView({block: 'center'}); arguments[0].click();", finishBtn);

            wait.until(ExpectedConditions.urlContains("/checkout-complete.html"));
            WebElement completeHeader = wait.until(ExpectedConditions.visibilityOfElementLocated(By.className("complete-header")));
            System.out.println("  Confirmation Received: " + completeHeader.getText());

            if (!completeHeader.getText().toLowerCase().contains("thank you")) {
                throw new AssertionError("Confirmation header does not contain 'Thank you'!");
            }
            System.out.println("  PASS: End-to-end checkout assertion validated.");
            return true;
        } catch (Exception e) {
            System.err.println("AUTOMATION FAILURE: " + e.getMessage());
            e.printStackTrace();
            return false;
        } finally {
            driver.quit();
        }
    }

    public static void main(String[] args) {
        System.out.println("==========================================================================================");
        System.out.println("   SELENIUM WEBDRIVER: USER AUTH, FILTERING, SORTING & CHECKOUT ON SAUCEDEMO.COM         ");
        System.out.println("==========================================================================================");
        WebDriver driver = createDriver();
        boolean success = runAutomation(driver);
        System.out.println("------------------------------------------------------------------------------------------");
        System.out.println("Automation Execution Result: " + (success ? "ALL ASSERTIONS PASSED" : "FAILED"));
        System.out.println("==========================================================================================");
    }
}
