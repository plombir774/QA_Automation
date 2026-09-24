package models;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class BookingResponse {

    @JsonProperty("bookingid")
    private Integer bookingId;

    private Booking booking;
}
