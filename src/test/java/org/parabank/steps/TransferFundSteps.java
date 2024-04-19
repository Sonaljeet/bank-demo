package org.parabank.steps;

import com.aventstack.extentreports.Status;
import com.aventstack.extentreports.cucumber.adapter.ExtentCucumberAdapter;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.restassured.response.Response;
import org.parabank.common.CommonAPI;

import java.io.IOException;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.parabank.utilities.JsonUtils.readJSONFile;
import static org.parabank.validators.TransferFundValidator.verifyCreditedAmountAfterTransfer;

public class TransferFundSteps {
    private static Response response;
    private static final String filename = "src/test/resources/testdata/TestData.json";

    @Given("^user makes the api call to transfer fund- (-?\\d+)$")
    public void callTransferFundAPI(int amount) throws IOException {
        ExtentCucumberAdapter.getCurrentStep().log(Status.INFO, "**INFO: Calling fund transfer api.");
        String uri = readJSONFile(filename, "$.uri.base") + readJSONFile(filename, "$.uri.trasfer-funds");
        response = CommonAPI.getResponse("POST", uri,
                Map.of("fromAccountId", readJSONFile(filename, "$.account.fromAccountId"), "toAccountId", readJSONFile(filename, "$.account.toAccountId"),
                        "amount", String.valueOf(amount)));
        ExtentCucumberAdapter.getCurrentStep().log(Status.INFO, "Received response after transferring fund.");
    }

    @Then("^the status of response should be 200$")
    public void verifyResponseStatus() {
        ExtentCucumberAdapter.getCurrentStep().log(Status.INFO, "**INFO: Verifying response status code.");
        try {
            assertEquals("Response status code", 200, response.getStatusCode());
            ExtentCucumberAdapter.getCurrentStep().log(Status.PASS, "Response status code is as expected: " + response.getStatusCode());
        } catch (AssertionError e) {
            ExtentCucumberAdapter.getCurrentStep().log(Status.FAIL, "Response status code is not as expected: " + response.getStatusCode());
        }
    }

    @Then("^user verify the response message for the transferred fund- (-?\\d+)$")
    public void verifyResponseMessageForTransferFund(int amount) {
        ExtentCucumberAdapter.getCurrentStep().log(Status.INFO, "**INFO: Verifying if the response message for transfer fund is as expected or not.");
        try {
            assertEquals("Response from after hitting transfer fund api", "Successfully transferred $" + amount + " from account #" + readJSONFile(filename, "$.account.fromAccountId") + " to account #" + readJSONFile(filename, "$.account.toAccountId"), response.getBody().asString());
            ExtentCucumberAdapter.getCurrentStep().log(Status.PASS, "Response from transfer fund api is as expected: " + response.getBody().asString());
        } catch (AssertionError | IOException e) {
            ExtentCucumberAdapter.getCurrentStep().log(Status.FAIL, "Response from transfer fund api is not as expected: " + response.getBody().asString());
        }
    }

    @Given("^user makes the api call for account activity$")
    public void callAccountActivityAPI() throws IOException {
        ExtentCucumberAdapter.getCurrentStep().log(Status.INFO, "**INFO: Calling account activity api.");
        String uri = readJSONFile(filename, "$.uri.base") + readJSONFile(filename, "$.uri.transactions");
        response = CommonAPI.getResponse("GET", uri,
                Map.of("accountID", readJSONFile(filename, "$.account.fromAccountId")));
        ExtentCucumberAdapter.getCurrentStep().log(Status.INFO, "Received response for account activity.");
    }

    @Then("^user verify the response message for the credited amount- (-?\\d+)$")
    public void verifyResponseMessageForCreditedAmount(int amount) {
        ExtentCucumberAdapter.getCurrentStep().log(Status.INFO, "**INFO: Verifying if the transferred amount is correctly credited to the account or not.");
        try {
            assertTrue(verifyCreditedAmountAfterTransfer(response, amount));
            ExtentCucumberAdapter.getCurrentStep().log(Status.PASS, "Transferred amount- " + amount + " is successfully credited to the account.");
        } catch (AssertionError | IOException e) {
            ExtentCucumberAdapter.getCurrentStep().log(Status.FAIL, "Transferred amount- " + amount + " is not credited to the account.");
            ExtentCucumberAdapter.getCurrentStep().log(Status.INFO, "**INFO: Something went wrong while transferring amount. For more details, please refer below response.");
            ExtentCucumberAdapter.getCurrentStep().log(Status.FAIL, response.getBody().prettyPrint());
        }
    }
}