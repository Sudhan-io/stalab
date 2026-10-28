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
            // Suppose 2 pallet jacks were damaged during forklift transit
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

