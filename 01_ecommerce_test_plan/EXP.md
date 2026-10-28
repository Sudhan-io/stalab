# Experiment 1: E-Commerce Test Plan Specification & Validation
> **Lab Exam Coverage:** Applicable for **Model 1 (Question 1)** and **Model 2 (Question 1)**.

## Aim
To develop a comprehensive test plan for testing an e-commerce web or mobile application (such as Amazon) according to IEEE 829 standards, specifying the testing objectives, scope, test environment, resources, test strategy, schedule, entry criteria, and exit criteria, and validating quality gates programmatically.

---

## Algorithm / Procedure

1. **Define Test Objectives and Scope:**
   - Identify business and testing objectives for multi-channel e-commerce systems.
   - Categorize application features into in-scope modules (Authentication, Search, Cart, Checkout, Payments, Order Management) and out-of-scope items (third-party payment switch internals, warehouse ERP).
2. **Specify Test Environments:**
   - Document cross-browser matrices (Chrome, Firefox, Edge on Windows/macOS), mobile device targets (Android, iOS), and backend staging clusters.
   - Configure network bandwidth profiles (4G, 5G, 3G Throttled).
3. **Allocate QA Resources & Roles:**
   - Map roles: QA Lead, Automation Engineers, Functional Testers, Performance & Security Engineers.
4. **Formulate Schedule and Milestones:**
   - Define phased test lifecycle across Requirements Analysis, Test Design, Execution Cycles, and Final Sign-Off (30-day timeline).
5. **Establish Entry, Suspension, and Exit Criteria:**
   - Entry Criteria: Baselined SRS, operational staging cluster, 100% smoke test pass, seeded test data.
   - Exit Criteria: 100% test execution, $\ge 98\%$ pass rate, 0 open P1/P2 defects, performance SLA met ($< 2.0\text{s}$ $p_{95}$ latency).
6. **Programmatic Quality Gate Verification:**
   - Implement Java data model and validation methods to evaluate entry readiness and production release exit gates.
   - Output structured quality gate audit results.

---

## Main Java Program

### File: `src/main/java/com/sta/testplan/TestPlanApp.java`
```java
package com.sta.testplan;

public class TestPlanApp {
    public static void main(String[] args) {
        System.out.println("===============================================================================");
        System.out.println(" STA LAB - EXPERIMENT 1: E-COMMERCE TEST PLAN RUNNER & GATE VALIDATOR");
        System.out.println("===============================================================================");

        EcommerceTestPlanModel plan = EcommerceTestPlanModel.createStandardPlan();
        TestPlanValidator validator = new TestPlanValidator();

        System.out.println("Plan ID       : " + plan.getPlanId());
        System.out.println("System Under Test: " + plan.getSystemName());
        System.out.println("Total Schedule: " + validator.calculateTotalScheduleDays(plan) + " Days");
        System.out.println();

        System.out.println("--- 1. IN-SCOPE MODULES ---");
        for (int i = 0; i < plan.getInScopeModules().size(); i++) {
            System.out.println("  " + (i + 1) + ". " + plan.getInScopeModules().get(i));
        }

        System.out.println("\n--- 2. TEST ENVIRONMENTS ---");
        plan.getTestEnvironments().forEach((env, desc) ->
            System.out.printf("  %-18s: %s%n", env, desc)
        );

        System.out.println("\n--- 3. RESOURCE ALLOCATION ---");
        plan.getResourceRoster().forEach((role, desc) ->
            System.out.printf("  %-25s: %s%n", role, desc)
        );

        System.out.println("\n--- 4. SCHEDULE & MILESTONES ---");
        for (EcommerceTestPlanModel.Milestone m : plan.getSchedule()) {
            System.out.printf("  [%s] %-48s (%2d days)%n",
                m.isCompleted() ? "COMPLETED" : "PENDING  ", m.getPhase(), m.getDurationDays());
        }

        System.out.println("\n--- 5. ENTRY CRITERIA EVALUATION ---");
        TestPlanValidator.GateEvaluationResult entryResult = validator.evaluateEntryCriteria(plan);
        System.out.printf("  Status: %s (%d/%d criteria satisfied)%n",
            entryResult.isPassed() ? "READY TO COMMENCE TESTING" : "BLOCKED",
            entryResult.getSatisfiedGates(), entryResult.getTotalGates());
        for (EcommerceTestPlanModel.QualityGate gate : plan.getEntryCriteria()) {
            System.out.printf("   * [%s] %s -> %s%n", gate.isSatisfied() ? "PASS" : "FAIL", gate.getCriterion(), gate.getNotes());
        }

        System.out.println("\n--- 6. EXIT / SIGN-OFF CRITERIA EVALUATION ---");
        TestPlanValidator.GateEvaluationResult exitResult = validator.evaluateExitCriteria(plan);
        System.out.printf("  Status: %s (%d/%d criteria satisfied)%n",
            exitResult.isPassed() ? "READY FOR PRODUCTION RELEASE SIGN-OFF" : "SIGN-OFF BLOCKED",
            exitResult.getSatisfiedGates(), exitResult.getTotalGates());
        for (EcommerceTestPlanModel.QualityGate gate : plan.getExitCriteria()) {
            System.out.printf("   * [%s] %s -> %s%n", gate.isSatisfied() ? "PASS" : "FAIL", gate.getCriterion(), gate.getNotes());
        }

        System.out.println("\n===============================================================================");
        System.out.println(" TEST PLAN VERIFICATION & GATE AUDIT: SUCCESSFUL");
        System.out.println("===============================================================================");
    }
}
```

