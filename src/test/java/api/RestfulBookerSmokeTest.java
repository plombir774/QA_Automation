package api;

import config.ApiConfig;
import io.qameta.allure.Feature;
import io.restassured.http.ContentType;
import models.Booking;
import models.BookingResponse;
import org.testng.annotations.Test;
import utils.TestData;

import static io.qameta.allure.Allure.step;
import static io.restassured.RestAssured.given;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertTrue;

@Feature("Restful Booker bookings")
public class RestfulBookerSmokeTest {

    @Test(groups = {"api", "smoke"}, description = "A booking can be created with the supplied details")
    public void canCreateBooking() {
        Booking expectedBooking = TestData.newBooking();

        BookingResponse response = step("Create a booking and verify HTTP 200 with JSON content", () ->
                given()
                        .spec(ApiConfig.requestSpec())
                        .body(expectedBooking)
                .when()
                        .post("/booking")
                .then()
                        .statusCode(200)
                        .contentType(ContentType.JSON)
                        .extract().as(BookingResponse.class));

        step("Verify the booking ID and all returned booking details", () -> {
            assertNotNull(response.getBookingId(), "The response must contain a booking ID");
            assertTrue(response.getBookingId() > 0, "The booking ID must be positive");
            assertEquals(response.getBooking(), expectedBooking, "The booking details must match the request");
        });
    }
}
