package utils;

import models.Booking;
import models.BookingDates;

import java.time.LocalDate;
import java.time.ZoneOffset;

public final class TestData {

    private TestData() {
    }

    public static Booking newBooking() {
        LocalDate checkIn = LocalDate.now(ZoneOffset.UTC).plusDays(7);

        return Booking.builder()
                .firstName("Portfolio")
                .lastName("SmokeTest")
                .totalPrice(150)
                .depositPaid(true)
                .bookingDates(new BookingDates(checkIn.toString(), checkIn.plusDays(2).toString()))
                .additionalNeeds("Breakfast")
                .build();
    }
}
