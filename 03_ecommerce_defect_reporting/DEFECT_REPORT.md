# E-Commerce Application Defect Report

**Project:** Amazon-Style E-Commerce Web & Mobile Application  
**Execution Cycle:** Cycle 1 System Integration Testing (SIT)  
**Report Identifier:** DEF-REP-2026-001  
**Status:** Open / Active Triage  

---

## 1. Executive Defect Summary

* **Total Test Cases Executed:** 17
* **Passed:** 12
* **Failed:** 5
* **Defect Density:** 29.4% failure rate on first integration cycle

### Severity Breakdown:
* **Critical (P1):** 2 (Order placement on failed payment, out-of-stock item bypass)
* **Major (P2):** 1 (Cart discount calculation rounding loss)
* **Moderate (P3):** 1 (Cleartext password leakage in response header)
* **Minor (P4):** 1 (Search pagination UI layout truncation)

---

## 2. Detailed Defect Log Table

| Defect ID | Description | Steps to Reproduce | Severity | Priority | Expected Result | Actual Result | Status |
| :--- | :--- | :--- | :--- | :--- | :--- | :--- | :--- |
| **DEF_ECOM_001** | Order confirmed despite payment gateway decline | 1. Add item to cart.<br>2. Proceed to payment.<br>3. Enter invalid/declined test card.<br>4. Click 'Pay Now'. | **Critical** | **P1** | System must display "Payment Declined. Please retry with another method" and abort order creation. | Order ID generated and marked 'Confirmed'; warehouse stock decremented. | OPEN |
| **DEF_ECOM_002** | Out-of-Stock product added to cart via double click | 1. Navigate to out-of-stock product PDP.<br>2. Rapidly double-click disabled 'Add to Cart' area.<br>3. Inspect cart counter. | **Critical** | **P1** | 'Add to Cart' button remains permanently disabled; cart items count remains 0. | Race condition allows 1 unit to be placed into cart; checkout can be initiated. | OPEN |
| **DEF_ECOM_003** | Cart subtotal roundoff error under 15% coupon | 1. Add item with price \$99.99.<br>2. Apply coupon code 'SAVE15'.<br>3. Verify discounted subtotal. | **Major** | **P2** | Subtotal = \$99.99 - \$15.00 = \$84.99 with standard 2-decimal half-up rounding. | Subtotal calculates as \$84.98 due to floor truncation instead of half-up round. | OPEN |
| **DEF_ECOM_004** | User registration leaks cleartext password in response header | 1. Submit valid registration request.<br>2. Inspect HTTP network response payload and headers. | **Moderate** | **P3** | User credentials must never be reflected in response bodies or headers. | Password string reflected in `X-Debug-User-Auth` header. | OPEN |
| **DEF_ECOM_005** | Search results pagination truncated on viewport < 1024px | 1. Search for keyword "laptop" (45 results).<br>2. Resize browser window to 900px.<br>3. Scroll to bottom pagination controls. | **Minor** | **P4** | Pagination buttons (1, 2, 3... Next) remain fully visible and clickable. | 'Next' and 'Last' buttons wrap off-screen; unclickable without horizontal scroll. | OPEN |

---

## 3. Defect Lifecycle & SLA Guidelines

* **Critical (P1):** Turnaround time < 4 hours. Blocker for release.
* **Major (P2):** Turnaround time < 24 hours. Must be resolved before regression cycle.
* **Moderate (P3):** Turnaround time < 48 hours. Resolved within sprint.
* **Minor (P4):** Resolved before candidate release.

