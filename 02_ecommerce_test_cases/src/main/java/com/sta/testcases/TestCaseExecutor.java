package com.sta.testcases;

import java.util.*;

public class TestCaseExecutor {

    public static void main(String[] args) {
        System.out.println("==========================================================================================");
        System.out.println(" STA LAB - EXPERIMENT 2: E-COMMERCE TEST CASE SUITE AUTOMATED EXECUTION");
        System.out.println("==========================================================================================");

        EcommerceBusinessEngine engine = new EcommerceBusinessEngine();
        List<TestCase> testCases = new ArrayList<>();

        // 1. Registration
        testCases.add(new TestCase("TC_REG_01", "Registration", "Valid Registration", "On Reg Page",
            "Submit valid name, unique email, strong password", "Name: Alice, Email: alice@test.com, Pass: P@ss1234", "SUCCESS"));
        testCases.add(new TestCase("TC_REG_02", "Registration", "Duplicate Email Rejection", "Email exists",
            "Submit existing email", "Email: existing@test.com", "ERROR: Email address already registered"));
        testCases.add(new TestCase("TC_REG_03", "Registration", "Weak Password Validation", "On Reg Page",
            "Submit simple password", "Pass: 12345", "ERROR: Password must be >= 8 chars and contain uppercase, number, symbol"));

        // 2. Login
        testCases.add(new TestCase("TC_LOG_01", "Login", "Valid Login", "User registered",
            "Enter correct email & password", "user@test.com / Secret#123", "SUCCESS"));
        testCases.add(new TestCase("TC_LOG_02", "Login", "Invalid Password", "User registered",
            "Enter incorrect password", "user@test.com / WrongPass", "ERROR: Invalid email or password"));
        testCases.add(new TestCase("TC_LOG_03", "Login", "5 Failed Attempts Lockout", "User registered",
            "Enter wrong password 5 times", "user@test.com / Fail#1..5", "ERROR: Account locked for 30 minutes due to 5 failed attempts"));

        // 3. Search
        testCases.add(new TestCase("TC_SRC_01", "Search", "Exact Product Search", "On Catalog",
            "Search for Sony WH-1000XM5", "Sony WH-1000XM5", "SUCCESS: Found Sony WH-1000XM5"));
        testCases.add(new TestCase("TC_SRC_02", "Search", "No Results Found", "On Catalog",
            "Search for non-existent product", "unknownitemxyz", "SUCCESS: 0 results returned"));

        // 4. Product Selection
        testCases.add(new TestCase("TC_SEL_01", "Product Selection", "In-Stock Product Selection", "Catalog open",
            "Select iPhone 15 Pro", "iPhone 15 Pro", "SUCCESS: Product loaded; Stock available"));
        testCases.add(new TestCase("TC_SEL_02", "Product Selection", "Out-of-Stock Badge Check", "Catalog open",
            "Select Vintage Camera", "Vintage Camera", "STATUS: Out of Stock; Add to Cart disabled"));

        // 5. Cart Management
        testCases.add(new TestCase("TC_CRT_01", "Cart Management", "Add Item to Cart", "Product selected",
            "Add 2 units to cart", "iPhone 15 Pro (qty: 2)", "SUCCESS: Item added to cart"));
        testCases.add(new TestCase("TC_CRT_02", "Cart Management", "Max Item Limit Check", "Cart open",
            "Attempt to add 12 units (limit 10)", "qty: 12", "ERROR: Maximum allowable limit for this item is 10"));

        // 6. Checkout
        testCases.add(new TestCase("TC_CHK_01", "Checkout", "Valid PIN Code Validation", "Items in cart",
            "Enter 6-digit postal code", "PIN: 600001", "SUCCESS: Delivery address verified for PIN 600001"));
        testCases.add(new TestCase("TC_CHK_02", "Checkout", "Invalid PIN Code Validation", "Items in cart",
            "Enter non-numeric PIN", "PIN: 6000AB", "ERROR: Please enter a valid 6-digit postal code"));

        // 7. Payment
        testCases.add(new TestCase("TC_PAY_01", "Payment", "Valid Credit Card Payment", "Checkout ready",
            "Submit 16-digit card", "Card: 4111222233334444", "SUCCESS: Payment authorized via Credit/Debit card"));
        testCases.add(new TestCase("TC_PAY_02", "Payment", "COD Order Amount Limit Check", "Checkout ready",
            "Submit COD for $7500 order", "COD / $7500", "ERROR: COD not available for orders above $5,000"));

        // 8. Order Confirmation
        testCases.add(new TestCase("TC_ORD_01", "Order Confirmation", "Order Confirmation & Stock Decrement", "Payment done",
            "Confirm order of 2 units of Sony WH-1000XM5", "2 units / Sony WH-1000XM5", "SUCCESS: ORD- confirmed"));

        // Execute and evaluate each test case
        int passed = 0;
        int failed = 0;

        System.out.printf("%-11s | %-16s | %-32s | %-8s%n", "TC ID", "Module", "Scenario", "Status");
        System.out.println("------------------------------------------------------------------------------------------");

        for (TestCase tc : testCases) {
            String actual;
            switch (tc.getId()) {
                case "TC_REG_01":
                    actual = engine.registerUser("Alice", "alice@test.com", "P@ss1234");
                    break;
                case "TC_REG_02":
                    actual = engine.registerUser("Existing", "existing@test.com", "P@ss1234");
                    break;
                case "TC_REG_03":
                    actual = engine.registerUser("Weak", "weak@test.com", "12345");
                    break;
                case "TC_LOG_01":
                    actual = engine.login("user@test.com", "Secret#123");
                    break;
                case "TC_LOG_02":
                    actual = engine.login("user@test.com", "WrongPass");
                    break;
                case "TC_LOG_03":
                    engine.login("user@test.com", "bad1");
                    engine.login("user@test.com", "bad2");
                    engine.login("user@test.com", "bad3");
                    engine.login("user@test.com", "bad4");
                    actual = engine.login("user@test.com", "bad5");
                    break;
                case "TC_SRC_01":
                    List<String> r1 = engine.searchProducts("Sony", null, null);
                    actual = (r1.contains("Sony WH-1000XM5")) ? "SUCCESS: Found Sony WH-1000XM5" : "FAILED";
                    break;
                case "TC_SRC_02":
                    List<String> r2 = engine.searchProducts("unknownitemxyz", null, null);
                    actual = (r2.isEmpty()) ? "SUCCESS: 0 results returned" : "FAILED";
                    break;
                case "TC_SEL_01":
                    actual = engine.selectProduct("iPhone 15 Pro");
                    break;
                case "TC_SEL_02":
                    actual = engine.selectProduct("Vintage Camera");
                    break;
                case "TC_CRT_01":
                    EcommerceBusinessEngine.Cart cart = new EcommerceBusinessEngine.Cart();
                    actual = cart.addItem("iPhone 15 Pro", 2, 8);
                    break;
                case "TC_CRT_02":
                    EcommerceBusinessEngine.Cart cart2 = new EcommerceBusinessEngine.Cart();
                    actual = cart2.addItem("iPhone 15 Pro", 12, 8);
                    break;
                case "TC_CHK_01":
                    actual = engine.validateShippingAddress("600001");
                    break;
                case "TC_CHK_02":
                    actual = engine.validateShippingAddress("6000AB");
                    break;
                case "TC_PAY_01":
                    actual = engine.processPayment("CARD", "4111222233334444", 399.0);
                    break;
                case "TC_PAY_02":
                    actual = engine.processPayment("COD", "N/A", 7500.0);
                    break;
                case "TC_ORD_01":
                    actual = engine.confirmOrder("Sony WH-1000XM5", 2, "SUCCESS");
                    break;
                default:
                    actual = "UNKNOWN TC";
            }

            tc.setActualResult(actual);
            boolean isMatch = actual.toLowerCase().contains(tc.getExpectedResult().toLowerCase()) ||
                             (tc.getExpectedResult().contains("SUCCESS") && actual.contains("SUCCESS")) ||
                             (tc.getExpectedResult().contains("ERROR") && actual.contains("ERROR")) ||
                             (tc.getExpectedResult().contains("STATUS") && actual.contains("STATUS"));

            if (isMatch) {
                tc.setStatus("PASS");
                passed++;
            } else {
                tc.setStatus("FAIL");
                failed++;
            }

            System.out.printf("%-11s | %-16s | %-32s | %-8s%n",
                tc.getId(), tc.getModule(), tc.getScenario(), "[" + tc.getStatus() + "]");
        }

        System.out.println("------------------------------------------------------------------------------------------");
        System.out.printf("Total Test Cases: %d | Passed: %d | Failed: %d | Pass Rate: %.1f%%%n",
            testCases.size(), passed, failed, (passed * 100.0 / testCases.size()));
        System.out.println("==========================================================================================");
    }
}

