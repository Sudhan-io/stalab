package com.sta.testplan;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class ShoppingTestPlanCriteriaTest {

    @Test
    @DisplayName("Verify Model 2 test plan contains all 6 required modules")
    public void testInScopeModules() {
        ShoppingTestPlanModel plan = ShoppingTestPlanModel.createStandardModel2Plan();
        assertEquals(6, plan.getInScopeModules().size());
        assertTrue(plan.getInScopeModules().stream().anyMatch(m -> m.contains("Wishlist Management")));
        assertTrue(plan.getInScopeModules().stream().anyMatch(m -> m.contains("Order Tracking")));
        assertTrue(plan.getInScopeModules().stream().anyMatch(m -> m.contains("Payment Processing")));
    }

    @Test
    @DisplayName("Verify Entry quality gates evaluate to PASS")
    public void testEntryCriteriaGate() {
        ShoppingTestPlanModel plan = ShoppingTestPlanModel.createStandardModel2Plan();
        ShoppingTestPlanValidator validator = new ShoppingTestPlanValidator();
        ShoppingTestPlanValidator.GateResult res = validator.evaluateEntryCriteria(plan);
        assertTrue(res.isPassed());
        assertEquals(4, res.getSatisfiedGates());
    }

    @Test
    @DisplayName("Verify Exit sign-off quality gates evaluate to PASS")
    public void testExitCriteriaGate() {
        ShoppingTestPlanModel plan = ShoppingTestPlanModel.createStandardModel2Plan();
        ShoppingTestPlanValidator validator = new ShoppingTestPlanValidator();
        ShoppingTestPlanValidator.GateResult res = validator.evaluateExitCriteria(plan);
        assertTrue(res.isPassed());
        assertEquals(5, res.getSatisfiedGates());
    }

    @Test
    @DisplayName("Verify total project schedule timeline calculation")
    public void testScheduleDuration() {
        ShoppingTestPlanModel plan = ShoppingTestPlanModel.createStandardModel2Plan();
        ShoppingTestPlanValidator validator = new ShoppingTestPlanValidator();
        int totalDays = validator.calculateTotalScheduleDays(plan);
        assertEquals(35, totalDays);
    }
}
