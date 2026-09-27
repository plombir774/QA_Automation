package api.client;

import config.ApiConfig;
import io.restassured.response.Response;
import models.Booking;

import static io.restassured.RestAssured.given;

public class BookingApiClient {

    public Response createBooking(Booking booking) {
        return given()
                .spec(ApiConfig.requestSpec())
                .body(booking)
                .when()
                .post("/booking");
    }

    public Response getBooking(int bookingId) {
        return given()
                .spec(ApiConfig.requestSpec())
                .pathParam("id", bookingId)
                .when()
                .get("/booking/{id}");
    }

    public Response updateBooking(int bookingId, Booking booking, String token) {
        return given()
                .spec(ApiConfig.requestSpec())
                .pathParam("id", bookingId)
                .cookie("token", token)
                .body(booking)
                .when()
                .put("/booking/{id}");
    }

    public Response deleteBooking(int bookingId, String token) {
        return given()
                .spec(ApiConfig.requestSpec())
                .pathParam("id", bookingId)
                .cookie("token", token)
                .when()
                .delete("/booking/{id}");
    }
}
