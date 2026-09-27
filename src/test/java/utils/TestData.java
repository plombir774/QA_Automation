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

    public static Booking updatedBooking() {
        LocalDate checkIn = LocalDate.now(ZoneOffset.UTC).plusDays(14);

        return Booking.builder()
                .firstName("Nikita")
                .lastName("UpdatedTest")
                .totalPrice(275)
                .depositPaid(false)
                .bookingDates(new BookingDates(checkIn.toString(), checkIn.plusDays(4).toString()))
                .additionalNeeds("Dinner")
                .build();
    }
}
