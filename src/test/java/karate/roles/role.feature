Feature: Request User Roles

  Scenario: All user roles can be retrieved
    Given url baseUrl + '/api/userroles'
    And headers authInfo
    When method GET
    Then status 200