package com.sta.epbva;

import java.util.ArrayList;
import java.util.List;

public class EpBvaTestRunner {

    public static class TestCase {
        private final String id;
        private final String module;
        private final String technique; // EP or BVA
        private final String description;
        private final String testData;
        private final String expectedResult;

        public TestCase(String id, String module, String technique, String description, String testData, String expectedResult) {
            this.id = id;
            this.module = module;
            this.technique = technique;
            this.description = description;
            this.testData = testData;
            this.expectedResult = expectedResult;
        }

        public String getId() { return id; }
        public String getModule() { return module; }
        public String getTechnique() { return technique; }
        public String getDescription() { return description; }
        public String getTestData() { return testData; }
        public String getExpectedResult() { return expectedResult; }
    }

    public static List<TestCase> getStandardTestCases() {
        List<TestCase> cases = new ArrayList<>();

        // 1. LOGIN MODULE (EP & BVA)
        cases.add(new TestCase("TC_M2_LOG_01", "Login", "EP", "Valid username nominal length (10 chars)", "john_doe10 / Secr@t12", "SUCCESS: Login credentials valid"));
        cases.add(new TestCase("TC_M2_LOG_02", "Login", "BVA", "Username below min boundary (3 chars)", "abc / Secr@t12", "ERROR: Username length must be at least 4 characters"));
        cases.add(new TestCase("TC_M2_LOG_03", "Login", "BVA", "Username at min boundary (4 chars)", "john / Secr@t12", "SUCCESS: Login credentials valid"));
        cases.add(new TestCase("TC_M2_LOG_04", "Login", "BVA", "Username at max boundary (30 chars)", "user_1234567890123456789012345 / Secr@t12", "SUCCESS: Login credentials valid"));
        cases.add(new TestCase("TC_M2_LOG_05", "Login", "BVA", "Username above max boundary (31 chars)", "user_12345678901234567890123456 / Secr@t12", "ERROR: Username length must not exceed 30 characters"));
        cases.add(new TestCase("TC_M2_LOG_06", "Login", "BVA", "Password below min boundary (7 chars)", "john / Pass#1", "ERROR: Password must be at least 8 characters"));
        cases.add(new TestCase("TC_M2_LOG_07", "Login", "BVA", "Password at min boundary (8 chars)", "john / P@ssw0rd", "SUCCESS: Login credentials valid"));
        cases.add(new TestCase("TC_M2_LOG_08", "Login", "EP", "Password missing special character", "john / Password12", "ERROR: Password must contain uppercase, number, and special character"));

        // 2. PRODUCT QUANTITY MODULE (EP & BVA)
        cases.add(new TestCase("TC_M2_QTY_01", "Quantity", "BVA", "Quantity below min boundary (0 units)", "qty = 0", "ERROR: Minimum order quantity is 1 unit"));
        cases.add(new TestCase("TC_M2_QTY_02", "Quantity", "BVA", "Quantity at min boundary (1 unit)", "qty = 1", "SUCCESS: Quantity 1 accepted"));
        cases.add(new TestCase("TC_M2_QTY_03", "Quantity", "EP",  "Quantity nominal valid partition (5 units)", "qty = 5", "SUCCESS: Quantity 5 accepted"));
        cases.add(new TestCase("TC_M2_QTY_04", "Quantity", "BVA", "Quantity at max boundary (10 units)", "qty = 10", "SUCCESS: Quantity 10 accepted"));
        cases.add(new TestCase("TC_M2_QTY_05", "Quantity", "BVA", "Quantity above max boundary (11 units)", "qty = 11", "ERROR: Maximum allowable order limit is 10 units"));

        // 3. SEARCH FIELDS MODULE (EP & BVA)
        cases.add(new TestCase("TC_M2_SRC_01", "Search", "BVA", "Query below min length (1 char)", "'a'", "ERROR: Search query must be at least 2 characters"));
        cases.add(new TestCase("TC_M2_SRC_02", "Search", "BVA", "Query at min length (2 chars)", "'TV'", "SUCCESS: Search query valid"));
        cases.add(new TestCase("TC_M2_SRC_03", "Search", "EP",  "Query valid alphanumeric search", "'Sony Headphones 1000'", "SUCCESS: Search query valid"));
        cases.add(new TestCase("TC_M2_SRC_04", "Search", "BVA", "Query above max length (51 chars)", "51 characters long string...", "ERROR: Search query exceeds maximum 50 characters"));
        cases.add(new TestCase("TC_M2_SRC_05", "Search", "EP",  "Query with invalid SQL/XSS characters", "'laptop <script>'", "ERROR: Search query contains prohibited special characters"));

        // 4. PAYMENT DETAILS MODULE (EP & BVA)
        cases.add(new TestCase("TC_M2_PAY_01", "Payment", "BVA", "Card number below boundary (15 digits)", "411122223333444", "ERROR: Card number must be exactly 16 numeric digits"));
        cases.add(new TestCase("TC_M2_PAY_02", "Payment", "BVA", "Card number valid boundary (16 digits)", "4111222233334444", "SUCCESS: Payment authorized"));
        cases.add(new TestCase("TC_M2_PAY_03", "Payment", "BVA", "CVV below boundary (2 digits)", "12", "ERROR: CVV must be exactly 3 numeric digits"));
        cases.add(new TestCase("TC_M2_PAY_04", "Payment", "BVA", "CVV valid boundary (3 digits)", "890", "SUCCESS: Payment authorized"));
        cases.add(new TestCase("TC_M2_PAY_05", "Payment", "BVA", "Transaction amount below min ($0.50)", "$0.50", "ERROR: Minimum payable transaction amount is $1.00"));
        cases.add(new TestCase("TC_M2_PAY_06", "Payment", "BVA", "Transaction amount at min ($1.00)", "$1.00", "SUCCESS: Payment authorized"));
        cases.add(new TestCase("TC_M2_PAY_07", "Payment", "BVA", "Transaction amount above max ($50,001.00)", "$50001.00", "ERROR: Maximum payable transaction limit is $50,000.00"));

        return cases;
    }

