Feature: E-Commerce User Journeys (Authentication and Shopping Cart)
  As an e-commerce customer
  I want to authenticate, browse catalog, and manage my cart
  So that I can purchase desired products seamlessly

  Background:
    Given the user navigates to the e-commerce storefront

  @Smoke @Regression
  Scenario: Successful user login with valid credentials
    When the user enters username "standard_user" and password "secret_sauce"
    And clicks on the login button
    Then the user should be redirected to the product catalog page
    And the catalog should display available products

  @Negative @Regression
  Scenario Outline: Login failure with invalid credentials
    When the user enters username "<username>" and password "<password>"
    And clicks on the login button
    Then an error message containing "<expected_message>" should be displayed

    Examples:
      | username         | password        | expected_message                               |
      | locked_out_user  | secret_sauce    | Sorry, this user has been locked out           |
      | invalid_customer | wrong_password  | Username and password do not match any user   |
      |                  | secret_sauce    | Username is required                           |

  @Cart @Regression
  Scenario: Adding product to cart and verifying cart contents
    Given the user is logged in with username "standard_user" and password "secret_sauce"
    When the user selects the product "Sauce Labs Backpack"
    And clicks the add to cart button
    Then the shopping cart badge count should be "1"
    When the user opens the shopping cart
    Then the cart should contain the item "Sauce Labs Backpack"
    When the user removes "Sauce Labs Backpack" from the cart
    Then the item should no longer be present in the cart

