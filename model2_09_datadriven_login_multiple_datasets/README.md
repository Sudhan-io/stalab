# Experiment 09 (Model 2): Data-Driven Testing Framework with Multiple Login Datasets

## Target Website
- **URL**: `https://www.saucedemo.com` (Live real-world e-commerce application)

## Overview
Designs and implements a data-driven testing framework using Selenium WebDriver and TestNG. Test cases read credentials dynamically from an external CSV file (`src/test/resources/data/login_datasets.csv`) and execute multiple positive and negative login scenarios:
1. **SCN_POS_01**: Valid standard user (`standard_user` / `secret_sauce`) -> Expects redirect to `/inventory.html`.
2. **SCN_NEG_02**: Locked out account (`locked_out_user`) -> Expects error *"Sorry, this user has been locked out"*.
3. **SCN_NEG_03**: Incorrect password -> Expects error *"Username and password do not match"*.
4. **SCN_NEG_04**: Empty username field -> Expects error *"Username is required"*.
5. **SCN_NEG_05**: Empty password field -> Expects error *"Password is required"*.
6. **SCN_POS_06**: Valid alternate persona (`problem_user`) -> Expects redirect to `/inventory.html`.

## Main Java File to Write in Lab
- **`src/test/java/com/sta/datadriven/DataDrivenLoginSuite.java`**

## Prerequisites
- Java JDK 17 or higher
- Apache Maven 3.8+
- Google Chrome browser installed

## How to Set Up and Run on a New Computer
1. Open terminal inside this folder:
   ```bash
   cd model2_09_datadriven_login_multiple_datasets
   ```
2. Run data-driven test suite in headless mode:
   ```bash
   mvn clean test
   ```
3. Run with **Visible Browser GUI Window**:
   ```bash
   mvn test -Dheadless=false
   ```
