Feature: System Health
  # The application has a health endpoint to check it is still functioning
  Scenario: Host server calls health end point to validate system health

    Given url 'http://localhost:5000/health'
    When method GET
    Then status 200