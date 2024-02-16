Feature: lddtool
  As an lddtool user
  I want to run lddtool with specific parameters
  So that I can verify if it provides the expected response

  Scenario Outline: Running lddtool with different parameters
    Given I have lddtool installed
    When I run lddtool with "<parameters>"
    Then I should receive "<expectedResponse>"

    Examples:
      | parameters        | expectedResponse         |
      | --help            | Shows help information   |
      | --version         | Displays version number  |
      | some invalid input| Shows error message      |

  Scenario: Specific functionality test
    Given I have lddtool installed
    And I have a specific configuration
    When I run lddtool with "specific parameters"
    Then I should receive "a specific expected response"
