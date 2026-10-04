@ui @login
Feature: Log in to the Demoblaze store
  Accounts are created through the REST API (fast, no UI dependency)
  and then used from the browser, proving both layers agree.

  Background:
    Given Ana has an account registered through the API
    And she is browsing the Demoblaze store

  Scenario: A user registered through the API logs in on the website
    When she logs in with her account
    Then the store welcomes her by username

  Scenario: A wrong password is rejected on the website
    When she logs in with a wrong password
    Then she is told "Wrong password."
