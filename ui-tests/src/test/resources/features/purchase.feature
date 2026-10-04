@ui @purchase
Feature: Buy products on the Demoblaze store
  As a customer of the store
  I want to put several products in my cart and check out
  So that I receive a receipt that matches my order

  Background:
    Given Ana is browsing the Demoblaze store

  Scenario: A guest buys two products and receives a matching receipt
    When she adds the following products to the cart
      | Samsung galaxy s6 |
      | Nokia lumia 1520  |
    Then the cart lists exactly those products
    And the cart total is the sum of their catalog prices
    When she places the order with the billing details of the "guest customer"
    Then the purchase is confirmed with a receipt that matches the order

  Scenario: The order form requires a name and a credit card
    When she adds the following products to the cart
      | Sony xperia z5 |
    And she tries to purchase without billing details
    Then she is told "Please fill out Name and Creditcard."
