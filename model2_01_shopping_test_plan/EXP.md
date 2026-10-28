# Model 2 - Experiment 1: Online Shopping Application Test Plan

> **Lab Exam Coverage:** Model 2 (Question 1)

## Aim
To prepare a comprehensive test plan for an online shopping application covering product browsing, user authentication, wishlist management, order placement, payment processing, and order tracking according to IEEE 829 standards, and evaluate quality gates programmatically.

---

## Algorithm / Procedure

1. **Define Test Objectives and Scope:**
   - Establish testing goals across the end-to-end shopping customer journey.
   - Specify in-scope modules: Product Browsing, User Authentication, Wishlist Management, Order Placement, Payment Processing, and Real-Time Order Tracking.
2. **Specify Test Environment Architecture:**
   - Document cross-browser matrices (Chrome, Edge, Firefox on Windows/macOS), mobile device profiles (Android 14, iOS 17.4), and microservice staging environments.
3. **Establish QA Resource Roster:**
   - Allocate roles: QA Lead, Automation Engineers, Functional Testers, and Performance/Security Engineers.
4. **Formulate 35-Day Phased Schedule:**
   - Map timeline milestones: Requirements Analysis, Test Design, Execution Cycles, Order Tracking/Payment Integration, and Sign-off.
5. **Establish Entry and Exit Criteria Quality Gates:**
   - Entry Criteria: Baselined SRS, active payment sandbox, 100% smoke test pass, catalog data seeded.
   - Exit Criteria: 100% test execution, $\ge 98\%$ pass rate, 0 open P1/P2 defects, payment SLA $< 1.5\text{s}$, zero security vulnerabilities.
6. **Programmatic Quality Gate Evaluation:**
   - Implement data models and gate validators to verify compliance before production release.

---

## Main Java Program

### File: `src/main/java/com/sta/testplan/ShoppingTestPlanApp.java`
```java
package com.sta.testplan;

public class ShoppingTestPlanApp {

    public static void main(String[] args) {
        System.out.println("===============================================================================");
        System.out.println(" MODEL 2 - EXPERIMENT 1: ONLINE SHOPPING TEST PLAN & QUALITY GATE AUDIT");
        System.out.println("===============================================================================");

        ShoppingTestPlanModel plan = ShoppingTestPlanModel.createStandardModel2Plan();
        ShoppingTestPlanValidator validator = new ShoppingTestPlanValidator();

        System.out.println("Plan Identifier   : " + plan.getPlanId());
        System.out.println("Application Name  : " + plan.getApplicationName());
        System.out.println("Total Timeline    : " + validator.calculateTotalScheduleDays(plan) + " Business Days");

        System.out.println("\n--- 1. IN-SCOPE SHOPPING MODULES ---");
        for (int i = 0; i < plan.getInScopeModules().size(); i++) {
            System.out.printf("  %d. %s%n", (i + 1), plan.getInScopeModules().get(i));
        }

        System.out.println("\n--- 2. TEST ENVIRONMENT CONFIGURATION ---");
        plan.getTestEnvironments().forEach((env, desc) ->
            System.out.printf("  %-25s: %s%n", env, desc)
        );

        System.out.println("\n--- 3. QA ROLES & RESOURCE MATRIX ---");
        plan.getResourceRoster().forEach((role, desc) ->
            System.out.printf("  %-25s: %s%n", role, desc)
        );

        System.out.println("\n--- 4. SCHEDULE & MILESTONES ---");
        for (ShoppingTestPlanModel.SchedulePhase phase : plan.getSchedule()) {
            System.out.printf("  [%s] %-55s (%2d days)%n",
                phase.isCompleted() ? "COMPLETED" : "IN PROGRESS",
                phase.getPhaseName(), phase.getDurationDays());
        }

        System.out.println("\n--- 5. ENTRY CRITERIA QUALITY GATE CHECK ---");
        ShoppingTestPlanValidator.GateResult entryRes = validator.evaluateEntryCriteria(plan);
        System.out.printf("  Gate Status: %s (%d/%d satisfied)%n",
            entryRes.isPassed() ? "READY TO COMMENCE TESTING" : "BLOCKED",
            entryRes.getSatisfiedGates(), entryRes.getTotalGates());
        for (ShoppingTestPlanModel.QualityGate gate : plan.getEntryCriteria()) {
            System.out.printf("   * [%s] %s -> %s%n",
                gate.isSatisfied() ? "PASS" : "FAIL", gate.getCriterion(), gate.getEvidence());
        }

        System.out.println("\n--- 6. EXIT / SIGN-OFF CRITERIA QUALITY GATE CHECK ---");
        ShoppingTestPlanValidator.GateResult exitRes = validator.evaluateExitCriteria(plan);
        System.out.printf("  Gate Status: %s (%d/%d satisfied)%n",
            exitRes.isPassed() ? "RELEASE APPROVED FOR PRODUCTION" : "SIGN-OFF BLOCKED",
            exitRes.getSatisfiedGates(), exitRes.getTotalGates());
        for (ShoppingTestPlanModel.QualityGate gate : plan.getExitCriteria()) {
            System.out.printf("   * [%s] %s -> %s%n",
                gate.isSatisfied() ? "PASS" : "FAIL", gate.getCriterion(), gate.getEvidence());
        }

        System.out.println("\n===============================================================================");
        System.out.println(" MODEL 2 TEST PLAN VERIFICATION & AUDIT: SUCCESSFUL");
        System.out.println("===============================================================================");
    }
}
```

