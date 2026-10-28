package com.sta.inventory;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class InventoryService {

    public interface StockAlertListener {
        void onStockAlert(String sku, String productName, int currentStock, int threshold, String alertType);
    }

    private final Map<String, Product> productCatalog = new ConcurrentHashMap<>();
    private final List<StockAlertListener> alertListeners = new ArrayList<>();
    private final List<String> alertLog = new ArrayList<>();

    public void addAlertListener(StockAlertListener listener) {
        this.alertListeners.add(listener);
    }

    public List<String> getAlertLog() {
        return Collections.unmodifiableList(alertLog);
    }

    // Module 1: Product Addition
    public void addProduct(Product product) {
        if (product == null) {
            throw new IllegalArgumentException("Product cannot be null");
        }
        if (product.getSku() == null || product.getSku().trim().isEmpty() ||
            product.getName() == null || product.getName().trim().isEmpty()) {
            throw new IllegalArgumentException("SKU and Name cannot be empty");
        }
        if (productCatalog.containsKey(product.getSku())) {
            throw new IllegalStateException("Product with SKU " + product.getSku() + " already exists");
        }
        if (product.getUnitPrice() < 0 || product.getQuantity() < 0 || product.getLowStockThreshold() < 0) {
            throw new IllegalArgumentException("Price, quantity, and threshold must be non-negative");
        }
        productCatalog.put(product.getSku(), product);
    }

    // Module 2: Stock Updates
    public void replenishStock(String sku, int quantityToAdd) {
        if (quantityToAdd <= 0) {
            throw new IllegalArgumentException("Adjustment quantity must be greater than zero");
        }
        Product product = getProductOrThrow(sku);
        product.setQuantity(product.getQuantity() + quantityToAdd);
    }

    public void deductStock(String sku, int quantityToDeduct) {
        if (quantityToDeduct <= 0) {
            throw new IllegalArgumentException("Adjustment quantity must be greater than zero");
        }
        Product product = getProductOrThrow(sku);
        if (product.getQuantity() < quantityToDeduct) {
            throw new IllegalStateException("Insufficient stock. Available: " + product.getQuantity() + ", Requested: " + quantityToDeduct);
        }
        int newQty = product.getQuantity() - quantityToDeduct;
        product.setQuantity(newQty);
        checkAndTriggerAlert(product);
    }

    // Module 3: Stock Availability
    public String checkAvailability(String sku) {
        Product product = getProductOrThrow(sku);
        if (product.getQuantity() == 0) {
            return "OUT_OF_STOCK (0 units)";
        }
        return "IN_STOCK (" + product.getQuantity() + " units)";
    }

    public int getAvailableQuantity(String sku) {
        return getProductOrThrow(sku).getQuantity();
    }

    // Module 4: Low-Stock Alerts
    private void checkAndTriggerAlert(Product product) {
        if (product.getQuantity() == 0) {
            String msg = "CRITICAL: " + product.getSku() + " (" + product.getName() + ") is completely OUT OF STOCK";
            alertLog.add(msg);
            notifyListeners(product.getSku(), product.getName(), product.getQuantity(), product.getLowStockThreshold(), "OUT_OF_STOCK");
        } else if (product.getQuantity() <= product.getLowStockThreshold()) {
            String msg = "LOW_STOCK_ALERT: " + product.getSku() + " (" + product.getName() + ") stock is " +
                         product.getQuantity() + " (Threshold: " + product.getLowStockThreshold() + ")";
            alertLog.add(msg);
            notifyListeners(product.getSku(), product.getName(), product.getQuantity(), product.getLowStockThreshold(), "LOW_STOCK");
        }
    }

    private void notifyListeners(String sku, String name, int stock, int threshold, String type) {
        for (StockAlertListener listener : alertListeners) {
            listener.onStockAlert(sku, name, stock, threshold, type);
        }
    }

    // Module 5: Inventory Reports
    public InventoryReport generateReport() {
        int totalSkus = productCatalog.size();
        int totalUnits = 0;
        double totalValuation = 0.0;
        List<Product> lowStockProducts = new ArrayList<>();

        for (Product p : productCatalog.values()) {
            totalUnits += p.getQuantity();
            totalValuation += (p.getUnitPrice() * p.getQuantity());
            if (p.getQuantity() <= p.getLowStockThreshold()) {
                lowStockProducts.add(p);
            }
        }
        return new InventoryReport(totalSkus, totalUnits, totalValuation, lowStockProducts);
    }

    public Product getProductOrThrow(String sku) {
        Product p = productCatalog.get(sku);
        if (p == null) {
            throw new NoSuchElementException("Product not found with SKU: " + sku);
        }
        return p;
    }

    public Collection<Product> getAllProducts() {
        return Collections.unmodifiableCollection(productCatalog.values());
    }
}

