package com.sta.warehouse;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.Map;
import static org.junit.jupiter.api.Assertions.*;

public class WarehouseInventoryTest {

    private WarehouseInventoryService service;

    @BeforeEach
    public void setup() {
        service = new WarehouseInventoryService();
        Supplier s = new Supplier("SUP-101", "Global Spares", "contact@global.com", "TAX-9988", true);
        service.registerSupplier(s);

        WarehouseItem item = new WarehouseItem("SKU-BOLT-10", "Steel Bolts Pack", "BIN-01", 100, 1.50);
        service.addWarehouseItem(item);
    }

    @Test
    public void testStockReceipt() {
        boolean received = service.receiveStock("GRN-01", "SUP-101", "SKU-BOLT-10", 50);
        assertTrue(received);
        assertEquals(150, service.getItem("SKU-BOLT-10").getOnHandStock());
    }

    @Test
    public void testStockIssueSuccessAndUnderflow() {
        service.issueStock("ORD-01", "SKU-BOLT-10", 40);
        assertEquals(60, service.getItem("SKU-BOLT-10").getOnHandStock());

        assertThrows(IllegalStateException.class, () -> {
            service.issueStock("ORD-02", "SKU-BOLT-10", 70); // only 60 available
        });
    }

    @Test
    public void testInventoryAdjustment() {
        service.adjustInventory("ADJ-01", "SKU-BOLT-10", 85, "SHRINKAGE");
        assertEquals(85, service.getItem("SKU-BOLT-10").getOnHandStock());
    }

    @Test
    public void testStockReconciliationVariance() {
        Map<String, Object> audit = service.reconcileStock("SKU-BOLT-10", 95);
        assertEquals(100, audit.get("bookStock"));
        assertEquals(95, audit.get("physicalCount"));
        assertEquals(-5, audit.get("variance"));
        assertFalse((Boolean) audit.get("reconciled"));
    }
}

