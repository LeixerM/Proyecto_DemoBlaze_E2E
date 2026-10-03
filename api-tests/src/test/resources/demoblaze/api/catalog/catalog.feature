@api @catalog
Feature: Product catalog on the Demoblaze API

  Background:
    * url baseUrl
    * def product = read('classpath:demoblaze/api/schemas/product.json')

  Scenario: The home page entries match the product schema
    Given path 'entries'
    When method get
    Then status 200
    And match response == { Items: '#[_ > 0] product', LastEvaluatedKey: '##object' }
    And match response.Items contains deep { title: 'Samsung galaxy s6', price: 360 }

  Scenario: A single product can be viewed by id
    Given path 'view'
    And request { id: '1' }
    When method post
    Then status 200
    And match response == product
    And match response contains { id: 1, title: 'Samsung galaxy s6', price: 360 }

  Scenario Outline: Filtering by category returns only <category> products
    Given path 'bycat'
    And request { cat: '<category>' }
    When method post
    Then status 200
    And match response.Items == '#[_ > 0] product'
    And match each response.Items contains { cat: '<category>' }

    Examples:
      | category |
      | phone    |
      | notebook |
      | monitor  |

  Scenario: Viewing an unknown product returns a not-found message
    Given path 'view'
    And request { id: '999999' }
    When method post
    Then status 200
    And match response == { errorMessage: 'Not found.' }
