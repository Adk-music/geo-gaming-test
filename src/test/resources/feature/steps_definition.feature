Feature: GeoGaming Bet slip section test. User is not logged in.

  Scenario: The user selects a bet on the site, the bet is appears in bet slip section.
    Given User try to open site on default SportBook page
    And User searches menu "E-Sports" on homepage
    And User select category "CS:GO"
    When User choose today's fist available match
    And User take a bet
    Then Bet is displayed on Bet Slip block and Correct

  Scenario Outline: The user selects a bet on the site and add positive amount of money to take a Bet
    Given User try to open site on default SportBook page
    And User searches menu "E-Sports" on homepage
    And User select category "CS:GO"
    When User choose today's fist available match
    And User take a bet
    And Bet is displayed on Bet Slip block
    Then The user adds amount <amount> of money to place a bet
    And Check button "Place bet" is enabled

    Examples:
      | amount |
      | "10"   |
      | "200"  |

  Scenario Outline: The user selects a bet on the site and add negative amount of money to take a Bet
    Given User try to open site on default SportBook page
    And User searches menu "E-Sports" on homepage
    And User select category "CS:GO"
    When User choose today's fist available match
    And User take a bet
    And Bet is displayed on Bet Slip block
    Then The user adds amount <amount> of money to place a bet
    And Get an Error message: <errorMessage>

    Examples:
      | amount | errorMessage                            |
      | "-10"  | "The stake value above is not a number" |
      | "-200" | "The stake value above is not a number" |

  Scenario: Not Logged User place a Bet
    Given User try to open site on default SportBook page
    And User searches menu "E-Sports" on homepage
    And User select category "CS:GO"
    When User choose today's fist available match
    And User take a bet
    And Bet is displayed on Bet Slip block
    Then The user adds amount "10" of money to place a bet
    And After clicking the button Place bet, a login window opens

  Scenario: User book a bet and Booking Code is generated in bet slip section
    Given User try to open site on default SportBook page
    And User searches menu "E-Sports" on homepage
    And User select category "CS:GO"
    When User choose today's fist available match
    And User take a bet
    And Bet is displayed on Bet Slip block
    Then The user adds amount "10" of money to place a bet
    And Click button " BOOK BET "
    And Booking Code is available