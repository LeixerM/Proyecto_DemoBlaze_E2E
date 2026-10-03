@ignore
Feature: Reusable helper - sign up a brand-new user and return its credentials

  Scenario: Sign up a unique user
    * def username = 'qa_api_' + java.util.UUID.randomUUID().toString().substring(0, 12)
    * def password = passwordPrefix + java.util.UUID.randomUUID().toString().substring(0, 8)
    * def credentials = { username: '#(username)', password: '#(password)' }
    Given url baseUrl
    And path 'signup'
    And request credentials
    When method post
    Then status 200
    # On success Demoblaze returns the JSON string "" (served as raw text), so parse it first.
    And match JSON.parse(response) == ''
