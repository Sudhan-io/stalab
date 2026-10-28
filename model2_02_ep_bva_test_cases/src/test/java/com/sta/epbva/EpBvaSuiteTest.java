package com.sta.epbva;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class EpBvaSuiteTest {

    private final EpBvaValidationEngine engine = new EpBvaValidationEngine();

    @Test
    @DisplayName("Verify Login BVA boundary checks (3, 4, 30, 31 chars)")
    public void testLoginBva() {
        assertTrue(engine.validateLogin("abc", "P@ss1234").contains("ERROR"));
        assertTrue(engine.validateLogin("abcd", "P@ss1234").contains("SUCCESS"));
        assertTrue(engine.validateLogin("a".repeat(30), "P@ss1234").contains("SUCCESS"));
        assertTrue(engine.validateLogin("a".repeat(31), "P@ss1234").contains("ERROR"));
    }

    @Test
    @DisplayName("Verify Quantity EP and BVA boundary checks (0, 1, 10, 11 units)")
    public void testQuantityBva() {
        assertTrue(engine.validateQuantity(0).contains("ERROR"));
        assertTrue(engine.validateQuantity(1).contains("SUCCESS"));
        assertTrue(engine.validateQuantity(5).contains("SUCCESS"));
        assertTrue(engine.validateQuantity(10).contains("SUCCESS"));
        assertTrue(engine.validateQuantity(11).contains("ERROR"));
    }

    @Test
    @DisplayName("Verify Search EP and BVA checks (1, 2, 50, 51 chars and XSS chars)")
    public void testSearchEpBva() {
        assertTrue(engine.validateSearch("a").contains("ERROR"));
        assertTrue(engine.validateSearch("ab").contains("SUCCESS"));
        assertTrue(engine.validateSearch("a".repeat(50)).contains("SUCCESS"));
        assertTrue(engine.validateSearch("a".repeat(51)).contains("ERROR"));
        assertTrue(engine.validateSearch("<script>").contains("ERROR"));
    }

    @Test
    @DisplayName("Verify Payment EP and BVA checks (Card digits, CVV, Amount boundaries)")
    public void testPaymentEpBva() {
        assertTrue(engine.validatePayment("411122223333444", "123", 100.0).contains("ERROR"));
        assertTrue(engine.validatePayment("4111222233334444", "12", 100.0).contains("ERROR"));
        assertTrue(engine.validatePayment("4111222233334444", "123", 0.50).contains("ERROR"));
        assertTrue(engine.validatePayment("4111222233334444", "123", 1.00).contains("SUCCESS"));
        assertTrue(engine.validatePayment("4111222233334444", "123", 50000.00).contains("SUCCESS"));
        assertTrue(engine.validatePayment("4111222233334444", "123", 50000.01).contains("ERROR"));
    }
}
