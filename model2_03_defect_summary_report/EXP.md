# Experiment 03 (Model 2)

## Aim
To execute the designed test cases for an e-commerce application, evaluate the actual results against the expected results, classify the identified defects by severity and priority, and prepare a defect summary report.

## Algorithm / Procedure
1. Define test cases across major e-commerce modules: Cart, Wishlist, Order Tracking, Payment Processing, Catalog Sorting, and Order Placement.
2. Formulate explicit Expected Results according to business specifications and regulatory standards (e.g., PCI-DSS for payment).
3. Execute the test cases against the application under test (`BuggyShoppingApp`).
4. Capture the Actual Result and evaluate variance against the Expected Result.
5. If a variance occurs, log a defect record with Defect ID, Module, Title, Steps to Reproduce, Severity (Critical, Major, Moderate, Minor), and Priority (P1-High, P2-Medium, P3-Low).
6. Aggregate all logged defects into an analytical summary matrix showing severity distribution, priority distribution, and detailed defect logs.

## Main Java Program
```java
package com.sta.defect;

import java.util.*;

public class DefectSummaryReporter {

    public List<DefectModel> executeTestsAndCollectDefects() {
        BuggyShoppingApp app = new BuggyShoppingApp();
        List<DefectModel> defects = new ArrayList<>();

        // Test 1: Negative Cart Quantity
        boolean cartAccepted = app.updateCartQuantity("ITEM-101", -5);
        if (cartAccepted) {
            defects.add(new DefectModel(
                "DEF-01",
                "Cart Management",
                "Negative quantity accepted in cart item update",
                "Critical",
                "P1 - High",
                "1. Add ITEM-101 to cart. 2. Send quantity update of -5. 3. Observe validation.",
                "System must reject negative quantity (-5) with validation error.",
                "Cart accepted quantity -5 without error; corrupted cart subtotal.",
                "Open"
            ));
        }

        // Test 2: Wishlist Duplicate Addition
        app.addToWishlist("PROD-88");
        int wishCount = app.addToWishlist("PROD-88");
        if (wishCount == 2) {
            defects.add(new DefectModel(
                "DEF-02",
                "Wishlist Management",
                "Wishlist allows duplicate entries of the same product",
                "Major",
                "P2 - Medium",
                "1. Open product PROD-88. 2. Click 'Add to Wishlist'. 3. Click 'Add to Wishlist' again.",
                "Wishlist should maintain distinct unique items; duplicate clicks ignored or prompt user.",
                "Product PROD-88 added twice; total wishlist count increased to 2.",
                "Open"
            ));
        }

        // Test 3: Carrier Tracking Webhook Update
        String trackingStatus = app.getTrackingStatus("ORD-9021", true);
        if (!"Delivered".equalsIgnoreCase(trackingStatus)) {
            defects.add(new DefectModel(
                "DEF-03",
                "Order Tracking",
                "Carrier tracking webhook does not update order status to Delivered",
                "Major",
                "P2 - Medium",
                "1. Place order ORD-9021. 2. Emit simulated carrier delivery webhook. 3. Query tracking status.",
                "Order status should update to 'Delivered'.",
                "Order status remains stuck in 'Processing' despite carrier delivery signal.",
                "Open"
            ));
        }

        // Test 4: CVV plain text leak
        String cvvHtml = app.renderCvvField("892");
        if (cvvHtml.contains("892")) {
            defects.add(new DefectModel(
                "DEF-04",
                "Payment Processing",
                "PCI-DSS Security Breach: Plain CVV leaked in DOM data attribute",
                "Critical",
                "P1 - High",
                "1. Navigate to payment form. 2. Enter CVV '892'. 3. Inspect DOM element.",
                "CVV must be masked in UI and strictly purged from DOM attributes.",
                "Raw CVV '892' exposed in data-cvv-raw attribute, accessible by client scripts.",
                "Open"
            ));
        }

        // Test 5: String sorting of prices
        List<String> rawPrices = Arrays.asList("$100.00", "$20.00", "$5.00");
        List<String> sortedPrices = app.sortPricesLowToHigh(rawPrices);
        List<String> expectedPrices = Arrays.asList("$5.00", "$20.00", "$100.00");
        if (!sortedPrices.equals(expectedPrices)) {
            defects.add(new DefectModel(
                "DEF-05",
                "Product Browsing",
                "Price sort Low-to-High sorts lexicographically instead of numerically",
                "Moderate",
                "P3 - Low",
                "1. Open catalog. 2. Choose 'Price: Low to High' on items priced $100.00, $20.00, $5.00.",
                "Sort order should be $5.00, $20.00, $100.00.",
                "Sort order was " + sortedPrices + " due to ASCII string comparison.",
                "Open"
            ));
        }

        // Test 6: Double Charge on Rapid Click
        int charges = app.executePayment(true);
        if (charges > 1) {
            defects.add(new DefectModel(
                "DEF-06",
                "Order Placement",
                "Payment gateway triggers duplicate charges on rapid double-click",
                "Critical",
                "P1 - High",
                "1. Proceed to payment page. 2. Rapidly double-click 'Pay Now'.",
                "Button should debounce; payment gateway must execute single charge using idempotent key.",
                "Two distinct charges recorded on customer card (charge count = 2).",
                "Open"
            ));
        }

        return defects;
    }

    public void printDefectSummaryReport() {
        List<DefectModel> defects = executeTestsAndCollectDefects();

        System.out.println("==========================================================================================");
        System.out.println("                         E-COMMERCE APPLICATION DEFECT SUMMARY REPORT                     ");
        System.out.println("==========================================================================================");
        System.out.println("Test Execution Scope: User Auth, Catalog, Cart, Wishlist, Order Tracking, Payment");
        System.out.println("Total Test Scenarios Executed: 6");
        System.out.println("Total Defects Identified: " + defects.size());
        System.out.println("------------------------------------------------------------------------------------------");

        Map<String, Integer> severityCount = new LinkedHashMap<>();
        severityCount.put("Critical", 0);
        severityCount.put("Major", 0);
        severityCount.put("Moderate", 0);
        severityCount.put("Minor", 0);

        Map<String, Integer> priorityCount = new LinkedHashMap<>();
        priorityCount.put("P1 - High", 0);
        priorityCount.put("P2 - Medium", 0);
        priorityCount.put("P3 - Low", 0);

        for (DefectModel d : defects) {
            severityCount.put(d.getSeverity(), severityCount.getOrDefault(d.getSeverity(), 0) + 1);
            priorityCount.put(d.getPriority(), priorityCount.getOrDefault(d.getPriority(), 0) + 1);
        }

        System.out.println("\n[SEVERITY DISTRIBUTION MATRIX]");
        severityCount.forEach((k, v) -> System.out.printf("  %-15s : %d defects\n", k, v));

        System.out.println("\n[PRIORITY DISTRIBUTION MATRIX]");
        priorityCount.forEach((k, v) -> System.out.printf("  %-15s : %d defects\n", k, v));

        System.out.println("\n[DETAILED DEFECT LOG TABLE]");
        System.out.printf("%-8s | %-18s | %-10s | %-10s | %-35s\n", "DEF ID", "MODULE", "SEVERITY", "PRIORITY", "DEFECT SUMMARY");
        System.out.println("------------------------------------------------------------------------------------------");
        for (DefectModel d : defects) {
            System.out.printf("%-8s | %-18s | %-10s | %-10s | %-35s\n",
                    d.getDefectId(), d.getModule(), d.getSeverity(), d.getPriority(), d.getTitle());
        }
        System.out.println("==========================================================================================");
    }

    public static void main(String[] args) {
        DefectSummaryReporter reporter = new DefectSummaryReporter();
        reporter.printDefectSummaryReport();
    }
}
```

