Feature: Request All Charge Stations

  Scenario: All charge stations can be retrieved
    Given url baseUrl + '/api/chargestation'
    And headers authInfo
    When method GET
    Then status 200