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
