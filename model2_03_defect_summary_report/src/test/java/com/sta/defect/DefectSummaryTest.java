package com.sta.defect;

import org.junit.jupiter.api.Test;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

public class DefectSummaryTest {

    @Test
    public void testDefectCollectionAndClassification() {
        DefectSummaryReporter reporter = new DefectSummaryReporter();
        List<DefectModel> defects = reporter.executeTestsAndCollectDefects();

        assertEquals(6, defects.size(), "Should detect all 6 deliberate defects");

        long criticalCount = defects.stream().filter(d -> "Critical".equals(d.getSeverity())).count();
        long majorCount = defects.stream().filter(d -> "Major".equals(d.getSeverity())).count();
        long p1Count = defects.stream().filter(d -> "P1 - High".equals(d.getPriority())).count();

        assertEquals(3, criticalCount, "Should classify 3 Critical severity defects (Cart, CVV leak, Double charge)");
        assertEquals(2, majorCount, "Should classify 2 Major severity defects (Wishlist, Tracking webhook)");
        assertEquals(3, p1Count, "Should classify 3 P1 High priority defects");
    }
}

