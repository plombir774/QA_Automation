package config;

import io.qameta.allure.restassured.AllureRestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;

import static io.restassured.config.HttpClientConfig.httpClientConfig;
import static io.restassured.config.LogConfig.logConfig;
import static io.restassured.config.ObjectMapperConfig.objectMapperConfig;
import static io.restassured.config.RestAssuredConfig.config;
import static io.restassured.mapper.ObjectMapperType.JACKSON_2;

public final class ApiConfig {

    private ApiConfig() {
    }

    public static RequestSpecification requestSpec() {
        return new RequestSpecBuilder()
                .setBaseUri(System.getProperty("api.baseUrl", "https://restful-booker.herokuapp.com"))
                .setContentType(ContentType.JSON)
                // Booker expects one exact media type, not REST Assured's list of JSON aliases.
                .setAccept("application/json")
                .setConfig(config()
                        .httpClient(httpClientConfig()
                                .setParam("http.connection.timeout", 10_000)
                                .setParam("http.socket.timeout", 30_000))
                        .logConfig(logConfig().enableLoggingOfRequestAndResponseIfValidationFails())
                        .objectMapperConfig(objectMapperConfig().defaultObjectMapperType(JACKSON_2)))
                .addFilter(new AllureRestAssured())
                .build();
    }
}
