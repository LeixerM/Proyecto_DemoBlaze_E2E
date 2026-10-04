@api @login
Feature: Log in on the Demoblaze API

  Background:
    * url baseUrl
    * def user = call read('classpath:demoblaze/api/common/new-user.feature')
    * def credentials = user.credentials

  Scenario: A registered user logs in and receives an auth token
    Given path 'login'
    And request credentials
    When method post
    Then status 200
    # The token arrives as a JSON string literal: "Auth_token: <base64>".
    * def body = JSON.parse(response)
    And match body == '#regex Auth_token: [A-Za-z0-9+/]+={0,2}'
    * def token = body.replace('Auth_token: ', '')
    * def decodedToken = new java.lang.String(java.util.Base64.getDecoder().decode(token))
    * match decodedToken contains credentials.username

  Scenario: Logging in with a wrong password is rejected
    Given path 'login'
    And request { username: '#(credentials.username)', password: 'not-the-password' }
    When method post
    Then status 200
    And match response == { errorMessage: 'Wrong password.' }

  Scenario: Logging in with an unknown username is rejected
    Given path 'login'
    And request { username: '#("unknown_" + credentials.username)', password: '#(credentials.password)' }
    When method post
    Then status 200
    And match response == { errorMessage: 'User does not exist.' }
