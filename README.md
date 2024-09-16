# Trello API Test Automation

This repository contains a test automation project that uses RestAssured and Cucumber BDD to test the Trello API. The project covers various API functionalities like creating boards, lists, cards, and reordering cards, among other features.

## Table of Contents

- [Tech Stack](#tech-stack)
- [Installation](#installation)
- [Configuration](#configuration)
- [Running Tests](#running-tests)
   - [Running Tests by Scenario Name](#running-tests-by-scenario-name)
   - [Running Tests by Tag](#running-tests-by-tag)
- [Viewing Test Reports](#viewing-test-reports)

## Tech Stack

- **Java**: Programming language used for writing the test cases.
- **RestAssured**: For API automation and assertions.
- **Cucumber BDD**: For defining feature files and step definitions.
- **TestNG**: For running and managing test cases.
- **Maven**: Build and dependency management tool.

## Installation

To get started with this project:

1. Clone the repository:

    ```bash
    git clone https://github.com/tskaarthick/trello-api-test.git
    ```

2. Navigate into the project directory:

    ```bash
    cd trello-api-test
    ```

3. Install dependencies:

    ```bash
    mvn clean install
    ```

## Configuration

You need to configure the following properties in `src/test/resources/configs/config.properties`:

```properties
api.key=YOUR_API_KEY
api.token=YOUR_API_TOKEN
base.url=https://api.trello.com/1
organization.id=YOUR_ORGANIZATION_ID
```

## Running Tests

Execute the tests using the following Maven command:
```bash
mvn test
```

## Running Tests by Scenario Name

```bash
mvn test -Dcucumber.options="--name 'Move Task 3 to the top of the list'"
```

## Running Tests by Tag
```bash
mvn clean test -Dcucumber.filter.tags=@regression
```

## Viewing Test Reports

After running the tests, the results will be available in the following files:

- **Surefire Reports**: Test execution results can be found in target/surefire-reports/.
- **Cucumber Reports**: The HTML Cucumber report can be viewed at target/cucumber-reports.html.
