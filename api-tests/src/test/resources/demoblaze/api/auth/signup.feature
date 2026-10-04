@api @signup
Feature: Sign up on the Demoblaze API
  Demoblaze answers 200 OK for both success and business errors,
  so every scenario asserts the response body, not only the status code.

  Background:
    * url baseUrl
    * def uniqueUsername = 'qa_api_' + java.util.UUID.randomUUID().toString().substring(0, 12)
    * def credentials = { username: '#(uniqueUsername)', password: '#(passwordPrefix + "signup")' }

  Scenario: A new user signs up with a unique username
    Given path 'signup'
    And request credentials
    When method post
    Then status 200
    # On success Demoblaze returns the JSON string "" (served as raw text), so parse it first.
    And match JSON.parse(response) == ''

  Scenario: Signing up twice with the same username is rejected
    Given path 'signup'
    And request credentials
    When method post
    Then status 200
    # On success Demoblaze returns the JSON string "" (served as raw text), so parse it first.
    And match JSON.parse(response) == ''

    Given path 'signup'
    And request credentials
    When method post
    Then status 200
    And match response == { errorMessage: 'This user already exist.' }
