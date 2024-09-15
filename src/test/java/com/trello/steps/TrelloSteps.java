package com.trello.steps;

import com.trello.utils.ConfigLoader;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;

import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static io.restassured.RestAssured.given;

public class TrelloSteps {

    private static final Logger logger = LoggerFactory.getLogger(TrelloSteps.class);

    private String apiKey;
    private String apiToken;
    private String baseUrl;
    private String boardId;
    private String listId;
    private Map<String, String> cardIds = new HashMap<>();
    private Response response;
    private String labelId;
    private String organizationId;

    // Constructor to initialize API keys and base URL
    public TrelloSteps() {
        ConfigLoader configLoader = ConfigLoader.getInstance();
        apiKey = configLoader.getApiKey();
        apiToken = configLoader.getApiToken();
        baseUrl = configLoader.getBaseUrl();
        organizationId = configLoader.getOrganizationId();
    }

    // Utility method for setting up common request parameters
    private RequestSpecification setupRequest() {
        return given()
                .queryParam("key", apiKey)
                .queryParam("token", apiToken)
                .header("Content-Type", "application/json");
    }

    // Utility method to validate status code
    private void assertStatusCode(Response response, int expectedStatusCode) {
        response.then().statusCode(expectedStatusCode);
    }

    // Utility method to fetch cards from the list
    private List<Map<String, Object>> getCardsInList() {
        response = setupRequest()
                .queryParam("fields", "name,pos")
                .when()
                .get(baseUrl + "/lists/{id}/cards", listId);
        assertStatusCode(response, 200);
        return response.jsonPath().getList("$");
    }

    // Step: Validate the Trello API key and token
    @Given("I have a valid Trello API key and token")
    public void validateTrelloApiKeyAndToken() {
        if (apiKey == null || apiToken == null) {
            throw new RuntimeException("API key and token must be set in the config file.");
        }
    }

//     Step: Create a new board
//    @Given("I have created a board named {string}")
//    public void createBoardNamed(String boardName) {
//        response = setupRequest()
//                .queryParam("name", boardName)
//                .when()
//                .post(baseUrl + "/boards/");
//        logger.info("Response: {}", response.prettyPrint());
//        assertStatusCode(response, 200);
//        boardId = response.jsonPath().getString("id");
//    }

    // Step: Create a new board
    @Given("I have created a board named {string}")
    public void createBoardNamed(String boardName) {
        response = setupRequest()
                .queryParam("name", boardName)
                .queryParam("idOrganization", organizationId) // Include the organization ID
                .when()
                .post(baseUrl + "/boards/");
        logger.info("Response: {}", response.prettyPrint());
        assertStatusCode(response, 200);
        boardId = response.jsonPath().getString("id");
    }

    // Step: Create a new list on the board
    @Given("I have created a list named {string} on the board")
    public void createListOnBoard(String listName) {
        response = setupRequest()
                .queryParam("name", listName)
                .queryParam("idBoard", boardId)
                .when()
                .post(baseUrl + "/lists");
        logger.info("Response: {}", response.prettyPrint());
        assertStatusCode(response, 200);
        listId = response.jsonPath().getString("id");
    }

    // Step: Create multiple cards on the list
    @Given("I have created cards {string}, {string}, {string} on the list")
    public void createCardsOnList(String card1, String card2, String card3) {
        String[] cards = {card1, card2, card3};
        for (String cardName : cards) {
            response = setupRequest()
                    .queryParam("name", cardName)
                    .queryParam("idList", listId)
                    .when()
                    .post(baseUrl + "/cards");
            logger.info("Created card '{}': {}", cardName, response.prettyPrint());
            assertStatusCode(response, 200);
            String cardId = response.jsonPath().getString("id");
            cardIds.put(cardName, cardId);
        }
    }

