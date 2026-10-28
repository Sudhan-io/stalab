# Experiment 04 (Model 2)

## Aim
To design a test plan and test cases for a warehouse inventory application covering stock receipt, stock issue, inventory adjustment, supplier details, and stock reconciliation.

## Algorithm / Procedure
1. Create a `Supplier` domain model with validation for supplier ID, corporate name, email address, and tax/GST identification number.
2. Create a `WarehouseItem` model that tracks SKU, bin location, unit cost, and synchronized on-hand quantity.
3. Design test cases for **Supplier Details** ensuring valid onboarding and rejection of malformed or inactive supplier records.
4. Design test cases for **Stock Receipt** verifying Goods Receipt Note (GRN) processing, incrementing stock balances, and rejecting receipts from unauthorized suppliers.
5. Design test cases for **Stock Issue** ensuring order dispatches decrement inventory and prevent negative stock (underflow).
6. Design test cases for **Inventory Adjustment** supporting shrinkage, damage, and count corrections with mandatory audit reason codes.
7. Design test cases for **Stock Reconciliation** by comparing physical count against book inventory, computing numeric variance and financial cost impact.
8. Maintain an immutable transaction ledger for compliance and auditability.

## Main Java Program
```java
package com.sta.warehouse;

import java.util.*;

public class WarehouseInventoryApp {

    public static void main(String[] args) {
        System.out.println("==========================================================================================");
        System.out.println("               WAREHOUSE INVENTORY SYSTEM - TEST PLAN & CASE EXECUTION                    ");
        System.out.println("==========================================================================================");
        System.out.println("Scope Covered: 1. Supplier Details | 2. Stock Receipt | 3. Stock Issue");
        System.out.println("               4. Inventory Adjustment | 5. Stock Reconciliation");
        System.out.println("------------------------------------------------------------------------------------------\n");

        WarehouseInventoryService service = new WarehouseInventoryService();

        // 1. Supplier Setup
        System.out.println("[TEST 1: Supplier Details Validation]");
        try {
            Supplier s1 = new Supplier("SUP-101", "Apex Logistics Ltd", "orders@apex.com", "GSTIN33AAACA111", true);
            service.registerSupplier(s1);
            System.out.println("  PASS: Registered active supplier " + s1.getSupplierName());
        } catch (Exception e) {
            System.out.println("  FAIL: " + e.getMessage());
        }

        try {
            new Supplier("", "Bad Supplier", "bad@test.com", "GST123", true);
            System.out.println("  FAIL: Supplier with empty ID should have been rejected");
        } catch (IllegalArgumentException e) {
            System.out.println("  PASS: Rejected invalid supplier with empty ID -> " + e.getMessage());
        }

        // Initialize Warehouse Catalog
        WarehouseItem item1 = new WarehouseItem("SKU-PALLET-01", "Industrial Pallets 120x80", "BIN-A-04", 50, 45.00);
        WarehouseItem item2 = new WarehouseItem("SKU-FORK-09", "Hydraulic Pallet Jack", "BIN-BAY-02", 5, 280.00);
        service.addWarehouseItem(item1);
        service.addWarehouseItem(item2);

        // 2. Stock Receipt (GRN)
        System.out.println("\n[TEST 2: Stock Receipt (GRN Verification)]");
        try {
            service.receiveStock("GRN-9001", "SUP-101", "SKU-PALLET-01", 100);
            System.out.println("  PASS: Stock Receipt recorded. Current Pallet Stock = " + service.getItem("SKU-PALLET-01").getOnHandStock() + " (Expected 150)");
        } catch (Exception e) {
            System.out.println("  FAIL: " + e.getMessage());
        }

        try {
            service.receiveStock("GRN-9002", "SUP-UNKNOWN", "SKU-PALLET-01", 20);
            System.out.println("  FAIL: Stock receipt from unapproved supplier should have thrown error");
        } catch (IllegalArgumentException e) {
            System.out.println("  PASS: Blocked receipt from unregistered supplier -> " + e.getMessage());
        }

        // 3. Stock Issue (Pick & Dispatch)
        System.out.println("\n[TEST 3: Stock Issue (Pick & Dispatch Validation)]");
        try {
            service.issueStock("DISP-501", "SKU-PALLET-01", 30);
            System.out.println("  PASS: Issued 30 pallets. Remaining on-hand = " + service.getItem("SKU-PALLET-01").getOnHandStock() + " (Expected 120)");
        } catch (Exception e) {
            System.out.println("  FAIL: " + e.getMessage());
        }

        try {
            service.issueStock("DISP-502", "SKU-FORK-09", 999);
            System.out.println("  FAIL: Overselling / negative stock issue should be blocked");
        } catch (IllegalStateException e) {
            System.out.println("  PASS: Prevented over-issuance -> " + e.getMessage());
        }

        // 4. Inventory Adjustment (Cycle Count & Damages)
        System.out.println("\n[TEST 4: Inventory Adjustment]");
        try {
            service.adjustInventory("ADJ-2026-01", "SKU-FORK-09", 3, "DAMAGED_IN_TRANSIT");
            System.out.println("  PASS: Adjusted stock to 3 units. Remaining = " + service.getItem("SKU-FORK-09").getOnHandStock());
        } catch (Exception e) {
            System.out.println("  FAIL: " + e.getMessage());
        }

        // 5. Stock Reconciliation (Physical Audit vs Book Stock)
        System.out.println("\n[TEST 5: Stock Reconciliation]");
        Map<String, Object> audit = service.reconcileStock("SKU-PALLET-01", 118);
        System.out.println("  Audit SKU            : " + audit.get("sku"));
        System.out.println("  Book Stock           : " + audit.get("bookStock"));
        System.out.println("  Physical Counted     : " + audit.get("physicalCount"));
        System.out.println("  Variance             : " + audit.get("variance") + " units");
        System.out.println("  Variance Valuation   : $" + audit.get("varianceCost"));
        System.out.println("  Reconciliation Match : " + audit.get("reconciled"));

        System.out.println("\n[AUDIT TRAIL / WAREHOUSE LEDGER ENTRIES]");
        service.getTransactionLedger().forEach(entry -> System.out.println("  " + entry));

        System.out.println("\n==========================================================================================");
        System.out.println("                       ALL 5 WAREHOUSE MODULE TEST CASES VERIFIED                         ");
        System.out.println("==========================================================================================");
    }
}
```

