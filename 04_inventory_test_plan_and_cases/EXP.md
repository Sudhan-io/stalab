# Experiment 4: Inventory Control System Test Plan, Design & Implementation
> **Lab Exam Coverage:** Applicable for **Model 1 (Question 4)** and **Model 2 (Question 4)**.

## Aim
To develop a test plan and design test cases for an Inventory Control System covering Product Addition, Stock Updates (Replenishment and Deductions), Stock Availability Checks, Low-Stock Alert Mechanisms, and Inventory Valuation Reports, and execute them programmatically.

---

## Algorithm / Procedure

1. **Formulate Inventory Test Plan & Scope:**
   - Define objectives: Guarantee inventory balance accuracy, enforce atomic stock updates, prevent negative quantity errors, dispatch real-time threshold alerts, and audit total asset valuation.
2. **Implement Core Inventory Engine (`InventoryService`):**
   - Provide methods to register products (`addProduct`) with SKU, name, category, unit price, initial stock, and safety threshold.
   - Provide thread-safe methods to replenish (`replenishStock`) and deduct (`deductStock`) units.
   - Attach Observer Pattern listeners (`addAlertListener`) that automatically trigger when available stock drops $\le \text{threshold}$ or reaches 0.
3. **Design Comprehensive Test Cases:**
   - Design test scenarios covering:
     - Adding valid products & duplicate SKU rejection.
     - Restocking positive quantities & zero/negative quantity validation.
     - Deducting valid stock & handling overdraft/insufficient stock rejections.
     - Low-Stock and Out-of-Stock alert event dispatching.
     - Accurate total catalog valuation calculation ($\sum \text{quantity} \times \text{unit price}$).
4. **Execute Operations and Generate Inventory Audit Report:**
   - Add products, perform stock updates, capture listener alert callbacks, and print the formatted valuation report.

---

## Main Java Program

### File: `src/main/java/com/sta/inventory/InventoryApp.java`
```java
package com.sta.inventory;

public class InventoryApp {

    public static void main(String[] args) {
        System.out.println("===============================================================================");
        System.out.println(" STA LAB - EXPERIMENT 4: INVENTORY CONTROL SYSTEM DEMONSTRATION");
        System.out.println("===============================================================================");

        InventoryService service = new InventoryService();

        // Register Alert Listener
        service.addAlertListener((sku, name, stock, threshold, type) -> {
            System.out.printf("  [ALERT DISPATCHED] Type: %s | SKU: %s (%s) | Remaining: %d | Threshold: %d%n",
                type, sku, name, stock, threshold);
        });

        System.out.println("\n--- 1. ADDING INITIAL PRODUCTS ---");
        service.addProduct(new Product("SKU-101", "Logitech MX Master 3S", "Peripherals", 99.99, 25, 5));
        service.addProduct(new Product("SKU-102", "Dell 27-inch 4K Monitor", "Displays", 349.50, 10, 4));
        service.addProduct(new Product("SKU-103", "Mechanical Keyboard", "Peripherals", 120.00, 8, 5));
        service.addProduct(new Product("SKU-104", "USB-C Hub Multiport", "Accessories", 35.00, 4, 6)); // Already low stock
        System.out.println("  Added 4 products successfully to warehouse catalog.");

        System.out.println("\n--- 2. STOCK AVAILABILITY CHECK ---");
        System.out.println("  SKU-101 status: " + service.checkAvailability("SKU-101"));
        System.out.println("  SKU-104 status: " + service.checkAvailability("SKU-104"));

        System.out.println("\n--- 3. PERFORMING STOCK MOVEMENTS & ALERT TRIGGERS ---");
        System.out.println("  -> Replenishing 10 units for SKU-101...");
        service.replenishStock("SKU-101", 10);
        System.out.println("  SKU-101 new balance: " + service.getAvailableQuantity("SKU-101"));

        System.out.println("  -> Deducting 5 units from SKU-103 (Keyboard: was 8, threshold 5)...");
        service.deductStock("SKU-103", 5); // Drops to 3, <= 5 threshold! Alert should fire!

        System.out.println("  -> Deducting all 4 units from SKU-104 (Drops to 0)...");
        service.deductStock("SKU-104", 4); // Drops to 0! Critical out-of-stock alert!

        System.out.println("\n--- 4. GENERATING INVENTORY AUDIT & VALUATION REPORT ---");
        InventoryReport report = service.generateReport();
        report.printReport();

        System.out.println("All operations completed successfully.");
    }
}
```

---

## Sample Output

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
All operations completed successfully.
```
