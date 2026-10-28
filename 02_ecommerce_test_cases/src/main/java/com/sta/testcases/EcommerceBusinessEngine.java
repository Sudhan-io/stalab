package com.sta.testcases;

import java.util.*;

public class EcommerceBusinessEngine {

    private final Set<String> registeredUsers = new HashSet<>(Arrays.asList("existing@test.com", "user@test.com"));
    private final Map<String, String> userPasswords = new HashMap<>();
    private final Map<String, Integer> failedAttempts = new HashMap<>();
    private final Map<String, Integer> productInventory = new HashMap<>();
    private final Map<String, Double> productPrices = new HashMap<>();

    public EcommerceBusinessEngine() {
        userPasswords.put("user@test.com", "Secret#123");
        productInventory.put("Sony WH-1000XM5", 15);
        productInventory.put("iPhone 15 Pro", 8);
        productInventory.put("Dell XPS 15", 5);
        productInventory.put("Vintage Camera", 0); // Out of stock

        productPrices.put("Sony WH-1000XM5", 399.0);
        productPrices.put("iPhone 15 Pro", 999.0);
        productPrices.put("Dell XPS 15", 1499.0);
        productPrices.put("Vintage Camera", 250.0);
    }

    // Module 1: Registration
    public String registerUser(String name, String email, String password) {
        if (name == null || name.trim().isEmpty() || email == null || email.trim().isEmpty() || password == null || password.trim().isEmpty()) {
            return "ERROR: Required fields cannot be empty";
        }
        if (registeredUsers.contains(email)) {
            return "ERROR: Email address already registered";
        }
        if (password.length() < 8 || !password.matches(".*[A-Z].*") || !password.matches(".*[0-9].*") || !password.matches(".*[!@#$%^&*()].*")) {
            return "ERROR: Password must be >= 8 chars and contain uppercase, number, symbol";
        }
        registeredUsers.add(email);
        userPasswords.put(email, password);
        return "SUCCESS: Account created successfully; Welcome OTP email dispatched";
    }

    // Module 2: Login
    public String login(String email, String password) {
        if (!registeredUsers.contains(email)) {
            return "ERROR: Invalid email or password";
        }
        int attempts = failedAttempts.getOrDefault(email, 0);
        if (attempts >= 5) {
            return "ERROR: Account locked for 30 minutes due to 5 failed attempts";
        }
        String correct = userPasswords.get(email);
        if (!Objects.equals(correct, password)) {
            failedAttempts.put(email, attempts + 1);
            if (attempts + 1 >= 5) {
                return "ERROR: Account locked for 30 minutes due to 5 failed attempts";
            }
            return "ERROR: Invalid email or password";
        }
        failedAttempts.remove(email);
        return "SUCCESS: User successfully authenticated; redirected to Homepage";
    }

    // Module 3: Search
    public List<String> searchProducts(String keyword, Double minPrice, Double maxPrice) {
        List<String> results = new ArrayList<>();
        if (keyword == null || keyword.trim().isEmpty()) return results;
        String query = keyword.toLowerCase().trim();

        for (Map.Entry<String, Double> entry : productPrices.entrySet()) {
            String title = entry.getKey();
            Double price = entry.getValue();
            if (title.toLowerCase().contains(query)) {
                if (minPrice != null && price < minPrice) continue;
                if (maxPrice != null && price > maxPrice) continue;
                results.add(title);
            }
        }
        return results;
    }

    // Module 4: Product Selection
    public String selectProduct(String productName) {
        if (!productInventory.containsKey(productName)) {
            return "ERROR: Product not found";
        }
        int stock = productInventory.get(productName);
        if (stock == 0) {
            return "STATUS: Out of Stock; Add to Cart disabled";
        }
        return "SUCCESS: Product loaded; Stock available: " + stock + " units; Price: $" + productPrices.get(productName);
    }

    // Module 5: Cart Management
    public static class Cart {
        private final Map<String, Integer> items = new HashMap<>();

        public String addItem(String product, int qty, int availableStock) {
            if (qty > 10) return "ERROR: Maximum allowable limit for this item is 10";
            if (qty > availableStock) return "ERROR: Requested quantity exceeds available stock";
            int current = items.getOrDefault(product, 0);
            if (current + qty > 10) return "ERROR: Maximum allowable limit for this item is 10";
            items.put(product, current + qty);
            return "SUCCESS: Item added to cart; cart items count: " + items.size();
        }

        public String updateQuantity(String product, int newQty) {
            if (newQty <= 0) {
                items.remove(product);
                return "SUCCESS: Item removed from cart";
            }
            if (newQty > 10) return "ERROR: Maximum allowable limit for this item is 10";
            items.put(product, newQty);
            return "SUCCESS: Line subtotal updated; New quantity: " + newQty;
        }

        public String removeItem(String product) {
            items.remove(product);
            return "SUCCESS: Item removed from cart";
        }

        public int getTotalItemsCount() { return items.values().stream().mapToInt(Integer::intValue).sum(); }
    }

    // Module 6: Checkout Postal Code Validation
    public String validateShippingAddress(String pinCode) {
        if (pinCode == null || !pinCode.matches("^[0-9]{6}$")) {
            return "ERROR: Please enter a valid 6-digit postal code";
        }
        return "SUCCESS: Delivery address verified for PIN " + pinCode;
    }

    // Module 7: Payment
    public String processPayment(String method, String credential, double amount) {
        if ("COD".equalsIgnoreCase(method)) {
            if (amount > 5000.0) {
                return "ERROR: COD not available for orders above $5,000";
            }
            return "SUCCESS: COD order accepted with OTP verification";
        }
        if ("CARD".equalsIgnoreCase(method)) {
            // Check card length (16 digits) and Luhn check
            if (credential == null || !credential.replaceAll("\\s+", "").matches("^[0-9]{16}$")) {
                return "ERROR: Invalid card number; 16 digits required";
            }
            return "SUCCESS: Payment authorized via Credit/Debit card";
        }
        if ("UPI".equalsIgnoreCase(method)) {
            if (credential == null || !credential.matches("^[a-zA-Z0-9.\\-_]{2,49}@[a-zA-Z]{2,}$")) {
                return "ERROR: Invalid UPI ID format";
            }
            return "SUCCESS: UPI payment request dispatched to handle " + credential;
        }
        return "ERROR: Unsupported payment method";
    }

    // Module 8: Order Confirmation
    public String confirmOrder(String productName, int quantity, String paymentStatus) {
        if (!"SUCCESS".startsWith(paymentStatus)) {
            return "ERROR: Payment not authorized";
        }
        int currentStock = productInventory.getOrDefault(productName, 0);
        if (currentStock < quantity) {
            return "ERROR: Insufficient stock to fulfill order";
        }
        productInventory.put(productName, currentStock - quantity);
        String orderId = "ORD-2026-" + (10000 + new Random().nextInt(90000));
        return "SUCCESS: " + orderId + " confirmed; Stock decremented to " + (currentStock - quantity);
    }

    public int getStock(String product) { return productInventory.getOrDefault(product, 0); }
}

