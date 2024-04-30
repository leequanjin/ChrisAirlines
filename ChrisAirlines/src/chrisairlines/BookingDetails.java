package chrisairlines;

import java.time.LocalDateTime;

public class BookingDetails {
    private Flight flight;
    private LocalDateTime bookingDateTime;

    public BookingDetails(Flight flight, LocalDateTime bookingDateTime) {
        this.flight = flight;
        this.bookingDateTime = bookingDateTime;
    }

    public Flight getFlight() {
        return flight;
    }

    public void setFlight(Flight flight) {
        this.flight = flight;
    }

    public LocalDateTime getBookingDateTime() {
        return bookingDateTime;
    }

    public void setBookingDateTime(LocalDateTime bookingDateTime) {
        this.bookingDateTime = bookingDateTime;
    }  
}
