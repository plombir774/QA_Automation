package api;

import api.client.AuthApiClient;
import api.client.BookingApiClient;
import io.qameta.allure.Feature;
import io.restassured.http.ContentType;
import models.AuthRequest;
import models.AuthResponse;
import models.Booking;
import models.BookingResponse;
import org.testng.annotations.Test;
import utils.TestData;

import static io.qameta.allure.Allure.step;
import static org.hamcrest.Matchers.equalTo;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertTrue;

@Feature("Restful Booker bookings")
public class RestfulBookerSmokeTest {

    private final BookingApiClient bookingClient = new BookingApiClient();
    private final AuthApiClient authClient = new AuthApiClient();

    @Test(groups = {"api", "smoke"}, description = "A booking can be created with the supplied details")
    public void canCreateBooking() {
        createBookingAndVerify(TestData.newBooking());
    }

    @Test(groups = {"api", "smoke"}, description = "A created booking can be retrieved by its generated ID")
    public void canGetCreatedBooking() {
        Booking expectedBooking = TestData.newBooking();
        int bookingId = createBookingAndVerify(expectedBooking);

        getBookingAndVerify(bookingId, expectedBooking);
    }

    @Test(groups = {"api", "smoke"}, description = "An authenticated user can update all booking details")
    public void canUpdateCreatedBooking() {
        int bookingId = createBookingAndVerify(TestData.newBooking());
        String token = authenticate();
        Booking expectedBooking = TestData.updatedBooking();

        Booking updatedBooking = step("Update booking " + bookingId + " and verify HTTP 200 with JSON content", () ->
                bookingClient.updateBooking(bookingId, expectedBooking, token)
                        .then()
                        .statusCode(200)
                        .contentType(ContentType.JSON)
                        .extract().as(Booking.class));

        step("Verify every field in the update response", () ->
                assertEquals(updatedBooking, expectedBooking, "The updated booking must match the request"));

        // Read the same ID to verify the update was persisted, not just echoed in the PUT response.
        getBookingAndVerify(bookingId, expectedBooking);
    }

    @Test(groups = {"api", "smoke"}, description = "An authenticated user can delete a booking and it is no longer found")
    public void canDeleteCreatedBooking() {
        int bookingId = createBookingAndVerify(TestData.newBooking());
        String token = authenticate();

        step("Delete booking " + bookingId + " and verify the deletion response", () ->
                bookingClient.deleteBooking(bookingId, token)
                        .then()
                        // Restful Booker documents HTTP 201 for a successful DELETE.
                        .statusCode(201)
                        .contentType(ContentType.TEXT)
                        .body(equalTo("Created")));

        step("Verify the deleted booking returns HTTP 404 and Not Found", () ->
                bookingClient.getBooking(bookingId)
                        .then()
                        .statusCode(404)
                        .contentType(ContentType.TEXT)
                        .body(equalTo("Not Found")));
    }

    private int createBookingAndVerify(Booking expectedBooking) {
        BookingResponse response = step("Create a booking and verify HTTP 200 with JSON content", () ->
                bookingClient.createBooking(expectedBooking)
                        .then()
                        .statusCode(200)
                        .contentType(ContentType.JSON)
                        .extract().as(BookingResponse.class));

        step("Verify the booking ID and all returned booking details", () -> {
            assertNotNull(response.getBookingId(), "The response must contain a booking ID");
            assertTrue(response.getBookingId() > 0, "The booking ID must be positive");
            assertEquals(response.getBooking(), expectedBooking, "The booking details must match the request");
        });

        return response.getBookingId();
    }

    private void getBookingAndVerify(int bookingId, Booking expectedBooking) {
        Booking actualBooking = step("Get booking " + bookingId + " and verify HTTP 200 with JSON content", () ->
                bookingClient.getBooking(bookingId)
                        .then()
                        .statusCode(200)
                        .contentType(ContentType.JSON)
                        .extract().as(Booking.class));

        step("Verify every field of the retrieved booking", () ->
                assertEquals(actualBooking, expectedBooking, "The stored booking must match the expected details"));
    }

    private String authenticate() {
        // Public demo credentials from Restful Booker's API documentation.
        AuthRequest credentials = new AuthRequest("admin", "password123");
        AuthResponse response = step("Authenticate and verify HTTP 200 with JSON content", () ->
                authClient.createToken(credentials)
                        .then()
                        .statusCode(200)
                        .contentType(ContentType.JSON)
                        .extract().as(AuthResponse.class));

        step("Verify authentication returned a non-blank token", () -> {
            assertNotNull(response.getToken(), "Authentication must return a token");
            assertFalse(response.getToken().isBlank(), "The authentication token must not be blank");
        });

        return response.getToken();
    }
}
