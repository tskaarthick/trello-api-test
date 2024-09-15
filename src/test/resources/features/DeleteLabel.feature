@regression
Feature: Deleting Labels from a Trello Card

  As a Trello User
  I need to delete a label on a card
  So that they can be re-categorized

  Background:
    Given I have a valid Trello API key and token
    And I have created a board named "Test Board"
    And I have created a list named "To Do" on the board
    And I have created a card named "Task A" on the list
    And I have created a label named "Urgent" on the board
    And I have added the label "Urgent" to the card "Task A"

  @test
  Scenario: Remove a label from a card
    When I remove the label "Urgent" from the card "Task A"
    Then the card "Task A" should not have the label "Urgent"
