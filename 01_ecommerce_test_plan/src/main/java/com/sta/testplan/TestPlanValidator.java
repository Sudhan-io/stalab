package com.sta.testplan;

import java.util.List;

/**
 * Validates whether test execution satisfies Entry, Suspension, and Exit criteria.
 */
public class TestPlanValidator {

    public static class GateEvaluationResult {
        private final boolean passed;
        private final int totalGates;
        private final int satisfiedGates;
        private final List<String> blockingIssues;

        public GateEvaluationResult(boolean passed, int totalGates, int satisfiedGates, List<String> blockingIssues) {
            this.passed = passed;
            this.totalGates = totalGates;
            this.satisfiedGates = satisfiedGates;
            this.blockingIssues = blockingIssues;
        }

        public boolean isPassed() { return passed; }
        public int getTotalGates() { return totalGates; }
        public int getSatisfiedGates() { return satisfiedGates; }
        public List<String> getBlockingIssues() { return blockingIssues; }
    }

    public GateEvaluationResult evaluateEntryCriteria(EcommerceTestPlanModel plan) {
        java.util.List<String> blockers = new java.util.ArrayList<>();
        int satisfied = 0;
        for (EcommerceTestPlanModel.QualityGate gate : plan.getEntryCriteria()) {
            if (gate.isSatisfied()) {
                satisfied++;
            } else {
                blockers.add("ENTRY BLOCKER: " + gate.getCriterion() + " (" + gate.getNotes() + ")");
            }
        }
        boolean passed = blockers.isEmpty();
        return new GateEvaluationResult(passed, plan.getEntryCriteria().size(), satisfied, blockers);
    }

    public GateEvaluationResult evaluateExitCriteria(EcommerceTestPlanModel plan) {
        java.util.List<String> blockers = new java.util.ArrayList<>();
        int satisfied = 0;
        for (EcommerceTestPlanModel.QualityGate gate : plan.getExitCriteria()) {
            if (gate.isSatisfied()) {
                satisfied++;
            } else {
                blockers.add("EXIT BLOCKER: " + gate.getCriterion() + " (" + gate.getNotes() + ")");
            }
        }
        boolean passed = blockers.isEmpty();
        return new GateEvaluationResult(passed, plan.getExitCriteria().size(), satisfied, blockers);
    }

    public int calculateTotalScheduleDays(EcommerceTestPlanModel plan) {
        return plan.getSchedule().stream().mapToInt(EcommerceTestPlanModel.Milestone::getDurationDays).sum();
    }
}

