Feature: Request All Charge Sites

  Scenario: All charge sites can be retrieved for mobile
    Given url baseUrl + '/api/chargesite'
    And headers authInfo
    When method GET
    Then status 200

  Scenario: All charge sites can be retrieved with complete information for the Management Application
    Given url baseUrl + '/api/chargesite/all'
    And headers authInfo
    When method GET
    Then status 200