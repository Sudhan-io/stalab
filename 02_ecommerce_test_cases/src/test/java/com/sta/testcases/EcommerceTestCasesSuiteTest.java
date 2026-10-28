package com.sta.testcases;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class EcommerceTestCasesSuiteTest {

    private EcommerceBusinessEngine engine;

    @BeforeEach
    public void setUp() {
        engine = new EcommerceBusinessEngine();
    }

    @Test
    @DisplayName("Module 1 - Registration Positive and Negative Validation")
    public void testRegistrationModule() {
        String resSuccess = engine.registerUser("Bob", "bob@example.com", "Str0ng#Pass");
        assertTrue(resSuccess.startsWith("SUCCESS"));

        String resDuplicate = engine.registerUser("Duplicate", "existing@test.com", "Str0ng#Pass");
        assertTrue(resDuplicate.contains("already registered"));

        String resWeak = engine.registerUser("Weak", "weak@example.com", "12345");
        assertTrue(resWeak.contains("Password must be"));
    }

    @Test
    @DisplayName("Module 2 - Login Authentication and Account Lockout")
    public void testLoginModule() {
        String resLogin = engine.login("user@test.com", "Secret#123");
        assertTrue(resLogin.startsWith("SUCCESS"));

        String resFail = engine.login("user@test.com", "wrong");
        assertTrue(resFail.contains("Invalid email or password"));

        for (int i = 0; i < 4; i++) {
            engine.login("user@test.com", "wrong" + i);
        }
        String resLock = engine.login("user@test.com", "wrong5");
        assertTrue(resLock.contains("Account locked"));
    }

    @Test
    @DisplayName("Module 3 - Product Search and Filters")
    public void testSearchModule() {
        List<String> results = engine.searchProducts("Sony", null, null);
        assertFalse(results.isEmpty());
        assertTrue(results.get(0).contains("Sony"));

        List<String> emptyResults = engine.searchProducts("nonexistent999", null, null);
        assertTrue(emptyResults.isEmpty());
    }

    @Test
    @DisplayName("Module 4 - Product Selection and Out of Stock detection")
    public void testSelectionModule() {
        String inStock = engine.selectProduct("iPhone 15 Pro");
        assertTrue(inStock.startsWith("SUCCESS"));

        String outOfStock = engine.selectProduct("Vintage Camera");
        assertTrue(outOfStock.contains("Out of Stock"));
    }

    @Test
    @DisplayName("Module 5 - Cart Management quantity and boundary checks")
    public void testCartModule() {
        EcommerceBusinessEngine.Cart cart = new EcommerceBusinessEngine.Cart();
        String addRes = cart.addItem("iPhone 15 Pro", 2, 8);
        assertTrue(addRes.startsWith("SUCCESS"));

        String limitExceeded = cart.addItem("iPhone 15 Pro", 12, 8);
        assertTrue(limitExceeded.contains("Maximum allowable limit"));

        String updateRes = cart.updateQuantity("iPhone 15 Pro", 0);
        assertTrue(updateRes.contains("Item removed"));
    }

    @Test
    @DisplayName("Module 6 - Checkout Address Validation")
    public void testCheckoutModule() {
        String validPin = engine.validateShippingAddress("600001");
        assertTrue(validPin.startsWith("SUCCESS"));

        String invalidPin = engine.validateShippingAddress("600ABC");
        assertTrue(invalidPin.contains("valid 6-digit postal code"));
    }

    @Test
    @DisplayName("Module 7 - Payment Gateway Card and COD limits")
    public void testPaymentModule() {
        String cardSuccess = engine.processPayment("CARD", "4111222233334444", 250.0);
        assertTrue(cardSuccess.startsWith("SUCCESS"));

        String codExceeded = engine.processPayment("COD", "N/A", 8000.0);
        assertTrue(codExceeded.contains("COD not available for orders above $5,000"));
    }

    @Test
    @DisplayName("Module 8 - Order Confirmation & Inventory decrement")
    public void testOrderConfirmationModule() {
        int initialStock = engine.getStock("Sony WH-1000XM5");
        String orderRes = engine.confirmOrder("Sony WH-1000XM5", 2, "SUCCESS");
        assertTrue(orderRes.startsWith("SUCCESS"));
        assertEquals(initialStock - 2, engine.getStock("Sony WH-1000XM5"));
    }
}

