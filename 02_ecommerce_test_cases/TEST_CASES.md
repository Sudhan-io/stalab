# E-Commerce Application Test Case Specification

**Application Name:** Amazon-Style E-Commerce Portal  
**Document Reference:** TC-ECOM-SPEC-2026-V1.0  
**Coverage Modules:** User Registration, Login, Product Search, Product Selection, Cart Management, Checkout, Payment, Order Confirmation  

---

## Module 1: User Registration
| Test Case ID | Test Scenario | Preconditions | Test Steps | Test Data | Expected Result | Actual Result | Status |
| :--- | :--- | :--- | :--- | :--- | :--- | :--- | :--- |
| **TC_REG_01** | Verify successful registration with valid inputs | User on Registration page | 1. Enter valid Full Name<br>2. Enter unique Email<br>3. Enter valid Password<br>4. Submit form | Name: "John Doe"<br>Email: "john.doe@test.com"<br>Pass: "P@ssw0rd2026!" | Account created successfully; Welcome OTP email dispatched | Account created successfully; Welcome OTP sent | PASS |
| **TC_REG_02** | Verify registration fails with duplicate existing email | Email already registered | 1. Enter existing email<br>2. Enter valid password<br>3. Submit | Email: "existing@test.com" | Validation error: "Email address already registered" | Displayed "Email address already registered" | PASS |
| **TC_REG_03** | Verify password complexity enforcement | User on Registration page | 1. Enter simple password (< 8 chars, no special char)<br>2. Submit | Pass: "pass123" | Validation error: "Password must be >= 8 chars and contain uppercase, number, symbol" | Error displayed matching policy | PASS |
| **TC_REG_04** | Verify registration form submission with empty fields | User on Registration page | 1. Leave mandatory fields blank<br>2. Click 'Register' | All fields empty | Inline red validation markers for Name, Email, Password | Required indicators displayed | PASS |

---

## Module 2: User Login
| Test Case ID | Test Scenario | Preconditions | Test Steps | Test Data | Expected Result | Actual Result | Status |
| :--- | :--- | :--- | :--- | :--- | :--- | :--- | :--- |
| **TC_LOG_01** | Verify valid user login | User registered & active | 1. Enter registered email<br>2. Enter correct password<br>3. Click 'Sign In' | Email: "user@test.com"<br>Pass: "Secret#123" | User successfully authenticated; redirected to Homepage with username banner | Authenticated; redirected to Homepage | PASS |
| **TC_LOG_02** | Verify login fails with incorrect password | User registered | 1. Enter valid email<br>2. Enter wrong password<br>3. Click 'Sign In' | Email: "user@test.com"<br>Pass: "WrongPass" | Error message: "Invalid email or password" | Displayed "Invalid email or password" | PASS |
| **TC_LOG_03** | Verify account lock after 5 consecutive failed attempts | User registered | 1. Enter incorrect password 5 times consecutively | Attempts 1 to 5 | Account locked for 30 minutes; alert message shown | Account locked after 5th attempt | PASS |
| **TC_LOG_04** | Verify password masking on login screen | User on Login page | 1. Type password into Password field | Text: "MyPassword" | Characters masked as dots or asterisks (type="password") | Masked as bullets | PASS |

---

## Module 3: Product Search
| Test Case ID | Test Scenario | Preconditions | Test Steps | Test Data | Expected Result | Actual Result | Status |
| :--- | :--- | :--- | :--- | :--- | :--- | :--- | :--- |
| **TC_SRC_01** | Verify keyword search returns exact match products | User on Homepage | 1. Type keyword in search box<br>2. Click Search button | Keyword: "Sony WH-1000XM5" | Search result page lists Sony WH-1000XM5 as top result | Item displayed at index 1 | PASS |
| **TC_SRC_02** | Verify partial keyword and case-insensitive search | User on Homepage | 1. Type lowercase partial keyword | Keyword: "laptop" | Results list laptops across brands (HP, Dell, Apple, Lenovo) | Laptops displayed correctly | PASS |
| **TC_SRC_03** | Verify search with no matching product | User on Homepage | 1. Enter non-existent keyword | Keyword: "xyznonexistentproduct999" | Informative message: "No results found for 'xyznonexistentproduct999'" | "No results found" displayed | PASS |
| **TC_SRC_04** | Verify category and price filter refinement | Search results displayed | 1. Select Category: "Electronics"<br>2. Set Price range: \$50 - \$200 | Category: Electronics<br>Price: \$50 - \$200 | Filtered catalog items strictly within \$50 to \$200 in Electronics | Filter applied correctly | PASS |

---

## Module 4: Product Selection
| Test Case ID | Test Scenario | Preconditions | Test Steps | Test Data | Expected Result | Actual Result | Status |
| :--- | :--- | :--- | :--- | :--- | :--- | :--- | :--- |
| **TC_SEL_01** | Verify navigation to Product Detail Page (PDP) | Search results listed | 1. Click on product title / thumbnail | Product: "iPhone 15 Pro" | PDP loaded with high-res images, pricing (\$999), specifications, and in-stock indicator | PDP loaded with full details | PASS |
| **TC_SEL_02** | Verify variant selection (Color, Storage) updates SKU | On PDP | 1. Select Color: "Natural Titanium"<br>2. Select Storage: "256GB" | Color: Titanium<br>Storage: 256GB | Price and SKU dynamically update to reflect chosen variant | Price updated to \$1099 | PASS |
| **TC_SEL_03** | Verify out-of-stock product selection disables 'Add to Cart' | Product has 0 stock | 1. Navigate to zero inventory item | Product: "Vintage Camera" | 'Add to Cart' button disabled; 'Out of Stock' banner displayed | Button disabled with banner | PASS |

