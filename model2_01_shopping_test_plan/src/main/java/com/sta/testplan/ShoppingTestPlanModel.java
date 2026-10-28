package com.sta.testplan;

import java.util.*;

public class ShoppingTestPlanModel {

    public static class QualityGate {
        private final String criterion;
        private final boolean satisfied;
        private final String evidence;

        public QualityGate(String criterion, boolean satisfied, String evidence) {
            this.criterion = criterion;
            this.satisfied = satisfied;
            this.evidence = evidence;
        }

        public String getCriterion() { return criterion; }
        public boolean isSatisfied() { return satisfied; }
        public String getEvidence() { return evidence; }
    }

    public static class SchedulePhase {
        private final String phaseName;
        private final int durationDays;
        private final boolean completed;

        public SchedulePhase(String phaseName, int durationDays, boolean completed) {
            this.phaseName = phaseName;
            this.durationDays = durationDays;
            this.completed = completed;
        }

        public String getPhaseName() { return phaseName; }
        public int getDurationDays() { return durationDays; }
        public boolean isCompleted() { return completed; }
    }

    private final String planId;
    private final String applicationName;
    private final List<String> inScopeModules;
    private final Map<String, String> testEnvironments;
    private final Map<String, String> resourceRoster;
    private final List<SchedulePhase> schedule;
    private final List<QualityGate> entryCriteria;
    private final List<QualityGate> exitCriteria;

    public ShoppingTestPlanModel(String planId, String applicationName,
                                 List<String> inScopeModules,
                                 Map<String, String> testEnvironments,
                                 Map<String, String> resourceRoster,
                                 List<SchedulePhase> schedule,
                                 List<QualityGate> entryCriteria,
                                 List<QualityGate> exitCriteria) {
        this.planId = planId;
        this.applicationName = applicationName;
        this.inScopeModules = inScopeModules;
        this.testEnvironments = testEnvironments;
        this.resourceRoster = resourceRoster;
        this.schedule = schedule;
        this.entryCriteria = entryCriteria;
        this.exitCriteria = exitCriteria;
    }

    public String getPlanId() { return planId; }
    public String getApplicationName() { return applicationName; }
    public List<String> getInScopeModules() { return inScopeModules; }
    public Map<String, String> getTestEnvironments() { return testEnvironments; }
    public Map<String, String> getResourceRoster() { return resourceRoster; }
    public List<SchedulePhase> getSchedule() { return schedule; }
    public List<QualityGate> getEntryCriteria() { return entryCriteria; }
    public List<QualityGate> getExitCriteria() { return exitCriteria; }

    public static ShoppingTestPlanModel createStandardModel2Plan() {
        List<String> modules = List.of(
            "Product Browsing & Faceted Filtering",
            "User Authentication (Sign-up, Sign-in, JWT Session, 2FA)",
            "Wishlist Management (Add, Remove, Move to Cart, Privacy)",
            "Order Placement & Delivery Address Allocation",
            "Payment Processing (Credit/Debit, UPI, Wallets, Webhooks)",
            "Real-Time Order Tracking & Milestone Shipment Updates"
        );

        Map<String, String> envs = new LinkedHashMap<>();
        envs.put("Web Staging Desktop", "Chrome 124+, Edge 124+, Firefox 125+ on Windows 11 & macOS Sonoma");
        envs.put("Mobile Testing Matrix", "Android 14 (Samsung S24, Pixel 8), iOS 17.4 (iPhone 15 Pro)");
        envs.put("Microservices Staging", "Kubernetes EKS Cluster, Redis Cache, Postgres Aurora DB");
        envs.put("Network Profiles", "Broadband Fiber, 4G LTE, 3G Throttled (1.5 Mbps) for latency resilience");

        Map<String, String> resources = new LinkedHashMap<>();
        resources.put("Test Lead / QA Manager", "1 Lead (Sign-off governance, schedule tracking, defect triage)");
        resources.put("Automation QA Engineers", "2 Engineers (Selenium WebDriver, TestNG, POM, Cucumber BDD)");
        resources.put("Functional & Usability QA", "2 Engineers (Wishlist, Payment, Order Tracking edge cases)");
        resources.put("Performance & Security QA", "1 Engineer (JMeter concurrency, OWASP ZAP security audits)");

        List<SchedulePhase> phases = List.of(
            new SchedulePhase("Scope Analysis & Test Strategy Design", 4, true),
            new SchedulePhase("Test Case Design & Test Data Provisioning", 6, true),
            new SchedulePhase("Environment Readiness & Smoke Suite Gate Check", 2, true),
            new SchedulePhase("Functional, Wishlist & Payment Integration Testing", 10, true),
            new SchedulePhase("Order Tracking & Payment Webhook End-to-End Run", 6, true),
            new SchedulePhase("Performance, Stress & Security Audits", 4, true),
            new SchedulePhase("Regression Verification & Production Sign-off", 3, true)
        );

        List<QualityGate> entryGates = List.of(
            new QualityGate("Wishlist & Order Tracking SRS baselined", true, "Approved by Product Owner"),
            new QualityGate("Staging environment with simulated payment gateway active", true, "Razorpay/Stripe sandbox ready"),
            new QualityGate("100% Smoke tests passed on latest release candidate", true, "15/15 smoke tests passing"),
            new QualityGate("Test catalog & user accounts provisioned", true, "5,000 products & 50 test users seeded")
        );

        List<QualityGate> exitGates = List.of(
            new QualityGate("100% of planned shopping journey test cases executed", true, "180/180 cases executed"),
            new QualityGate("Overall test pass rate >= 98%", true, "98.9% pass rate achieved"),
            new QualityGate("Zero open Critical (P1) or Major (P2) defects", true, "0 P1/P2 remaining in backlog"),
            new QualityGate("Payment processing & order tracking SLA < 1.5s", true, "p95 response time 1.18s"),
            new QualityGate("Zero high/critical security vulnerabilities", true, "OWASP ZAP scan clean")
        );

        return new ShoppingTestPlanModel(
            "TP-M2-SHOP-2026",
            "Omnichannel Online Shopping & Order Tracking Platform",
            modules, envs, resources, phases, entryGates, exitGates
        );
    }
}
