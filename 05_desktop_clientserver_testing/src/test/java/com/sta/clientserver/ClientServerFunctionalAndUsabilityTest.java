package com.sta.clientserver;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class ClientServerFunctionalAndUsabilityTest {

    @Test
    @DisplayName("Verify Client-Server audit detects functional and usability defects")
    public void testAuditExecution() {
        List<ClientServerDefect> defects = ClientServerTestRunner.runAudit();
        assertFalse(defects.isEmpty(), "Audit should identify defects in the test application");
        assertTrue(defects.size() >= 4, "Should identify at least 4 defects across functional & usability");
    }

    @Test
    @DisplayName("Verify distinction between Functional and Usability defect categories")
    public void testCategoryClassification() {
        List<ClientServerDefect> defects = ClientServerTestRunner.runAudit();
        boolean hasFunctional = defects.stream().anyMatch(d -> "FUNCTIONAL".equals(d.getCategory()));
        boolean hasUsability = defects.stream().anyMatch(d -> "USABILITY".equals(d.getCategory()));

        assertTrue(hasFunctional, "Must identify functional defects");
        assertTrue(hasUsability, "Must identify usability defects");
    }

    @Test
    @DisplayName("Verify concurrent overdraft defect is prioritized as Critical (P1)")
    public void testCriticalFunctionalDefect() {
        List<ClientServerDefect> defects = ClientServerTestRunner.runAudit();
        ClientServerDefect overdraftDefect = defects.stream()
            .filter(d -> "DEF_CS_FUNC_001".equals(d.getDefectId()))
            .findFirst()
            .orElse(null);

        assertNotNull(overdraftDefect);
        assertEquals("Critical", overdraftDefect.getSeverity());
        assertEquals("P1", overdraftDefect.getPriority());
    }
}

