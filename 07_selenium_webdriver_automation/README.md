# Experiment 07: Selenium WebDriver E-Commerce Automation

## Question
> Develop and execute Selenium WebDriver scripts to automate login, product search, product selection, and cart management in an e-commerce application.

---

## 📝 Main Java File(s) to Write in Lab Exam / Practical
In your lab exam, this is the core Selenium automation script you write to drive the browser:
* **Main Java Class to Write:** [`src/main/java/com/sta/selenium/EcommerceSeleniumScript.java`](file:///d:/PROJECTS/stalab/07_selenium_webdriver_automation/src/main/java/com/sta/selenium/EcommerceSeleniumScript.java)
  * **Class Name:** `com.sta.selenium.EcommerceSeleniumScript`
  * **Function:** Automates end-to-end user flows in Google Chrome:
    1. Initializes `ChromeDriver` with ChromeOptions (supports headless and UI).
    2. Enters credentials (`standard_user` / `secret_sauce`) and clicks Login.
    3. Searches and inspects the product catalog.
    4. Selects "Sauce Labs Backpack" and navigates to the Product Detail Page (PDP).
    5. Clicks "Add to cart", asserts that the badge counter increments to 1, navigates to cart, and removes the item.
    6. Automatically takes milestone screenshots saved to `target/screenshots/`.
* **Automated JUnit 5 Test Suite:** [`src/test/java/com/sta/selenium/EcommerceSeleniumTest.java`](file:///d:/PROJECTS/stalab/07_selenium_webdriver_automation/src/test/java/com/sta/selenium/EcommerceSeleniumTest.java)

---

## Deliverables in this Directory
1. **`src/main/java/com/sta/selenium/EcommerceSeleniumScript.java`** - Standalone automation script with console audit logging and screenshot capture.
2. **`src/test/java/com/sta/selenium/EcommerceSeleniumTest.java`** - Ordered JUnit 5 test suite verifying each workflow.
3. **`src/main/resources/mock_store/index.html`** - Self-contained offline mock store (with identical selectors) for testing without an active internet connection.
4. **`pom.xml`** - Self-contained Maven configuration with Selenium 4.

---

## Prerequisites (Setting Up on Any New Computer)
1. **Java JDK (17 or higher)** (`java -version`)
2. **Apache Maven (3.8 or higher)** (`mvn -version`)
3. **Google Chrome Browser** (Latest version).
   * Selenium 4 features built-in **SeleniumManager**, so you do **NOT** need to manually download `chromedriver.exe`! Selenium will detect Chrome and configure the driver automatically.

---

## How to Build and Run

Open terminal/PowerShell inside this folder:
```bash
cd 07_selenium_webdriver_automation
```

### 1. Run Automated Test Suite (Headless Chrome)
```bash
mvn clean test
```

### 2. Run Automation Script with Console Audit & Screenshots
```bash
mvn compile exec:java
```

### Optional: Run in Visible Headed Mode (to watch the browser window)
```bash
mvn compile exec:java -Dheadless=false
```

---

## Expected Output
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
    ...

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
Screenshots will be generated inside `target/screenshots/`.
