package org.parabank.common;

import io.restassured.RestAssured;
import io.restassured.response.Response;

import java.util.Map;

public class CommonAPI {

    /**
     * Get response after hitting the uri
     *
     * @param method     type of method - GET or POST
     * @param uri        parameterized uri
     * @param parameters values to pass in uri in Key, Value pattern
     * @return actual response
     */
    public static Response getResponse(String method, String uri, Map<String, String> parameters) {
        return switch (method) {
            case "GET" -> RestAssured.given()
                    .pathParams(parameters)
                    .get(uri);
            case "POST" -> RestAssured.given()
                    .pathParams(parameters)
                    .post(uri);
            default -> throw new IllegalStateException("Unexpected value: " + method);
        };
    }
}