## Sample Output
```text
==========================================================================================
                         E-COMMERCE APPLICATION DEFECT SUMMARY REPORT                     
==========================================================================================
Test Execution Scope: User Auth, Catalog, Cart, Wishlist, Order Tracking, Payment
Total Test Scenarios Executed: 6
Total Defects Identified: 6
------------------------------------------------------------------------------------------

[SEVERITY DISTRIBUTION MATRIX]
  Critical        : 3 defects
  Major           : 2 defects
  Moderate        : 1 defects
  Minor           : 0 defects

[PRIORITY DISTRIBUTION MATRIX]
  P1 - High       : 3 defects
  P2 - Medium     : 2 defects
  P3 - Low        : 1 defects

[DETAILED DEFECT LOG TABLE]
DEF ID   | MODULE             | SEVERITY   | PRIORITY   | DEFECT SUMMARY                     
------------------------------------------------------------------------------------------
DEF-01   | Cart Management    | Critical   | P1 - High  | Negative quantity accepted in cart item update
DEF-02   | Wishlist Management | Major      | P2 - Medium | Wishlist allows duplicate entries of the same product
DEF-03   | Order Tracking     | Major      | P2 - Medium | Carrier tracking webhook does not update order status to Delivered
DEF-04   | Payment Processing | Critical   | P1 - High  | PCI-DSS Security Breach: Plain CVV leaked in DOM data attribute
DEF-05   | Product Browsing   | Moderate   | P3 - Low   | Price sort Low-to-High sorts lexicographically instead of numerically
DEF-06   | Order Placement    | Critical   | P1 - High  | Payment gateway triggers duplicate charges on rapid double-click
==========================================================================================
```

