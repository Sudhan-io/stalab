Feature: E-Commerce Shopping Journey and Authentication

  Scenario Outline: User Authentication with Different Personas
    Given the customer is on the SauceDemo login page
    When user enters credentials "<username>" and "<password>"
    And user clicks the login button
    Then login outcome should be "<outcome>" with expected text "<expectedText>"

    Examples:
      | username        | password       | outcome | expectedText                         |
      | standard_user   | secret_sauce   | SUCCESS | /inventory.html                      |
      | locked_out_user | secret_sauce   | ERROR   | Sorry, this user has been locked out |
      | standard_user   | wrong_pwd_99   | ERROR   | Username and password do not match   |

  Scenario: End-to-End Product Selection and Checkout
    Given the user is logged in as "standard_user" with "secret_sauce"
    When user adds "sauce-labs-backpack" and "sauce-labs-bike-light" to the cart
    And user proceeds through checkout with "Diana" "Prince" "90210"
    Then order confirmation message should be "Thank you for your order!"
