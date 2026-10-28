# Experiment 10: Page Object Model (POM) Automation Framework

## Question
> Develop an automation framework using Selenium WebDriver, TestNG, and the Page Object Model (POM). Automate login, product search, and cart operations, and evaluate the framework's maintainability.

---

## 📝 Main Java File(s) to Write in Lab Exam / Practical
In your lab exam, this is the Page Object Model architecture and test suite you write:
* **Documentation File:** `MAINTAINABILITY_EVALUATION.md` (Detailed maintainability evaluation report comparing Procedural vs POM architecture, locator deduplication, and flakiness reduction)
* **Main Page Classes to Write (`src/main/java/com/sta/pom/pages/`):**
  1. [`LoginPage.java`](file:///d:/PROJECTS/stalab/10_pom_framework_selenium_testng/src/main/java/com/sta/pom/pages/LoginPage.java) - Encapsulates `#user-name`, `#password`, `#login-button`, error message locators, and `loginAs()` method.
  2. [`ProductsPage.java`](file:///d:/PROJECTS/stalab/10_pom_framework_selenium_testng/src/main/java/com/sta/pom/pages/ProductsPage.java) - Encapsulates catalog cards, `selectProduct()`, `addProductToCart()`, `getCartBadgeCount()`, and `openCart()`.
  3. [`ProductDetailPage.java`](file:///d:/PROJECTS/stalab/10_pom_framework_selenium_testng/src/main/java/com/sta/pom/pages/ProductDetailPage.java) - Encapsulates PDP titles, prices, and 'Add to cart'.
  4. [`CartPage.java`](file:///d:/PROJECTS/stalab/10_pom_framework_selenium_testng/src/main/java/com/sta/pom/pages/CartPage.java) - Encapsulates shopping cart items and `removeItem()`.
* **Main TestNG Test Suite to Write:** [`src/test/java/com/sta/pom/tests/EcommercePOMTest.java`](file:///d:/PROJECTS/stalab/10_pom_framework_selenium_testng/src/test/java/com/sta/pom/tests/EcommercePOMTest.java)
  * **Class Name:** `com.sta.pom.tests.EcommercePOMTest`
  * **Function:** Uses the Page Objects to test:
    - `testValidLogin()`
    - `testProductCatalogAndSearch()`
    - `testProductSelectionDetails()`
    - `testCartOperations()`
    - `testInvalidLoginErrorMessage()`
* **Base Test Setup:** [`src/test/java/com/sta/pom/tests/BaseTest.java`](file:///d:/PROJECTS/stalab/10_pom_framework_selenium_testng/src/test/java/com/sta/pom/tests/BaseTest.java)

---

## Deliverables in this Directory
1. **[MAINTAINABILITY_EVALUATION.md](file:///d:/PROJECTS/stalab/10_pom_framework_selenium_testng/MAINTAINABILITY_EVALUATION.md)** - Full engineering evaluation report on POM maintainability.
2. **Page Objects (`src/main/java/com/sta/pom/pages/`)** - `BasePage.java`, `LoginPage.java`, `ProductsPage.java`, `ProductDetailPage.java`, `CartPage.java`.
3. **Automated POM Tests (`src/test/java/com/sta/pom/tests/`)** - `BaseTest.java`, `EcommercePOMTest.java`.
4. **`src/test/resources/testng.xml`** - TestNG suite runner.
5. **`pom.xml`** - Self-contained Maven configuration.

---

## Prerequisites (Setting Up on Any New Computer)
1. **Java JDK (17 or higher)** (`java -version`)
2. **Apache Maven (3.8 or higher)** (`mvn -version`)
3. **Google Chrome Browser** (Latest version). Driver managed automatically.

---

## How to Build and Run

Open terminal/PowerShell inside this folder:
```bash
cd 10_pom_framework_selenium_testng
```

### Run Page Object Model Automation Suite
```bash
mvn clean test
```

---

## Expected Output
```text
>>> [POM TEST 1] Executing Valid Login Test...
  * [PASS] Successfully logged in using LoginPage object.
>>> [POM TEST 2] Executing Product Catalog Verification...
  * Found 6 products in catalog.
>>> [POM TEST 3] Executing Product Selection Test...
  * [PASS] Verified details via ProductDetailPage object: Sauce Labs Backpack ($29.99)
>>> [POM TEST 4] Executing Cart Operations Test...
  * [PASS] Cart badge incremented to 1.
  * [PASS] Verified 'Sauce Labs Backpack' in CartPage.
  * [PASS] Removed item from CartPage successfully.
>>> [POM TEST 5] Executing Negative Login Validation...
  * [PASS] Captured expected error: Epic sadface: Username and password do not match any user in this service
[INFO] Tests run: 5, Failures: 0, Errors: 0, Skipped: 0
[INFO] BUILD SUCCESS
```
HTML reports are generated at `target/surefire-reports/index.html`.
