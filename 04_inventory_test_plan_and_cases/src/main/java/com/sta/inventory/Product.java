package com.sta.inventory;

public class Product {
    private String sku;
    private String name;
    private String category;
    private double unitPrice;
    private int quantity;
    private int lowStockThreshold;

    public Product(String sku, String name, String category, double unitPrice, int quantity, int lowStockThreshold) {
        this.sku = sku;
        this.name = name;
        this.category = category;
        this.unitPrice = unitPrice;
        this.quantity = quantity;
        this.lowStockThreshold = lowStockThreshold;
    }

    public String getSku() { return sku; }
    public String getName() { return name; }
    public String getCategory() { return category; }
    public double getUnitPrice() { return unitPrice; }
    public int getQuantity() { return quantity; }
    public void setQuantity(int quantity) { this.quantity = quantity; }
    public int getLowStockThreshold() { return lowStockThreshold; }
    public void setLowStockThreshold(int threshold) { this.lowStockThreshold = threshold; }
}

