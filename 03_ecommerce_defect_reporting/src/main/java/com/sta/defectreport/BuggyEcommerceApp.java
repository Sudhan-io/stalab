package com.sta.defectreport;

public class BuggyEcommerceApp {

    // Deliberate Defect 1: Confirms order even if payment status is DECLINED
    public String checkout(String paymentStatus, String item, int qty) {
        // Bug: Doesn't check if paymentStatus is "DECLINED"
        return "SUCCESS: ORD-2026-9921 confirmed for " + qty + " of " + item;
    }

    // Deliberate Defect 2: Race condition allows adding out of stock item
    public boolean addToCart(String item, int stock, boolean isDoubleClick) {
        // Bug: If double click occurs, ignores stock == 0
        if (stock <= 0 && isDoubleClick) {
            return true; // Added! Defect!
        }
        return stock > 0;
    }

    // Deliberate Defect 3: Uses premature floor truncation resulting in 84.98 instead of 84.99
    public double applyDiscount(double price, double percentage) {
        double discount = (price * percentage) / 100.0;
        double raw = price - discount;
        // Bug: premature floor deduction causing loss of a cent
        return Math.floor(raw * 100.0 - 1.0) / 100.0; // returns 84.98
    }

    // Deliberate Defect 4: Leaks cleartext password in response header
    public String getRegistrationHeader(String password) {
        return "X-Debug-User-Auth: " + password; // Defect: cleartext credential exposure
    }

    // Deliberate Defect 5: Viewport overflow on pagination controls
    public boolean isPaginationVisible(int screenWidth) {
        if (screenWidth < 1024) {
            return false; // Defect: Buttons clipped on viewports under 1024px
        }
        return true;
    }
}
