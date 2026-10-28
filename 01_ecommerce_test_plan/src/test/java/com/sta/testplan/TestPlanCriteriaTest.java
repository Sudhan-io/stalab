package com.sta.testplan;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class TestPlanCriteriaTest {

    private EcommerceTestPlanModel plan;
    private TestPlanValidator validator;

    @BeforeEach
    public void setUp() {
        plan = EcommerceTestPlanModel.createStandardPlan();
        validator = new TestPlanValidator();
    }

    @Test
    @DisplayName("Verify Entry Criteria passes when all gates are satisfied")
    public void testEntryCriteriaPass() {
        TestPlanValidator.GateEvaluationResult result = validator.evaluateEntryCriteria(plan);
        assertTrue(result.isPassed(), "All standard entry criteria should be met");
        assertEquals(4, result.getSatisfiedGates());
        assertTrue(result.getBlockingIssues().isEmpty());
    }

    @Test
    @DisplayName("Verify Entry Criteria fails and reports blocker if environment is not ready")
    public void testEntryCriteriaBlocker() {
        plan.getEntryCriteria().get(1).setSatisfied(false); // Staging environment broken
        TestPlanValidator.GateEvaluationResult result = validator.evaluateEntryCriteria(plan);
        assertFalse(result.isPassed());
        assertEquals(1, result.getBlockingIssues().size());
        assertTrue(result.getBlockingIssues().get(0).contains("ENTRY BLOCKER"));
    }

    @Test
    @DisplayName("Verify Exit Criteria passes when defect counts and SLAs satisfy gates")
    public void testExitCriteriaPass() {
        TestPlanValidator.GateEvaluationResult result = validator.evaluateExitCriteria(plan);
        assertTrue(result.isPassed(), "All standard exit criteria should be met");
        assertEquals(5, result.getSatisfiedGates());
    }

    @Test
    @DisplayName("Verify Total Schedule calculation aligns with phase milestone sum")
    public void testScheduleDuration() {
        int days = validator.calculateTotalScheduleDays(plan);
        assertEquals(30, days, "Schedule should span exactly 30 working days");
    }
}

