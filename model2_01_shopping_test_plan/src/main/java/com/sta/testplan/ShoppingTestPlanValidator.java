package com.sta.testplan;

public class ShoppingTestPlanValidator {

    public static class GateResult {
        private final boolean passed;
        private final int satisfiedGates;
        private final int totalGates;

        public GateResult(boolean passed, int satisfiedGates, int totalGates) {
            this.passed = passed;
            this.satisfiedGates = satisfiedGates;
            this.totalGates = totalGates;
        }

        public boolean isPassed() { return passed; }
        public int getSatisfiedGates() { return satisfiedGates; }
        public int getTotalGates() { return totalGates; }
    }

    public GateResult evaluateEntryCriteria(ShoppingTestPlanModel plan) {
        int satisfied = 0;
        for (ShoppingTestPlanModel.QualityGate gate : plan.getEntryCriteria()) {
            if (gate.isSatisfied()) satisfied++;
        }
        boolean allPassed = satisfied == plan.getEntryCriteria().size();
        return new GateResult(allPassed, satisfied, plan.getEntryCriteria().size());
    }

    public GateResult evaluateExitCriteria(ShoppingTestPlanModel plan) {
        int satisfied = 0;
        for (ShoppingTestPlanModel.QualityGate gate : plan.getExitCriteria()) {
            if (gate.isSatisfied()) satisfied++;
        }
        boolean allPassed = satisfied == plan.getExitCriteria().size();
        return new GateResult(allPassed, satisfied, plan.getExitCriteria().size());
    }

    public int calculateTotalScheduleDays(ShoppingTestPlanModel plan) {
        return plan.getSchedule().stream().mapToInt(ShoppingTestPlanModel.SchedulePhase::getDurationDays).sum();
    }
}
