Feature: As a parabank user I want to transfer fund from one account to another and verify the same in transactions

  Scenario Outline: Transfer different amount between accounts
    Given user makes the api call to transfer fund- <funds>
    Then the status of response should be 200
    And user verify the response message for the transferred fund- <funds>

    Examples:
      | funds |
      | 5000  |
      | -5000 |
      | 0     |

  Scenario Outline: Verify correct amount is transferred
    Given user makes the api call for account activity
    Then the status of response should be 200
    And user verify the response message for the credited amount- <funds>

    Examples:
      | funds |
      | 5000  |
      | -5000 |
      | 0     |
