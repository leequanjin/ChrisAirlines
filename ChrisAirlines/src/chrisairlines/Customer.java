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
    private List<VoucherDetails> redeemedVouchers;
    private List<BookingDetails> bookedFlights;
    

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
        this.bookedFlights = bookedFlights;
    }
    // </editor-fold>
    
    public void bookFlight(Flight flight, LocalDateTime bookingDateTime) {
        double spentAmount = flight.getFare();
        earnMileagePoints(spentAmount);
        earnLoyaltyPoints(spentAmount);
        bookedFlights.add(new BookingDetails(flight, bookingDateTime));
    }

    public List<VoucherDetails> getRedeemedVouchers() {
        return redeemedVouchers;
    }

    public void setRedeemedVouchers(List<VoucherDetails> redeemedVouchers) {
        this.redeemedVouchers = redeemedVouchers;
    }
    
    public void redeemVoucher(Voucher voucher, LocalDateTime bookingDateTime) {
        redeemedVouchers.add(new VoucherDetails(voucher, bookingDateTime));
    }
    
    public void earnMileagePoints(double spentAmount) {
        double bonusPercentage = PointCalculation.getBonusPercentage(loyaltyTier);
        int pointsEarned = PointCalculation.calculateMileagePoints(spentAmount, bonusPercentage);
        this.mileagePoints += pointsEarned;
        updateLoyaltyTier();
    }

    public void earnLoyaltyPoints(double spentAmount) {
        int pointsEarned = PointCalculation.calculateLoyaltyPoints(spentAmount);
        this.loyaltyPoints += pointsEarned;
        updateLoyaltyTier();
    }
    
    public void redeemPoints(int points) {
        if (this.mileagePoints >= points) {
            this.mileagePoints -= points;
            System.out.println("Voucher redeemed successfully!");
        } else {
            System.out.println("Insufficient mileage points for redemption.");
        }
    }
    
    private void updateLoyaltyTier() {
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
}
