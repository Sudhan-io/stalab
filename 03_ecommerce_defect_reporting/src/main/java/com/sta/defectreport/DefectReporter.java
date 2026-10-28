package com.sta.defectreport;

import java.util.*;

public class DefectReporter {

    public static List<Defect> runTestsAndDetectDefects() {
        List<Defect> defects = new ArrayList<>();
        BuggyEcommerceApp app = new BuggyEcommerceApp();

        // Test 1: Order on failed payment
        String orderRes = app.checkout("DECLINED", "iPhone 15", 1);
        if (orderRes.contains("SUCCESS")) {
            defects.add(new Defect(
                "DEF_ECOM_001",
                "Order confirmed despite payment gateway decline",
                "1. Add item to cart. 2. Proceed to payment. 3. Enter declined card. 4. Click 'Pay Now'.",
                "Critical",
                "P1",
                "System must display 'Payment Declined' and reject order creation",
                "Order ID generated and marked 'Confirmed' despite payment status DECLINED",
                "OPEN"
            ));
        }

        // Test 2: Double click on out-of-stock item
        boolean addedOutOfStock = app.addToCart("Vintage Camera", 0, true);
        if (addedOutOfStock) {
            defects.add(new Defect(
                "DEF_ECOM_002",
                "Out-of-Stock product added to cart via double click",
                "1. Navigate to 0 stock item. 2. Rapidly double-click disabled 'Add to Cart'. 3. Inspect cart.",
                "Critical",
                "P1",
                "Add to cart disabled; Cart counter remains 0",
                "Cart accepted item despite zero inventory",
                "OPEN"
            ));
        }

        // Test 3: Discount calculation rounding error
        double discounted = app.applyDiscount(99.99, 15.0);
        // Correct is 99.99 * 0.85 = 84.9915 -> rounded to 84.99
        if (Math.abs(discounted - 84.99) > 0.001) {
            defects.add(new Defect(
                "DEF_ECOM_003",
                "Cart subtotal roundoff error under 15% coupon",
                "1. Add $99.99 item. 2. Apply coupon 'SAVE15'. 3. Verify discounted subtotal.",
                "Major",
                "P2",
                "Subtotal calculates as $84.99 (half-up rounding)",
                "Subtotal truncated to $" + String.format("%.2f", discounted),
                "OPEN"
            ));
        }

        // Test 4: Cleartext password leakage in header
        String header = app.getRegistrationHeader("Secret#2026");
        if (header.contains("Secret#2026")) {
            defects.add(new Defect(
                "DEF_ECOM_004",
                "User registration leaks cleartext password in response header",
                "1. Submit valid registration. 2. Inspect HTTP network response payload and headers.",
                "Moderate",
                "P3",
                "Credentials must never appear in response headers",
                "Password exposed in header: " + header,
                "OPEN"
            ));
        }

        // Test 5: Pagination on 900px screen
        boolean paginationVisible = app.isPaginationVisible(900);
        if (!paginationVisible) {
            defects.add(new Defect(
                "DEF_ECOM_005",
                "Search results pagination truncated on viewport < 1024px",
                "1. Search 'laptop'. 2. Resize browser to 900px width. 3. Scroll to pagination.",
                "Minor",
                "P4",
                "Pagination buttons visible and accessible",
                "Pagination wrapped off-screen and inaccessible without scroll",
                "OPEN"
            ));
        }

        return defects;
    }

    public static void main(String[] args) {
        System.out.println("==========================================================================================");
        System.out.println(" STA LAB - EXPERIMENT 3: DEFECT IDENTIFICATION AND FORMAL REPORT GENERATOR");
        System.out.println("==========================================================================================");

        List<Defect> defects = runTestsAndDetectDefects();

        long criticalCount = defects.stream().filter(d -> "Critical".equalsIgnoreCase(d.getSeverity())).count();
        long majorCount = defects.stream().filter(d -> "Major".equalsIgnoreCase(d.getSeverity())).count();
        long moderateCount = defects.stream().filter(d -> "Moderate".equalsIgnoreCase(d.getSeverity())).count();
        long minorCount = defects.stream().filter(d -> "Minor".equalsIgnoreCase(d.getSeverity())).count();

        System.out.println("--- DEFECT SUMMARY METRICS ---");
        System.out.printf("Total Defects Identified: %d%n", defects.size());
        System.out.printf("  * Critical (P1) : %d%n", criticalCount);
        System.out.printf("  * Major (P2)    : %d%n", majorCount);
        System.out.printf("  * Moderate (P3) : %d%n", moderateCount);
        System.out.printf("  * Minor (P4)    : %d%n", minorCount);
        System.out.println();

        System.out.println("--- DETAILED DEFECT LOG ---");
        for (Defect d : defects) {
            System.out.println("------------------------------------------------------------------------------------------");
            System.out.printf("Defect ID  : %s | Severity: %s | Priority: %s | Status: %s%n",
                d.getDefectId(), d.getSeverity(), d.getPriority(), d.getStatus());
            System.out.println("Description: " + d.getDescription());
            System.out.println("Steps      : " + d.getStepsToReproduce());
            System.out.println("Expected   : " + d.getExpectedResult());
            System.out.println("Actual     : " + d.getActualResult());
        }
        System.out.println("==========================================================================================");
    }
}

