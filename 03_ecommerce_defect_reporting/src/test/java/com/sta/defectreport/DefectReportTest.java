package com.sta.defectreport;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class DefectReportTest {

    @Test
    @DisplayName("Verify defect detection suite captures all 5 deliberate defects")
    public void testDefectCount() {
        List<Defect> defects = DefectReporter.runTestsAndDetectDefects();
        assertEquals(5, defects.size(), "Should capture exactly 5 defects across modules");
    }

    @Test
    @DisplayName("Verify critical defects (P1) are correctly prioritized")
    public void testCriticalDefects() {
        List<Defect> defects = DefectReporter.runTestsAndDetectDefects();
        long critical = defects.stream().filter(d -> "Critical".equals(d.getSeverity())).count();
        assertEquals(2, critical, "Should log 2 critical defects");

        Defect paymentDefect = defects.stream()
            .filter(d -> "DEF_ECOM_001".equals(d.getDefectId()))
            .findFirst()
            .orElse(null);

        assertNotNull(paymentDefect);
        assertEquals("P1", paymentDefect.getPriority());
        assertTrue(paymentDefect.getActualResult().contains("DECLINED"));
    }

    @Test
    @DisplayName("Verify defect report fields conform to IEEE standards")
    public void testDefectFieldIntegrity() {
        List<Defect> defects = DefectReporter.runTestsAndDetectDefects();
        for (Defect d : defects) {
            assertNotNull(d.getDefectId());
            assertNotNull(d.getDescription());
            assertNotNull(d.getStepsToReproduce());
            assertNotNull(d.getSeverity());
            assertNotNull(d.getPriority());
            assertNotNull(d.getExpectedResult());
            assertNotNull(d.getActualResult());
            assertEquals("OPEN", d.getStatus());
        }
    }
}