    public static void main(String[] args) {
        System.out.println("==========================================================================================");
        System.out.println(" MODEL 2 - EXPERIMENT 2: EQUIVALENCE PARTITIONING & BOUNDARY VALUE ANALYSIS TEST SUITE");
        System.out.println("==========================================================================================");

        EpBvaValidationEngine engine = new EpBvaValidationEngine();
        List<TestCase> testCases = getStandardTestCases();

        System.out.printf("%-14s | %-9s | %-4s | %-44s | %-6s%n", "Test ID", "Module", "Type", "Scenario / Boundary", "Result");
        System.out.println("------------------------------------------------------------------------------------------");

        int passed = 0;
        int failed = 0;

        for (TestCase tc : testCases) {
            String actual = "";
            switch (tc.getId()) {
                case "TC_M2_LOG_01": actual = engine.validateLogin("john_doe10", "Secr@t12"); break;
                case "TC_M2_LOG_02": actual = engine.validateLogin("abc", "Secr@t12"); break;
                case "TC_M2_LOG_03": actual = engine.validateLogin("john", "Secr@t12"); break;
                case "TC_M2_LOG_04": actual = engine.validateLogin("user_1234567890123456789012345", "Secr@t12"); break;
                case "TC_M2_LOG_05": actual = engine.validateLogin("user_12345678901234567890123456", "Secr@t12"); break;
                case "TC_M2_LOG_06": actual = engine.validateLogin("john", "Pass#1"); break;
                case "TC_M2_LOG_07": actual = engine.validateLogin("john", "P@ssw0rd"); break;
                case "TC_M2_LOG_08": actual = engine.validateLogin("john", "Password12"); break;

                case "TC_M2_QTY_01": actual = engine.validateQuantity(0); break;
                case "TC_M2_QTY_02": actual = engine.validateQuantity(1); break;
                case "TC_M2_QTY_03": actual = engine.validateQuantity(5); break;
                case "TC_M2_QTY_04": actual = engine.validateQuantity(10); break;
                case "TC_M2_QTY_05": actual = engine.validateQuantity(11); break;

                case "TC_M2_SRC_01": actual = engine.validateSearch("a"); break;
                case "TC_M2_SRC_02": actual = engine.validateSearch("TV"); break;
                case "TC_M2_SRC_03": actual = engine.validateSearch("Sony Headphones 1000"); break;
                case "TC_M2_SRC_04": actual = engine.validateSearch("This search query is deliberately engineered to exceed fifty characters length"); break;
                case "TC_M2_SRC_05": actual = engine.validateSearch("laptop <script>"); break;

                case "TC_M2_PAY_01": actual = engine.validatePayment("411122223333444", "123", 100.0); break;
                case "TC_M2_PAY_02": actual = engine.validatePayment("4111222233334444", "123", 100.0); break;
                case "TC_M2_PAY_03": actual = engine.validatePayment("4111222233334444", "12", 100.0); break;
                case "TC_M2_PAY_04": actual = engine.validatePayment("4111222233334444", "890", 100.0); break;
                case "TC_M2_PAY_05": actual = engine.validatePayment("4111222233334444", "890", 0.50); break;
                case "TC_M2_PAY_06": actual = engine.validatePayment("4111222233334444", "890", 1.00); break;
                case "TC_M2_PAY_07": actual = engine.validatePayment("4111222233334444", "890", 50001.00); break;
            }

            boolean isPass = actual.equals(tc.getExpectedResult());
            if (isPass) {
                passed++;
                System.out.printf("%-14s | %-9s | %-4s | %-44s | [PASS]%n",
                    tc.getId(), tc.getModule(), tc.getTechnique(), tc.getDescription());
            } else {
                failed++;
                System.out.printf("%-14s | %-9s | %-4s | %-44s | [FAIL]%n",
                    tc.getId(), tc.getModule(), tc.getTechnique(), tc.getDescription());
            }
        }

        System.out.println("------------------------------------------------------------------------------------------");
        double rate = (passed * 100.0) / testCases.size();
        System.out.printf("Total EP & BVA Test Cases: %d | Passed: %d | Failed: %d | Pass Rate: %.1f%%%n",
            testCases.size(), passed, failed, rate);
        System.out.println("==========================================================================================");
    }
}
