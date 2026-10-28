package com.sta.defect;

import java.util.*;

public class BuggyShoppingApp {

    // Deliberate Defect 1: Negative quantity accepted in cart (Critical, P1)
    public boolean updateCartQuantity(String itemId, int qty) {
        if (qty < 0) {
            // BUG: Fails to reject negative quantity, updates cart
            return true;
        }
        return qty >= 1 && qty <= 10;
    }

    // Deliberate Defect 2: Wishlist allows duplicate items (Major, P2)
    private final List<String> wishlist = new ArrayList<>();
    public int addToWishlist(String itemId) {
        // BUG: Adds without checking if already present in wishlist
        wishlist.add(itemId);
        return wishlist.size();
    }

    // Deliberate Defect 3: Order tracking webhook fails to update status (Major, P2)
    public String getTrackingStatus(String orderId, boolean webhookDelivered) {
        // BUG: Always returns "Processing" even after carrier delivered webhook
        return "Processing";
    }

    // Deliberate Defect 4: Leaks plain CVV in debug attribute (Critical, P1)
    public String renderCvvField(String cvv) {
        // BUG: Renders masked text but leaks plaintext in data-attribute
        return "<input type='password' value='***' data-cvv-raw='" + cvv + "' />";
    }

    // Deliberate Defect 5: Sort prices as strings: "$100.00" < "$20.00" (Moderate, P3)
    public List<String> sortPricesLowToHigh(List<String> prices) {
        List<String> sorted = new ArrayList<>(prices);
        // BUG: Lexicographical string sort instead of double parsing
        Collections.sort(sorted);
        return sorted;
    }

    // Deliberate Defect 6: Double charge on rapid checkout click (Critical, P1)
    private int paymentChargeCount = 0;
    public int executePayment(boolean rapidDoubleTap) {
        paymentChargeCount++;
        if (rapidDoubleTap) {
            paymentChargeCount++; // BUG: Lack of idempotency / debounce
        }
        return paymentChargeCount;
    }
}