---

## Sample Output

```text
===============================================================================
 MODEL 2 - EXPERIMENT 1: ONLINE SHOPPING TEST PLAN & QUALITY GATE AUDIT
===============================================================================
Plan Identifier   : TP-M2-SHOP-2026
Application Name  : Omnichannel Online Shopping & Order Tracking Platform
Total Timeline    : 35 Business Days

--- 1. IN-SCOPE SHOPPING MODULES ---
  1. Product Browsing & Faceted Filtering
  2. User Authentication (Sign-up, Sign-in, JWT Session, 2FA)
  3. Wishlist Management (Add, Remove, Move to Cart, Privacy)
  4. Order Placement & Delivery Address Allocation
  5. Payment Processing (Credit/Debit, UPI, Wallets, Webhooks)
  6. Real-Time Order Tracking & Milestone Shipment Updates

--- 2. TEST ENVIRONMENT CONFIGURATION ---
  Web Staging Desktop      : Chrome 124+, Edge 124+, Firefox 125+ on Windows 11 & macOS Sonoma
  Mobile Testing Matrix    : Android 14 (Samsung S24, Pixel 8), iOS 17.4 (iPhone 15 Pro)
  Microservices Staging    : Kubernetes EKS Cluster, Redis Cache, Postgres Aurora DB
  Network Profiles         : Broadband Fiber, 4G LTE, 3G Throttled (1.5 Mbps) for latency resilience

--- 3. QA ROLES & RESOURCE MATRIX ---
  Test Lead / QA Manager   : 1 Lead (Sign-off governance, schedule tracking, defect triage)
  Automation QA Engineers  : 2 Engineers (Selenium WebDriver, TestNG, POM, Cucumber BDD)
  Functional & Usability QA: 2 Engineers (Wishlist, Payment, Order Tracking edge cases)
  Performance & Security QA: 1 Engineer (JMeter concurrency, OWASP ZAP security audits)

--- 4. SCHEDULE & MILESTONES ---
  [COMPLETED] Scope Analysis & Test Strategy Design                   ( 4 days)
  [COMPLETED] Test Case Design & Test Data Provisioning               ( 6 days)
  [COMPLETED] Environment Readiness & Smoke Suite Gate Check          ( 2 days)
  [COMPLETED] Functional, Wishlist & Payment Integration Testing      (10 days)
  [COMPLETED] Order Tracking & Payment Webhook End-to-End Run         ( 6 days)
  [COMPLETED] Performance, Stress & Security Audits                   ( 4 days)
  [COMPLETED] Regression Verification & Production Sign-off           ( 3 days)

--- 5. ENTRY CRITERIA QUALITY GATE CHECK ---
  Gate Status: READY TO COMMENCE TESTING (4/4 satisfied)
   * [PASS] Wishlist & Order Tracking SRS baselined -> Approved by Product Owner
   * [PASS] Staging environment with simulated payment gateway active -> Razorpay/Stripe sandbox ready
   * [PASS] 100% Smoke tests passed on latest release candidate -> 15/15 smoke tests passing
   * [PASS] Test catalog & user accounts provisioned -> 5,000 products & 50 test users seeded

--- 6. EXIT / SIGN-OFF CRITERIA QUALITY GATE CHECK ---
  Gate Status: RELEASE APPROVED FOR PRODUCTION (5/5 satisfied)
   * [PASS] 100% of planned shopping journey test cases executed -> 180/180 cases executed
   * [PASS] Overall test pass rate >= 98% -> 98.9% pass rate achieved
   * [PASS] Zero open Critical (P1) or Major (P2) defects -> 0 P1/P2 remaining in backlog
   * [PASS] Payment processing & order tracking SLA < 1.5s -> p95 response time 1.18s
   * [PASS] Zero high/critical security vulnerabilities -> OWASP ZAP scan clean

===============================================================================
 MODEL 2 TEST PLAN VERIFICATION & AUDIT: SUCCESSFUL
===============================================================================
```

