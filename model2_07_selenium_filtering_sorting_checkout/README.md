# Experiment 07 (Model 2): Selenium Automation for User Auth, Filtering, Sorting & Checkout

## Target Website
- **URL**: `https://www.saucedemo.com` (Real live e-commerce web application)

## Overview
Automates complete end-to-end user journeys using Selenium WebDriver:
1. **User Authentication**: Automated login with credential validation.
2. **Product Sorting (Numeric)**: Sorts catalog by *Price: Low to High* (`lohi`), extracts all displayed prices, parses into numeric doubles, and asserts ascending order.
3. **Product Sorting (Alphabetical)**: Sorts catalog by *Name: Z to A* (`za`), extracts product titles, and asserts descending alphabetical order.
4. **Product Selection & Cart Operations**: Adds multiple items to cart and asserts shopping cart badge reflects exact item count.
5. **Checkout Workflow**: Submits shipping customer details, verifies total order cost, and asserts order completion message (*"Thank you for your order!"*).

## Main Java File to Write in Lab
- **`src/main/java/com/sta/selenium/SortingCheckoutAutomation.java`**

## Prerequisites
- Java JDK 17 or higher
- Apache Maven 3.8+
- Google Chrome browser installed

## How to Set Up and Run on a New Computer
1. Open terminal inside this folder:
   ```bash
   cd model2_07_selenium_filtering_sorting_checkout
   ```
2. Run automated tests in headless mode:
   ```bash
   mvn clean test
   ```
3. Run with **Visible Browser GUI Window** (opens real desktop Chrome):
   ```bash
   mvn test -Dheadless=false
   ```
   Or run the main program directly:
   ```bash
   mvn exec:java -Dheadless=false
   ```

