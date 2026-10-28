package com.sta.datadriven;

import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.*;

import java.time.Duration;

public class DataDrivenLoginTest {

    private WebDriver driver;
    private WebDriverWait wait;
    private static final String APP_URL = "https://www.saucedemo.com";

    @BeforeMethod(alwaysRun = true)
    public void setUp() {
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--headless=new");
        options.addArguments("--no-sandbox");
        options.addArguments("--disable-dev-shm-usage");
        options.addArguments("--disable-gpu");
        options.addArguments("--window-size=1920,1080");
        options.addArguments("--remote-allow-origins=*");

        driver = new ChromeDriver(options);
        wait = new WebDriverWait(driver, Duration.ofSeconds(8));
        driver.get(APP_URL);
    }

    @AfterMethod(alwaysRun = true)
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }

    @DataProvider(name = "loginCsvData")
    public Object[][] getLoginData() {
        return CsvDataReader.readCsvData("data/login_data.csv");
    }

    @Test(dataProvider = "loginCsvData", description = "Execute login test across multiple sets of CSV input data")
    public void testLoginDataDriven(String tcId, String username, String password,
                                    String expectedStatus, String expectedMessage) {
        System.out.printf(">>> Executing [%s] | User: '%s' | Pass: '%s' | Expected: %s%n",
            tcId, username, password.isEmpty() ? "<EMPTY>" : "******", expectedStatus);

        WebElement userField = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("user-name")));
        WebElement passField = driver.findElement(By.id("password"));
        WebElement loginBtn = driver.findElement(By.id("login-button"));

        userField.clear();
        if (!username.isEmpty()) {
            userField.sendKeys(username);
        }

        passField.clear();
        if (!password.isEmpty()) {
            passField.sendKeys(password);
        }

        loginBtn.click();

        if ("SUCCESS".equalsIgnoreCase(expectedStatus)) {
            // Verify redirected to inventory
            wait.until(ExpectedConditions.urlContains("inventory"));
            String currentUrl = driver.getCurrentUrl();
            System.out.println("  * [PASS] Successfully authenticated; URL contains: " + currentUrl);
            Assert.assertTrue(currentUrl.contains(expectedMessage),
                "Expected URL to contain " + expectedMessage + " but was " + currentUrl);

        } else if ("ERROR".equalsIgnoreCase(expectedStatus)) {
            // Verify error message displayed in error container
            WebElement errorElement = wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.cssSelector("h3[data-test='error']")));
            String actualError = errorElement.getText().trim();
            System.out.println("  * [PASS] Caught Expected Validation Error: \"" + actualError + "\"");

            Assert.assertTrue(actualError.toLowerCase().contains(expectedMessage.toLowerCase()),
                "Expected error message '" + expectedMessage + "' but got '" + actualError + "'");
        } else {
            Assert.fail("Unsupported expectedStatus in CSV: " + expectedStatus);
        }
    }
}