---

## Module 5: Cart Management
| Test Case ID | Test Scenario | Preconditions | Test Steps | Test Data | Expected Result | Actual Result | Status |
| :--- | :--- | :--- | :--- | :--- | :--- | :--- | :--- |
| **TC_CRT_01** | Verify adding product to cart updates badge counter | On PDP | 1. Select quantity: 1<br>2. Click 'Add to Cart' | Qty: 1 | Item added to cart; cart icon badge increments from 0 to 1 | Cart badge shows 1 | PASS |
| **TC_CRT_02** | Verify cart quantity update recalculates subtotal | Item in Cart | 1. Open Cart<br>2. Change quantity from 1 to 3 | Unit Price: \$40<br>New Qty: 3 | Line subtotal updates to \$120; grand total recalculated | Subtotal updated to \$120 | PASS |
| **TC_CRT_03** | Verify removing product from cart | Item in Cart | 1. Click 'Remove' icon next to item | Item: "USB-C Cable" | Item removed from cart; message "Item removed"; total adjusted | Item removed; total updated | PASS |
| **TC_CRT_04** | Verify maximum allowable quantity per item limit | Item in Cart | 1. Attempt to change quantity to 15 (max limit 10) | Qty: 15 | Error message: "Maximum allowable limit for this item is 10" | Error displayed, quantity capped at 10 | PASS |

---

## Module 6: Checkout Process
| Test Case ID | Test Scenario | Preconditions | Test Steps | Test Data | Expected Result | Actual Result | Status |
| :--- | :--- | :--- | :--- | :--- | :--- | :--- | :--- |
| **TC_CHK_01** | Verify address selection from saved addresses | User logged in with items in cart | 1. Click 'Proceed to Checkout'<br>2. Choose default saved address | Address ID: "ADDR-101" | Delivery address selected; delivery date estimation shown | Address selected successfully | PASS |
| **TC_CHK_02** | Verify adding new delivery address with valid Postal Code | In Checkout | 1. Click 'Add New Address'<br>2. Enter street, city, valid PIN | PIN: "600001"<br>City: "Chennai" | New address saved and selected for current shipment | New address saved and selected | PASS |
| **TC_CHK_03** | Verify invalid ZIP / PIN Code validation | In Checkout | 1. Enter alphanumeric invalid PIN | PIN: "ABC99!" | Validation error: "Please enter a valid 6-digit postal code" | Validation error displayed | PASS |

---

## Module 7: Payment Gateway
| Test Case ID | Test Scenario | Preconditions | Test Steps | Test Data | Expected Result | Actual Result | Status |
| :--- | :--- | :--- | :--- | :--- | :--- | :--- | :--- |
| **TC_PAY_01** | Verify Credit Card payment with valid details | On Payment page | 1. Select Card<br>2. Enter 16-digit card, expiry, CVV<br>3. Submit | Card: Valid Luhn 4111...<br>Exp: 12/28, CVV: 123 | Transaction authorized; payment gateway returns SUCCESS | Payment authorized | PASS |
| **TC_PAY_02** | Verify expired card payment rejection | On Payment page | 1. Enter card with past expiry month/year | Exp: 01/22 | Validation error: "Card expiration date cannot be in the past" | Validation error shown | PASS |
| **TC_PAY_03** | Verify UPI payment format validation | On Payment page | 1. Select UPI<br>2. Enter valid VPA handle | UPI ID: "customer@okhdfcbank" | UPI request dispatched to user app; status pending approval | UPI prompt dispatched | PASS |
| **TC_PAY_04** | Verify Cash on Delivery (COD) availability threshold | Order total \$8,000 | 1. Choose COD (Max limit \$5,000) | Order total: \$8,000 | COD option disabled: "COD not available for orders above \$5,000" | COD option disabled with warning | PASS |

---

## Module 8: Order Confirmation
| Test Case ID | Test Scenario | Preconditions | Test Steps | Test Data | Expected Result | Actual Result | Status |
| :--- | :--- | :--- | :--- | :--- | :--- | :--- | :--- |
| **TC_ORD_01** | Verify order placement generates unique Order ID | Payment approved | 1. Complete payment | Order checkout payload | "Thank you for your order!" displayed with unique Order ID (e.g. ORD-2026-XXXX) | Order ID generated and displayed | PASS |
| **TC_ORD_02** | Verify inventory stock is decremented post-order | Order confirmed for 2 units | 1. Check product stock before & after | Initial stock: 10<br>Ordered: 2 | Inventory stock reduces to 8 units in database | Stock decremented to 8 | PASS |
| **TC_ORD_03** | Verify order summary email & SMS trigger | Order confirmed | 1. Inspect notification service log | Email: "user@test.com" | Email and SMS dispatched with invoice breakdown and tracking link | Dispatch log confirmed | PASS |
| **TC_ORD_04** | Verify order status tracking page | Order placed | 1. Navigate to 'My Orders'<br>2. View order status | Order: "ORD-2026-1001" | Status shows 'Order Placed', tracking timeline reflects stage 1 | Timeline stage displayed | PASS |

