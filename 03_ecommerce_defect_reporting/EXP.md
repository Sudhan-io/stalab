# Experiment 3: E-Commerce Defect Identification & Formal Reporting

## Aim
To test an e-commerce application by executing designed test cases, identify functional and data-integrity defects, and prepare a formal defect report containing Defect ID, Description, Steps to Reproduce, Severity, Priority, Expected Result, and Actual Result.

---

## Algorithm / Procedure

1. **Establish Defect Classification Criteria:**
   - Define Severity Levels: Critical (system crash, data corruption, revenue loss), Major (feature severely impaired), Moderate (secondary functionality defective), Minor (UI/cosmetic defect).
   - Define Priority Levels: P1 (fix immediately), P2 (fix before next build), P3 (fix in release cycle), P4 (deferrable).
2. **Execute Functional Test Scenarios:**
   - Execute negative payment gateway scenarios (declined cards).
   - Test concurrency and race conditions (rapid double-clicking disabled buttons).
   - Execute boundary calculations (floating-point discount percentages).
   - Inspect network headers for security vulnerabilities (sensitive data leakage).
   - Validate responsive viewport rendering across screen widths.
3. **Capture Defect Details:**
   - For every failure, record:
     - Unique Defect ID (e.g., `DEF_ECOM_001`)
     - Concise Defect Description
     - Step-by-step reproducible steps
     - Severity and Priority classification
     - Expected system behavior
     - Actual system behavior observed
4. **Compile Structured Defect Report:**
   - Aggregate identified defects, summarize severity metrics (P1-P4 counts), and print a formatted defect log for engineering triage.

---

## Main Java Program

### File: `src/main/java/com/sta/defectreport/DefectReporter.java`
```java
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
        System.out.println("Total Defects Identified: " + defects.size());
        System.out.println("  * Critical (P1) : " + criticalCount);
        System.out.println("  * Major (P2)    : " + majorCount);
        System.out.println("  * Moderate (P3) : " + moderateCount);
        System.out.println("  * Minor (P4)    : " + minorCount);

        System.out.println("\n--- DETAILED DEFECT LOG ---");
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
```

---

## Sample Output

```text
==========================================================================================
 STA LAB - EXPERIMENT 3: DEFECT IDENTIFICATION AND FORMAL REPORT GENERATOR
==========================================================================================
--- DEFECT SUMMARY METRICS ---
Total Defects Identified: 5
  * Critical (P1) : 2
  * Major (P2)    : 1
  * Moderate (P3) : 1
  * Minor (P4)    : 1

--- DETAILED DEFECT LOG ---
------------------------------------------------------------------------------------------
Defect ID  : DEF_ECOM_001 | Severity: Critical | Priority: P1 | Status: OPEN
Description: Order confirmed despite payment gateway decline
Steps      : 1. Add item to cart. 2. Proceed to payment. 3. Enter declined card. 4. Click 'Pay Now'.
Expected   : System must display 'Payment Declined' and reject order creation
Actual     : Order ID generated and marked 'Confirmed' despite payment status DECLINED
------------------------------------------------------------------------------------------
Defect ID  : DEF_ECOM_002 | Severity: Critical | Priority: P1 | Status: OPEN
Description: Out-of-Stock product added to cart via double click
Steps      : 1. Navigate to 0 stock item. 2. Rapidly double-click disabled 'Add to Cart'. 3. Inspect cart.
Expected   : Add to cart disabled; Cart counter remains 0
Actual     : Cart accepted item despite zero inventory
------------------------------------------------------------------------------------------
Defect ID  : DEF_ECOM_003 | Severity: Major | Priority: P2 | Status: OPEN
Description: Cart subtotal roundoff error under 15% coupon
Steps      : 1. Add $99.99 item. 2. Apply coupon 'SAVE15'. 3. Verify discounted subtotal.
Expected   : Subtotal calculates as $84.99 (half-up rounding)
Actual     : Subtotal truncated to $84.98
------------------------------------------------------------------------------------------
Defect ID  : DEF_ECOM_004 | Severity: Moderate | Priority: P3 | Status: OPEN
Description: User registration leaks cleartext password in response header
Steps      : 1. Submit valid registration. 2. Inspect HTTP network response payload and headers.
Expected   : Credentials must never appear in response headers
Actual     : Password exposed in header: X-Debug-User-Auth: Secret#2026
------------------------------------------------------------------------------------------
Defect ID  : DEF_ECOM_005 | Severity: Minor | Priority: P4 | Status: OPEN
Description: Search results pagination truncated on viewport < 1024px
Steps      : 1. Search 'laptop'. 2. Resize browser to 900px width. 3. Scroll to pagination.
Expected   : Pagination buttons visible and accessible
Actual     : Pagination wrapped off-screen and inaccessible without scroll
==========================================================================================
```
