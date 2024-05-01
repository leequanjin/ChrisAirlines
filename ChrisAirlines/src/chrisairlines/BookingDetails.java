package chrisairlines;

import java.time.LocalDateTime;

public class BookingDetails {
    private Flight flight;
    private int quantity;
    private double totalAmount;
    private LocalDateTime bookingDateTime;
    private double discount;
    private String reward;

    public BookingDetails(Flight flight, int quantity, double totalAmount, LocalDateTime bookingDateTime) {
        this.flight = flight;
        this.quantity = quantity;
        this.totalAmount = totalAmount;
        this.bookingDateTime = bookingDateTime;
        discount = 0;
        reward = "-";
    }
    
    public BookingDetails(Flight flight, int quantity, double totalAmount, LocalDateTime bookingDateTime, double discount) {
        this.flight = flight;
        this.quantity = quantity;
        this.totalAmount = totalAmount;
        this.bookingDateTime = bookingDateTime;
        this.discount = discount;
        reward = "-";
    }
    
    public BookingDetails(Flight flight, int quantity, double totalAmount, LocalDateTime bookingDateTime, String reward) {
        this.flight = flight;
        this.quantity = quantity;
        this.totalAmount = totalAmount;
        this.bookingDateTime = bookingDateTime;
        discount = 0;
        this.reward = reward;
    }

    public Flight getFlight() {
        return flight;
    }

    public void setFlight(Flight flight) {
        this.flight = flight;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public double getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(double totalAmount) {
        this.totalAmount = totalAmount;
    }

    public LocalDateTime getBookingDateTime() {
        return bookingDateTime;
    }

    public void setBookingDateTime(LocalDateTime bookingDateTime) {
        this.bookingDateTime = bookingDateTime;
    }  

    public double getDiscount() {
        return discount;
    }

    public void setDiscount(double discount) {
        this.discount = discount;
    }

    public String getReward() {
        return reward;
    }

    public void setReward(String reward) {
        this.reward = reward;
    }
}
