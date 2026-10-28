package com.sta.testplan;

import java.util.*;

/**
 * Model representing an IEEE 829 Test Plan for E-Commerce Platforms.
 */
public class EcommerceTestPlanModel {
    private String planId;
    private String systemName;
    private List<String> inScopeModules;
    private List<String> outOfScopeModules;
    private Map<String, String> testEnvironments;
    private Map<String, String> resourceRoster;
    private List<Milestone> schedule;
    private List<QualityGate> entryCriteria;
    private List<QualityGate> exitCriteria;

    public static class Milestone {
        private String phase;
        private int durationDays;
        private boolean completed;

        public Milestone(String phase, int durationDays, boolean completed) {
            this.phase = phase;
            this.durationDays = durationDays;
            this.completed = completed;
        }

        public String getPhase() { return phase; }
        public int getDurationDays() { return durationDays; }
        public boolean isCompleted() { return completed; }
        public void setCompleted(boolean completed) { this.completed = completed; }
    }

    public static class QualityGate {
        private String criterion;
        private boolean satisfied;
        private String notes;

        public QualityGate(String criterion, boolean satisfied, String notes) {
            this.criterion = criterion;
            this.satisfied = satisfied;
            this.notes = notes;
        }

        public String getCriterion() { return criterion; }
        public boolean isSatisfied() { return satisfied; }
        public void setSatisfied(boolean satisfied) { this.satisfied = satisfied; }
        public String getNotes() { return notes; }
    }

    public EcommerceTestPlanModel() {
        this.inScopeModules = new ArrayList<>();
        this.outOfScopeModules = new ArrayList<>();
        this.testEnvironments = new LinkedHashMap<>();
        this.resourceRoster = new LinkedHashMap<>();
        this.schedule = new ArrayList<>();
        this.entryCriteria = new ArrayList<>();
        this.exitCriteria = new ArrayList<>();
    }

    public static EcommerceTestPlanModel createStandardPlan() {
        EcommerceTestPlanModel plan = new EcommerceTestPlanModel();
        plan.planId = "TP-ECOM-2026-V1.0";
        plan.systemName = "Amazon-Like Multi-Channel E-Commerce Application";

        plan.inScopeModules.addAll(Arrays.asList(
            "User Registration & Authentication (OTP, SSO)",
            "Product Catalog, Search & Faceted Filtering",
            "Product Detail View & Stock Status",
            "Cart Management & Promo Code Calculations",
            "Checkout Delivery Address & Time Slot",
            "Payment Gateway Integration (Cards, UPI, COD)",
            "Order Confirmation, Invoicing & Notifications"
        ));

        plan.outOfScopeModules.addAll(Arrays.asList(
            "Internal automated warehouse robotic control systems",
            "Third-party bank settlement batch processor code",
            "Physical logistics sorting conveyor machinery"
        ));

        plan.testEnvironments.put("Web Desktop", "Chrome 122+, Edge 122+, Firefox 123+ on Windows 11 & macOS");
        plan.testEnvironments.put("Mobile Handsets", "Android 14 (Pixel/Samsung), iOS 17 (iPhone 14/15)");
        plan.testEnvironments.put("Staging Server", "AWS ECS Container Cluster with Aurora MySQL & Redis");
        plan.testEnvironments.put("Network Emulation", "4G LTE, 5G, and 3G Throttled (1.5 Mbps)");

        plan.resourceRoster.put("QA Lead / Test Manager", "1 Lead (Test planning, sign-off governance)");
        plan.resourceRoster.put("Automation Engineers", "2 Engineers (Selenium, TestNG, POM, Cucumber)");
        plan.resourceRoster.put("Functional / Manual QA", "2 Engineers (Test execution, exploratory, defect triage)");
        plan.resourceRoster.put("Performance & Security QA", "1 Engineer (JMeter, load testing, OWASP scanning)");

        plan.schedule.add(new Milestone("Requirements Analysis & Test Plan Draft", 3, true));
        plan.schedule.add(new Milestone("Test Case Design & Test Data Generation", 5, true));
        plan.schedule.add(new Milestone("Environment Setup & Smoke Test Verification", 2, true));
        plan.schedule.add(new Milestone("Functional & Integration Cycle 1 Execution", 8, true));
        plan.schedule.add(new Milestone("Defect Verification & Regression Cycle 2", 6, true));
        plan.schedule.add(new Milestone("Performance & Security Assessment", 4, true));
        plan.schedule.add(new Milestone("Final Sign-off & Reporting", 2, true));

        // Entry Criteria
        plan.entryCriteria.add(new QualityGate("SRS and UI Wireframes baselined", true, "Signed off by Product Owner"));
        plan.entryCriteria.add(new QualityGate("Staging QA environment deployed and operational", true, "AWS staging healthy"));
        plan.entryCriteria.add(new QualityGate("Smoke testing suite executed 100% pass", true, "Build verified"));
        plan.entryCriteria.add(new QualityGate("Test catalog data & user credentials populated", true, "10k SKUs seeded"));

        // Exit Criteria
        plan.exitCriteria.add(new QualityGate("100% of planned test cases executed", true, "All 150 test cases run"));
        plan.exitCriteria.add(new QualityGate(">= 98% overall test pass percentage", true, "Achieved 98.7% pass rate"));
        plan.exitCriteria.add(new QualityGate("Zero open Critical (P1) or Major (P2) defects", true, "0 P1/P2 remaining"));
        plan.exitCriteria.add(new QualityGate("Performance SLA met (< 2.0s 95th percentile latency)", true, "Achieved 1.42s"));
        plan.exitCriteria.add(new QualityGate("Zero high/critical security vulnerabilities", true, "Clean OWASP scan"));

        return plan;
    }

    // Getters
    public String getPlanId() { return planId; }
    public String getSystemName() { return systemName; }
    public List<String> getInScopeModules() { return inScopeModules; }
    public List<String> getOutOfScopeModules() { return outOfScopeModules; }
    public Map<String, String> getTestEnvironments() { return testEnvironments; }
    public Map<String, String> getResourceRoster() { return resourceRoster; }
    public List<Milestone> getSchedule() { return schedule; }
    public List<QualityGate> getEntryCriteria() { return entryCriteria; }
    public List<QualityGate> getExitCriteria() { return exitCriteria; }
}

