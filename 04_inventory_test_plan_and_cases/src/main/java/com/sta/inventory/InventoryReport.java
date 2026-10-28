package com.sta.inventory;

import java.util.List;

public class InventoryReport {
    private final int totalSkus;
    private final int totalUnits;
    private final double totalValuation;
    private final List<Product> lowStockProducts;

    public InventoryReport(int totalSkus, int totalUnits, double totalValuation, List<Product> lowStockProducts) {
        this.totalSkus = totalSkus;
        this.totalUnits = totalUnits;
        this.totalValuation = totalValuation;
        this.lowStockProducts = lowStockProducts;
    }

    public int getTotalSkus() { return totalSkus; }
    public int getTotalUnits() { return totalUnits; }
    public double getTotalValuation() { return totalValuation; }
    public List<Product> getLowStockProducts() { return lowStockProducts; }

    public void printReport() {
        System.out.println("=====================================================================");
        System.out.println("               ENTERPRISE INVENTORY AUDIT REPORT                    ");
        System.out.println("=====================================================================");
        System.out.printf("Total Distinct SKUs In Catalog : %d%n", totalSkus);
        System.out.printf("Total Stocked Units In Storage : %d units%n", totalUnits);
        System.out.printf("Total Inventory Asset Valuation: $%,.2f%n", totalValuation);
        System.out.printf("Number of Low/Depleted SKUs    : %d%n", lowStockProducts.size());
        System.out.println("---------------------------------------------------------------------");
        if (!lowStockProducts.isEmpty()) {
            System.out.println("FLAGGED LOW-STOCK SKUs FOR REORDER:");
            for (Product p : lowStockProducts) {
                System.out.printf("  * SKU: %-10s | %-24s | Qty: %2d (Threshold: %2d)%n",
                    p.getSku(), p.getName(), p.getQuantity(), p.getLowStockThreshold());
            }
        }
        System.out.println("=====================================================================");
    }
}

