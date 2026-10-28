# Experiment 09: Data-Driven Automation Framework Using Selenium & TestNG

## Question
> Build a data-driven automation framework using Selenium WebDriver and TestNG. Read test data from an external source and execute the same test cases with multiple sets of input data.

---

## 📝 Main Java File(s) to Write in Lab Exam / Practical
In your lab exam, this is the Data-Driven TestNG test class and CSV reader you write:
* **External Test Data File:** [`src/test/resources/data/login_data.csv`](file:///d:/PROJECTS/stalab/09_datadriven_framework_testng/src/test/resources/data/login_data.csv) (Contains 6 test datasets: valid credentials, locked user, invalid password, unregistered user, empty username, empty password)
* **Main Java Class to Write:** [`src/test/java/com/sta/datadriven/DataDrivenLoginTest.java`](file:///d:/PROJECTS/stalab/09_datadriven_framework_testng/src/test/java/com/sta/datadriven/DataDrivenLoginTest.java)
  * **Class Name:** `com.sta.datadriven.DataDrivenLoginTest`
  * **Function:** Implements `@DataProvider(name = "loginCsvData")` which feeds `@Test(dataProvider = "loginCsvData")`. Runs the same login test across multiple sets of credentials and asserts expected outcomes.
* **External Data Parser:** [`src/main/java/com/sta/datadriven/CsvDataReader.java`](file:///d:/PROJECTS/stalab/09_datadriven_framework_testng/src/main/java/com/sta/datadriven/CsvDataReader.java) (Reads CSV rows into `Object[][]`)
* **TestNG Suite XML:** `src/test/resources/testng.xml`

---

## Deliverables in this Directory
1. **`src/test/resources/data/login_data.csv`** - External CSV data source containing multiple test permutations.
2. **`src/main/java/com/sta/datadriven/CsvDataReader.java`** - Generic CSV parser extracting records into `Object[][]` for TestNG `@DataProvider`.
3. **`src/test/java/com/sta/datadriven/DataDrivenLoginTest.java`** - TestNG test parameterized with `@DataProvider`, iterating through every row of test data dynamically.
4. **`src/test/resources/testng.xml`** - TestNG suite runner.
5. **`pom.xml`** - Self-contained Maven configuration.

---

## Prerequisites (Setting Up on Any New Computer)
1. **Java JDK (17 or higher)** (`java -version`)
2. **Apache Maven (3.8 or higher)** (`mvn -version`)
3. **Google Chrome Browser** (Latest version). Selenium 4 manages driver binaries automatically.

---

## How to Build and Run

Open terminal/PowerShell inside this folder:
```bash
cd 09_datadriven_framework_testng
```

### Run Data-Driven TestNG Automation Suite
```bash
mvn clean test
```

---

## Viewing Test Execution Results
After execution, detailed HTML test reports are generated in:
- `target/surefire-reports/index.html`
- `target/surefire-reports/emailable-report.html`

---

## Expected Output
```text
===============================================================================
 STA LAB - EXPERIMENT 9: DATA-DRIVEN TESTNG AUTOMATION FRAMEWORK
 Reading Test Data from: src/test/resources/data/login_data.csv
===============================================================================
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
[INFO] Tests run: 6, Failures: 0, Errors: 0, Skipped: 0
[INFO] BUILD SUCCESS
```
