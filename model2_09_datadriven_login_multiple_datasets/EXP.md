# Experiment 09 (Model 2)

## Aim
To design and implement a data-driven testing framework using Selenium WebDriver and TestNG, execute positive and negative login scenarios using multiple datasets, and evaluate the test results.

## Algorithm / Procedure
1. Create an external test data file (`login_datasets.csv`) containing columns for `scenario_id`, `username`, `password`, `expected_outcome` (`SUCCESS` or `ERROR`), and `expected_message_or_url`.
2. Populate the CSV with positive scenarios (valid users) and negative scenarios (locked user, incorrect password, empty username, empty password).
3. Develop a `CsvDataLoader` utility class using `BufferedReader` to parse CSV records into an `Object[][]` matrix.
4. Define a TestNG test suite with a `@DataProvider(name = "loginDatasets")` linking to the CSV data loader.
5. In `@BeforeClass`, instantiate `ChromeDriver` configured for both headless and visible desktop browser execution.
6. For each dataset delivered by the `@DataProvider`:
   - Navigate to `https://www.saucedemo.com`.
   - Clear and populate `#user-name` and `#password` input fields.
   - Click `#login-button`.
   - If expected outcome is `SUCCESS`, assert URL contains `/inventory.html`, then clear session cookies.
   - If expected outcome is `ERROR`, locate error container (`[data-test='error']`) and assert presence of expected error text.
7. Execute all test iterations via Maven Surefire and evaluate overall pass/fail status.

## Main Java Program
```java
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
            wait.until(ExpectedConditions.urlContains(expectedMessageOrUrl));
            String currentUrl = driver.getCurrentUrl();
            Assert.assertTrue(currentUrl.contains(expectedMessageOrUrl),
                    "Expected landing page URL containing: " + expectedMessageOrUrl + " but got: " + currentUrl);
            System.out.println("  PASS: Successfully authenticated and redirected to inventory catalog.");
            driver.manage().deleteAllCookies();
        } else {
            WebElement errorContainer = wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("[data-test='error']")));
            String actualError = errorContainer.getText();
            System.out.println("  PASS: Caught expected error message: '" + actualError + "'");
            Assert.assertTrue(actualError.contains(expectedMessageOrUrl),
                    "Expected error containing [" + expectedMessageOrUrl + "] but was [" + actualError + "]");
        }
    }
}
```

## Sample Output
```text
Initialized WebDriver session for Data-Driven Testing on https://www.saucedemo.com

[EXECUTING SCN_POS_01] User='standard_user' | Expected=SUCCESS
  PASS: Successfully authenticated and redirected to inventory catalog.
[EXECUTING SCN_NEG_02] User='locked_out_user' | Expected=ERROR
  PASS: Caught expected error message: 'Epic sadface: Sorry, this user has been locked out.'
[EXECUTING SCN_NEG_03] User='standard_user' | Expected=ERROR
  PASS: Caught expected error message: 'Epic sadface: Username and password do not match any user in this service'
[EXECUTING SCN_NEG_04] User='' | Expected=ERROR
  PASS: Caught expected error message: 'Epic sadface: Username is required'
[EXECUTING SCN_NEG_05] User='standard_user' | Expected=ERROR
  PASS: Caught expected error message: 'Epic sadface: Password is required'
[EXECUTING SCN_POS_06] User='problem_user' | Expected=SUCCESS
  PASS: Successfully authenticated and redirected to inventory catalog.

Closed WebDriver session successfully.
```
