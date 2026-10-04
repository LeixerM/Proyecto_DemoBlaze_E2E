@ui @cart
Feature: Manage the shopping cart

  Background:
    Given Ana is browsing the Demoblaze store

  Scenario: Removing a product updates the cart contents and the total
    Given she adds the following products to the cart
      | Samsung galaxy s6 |
      | Nokia lumia 1520  |
    When she removes "Nokia lumia 1520" from the cart
    Then the cart lists exactly these products
      | Samsung galaxy s6 |
    And the cart total is the sum of their catalog prices