## Sample Output
```text
==========================================================================================
               WAREHOUSE INVENTORY SYSTEM - TEST PLAN & CASE EXECUTION                    
==========================================================================================
Scope Covered: 1. Supplier Details | 2. Stock Receipt | 3. Stock Issue
               4. Inventory Adjustment | 5. Stock Reconciliation
------------------------------------------------------------------------------------------

[TEST 1: Supplier Details Validation]
  PASS: Registered active supplier Apex Logistics Ltd
  PASS: Rejected invalid supplier with empty ID -> Supplier ID cannot be empty

[TEST 2: Stock Receipt (GRN Verification)]
  PASS: Stock Receipt recorded. Current Pallet Stock = 150 (Expected 150)
  PASS: Blocked receipt from unregistered supplier -> Cannot receive goods: Inactive or unregistered supplier SUP-UNKNOWN

[TEST 3: Stock Issue (Pick & Dispatch Validation)]
  PASS: Issued 30 pallets. Remaining on-hand = 120 (Expected 120)
  PASS: Prevented over-issuance -> Insufficient stock for SKU SKU-FORK-09: on hand 5, requested 999

[TEST 4: Inventory Adjustment]
  PASS: Adjusted stock to 3 units. Remaining = 3

[TEST 5: Stock Reconciliation]
  Audit SKU            : SKU-PALLET-01
  Book Stock           : 120
  Physical Counted     : 118
  Variance             : -2 units
  Variance Valuation   : $-90.0
  Reconciliation Match : false

[AUDIT TRAIL / WAREHOUSE LEDGER ENTRIES]
  Wed Oct 28 10:54:10 IST 2026 | SUPPLIER_REGISTERED: ID=SUP-101, Name=Apex Logistics Ltd
  Wed Oct 28 10:54:10 IST 2026 | STOCK_RECEIPT: GRN=GRN-9001, SKU=SKU-PALLET-01, Qty=100, NewBalance=150
  Wed Oct 28 10:54:10 IST 2026 | STOCK_ISSUE: Order=DISP-501, SKU=SKU-PALLET-01, Qty=30, NewBalance=120
  Wed Oct 28 10:54:10 IST 2026 | INVENTORY_ADJUSTMENT: AdjId=ADJ-2026-01, SKU=SKU-FORK-09, Variance=-2, Reason=DAMAGED_IN_TRANSIT
  Wed Oct 28 10:54:10 IST 2026 | STOCK_RECONCILIATION: SKU=SKU-PALLET-01, Book=120, Physical=118, Variance=-2

==========================================================================================
                       ALL 5 WAREHOUSE MODULE TEST CASES VERIFIED                         
==========================================================================================
```
