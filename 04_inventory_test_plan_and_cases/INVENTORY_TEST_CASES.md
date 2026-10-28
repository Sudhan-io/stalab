# Inventory Control System Test Cases Specification

**Document ID:** TC-ICS-SPEC-2026-V1.0  
**Coverage Modules:** Product Addition, Stock Updates, Stock Availability, Low-Stock Alerts, Inventory Reports  

---

## Module 1: Product Addition
| TC ID | Scenario | Preconditions | Steps | Test Data | Expected Result | Status |
| :--- | :--- | :--- | :--- | :--- | :--- | :--- |
| **TC_INV_ADD_01** | Add new product with valid attributes | System operational | 1. Enter unique SKU, name, price, qty, threshold.<br>2. Submit. | SKU: "SKU-1001", Name: "Wireless Mouse", Price: \$25.00, Qty: 50, Threshold: 10 | Product added successfully to catalog | PASS |
| **TC_INV_ADD_02** | Reject product addition with duplicate SKU | SKU-1001 exists | 1. Attempt to add another product with SKU-1001. | SKU: "SKU-1001", Name: "Gaming Mouse" | Error: "Product with SKU SKU-1001 already exists" | PASS |
| **TC_INV_ADD_03** | Reject product with negative price or stock | System operational | 1. Enter negative price (-\$10) and negative qty (-5). | Price: -10.0, Qty: -5 | Error: "Price and quantity must be non-negative" | PASS |
| **TC_INV_ADD_04** | Reject product with blank SKU or Name | System operational | 1. Leave SKU or Name empty.<br>2. Submit. | SKU: "", Name: "Keyboard" | Error: "SKU and Name cannot be empty" | PASS |

---

## Module 2: Stock Updates
| TC ID | Scenario | Preconditions | Steps | Test Data | Expected Result | Status |
| :--- | :--- | :--- | :--- | :--- | :--- | :--- |
| **TC_INV_UPD_01** | Replenish stock (Restock inward shipment) | Product exists with 50 units | 1. Restock 30 units to SKU-1001. | Add Qty: 30 | Stock balance increments from 50 to 80 units | PASS |
| **TC_INV_UPD_02** | Deduct stock (Order dispatch outward shipment) | Product has 80 units | 1. Deduct 20 units for outgoing order. | Deduct Qty: 20 | Stock balance decrements to 60 units | PASS |
| **TC_INV_UPD_03** | Reject deduction exceeding available stock | Product has 10 units | 1. Attempt to deduct 15 units. | Deduct Qty: 15 | Error: "Insufficient stock. Available: 10, Requested: 15" | PASS |
| **TC_INV_UPD_04** | Reject update with negative or zero quantity | Product exists | 1. Restock with quantity -5. | Qty: -5 | Error: "Adjustment quantity must be greater than zero" | PASS |

---

## Module 3: Stock Availability
| TC ID | Scenario | Preconditions | Steps | Test Data | Expected Result | Status |
| :--- | :--- | :--- | :--- | :--- | :--- | :--- |
| **TC_INV_AVL_01** | Query available stock for in-stock SKU | SKU exists with 60 units | 1. Request availability for SKU-1001. | SKU: "SKU-1001" | Returns status: IN_STOCK, Available Units: 60 | PASS |
| **TC_INV_AVL_02** | Query stock for zero-inventory SKU | Product has 0 units | 1. Request availability for depleted SKU. | SKU: "SKU-OUT" | Returns status: OUT_OF_STOCK, Available Units: 0 | PASS |
| **TC_INV_AVL_03** | Query stock for non-existent SKU | SKU not in catalog | 1. Query random non-existent SKU. | SKU: "SKU-UNKNOWN" | Returns Error: "Product not found" | PASS |

---

## Module 4: Low-Stock Alerts
| TC ID | Scenario | Preconditions | Steps | Test Data | Expected Result | Status |
| :--- | :--- | :--- | :--- | :--- | :--- | :--- |
| **TC_INV_ALT_01** | Trigger alert when stock drops below threshold | Threshold is 10; current stock is 12 | 1. Deduct 5 units.<br>2. Current stock becomes 7. | Deduct: 5 | Low-stock alert triggered: "ALERT: SKU-1001 is LOW (7 units remaining, threshold: 10)" | PASS |
| **TC_INV_ALT_02** | No alert triggered when stock remains above threshold | Threshold is 10; stock 25 | 1. Deduct 5 units (stock becomes 20). | Deduct: 5 | Stock updated; No low-stock alert emitted | PASS |
| **TC_INV_ALT_03** | Critical Out-of-Stock alert when stock drops to 0 | Threshold is 10; stock 2 | 1. Deduct 2 units (stock becomes 0). | Deduct: 2 | Alert triggered: "CRITICAL: SKU-1001 is completely OUT OF STOCK" | PASS |

---

## Module 5: Inventory Reports
| TC ID | Scenario | Preconditions | Steps | Test Data | Expected Result | Status |
| :--- | :--- | :--- | :--- | :--- | :--- | :--- |
| **TC_INV_REP_01** | Generate valuation summary report | 3 products with varying prices and quantities | 1. Generate inventory summary report. | Catalog items | Computes Total SKUs, Total Units count, and Total Valuation (\$ amount) accurately | PASS |
| **TC_INV_REP_02** | Generate filtered Low-Stock items audit report | 2 items below threshold, 3 items normal | 1. Request low stock report. | Catalog items | Returns list containing strictly the 2 items below their configured thresholds | PASS |

