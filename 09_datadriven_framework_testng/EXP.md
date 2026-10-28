# Experiment 9: Data-Driven Automation Framework using Selenium & TestNG

## Aim
To build a data-driven automation testing framework using Selenium WebDriver and TestNG, read external test data from a CSV file (`login_data.csv`), and execute the same test cases across multiple positive, negative, and edge-case datasets against a real e-commerce website (`https://www.saucedemo.com`).

---

## Algorithm / Procedure

1. **Design External Test Dataset:**
   - Create `src/test/resources/data/login_data.csv` containing test scenarios:
     - Positive test cases (e.g., `standard_user` with valid password).
     - Negative test cases (e.g., locked-out account `locked_out_user`).
     - Invalid credentials (wrong password).
     - Non-existent user accounts.
     - Empty username / empty password boundary checks.
2. **Implement External File Reader Utility (`CsvDataReader`):**
   - Read lines from CSV using `BufferedReader`.
   - Skip the header row and split lines into tokens while safely preserving comma-containing strings.
   - Return structured data as a two-dimensional array: `Object[][]`.
3. **Configure TestNG `@DataProvider`:**
   - Define a method annotated with `@DataProvider(name = "loginCsvData")` that calls the CSV reader to supply test iterations dynamically.
4. **Implement Parameterized Test Method:**
   - Annotate test with `@Test(dataProvider = "loginCsvData")` accepting parameters: `tcId`, `username`, `password`, `expectedStatus`, `expectedMessage`.
   - Clear input fields and input data for each dataset iteration.
   - For `SUCCESS`: Assert URL redirection to `/inventory.html`.
   - For `ERROR`: Locate `h3[data-test='error']` and assert that the error text matches expected message.
5. **Enforce Isolated Test Lifecycle:**
   - Use `@BeforeMethod` and `@AfterMethod` to launch a clean browser instance per test dataset, preventing session/cookie pollution across iterations.

---

## Main Java Program

### File: `src/test/java/com/sta/datadriven/DataDrivenLoginTest.java`
```java
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
        boolean headless = Boolean.parseBoolean(System.getProperty("headless", "true"));
        if (headless) {
            options.addArguments("--headless=new");
        }
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
```

### Supporting Utility: `src/main/java/com/sta/datadriven/CsvDataReader.java`
```java
package com.sta.datadriven;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class CsvDataReader {

    public static Object[][] readCsvData(String relativeResourcePath) {
        List<Object[]> records = new ArrayList<>();
        InputStream is = CsvDataReader.class.getClassLoader().getResourceAsStream(relativeResourcePath);

        if (is == null) {
            File f = new File("src/test/resources/" + relativeResourcePath);
            if (f.exists()) {
                try {
                    is = new FileInputStream(f);
                } catch (FileNotFoundException e) {
                    throw new RuntimeException("CSV data file not found at: " + f.getAbsolutePath());
                }
            }
        }

        try (BufferedReader br = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8))) {
            String line;
            boolean firstLine = true;
            while ((line = br.readLine()) != null) {
                if (firstLine) {
                    firstLine = false; // Skip CSV header
                    continue;
                }
                if (line.trim().isEmpty()) continue;

                String[] tokens = line.split(",", 5);
                for (int i = 0; i < tokens.length; i++) {
                    tokens[i] = tokens[i].trim().replaceAll("^\"|\"$", "");
                }
                records.add(tokens);
            }
        } catch (IOException e) {
            throw new RuntimeException("Error reading CSV test data: " + e.getMessage(), e);
        }

        return records.toArray(new Object[0][]);
    }
}
```

---

## Sample Output (Live Website: SauceDemo.com)

```text
>>> Executing [TC_DDT_01] | User: 'standard_user' | Pass: '******' | Expected: SUCCESS
  * [PASS] Successfully authenticated; URL contains: https://www.saucedemo.com/inventory.html

>>> Executing [TC_DDT_02] | User: 'locked_out_user' | Pass: '******' | Expected: ERROR
  * [PASS] Caught Expected Validation Error: "Epic sadface: Sorry, this user has been locked out."

>>> Executing [TC_DDT_03] | User: 'standard_user' | Pass: '******' | Expected: ERROR
  * [PASS] Caught Expected Validation Error: "Epic sadface: Username and password do not match any user in this service"

>>> Executing [TC_DDT_04] | User: 'unregistered_customer' | Pass: '******' | Expected: ERROR
  * [PASS] Caught Expected Validation Error: "Epic sadface: Username and password do not match any user in this service"

>>> Executing [TC_DDT_05] | User: '' | Pass: '******' | Expected: ERROR
  * [PASS] Caught Expected Validation Error: "Epic sadface: Username is required"

>>> Executing [TC_DDT_06] | User: 'standard_user' | Pass: '<EMPTY>' | Expected: ERROR
  * [PASS] Caught Expected Validation Error: "Epic sadface: Password is required"

Results:
Tests run: 6, Failures: 0, Errors: 0, Skipped: 0
```