---

## Sample Output

```text
===============================================================================
 STA LAB - EXPERIMENT 1: E-COMMERCE TEST PLAN RUNNER & GATE VALIDATOR
===============================================================================
Plan ID       : TP-ECOM-2026-V1.0
System Under Test: Amazon-Like Multi-Channel E-Commerce Application
Total Schedule: 30 Days

--- 1. IN-SCOPE MODULES ---
  1. User Registration & Authentication (OTP, SSO)
  2. Product Catalog, Search & Faceted Filtering
  3. Product Detail View & Stock Status
  4. Cart Management & Promo Code Calculations
  5. Checkout Delivery Address & Time Slot
  6. Payment Gateway Integration (Cards, UPI, COD)
  7. Order Confirmation, Invoicing & Notifications

--- 2. TEST ENVIRONMENTS ---
  Web Desktop       : Chrome 122+, Edge 122+, Firefox 123+ on Windows 11 & macOS
  Mobile Handsets   : Android 14 (Pixel/Samsung), iOS 17 (iPhone 14/15)
  Staging Server    : AWS ECS Container Cluster with Aurora MySQL & Redis
  Network Emulation : 4G LTE, 5G, and 3G Throttled (1.5 Mbps)

--- 3. RESOURCE ALLOCATION ---
  QA Lead / Test Manager   : 1 Lead (Test planning, sign-off governance)
  Automation Engineers     : 2 Engineers (Selenium, TestNG, POM, Cucumber)
  Functional / Manual QA   : 2 Engineers (Test execution, exploratory, defect triage)
  Performance & Security QA: 1 Engineer (JMeter, load testing, OWASP scanning)

--- 4. SCHEDULE & MILESTONES ---
  [COMPLETED] Requirements Analysis & Test Plan Draft          ( 3 days)
  [COMPLETED] Test Case Design & Test Data Generation          ( 5 days)
  [COMPLETED] Environment Setup & Smoke Test Verification      ( 2 days)
  [COMPLETED] Functional & Integration Cycle 1 Execution       ( 8 days)
  [COMPLETED] Defect Verification & Regression Cycle 2         ( 6 days)
  [COMPLETED] Performance & Security Assessment                ( 4 days)
  [COMPLETED] Final Sign-off & Reporting                       ( 2 days)

--- 5. ENTRY CRITERIA EVALUATION ---
  Status: READY TO COMMENCE TESTING (4/4 criteria satisfied)
   * [PASS] SRS and UI Wireframes baselined -> Signed off by Product Owner
   * [PASS] Staging QA environment deployed and operational -> AWS staging healthy
   * [PASS] Smoke testing suite executed 100% pass -> Build verified
   * [PASS] Test catalog data & user credentials populated -> 10k SKUs seeded

--- 6. EXIT / SIGN-OFF CRITERIA EVALUATION ---
  Status: READY FOR PRODUCTION RELEASE SIGN-OFF (5/5 criteria satisfied)
   * [PASS] 100% of planned test cases executed -> All 150 test cases run
   * [PASS] >= 98% overall test pass percentage -> Achieved 98.7% pass rate
   * [PASS] Zero open Critical (P1) or Major (P2) defects -> 0 P1/P2 remaining
   * [PASS] Performance SLA met (< 2.0s 95th percentile latency) -> Achieved 1.42s
   * [PASS] Zero high/critical security vulnerabilities -> Clean OWASP scan

===============================================================================
 TEST PLAN VERIFICATION & GATE AUDIT: SUCCESSFUL
===============================================================================
```
