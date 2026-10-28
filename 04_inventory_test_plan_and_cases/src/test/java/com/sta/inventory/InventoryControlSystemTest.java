package com.sta.inventory;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.*;

public class InventoryControlSystemTest {

    private InventoryService service;

    @BeforeEach
    public void setUp() {
        service = new InventoryService();
        service.addProduct(new Product("SKU-001", "Gaming Mouse", "Peripherals", 50.0, 20, 5));
        service.addProduct(new Product("SKU-002", "HDMI Cable", "Accessories", 15.0, 10, 3));
    }

    // Module 1: Product Addition Tests
    @Test
    @DisplayName("Module 1: Successfully add valid product")
    public void testAddValidProduct() {
        Product p = new Product("SKU-003", "Webcam", "Video", 75.0, 15, 4);
        service.addProduct(p);
        assertEquals(3, service.getAllProducts().size());
        assertEquals("Webcam", service.getProductOrThrow("SKU-003").getName());
    }

    @Test
    @DisplayName("Module 1: Reject duplicate SKU addition")
    public void testRejectDuplicateSku() {
        Product duplicate = new Product("SKU-001", "Another Mouse", "Peripherals", 50.0, 10, 5);
        assertThrows(IllegalStateException.class, () -> service.addProduct(duplicate));
    }

    @Test
    @DisplayName("Module 1: Reject product with negative price or empty SKU")
    public void testRejectInvalidProductAttributes() {
        Product negPrice = new Product("SKU-NEG", "Item", "Cat", -10.0, 5, 2);
        assertThrows(IllegalArgumentException.class, () -> service.addProduct(negPrice));

        Product emptySku = new Product("", "Item", "Cat", 10.0, 5, 2);
        assertThrows(IllegalArgumentException.class, () -> service.addProduct(emptySku));
    }

    // Module 2: Stock Updates Tests
    @Test
    @DisplayName("Module 2: Replenish stock increments inventory")
    public void testReplenishStock() {
        service.replenishStock("SKU-001", 15);
        assertEquals(35, service.getAvailableQuantity("SKU-001"));
    }

    @Test
    @DisplayName("Module 2: Deduct stock decrements inventory accurately")
    public void testDeductStock() {
        service.deductStock("SKU-001", 10);
        assertEquals(10, service.getAvailableQuantity("SKU-001"));
    }

    @Test
    @DisplayName("Module 2: Reject deduction exceeding current stock")
    public void testDeductExceedingStockThrowsException() {
        assertThrows(IllegalStateException.class, () -> service.deductStock("SKU-001", 999));
    }

    @Test
    @DisplayName("Module 2: Reject negative or zero update quantity")
    public void testRejectNegativeAdjustment() {
        assertThrows(IllegalArgumentException.class, () -> service.replenishStock("SKU-001", 0));
        assertThrows(IllegalArgumentException.class, () -> service.deductStock("SKU-001", -5));
    }

    // Module 3: Stock Availability Tests
    @Test
    @DisplayName("Module 3: Stock availability query returns correct status")
    public void testAvailabilityCheck() {
        assertTrue(service.checkAvailability("SKU-001").startsWith("IN_STOCK"));

        service.deductStock("SKU-002", 10); // Decrements to 0
        assertTrue(service.checkAvailability("SKU-002").startsWith("OUT_OF_STOCK"));
    }

    @Test
    @DisplayName("Module 3: Querying non-existent SKU throws NoSuchElementException")
    public void testNonExistentSkuAvailability() {
        assertThrows(NoSuchElementException.class, () -> service.checkAvailability("NON-EXISTENT-SKU"));
    }

    // Module 4: Low-Stock Alert Tests
    @Test
    @DisplayName("Module 4: Alert is triggered when stock drops below threshold")
    public void testLowStockAlertTrigger() {
        // SKU-001 has 20 units, threshold 5. Deduct 16 -> Remaining 4 (<= 5)
        service.deductStock("SKU-001", 16);
        assertEquals(4, service.getAvailableQuantity("SKU-001"));
        assertFalse(service.getAlertLog().isEmpty());
        assertTrue(service.getAlertLog().get(0).contains("LOW_STOCK_ALERT"));
    }

    @Test
    @DisplayName("Module 4: Critical out-of-stock alert triggered when stock reaches 0")
    public void testOutOfStockAlertTrigger() {
        service.deductStock("SKU-002", 10);
        assertEquals(0, service.getAvailableQuantity("SKU-002"));
        boolean hasCritical = service.getAlertLog().stream().anyMatch(msg -> msg.contains("CRITICAL"));
        assertTrue(hasCritical);
    }

    // Module 5: Inventory Reports Tests
    @Test
    @DisplayName("Module 5: Inventory Report computes total units, SKUs, and valuation correctly")
    public void testInventoryReportValuation() {
        // Initial:
        // SKU-001: 20 units * $50 = $1,000
        // SKU-002: 10 units * $15 = $150
        // Total = $1,150.00, Total Units = 30
        InventoryReport report = service.generateReport();
        assertEquals(2, report.getTotalSkus());
        assertEquals(30, report.getTotalUnits());
        assertEquals(1150.0, report.getTotalValuation(), 0.001);
    }

    @Test
    @DisplayName("Module 5: Inventory Report accurately filters low-stock items")
    public void testInventoryReportLowStockFilter() {
        // Deduct SKU-002 to 2 units (Threshold is 3 -> flagged)
        service.deductStock("SKU-002", 8);
        InventoryReport report = service.generateReport();
        assertEquals(1, report.getLowStockProducts().size());
        assertEquals("SKU-002", report.getLowStockProducts().get(0).getSku());
    }
}

