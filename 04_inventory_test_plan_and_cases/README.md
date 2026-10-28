# Experiment 04: Inventory Control System Test Plan and Test Cases

## Question
> Develop a test plan and design test cases for an inventory control system covering product addition, stock updates, stock availability, low-stock alerts, and inventory reports.

---

## 📝 Main Java File(s) to Write in Lab Exam / Practical
In your lab exam, you need to provide the IEEE test plan, test cases document, and the inventory control system with unit tests:
* **Documentation Files:**
  - `INVENTORY_TEST_PLAN.md` (IEEE 829 Test Plan for Inventory Control System)
  - `INVENTORY_TEST_CASES.md` (Test Cases for Product Addition, Stock Updates, Availability, Low-Stock Alerts, and Reports)
* **Main Java Class to Write:** [`src/main/java/com/sta/inventory/InventoryService.java`](file:///d:/PROJECTS/stalab/04_inventory_test_plan_and_cases/src/main/java/com/sta/inventory/InventoryService.java)
  * **Class Name:** `com.sta.inventory.InventoryService`
  * **Key Methods:** `addProduct()`, `replenishStock()`, `deductStock()`, `checkAvailability()`, `generateReport()`, and observer alerts.
* **Interactive Demo Runner:** [`src/main/java/com/sta/inventory/InventoryApp.java`](file:///d:/PROJECTS/stalab/04_inventory_test_plan_and_cases/src/main/java/com/sta/inventory/InventoryApp.java)
* **Automated JUnit 5 Test Suite:** [`src/test/java/com/sta/inventory/InventoryControlSystemTest.java`](file:///d:/PROJECTS/stalab/04_inventory_test_plan_and_cases/src/test/java/com/sta/inventory/InventoryControlSystemTest.java)

---

## Deliverables in this Directory
1. **[INVENTORY_TEST_PLAN.md](file:///d:/PROJECTS/stalab/04_inventory_test_plan_and_cases/INVENTORY_TEST_PLAN.md)** - IEEE 829 Standard Test Plan for the Inventory Control System.
2. **[INVENTORY_TEST_CASES.md](file:///d:/PROJECTS/stalab/04_inventory_test_plan_and_cases/INVENTORY_TEST_CASES.md)** - Exhaustive test cases covering Product Addition, Stock Updates, Availability, Low-Stock Alerts, and Reports.
3. **`src/main/java/com/sta/inventory/`** -
   - `Product.java`: Product catalog model with price, quantity, thresholds.
   - `InventoryService.java`: Full operational service with thread-safe stock management and alert observers.
   - `InventoryReport.java`: Audit and financial valuation reporting engine.
   - `InventoryApp.java`: Interactive driver demonstrating all 5 modules in sequence.
4. **`src/test/java/com/sta/inventory/InventoryControlSystemTest.java`** - Complete JUnit 5 test suite verifying all 5 inventory modules under positive, negative, and edge-case boundaries.
5. **`pom.xml`** - Self-contained Maven configuration.

---

## Prerequisites (Setting Up on Any New Computer)
1. **Java JDK (17 or higher)** (`java -version`)
2. **Apache Maven (3.8 or higher)** (`mvn -version`)

---

## How to Build and Run

Open terminal/PowerShell inside this folder:
```bash
cd 04_inventory_test_plan_and_cases
```

### 1. Run Automated Test Suite (All 5 Modules)
```bash
mvn clean test
```

### 2. Run Interactive Inventory Demonstration Application
```bash
mvn compile exec:java
```

---

## Expected Output
```text
===============================================================================
 STA LAB - EXPERIMENT 4: INVENTORY CONTROL SYSTEM DEMONSTRATION
===============================================================================

--- 1. ADDING INITIAL PRODUCTS ---
  Added 4 products successfully to warehouse catalog.

--- 2. STOCK AVAILABILITY CHECK ---
  SKU-101 status: IN_STOCK (25 units)
  SKU-104 status: IN_STOCK (4 units)

--- 3. PERFORMING STOCK MOVEMENTS & ALERT TRIGGERS ---
  -> Replenishing 10 units for SKU-101...
  SKU-101 new balance: 35
  -> Deducting 5 units from SKU-103 (Keyboard: was 8, threshold 5)...
  [ALERT DISPATCHED] Type: LOW_STOCK | SKU: SKU-103 (Mechanical Keyboard) | Remaining: 3 | Threshold: 5
  -> Deducting all 4 units from SKU-104 (Drops to 0)...
  [ALERT DISPATCHED] Type: OUT_OF_STOCK | SKU: SKU-104 (USB-C Hub Multiport) | Remaining: 0 | Threshold: 6

--- 4. GENERATING INVENTORY AUDIT & VALUATION REPORT ---
=====================================================================
               ENTERPRISE INVENTORY AUDIT REPORT                    
=====================================================================
Total Distinct SKUs In Catalog : 4
Total Stocked Units In Storage : 48 units
Total Inventory Asset Valuation: $7,354.65
Number of Low/Depleted SKUs    : 2
---------------------------------------------------------------------
FLAGGED LOW-STOCK SKUs FOR REORDER:
  * SKU: SKU-104    | USB-C Hub Multiport      | Qty:  0 (Threshold:  6)
  * SKU: SKU-103    | Mechanical Keyboard      | Qty:  3 (Threshold:  5)
=====================================================================
```
