package com.sta.inventory;

public class InventoryApp {

    public static void main(String[] args) {
        System.out.println("===============================================================================");
        System.out.println(" STA LAB - EXPERIMENT 4: INVENTORY CONTROL SYSTEM DEMONSTRATION");
        System.out.println("===============================================================================");

        InventoryService service = new InventoryService();

        // Register Alert Listener
        service.addAlertListener((sku, name, stock, threshold, type) -> {
            System.out.printf("  [ALERT DISPATCHED] Type: %s | SKU: %s (%s) | Remaining: %d | Threshold: %d%n",
                type, sku, name, stock, threshold);
        });

        System.out.println("\n--- 1. ADDING INITIAL PRODUCTS ---");
        service.addProduct(new Product("SKU-101", "Logitech MX Master 3S", "Peripherals", 99.99, 25, 5));
        service.addProduct(new Product("SKU-102", "Dell 27-inch 4K Monitor", "Displays", 349.50, 10, 4));
        service.addProduct(new Product("SKU-103", "Mechanical Keyboard", "Peripherals", 120.00, 8, 5));
        service.addProduct(new Product("SKU-104", "USB-C Hub Multiport", "Accessories", 35.00, 4, 6)); // Already low stock
        System.out.println("  Added 4 products successfully to warehouse catalog.");

        System.out.println("\n--- 2. STOCK AVAILABILITY CHECK ---");
        System.out.println("  SKU-101 status: " + service.checkAvailability("SKU-101"));
        System.out.println("  SKU-104 status: " + service.checkAvailability("SKU-104"));

        System.out.println("\n--- 3. PERFORMING STOCK MOVEMENTS & ALERT TRIGGERS ---");
        System.out.println("  -> Replenishing 10 units for SKU-101...");
        service.replenishStock("SKU-101", 10);
        System.out.println("  SKU-101 new balance: " + service.getAvailableQuantity("SKU-101"));

        System.out.println("  -> Deducting 5 units from SKU-103 (Keyboard: was 8, threshold 5)...");
        service.deductStock("SKU-103", 5); // Drops to 3, <= 5 threshold! Alert should fire!

        System.out.println("  -> Deducting all 4 units from SKU-104 (Drops to 0)...");
        service.deductStock("SKU-104", 4); // Drops to 0! Critical out-of-stock alert!

        System.out.println("\n--- 4. GENERATING INVENTORY AUDIT & VALUATION REPORT ---");
        InventoryReport report = service.generateReport();
        report.printReport();

        System.out.println("All operations completed successfully.");
    }
}

