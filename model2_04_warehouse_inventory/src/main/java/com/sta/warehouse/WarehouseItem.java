package com.sta.warehouse;

public class WarehouseItem {
    private final String sku;
    private final String name;
    private final String binLocation;
    private int onHandStock;
    private final double unitCost;

    public WarehouseItem(String sku, String name, String binLocation, int initialStock, double unitCost) {
        if (sku == null || sku.trim().isEmpty()) {
            throw new IllegalArgumentException("SKU cannot be empty");
        }
        if (initialStock < 0) {
            throw new IllegalArgumentException("Initial stock cannot be negative");
        }
        this.sku = sku;
        this.name = name;
        this.binLocation = binLocation;
        this.onHandStock = initialStock;
        this.unitCost = unitCost;
    }

    public String getSku() { return sku; }
    public String getName() { return name; }
    public String getBinLocation() { return binLocation; }
    public int getOnHandStock() { return onHandStock; }
    public double getUnitCost() { return unitCost; }

    public synchronized void addStock(int qty) {
        if (qty <= 0) throw new IllegalArgumentException("Quantity to add must be positive");
        this.onHandStock += qty;
    }

    public synchronized void issueStock(int qty) {
        if (qty <= 0) throw new IllegalArgumentException("Quantity to issue must be positive");
        if (qty > this.onHandStock) {
            throw new IllegalStateException("Insufficient stock for SKU " + sku + ": on hand " + onHandStock + ", requested " + qty);
        }
        this.onHandStock -= qty;
    }

    public synchronized void adjustStock(int newOnHand) {
        if (newOnHand < 0) throw new IllegalArgumentException("Adjusted stock cannot be negative");
        this.onHandStock = newOnHand;
    }
}

