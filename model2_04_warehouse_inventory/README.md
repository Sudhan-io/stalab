# Experiment 04 (Model 2): Warehouse Inventory System Test Plan & Test Cases

## Overview
Develops a comprehensive test plan and designs/executes test cases for a warehouse inventory management application covering:
1. **Stock Receipt** (Goods Receipt Note / GRN validation, catalog addition)
2. **Stock Issue** (Pick & Dispatch, negative stock prevention)
3. **Inventory Adjustment** (Discrepancy write-off, damaged goods reporting)
4. **Supplier Details** (Supplier onboarding, GST/Tax validation, active status)
5. **Stock Reconciliation** (Physical count vs book inventory audit, variance calculation)

## Main Java File to Write in Lab
- **`src/main/java/com/sta/warehouse/WarehouseInventoryApp.java`**

## Prerequisites
- Java JDK 17 or higher
- Apache Maven 3.8+

## How to Set Up and Run on a New Computer
1. Open terminal in this directory:
   ```bash
   cd model2_04_warehouse_inventory
   ```
2. Run unit tests:
   ```bash
   mvn clean test
   ```
3. Run the complete application simulation:
   ```bash
   mvn exec:java
   ```
   Or package into jar and run:
   ```bash
   mvn clean package
   java -cp target/model2_04_warehouse_inventory-1.0.0.jar com.sta.warehouse.WarehouseInventoryApp
   ```
