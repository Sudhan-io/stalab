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

