package com.sta.epbva;

public class EpBvaValidationEngine {

    // 1. Login Validation: Username [4..30], Password [8..20] with complexity
    public String validateLogin(String username, String password) {
        if (username == null || username.length() < 4) {
            return "ERROR: Username length must be at least 4 characters";
        }
        if (username.length() > 30) {
            return "ERROR: Username length must not exceed 30 characters";
        }
        if (password == null || password.length() < 8) {
            return "ERROR: Password must be at least 8 characters";
        }
        if (password.length() > 20) {
            return "ERROR: Password must not exceed 20 characters";
        }
        if (!password.matches("^(?=.*[A-Z])(?=.*[0-9])(?=.*[@#$%^&+=!]).+$")) {
            return "ERROR: Password must contain uppercase, number, and special character";
        }
        return "SUCCESS: Login credentials valid";
    }

    // 2. Product Quantity Validation: [1..10]
    public String validateQuantity(int quantity) {
        if (quantity < 1) {
            return "ERROR: Minimum order quantity is 1 unit";
        }
        if (quantity > 10) {
            return "ERROR: Maximum allowable order limit is 10 units";
        }
        return "SUCCESS: Quantity " + quantity + " accepted";
    }

    // 3. Search Query Validation: [2..50] characters, alphanumeric & spaces
    public String validateSearch(String query) {
        if (query == null || query.trim().length() < 2) {
            return "ERROR: Search query must be at least 2 characters";
        }
        if (query.length() > 50) {
            return "ERROR: Search query exceeds maximum 50 characters";
        }
        if (!query.matches("^[a-zA-Z0-9 ]+$")) {
            return "ERROR: Search query contains prohibited special characters";
        }
        return "SUCCESS: Search query valid";
    }

    // 4. Payment Details Validation: Card 16 digits, CVV 3 digits, Amount [$1.00..$50,000.00]
    public String validatePayment(String cardNumber, String cvv, double amount) {
        if (cardNumber == null || !cardNumber.matches("^\\d{16}$")) {
            return "ERROR: Card number must be exactly 16 numeric digits";
        }
        if (cvv == null || !cvv.matches("^\\d{3}$")) {
            return "ERROR: CVV must be exactly 3 numeric digits";
        }
        if (amount < 1.00) {
            return "ERROR: Minimum payable transaction amount is $1.00";
        }
        if (amount > 50000.00) {
            return "ERROR: Maximum payable transaction limit is $50,000.00";
        }
        return "SUCCESS: Payment authorized";
    }
}
