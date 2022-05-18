Feature: Authentication

  Background:
    * url authUrl
    * def data = login_request

  Scenario: A user can authenticate to login with the correct username and password

    Given url authUrl
    And header Authorization =  call read('basic-auth.js') data
    When method POST
    Then status 200


