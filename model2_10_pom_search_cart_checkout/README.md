# Experiment 10 (Model 2): Page Object Model (POM) Framework for Search, Cart & Checkout

## Target Website
- **URL**: `https://www.saucedemo.com` (Live real-world e-commerce application)

## Overview
Constructs a robust Page Object Model (POM) automation architecture using Selenium WebDriver and TestNG:
1. **`BasePage`**: Base class encapsulating driver initialization, explicit waits, scrolling, and action dispatchers.
2. **`LoginPage`**: Encapsulates login elements and credentials authentication.
3. **`InventoryPage`**: Encapsulates product catalog display, keyword filtering/search, sorting, and cart additions.
4. **`CartPage`**: Encapsulates cart item inspection and checkout transition.
5. **`CheckoutPage`**: Encapsulates shipping customer information form and payment summary calculations.
6. **`CheckoutCompletePage`**: Encapsulates final order confirmation banner.
7. **`REUSABILITY_AND_MAINTAINABILITY.md`**: In-depth evaluation report analyzing framework decoupling and maintenance cost reduction.

## Main Java File to Write in Lab
- **`src/test/java/com/sta/pom/Model2POMTestSuite.java`**

## Prerequisites
- Java JDK 17 or higher
- Apache Maven 3.8+
- Google Chrome browser installed

## How to Set Up and Run on a New Computer
1. Open terminal inside this directory:
   ```bash
   cd model2_10_pom_search_cart_checkout
   ```
2. Run POM test suite via Maven:
   ```bash
   mvn clean test
   ```
3. Run with **Visible Browser GUI Window**:
   ```bash
   mvn test -Dheadless=false
   ```
