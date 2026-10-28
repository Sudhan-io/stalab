package com.sta.datadriven;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.*;

import java.io.IOException;
import java.time.Duration;

public class DataDrivenLoginSuite {

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
        System.out.println("Initialized WebDriver session for Data-Driven Testing on https://www.saucedemo.com\n");
    }

    @AfterClass(alwaysRun = true)
    public void tearDown() {
        if (driver != null) {
            driver.quit();
            System.out.println("\nClosed WebDriver session successfully.");
        }
    }

    @DataProvider(name = "loginDatasets")
    public Object[][] provideLoginDatasets() throws IOException {
        return CsvDataLoader.loadLoginData("/data/login_datasets.csv");
    }

    @Test(dataProvider = "loginDatasets")
    public void testLoginScenario(String scenarioId, String username, String password,
                                  String expectedOutcome, String expectedMessageOrUrl) {
        System.out.printf("[EXECUTING %-10s] User='%s' | Expected=%s\n", scenarioId, username, expectedOutcome);

        driver.get("https://www.saucedemo.com");

        WebElement userField = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("user-name")));
        userField.clear();
        if (!username.isEmpty()) {
            userField.sendKeys(username);
        }

        WebElement passField = driver.findElement(By.id("password"));
        passField.clear();
        if (!password.isEmpty()) {
            passField.sendKeys(password);
        }

        driver.findElement(By.id("login-button")).click();

        if ("SUCCESS".equalsIgnoreCase(expectedOutcome)) {
            // Validate URL contains expected inventory endpoint
            wait.until(ExpectedConditions.urlContains(expectedMessageOrUrl));
            String currentUrl = driver.getCurrentUrl();
            Assert.assertTrue(currentUrl.contains(expectedMessageOrUrl),
                    "Expected landing page URL containing: " + expectedMessageOrUrl + " but got: " + currentUrl);
            System.out.println("  PASS: Successfully authenticated and redirected to inventory catalog.");

            // Clear session cookies to prepare clean state for next test
            driver.manage().deleteAllCookies();
        } else {
            // Validate Error message container
            WebElement errorContainer = wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("[data-test='error']")));
            String actualError = errorContainer.getText();
            System.out.println("  PASS: Caught expected error message: '" + actualError + "'");
            Assert.assertTrue(actualError.contains(expectedMessageOrUrl),
                    "Expected error containing [" + expectedMessageOrUrl + "] but was [" + actualError + "]");
        }
    }
}
