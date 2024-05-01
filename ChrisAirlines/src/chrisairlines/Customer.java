package chrisairlines;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class Customer {
    private String id;
    private String name;
    private String email;
    private String phone;
    private int mileagePoints;
    private int loyaltyPoints;
    private String loyaltyTier;
    private ArrayList<VoucherDetails> redeemedVouchers;
    private ArrayList<BookingDetails> bookedFlights;
    

    public Customer(String id, String name, String email, String phone) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.mileagePoints = 0;
        this.loyaltyPoints = 0;
        this.loyaltyTier = "Bronze";
        this.redeemedVouchers = new ArrayList<>();
        this.bookedFlights = new ArrayList<>();
    }
    
    public Customer(String id, String name, String email, String phone, int mileagePoints, int loyaltyPoints) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.mileagePoints = mileagePoints;
        this.loyaltyPoints = loyaltyPoints;
        this.loyaltyTier = "Bronze";
        this.redeemedVouchers = new ArrayList<>();
        this.bookedFlights = new ArrayList<>();
        updateLoyaltyTier();
    }

    // <editor-fold desc="getter & setters">
        public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public int getMileagePoints() {
        return mileagePoints;
    }

    public void setMileagePoints(int mileagePoints) {
        this.mileagePoints = mileagePoints;
    }

    public int getLoyaltyPoints() {
        return loyaltyPoints;
    }

    public void setLoyaltyPoints(int loyaltyPoints) {
        this.loyaltyPoints = loyaltyPoints;
        updateLoyaltyTier();
    }

    public String getLoyaltyTier() {
        return loyaltyTier;
    }

    public void setLoyaltyTier(String loyaltyTier) {
        this.loyaltyTier = loyaltyTier;
    }

    public List<BookingDetails> getBookedFlights() {
        return bookedFlights;
    }

    public void setBookedFlights(List<BookingDetails> bookedFlights) {
        this.bookedFlights = (ArrayList<BookingDetails>) bookedFlights;
    }
    // </editor-fold>
    
    public BookingDetails bookFlight(Flight flight, int quantity, LocalDateTime bookingDateTime) {
        double discount = 0;
        double totalAmount = flight.getFare() * quantity - discount;
        earnMileagePoints(totalAmount);
        earnLoyaltyPoints(totalAmount);
        BookingDetails bookingDetails = new BookingDetails(flight, quantity, totalAmount, bookingDateTime);
        bookedFlights.add(bookingDetails);
        return bookingDetails;
    }
    
    public BookingDetails bookFlightWithDiscountAmount(Flight flight, int quantity, LocalDateTime bookingDateTime, double discountAmount) {
        double discount = discountAmount;
        double totalAmount = flight.getFare() * quantity - discount;
        earnMileagePoints(totalAmount);
        earnLoyaltyPoints(totalAmount);
        BookingDetails bookingDetails = new BookingDetails(flight, quantity, totalAmount, bookingDateTime, discount);
        bookedFlights.add(bookingDetails);
        return bookingDetails;
    }

    public BookingDetails bookFlightWithDiscountRate(Flight flight, int quantity, LocalDateTime bookingDateTime, double discountRate) {
        double subTotal = flight.getFare() * quantity;
        double discount = subTotal * (discountRate);
        double totalAmount = subTotal - discount;
        earnMileagePoints(totalAmount);
        earnLoyaltyPoints(totalAmount);
        BookingDetails bookingDetails = new BookingDetails(flight, quantity, totalAmount, bookingDateTime, discount);
        bookedFlights.add(bookingDetails);
        return bookingDetails;
    }
    
    public BookingDetails bookFlightWithReward(Flight flight, int quantity, LocalDateTime bookingDateTime, String reward) {
        double subTotal = flight.getFare() * quantity;
        double discount = 0;
        double totalAmount = subTotal - discount;
        earnMileagePoints(totalAmount);
        earnLoyaltyPoints(totalAmount);
        BookingDetails bookingDetails = new BookingDetails(flight, quantity, totalAmount, bookingDateTime, reward);
        bookedFlights.add(bookingDetails);
        return bookingDetails;
    }

    public List<VoucherDetails> getRedeemedVouchers() {
        return redeemedVouchers;
    }

    public void setRedeemedVouchers(List<VoucherDetails> redeemedVouchers) {
        this.redeemedVouchers = (ArrayList<VoucherDetails>) redeemedVouchers;
        updateVoucherStatus();
    }
    
    public void redeemVoucher(int requiredPoints, Voucher voucher, LocalDateTime redeemedDateTime) {
        this.mileagePoints -= requiredPoints;
        redeemedVouchers.add(new VoucherDetails(voucher, redeemedDateTime));
    }
    
    public void earnMileagePoints(double spentAmount) {
        double bonusPercentage = PointCalculation.getBonusPercentage(loyaltyTier);
        int pointsEarned = PointCalculation.calculateMileagePoints(spentAmount, bonusPercentage);
        this.mileagePoints += pointsEarned;
    }

    public void earnLoyaltyPoints(double spentAmount) {
        int pointsEarned = PointCalculation.calculateLoyaltyPoints(spentAmount);
        this.loyaltyPoints += pointsEarned;
        updateLoyaltyTier();
    }

    public void updateLoyaltyTier() {
        if (loyaltyPoints < 10000) {
            this.loyaltyTier = "Bronze";
        } else if (loyaltyPoints < 50000) {
            this.loyaltyTier = "Silver";
        } else if (loyaltyPoints < 100000) {
            this.loyaltyTier = "Gold";
        } else {
            this.loyaltyTier = "Platinum";
        }
    }
    
    public void updateVoucherStatus() {
        for (VoucherDetails redeemedVoucher : redeemedVouchers) {
                if ((redeemedVoucher.getExpiryDateTime().isBefore(LocalDateTime.now())) && (!redeemedVoucher.getStatus().equals("Used"))){
                    redeemedVoucher.setStatus("Expired");
                }
            }  
        }
}
