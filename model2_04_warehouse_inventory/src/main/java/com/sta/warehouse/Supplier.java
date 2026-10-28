package com.sta.warehouse;

public class Supplier {
    private final String supplierId;
    private final String supplierName;
    private final String contactEmail;
    private final String taxId;
    private final boolean active;

    public Supplier(String supplierId, String supplierName, String contactEmail, String taxId, boolean active) {
        if (supplierId == null || supplierId.trim().isEmpty()) {
            throw new IllegalArgumentException("Supplier ID cannot be empty");
        }
        if (supplierName == null || supplierName.trim().isEmpty()) {
            throw new IllegalArgumentException("Supplier Name cannot be empty");
        }
        if (contactEmail == null || !contactEmail.contains("@")) {
            throw new IllegalArgumentException("Valid Contact Email is required");
        }
        if (taxId == null || taxId.length() < 5) {
            throw new IllegalArgumentException("Valid Tax/GST ID is required");
        }
        this.supplierId = supplierId;
        this.supplierName = supplierName;
        this.contactEmail = contactEmail;
        this.taxId = taxId;
        this.active = active;
    }

    public String getSupplierId() { return supplierId; }
    public String getSupplierName() { return supplierName; }
    public String getContactEmail() { return contactEmail; }
    public String getTaxId() { return taxId; }
    public boolean isActive() { return active; }
}

