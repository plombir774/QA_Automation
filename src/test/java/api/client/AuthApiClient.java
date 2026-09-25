package api.client;

import config.ApiConfig;
import io.restassured.response.Response;
import models.AuthRequest;

import static io.restassured.RestAssured.given;

public class AuthApiClient {

    public Response createToken(AuthRequest credentials) {
        return given()
                .spec(ApiConfig.requestSpec())
                .body(credentials)
                .when()
                .post("/auth");
    }
}
