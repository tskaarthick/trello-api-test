@regression
Feature: Reordering Cards on a Trello List

  As a Trello User
  I need to reorder cards on a list
  So that they can be prioritized

  Background:
    Given I have a valid Trello API key and token
    And I have created a board named "Test Board"
    And I have created a list named "To Do" on the board
    And I have created cards "Task 1", "Task 2", "Task 3" on the list

  @test
  Scenario: Move "Task 3" to the top of the list
    When I move the card "Task 3" to position "top"
    Then the card "Task 3" should be at the first position in the list

  @test
  Scenario: Move "Task 2" to the bottom of the list
    When I move the card "Task 2" to position "bottom"
    Then the card "Task 2" should be at the last position in the list
