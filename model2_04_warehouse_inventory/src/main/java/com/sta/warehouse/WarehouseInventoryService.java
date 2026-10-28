package com.sta.warehouse;

import java.util.*;

public class WarehouseInventoryService {

    private final Map<String, Supplier> suppliers = new HashMap<>();
    private final Map<String, WarehouseItem> catalog = new HashMap<>();
    private final List<String> transactionLedger = new ArrayList<>();

    // 1. Supplier Details
    public void registerSupplier(Supplier supplier) {
        suppliers.put(supplier.getSupplierId(), supplier);
        log("SUPPLIER_REGISTERED: ID=" + supplier.getSupplierId() + ", Name=" + supplier.getSupplierName());
    }

    public Supplier getSupplier(String supplierId) {
        return suppliers.get(supplierId);
    }

    public void addWarehouseItem(WarehouseItem item) {
        catalog.put(item.getSku(), item);
    }

    public WarehouseItem getItem(String sku) {
        return catalog.get(sku);
    }

    // 2. Stock Receipt (Goods Receipt Note)
    public boolean receiveStock(String grnNumber, String supplierId, String sku, int quantity) {
        Supplier supplier = suppliers.get(supplierId);
        if (supplier == null || !supplier.isActive()) {
            throw new IllegalArgumentException("Cannot receive goods: Inactive or unregistered supplier " + supplierId);
        }
        WarehouseItem item = catalog.get(sku);
        if (item == null) {
            throw new IllegalArgumentException("SKU not registered in catalog: " + sku);
        }
        if (quantity <= 0) {
            throw new IllegalArgumentException("Received quantity must be positive");
        }
        item.addStock(quantity);
        log("STOCK_RECEIPT: GRN=" + grnNumber + ", SKU=" + sku + ", Qty=" + quantity + ", NewBalance=" + item.getOnHandStock());
        return true;
    }

    // 3. Stock Issue (Pick & Dispatch)
    public boolean issueStock(String dispatchOrderId, String sku, int quantity) {
        WarehouseItem item = catalog.get(sku);
        if (item == null) {
            throw new IllegalArgumentException("SKU not found: " + sku);
        }
        item.issueStock(quantity);
        log("STOCK_ISSUE: Order=" + dispatchOrderId + ", SKU=" + sku + ", Qty=" + quantity + ", NewBalance=" + item.getOnHandStock());
        return true;
    }

    // 4. Inventory Adjustment
    public boolean adjustInventory(String adjustmentId, String sku, int actualCount, String reasonCode) {
        WarehouseItem item = catalog.get(sku);
        if (item == null) {
            throw new IllegalArgumentException("SKU not found: " + sku);
        }
        int variance = actualCount - item.getOnHandStock();
        item.adjustStock(actualCount);
        log("INVENTORY_ADJUSTMENT: AdjId=" + adjustmentId + ", SKU=" + sku + ", Variance=" + variance + ", Reason=" + reasonCode);
        return true;
    }

    // 5. Stock Reconciliation
    public Map<String, Object> reconcileStock(String sku, int physicalCount) {
        WarehouseItem item = catalog.get(sku);
        if (item == null) {
            throw new IllegalArgumentException("SKU not found: " + sku);
        }
        int bookStock = item.getOnHandStock();
        int variance = physicalCount - bookStock;
        double varianceCost = variance * item.getUnitCost();

        Map<String, Object> audit = new LinkedHashMap<>();
        audit.put("sku", sku);
        audit.put("itemName", item.getName());
        audit.put("bookStock", bookStock);
        audit.put("physicalCount", physicalCount);
        audit.put("variance", variance);
        audit.put("varianceCost", varianceCost);
        audit.put("reconciled", variance == 0);
        log("STOCK_RECONCILIATION: SKU=" + sku + ", Book=" + bookStock + ", Physical=" + physicalCount + ", Variance=" + variance);
        return audit;
    }

    private void log(String entry) {
        transactionLedger.add(new Date() + " | " + entry);
    }

    public List<String> getTransactionLedger() {
        return Collections.unmodifiableList(transactionLedger);
    }
}