    // Step: Move a card to a specific position
    @When("I move the card {string} to position {string}")
    public void moveCardToPosition(String cardName, String position) {
        String cardId = cardIds.get(cardName);
        response = setupRequest()
                .queryParam("pos", position)
                .when()
                .put(baseUrl + "/cards/{id}", cardId);
        logger.info("Moved card '{}': {}", cardName, response.prettyPrint());
        assertStatusCode(response, 200);
    }

    // Step: Verify a card is at the first position in the list
    @Then("the card {string} should be at the first position in the list")
    public void cardShouldBeAtFirstPosition(String cardName) {
        validateCardPosition(cardName, 0);
    }

    // Step: Create a single card on the list
    @Given("I have created a card named {string} on the list")
    public void createCardOnList(String cardName) {
        response = setupRequest()
                .queryParam("name", cardName)
                .queryParam("idList", listId)
                .when()
                .post(baseUrl + "/cards");
        logger.info("Created card '{}': {}", cardName, response.prettyPrint());
        assertStatusCode(response, 200);
        String cardId = response.jsonPath().getString("id");
        cardIds.put(cardName, cardId);
    }

    // Step: Create a label on the board
    @Given("I have created a label named {string} on the board")
    public void createLabelOnBoard(String labelName) {
        response = setupRequest()
                .queryParam("name", labelName)
                .queryParam("color", "red")
                .queryParam("idBoard", boardId)
                .when()
                .post(baseUrl + "/labels");
        logger.info("Created label '{}': {}", labelName, response.prettyPrint());
        assertStatusCode(response, 200);
        labelId = response.jsonPath().getString("id");
    }

    // Step: Add a label to a card
    @Given("I have added the label {string} to the card {string}")
    public void addLabelToCard(String labelName, String cardName) {
        String cardId = cardIds.get(cardName);
        response = setupRequest()
                .queryParam("value", labelId)
                .when()
                .post(baseUrl + "/cards/{id}/idLabels", cardId);
        logger.info("Added label '{}' to card '{}': {}", labelName, cardName, response.prettyPrint());
        assertStatusCode(response, 200);
    }

    // Step: Remove a label from a card
    @When("I remove the label {string} from the card {string}")
    public void removeLabelFromCard(String labelName, String cardName) {
        String cardId = cardIds.get(cardName);
        response = setupRequest()
                .when()
                .delete(baseUrl + "/cards/{idCard}/idLabels/{idLabel}", cardId, labelId);
        logger.info("Removed label '{}' from card '{}': {}", labelName, cardName, response.prettyPrint());
        assertStatusCode(response, 200);
    }

    // Step: Verify a card no longer has a specific label
    @Then("the card {string} should not have the label {string}")
    public void cardShouldNotHaveLabel(String cardName, String labelName) {
        String cardId = cardIds.get(cardName);
        response = setupRequest()
                .queryParam("fields", "idLabels")
                .when()
                .get(baseUrl + "/cards/{id}", cardId);
        logger.info("Card labels: {}", response.prettyPrint());
        assertStatusCode(response, 200);
        List<String> labels = response.jsonPath().getList("idLabels");
        Assert.assertFalse(labels.contains(labelId), "Label was not removed from the card.");
    }

    // Step: Verify a card is at the last position in the list
    @Then("the card {string} should be at the last position in the list")
    public void cardShouldBeAtLastPosition(String cardName) {
        List<Map<String, Object>> cards = getCardsInList();
        int lastIndex = cards.size() - 1;
        validateCardPosition(cardName, lastIndex);
    }

    // Utility method to validate card position in the list
    private void validateCardPosition(String cardName, int expectedIndex) {
        List<Map<String, Object>> cards = getCardsInList();
        // Sort cards by 'pos' field
        cards.sort(Comparator.comparingDouble(card -> ((Number) card.get("pos")).doubleValue()));
        String actualCardName = (String) cards.get(expectedIndex).get("name");
        Assert.assertEquals(actualCardName, cardName, "Card is not at the expected position.");
    }
}
