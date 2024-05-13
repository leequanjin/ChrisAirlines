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
    private LoyaltyTier loyaltyTier;
    private ArrayList<VoucherDetails> redeemedVouchers;
    private ArrayList<BookingDetails> bookedFlights;

    public Customer(){
    
    }
    
    public Customer(String id, String name, String email, String phone) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.mileagePoints = 0;
        this.loyaltyPoints = 0;
        updateLoyaltyTier();
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
        updateLoyaltyTier();
        this.redeemedVouchers = new ArrayList<>();
        this.bookedFlights = new ArrayList<>();  
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

    public LoyaltyTier getLoyaltyTier() {
        return loyaltyTier;
    }

    public void setLoyaltyTier(LoyaltyTier loyaltyTier) {
        this.loyaltyTier = loyaltyTier;
    }

    public List<BookingDetails> getBookedFlights() {
        return bookedFlights;
    }

    public void setBookedFlights(List<BookingDetails> bookedFlights) {
        this.bookedFlights = (ArrayList<BookingDetails>) bookedFlights;
    }
    // </editor-fold>
    
    public BookingDetails bookFlight(String customerId, Flight flight, int quantity, LocalDateTime bookingDateTime) {
        double totalAmount = flight.getFare() * quantity;
        earnMileagePoints(totalAmount);
        earnLoyaltyPoints(totalAmount);
        BookingDetails bookingDetails = new BookingDetails(flight, quantity, totalAmount, bookingDateTime, 0, "-");
        return bookingDetails;
    }
    
    public BookingDetails bookFlightWithDiscountAmount(String customerId, Flight flight, int quantity, LocalDateTime bookingDateTime, double discountAmount) {
        double discount = discountAmount;
        double totalAmount = flight.getFare() * quantity - discount;
        earnMileagePoints(totalAmount);
        earnLoyaltyPoints(totalAmount);
        BookingDetails bookingDetails = new BookingDetails(flight, quantity, totalAmount, bookingDateTime, discount, "-");
        return bookingDetails;
    }

    public BookingDetails bookFlightWithDiscountRate(String customerId, Flight flight, int quantity, LocalDateTime bookingDateTime, double discountRate) {
        double subTotal = flight.getFare() * quantity;
        double discount = subTotal * (discountRate);
        double totalAmount = subTotal - discount;
        earnMileagePoints(totalAmount);
        earnLoyaltyPoints(totalAmount);
        BookingDetails bookingDetails = new BookingDetails(flight, quantity, totalAmount, bookingDateTime, discount, "-");
        return bookingDetails;
    }
    
    public BookingDetails bookFlightWithReward(String customerId, Flight flight, int quantity, LocalDateTime bookingDateTime, String reward) {
        double subTotal = flight.getFare() * quantity;
        double discount = 0;
        double totalAmount = subTotal - discount;
        earnMileagePoints(totalAmount);
        earnLoyaltyPoints(totalAmount);
        BookingDetails bookingDetails = new BookingDetails(flight, quantity, totalAmount, bookingDateTime, 0, reward);
        return bookingDetails;
    }

    public List<VoucherDetails> getRedeemedVouchers() {
        return redeemedVouchers;
    }

    public void setRedeemedVouchers(List<VoucherDetails> redeemedVouchers) {
        this.redeemedVouchers = (ArrayList<VoucherDetails>) redeemedVouchers;
    }
    
    public VoucherDetails redeemVoucher(String id, int requiredPoints, Voucher voucher, LocalDateTime redeemedDateTime) {
        this.mileagePoints -= requiredPoints;
        voucher.setStock(voucher.getStock() - 1);
        
        double discountAmount = 0.0;
        double discountRate = 0.0;
        String reward = "-";
        
        switch (voucher) {
            case DiscountAmtVoucher discountAmtVoucher -> {
                discountAmount = discountAmtVoucher.getDiscountAmount();
            }
            case DiscountRateVoucher discountRateVoucher -> {
                discountRate = discountRateVoucher.getDiscountRate();
            }
            case RewardVoucher rewardVoucher -> {
                reward = rewardVoucher.getReward();
            }
            default -> {
            }
        }
        
        VoucherDetails voucherDetails = new VoucherDetails(id, this.id, voucher, redeemedDateTime, discountAmount, discountRate, reward);
        return voucherDetails;
    }
    
    public void earnMileagePoints(double totalAmount) {
        double bonusMileage = loyaltyTier.calculateBonusMileage(totalAmount);
        int pointsEarned = (int) (totalAmount + bonusMileage);
        this.mileagePoints += pointsEarned;
    }

    public void earnLoyaltyPoints(double totalAmount) {
        int pointsEarned = (int) totalAmount;
        this.loyaltyPoints += pointsEarned;
        updateLoyaltyTier();
    }

    public final void updateLoyaltyTier() {
        if (loyaltyPoints >= 0 && loyaltyPoints <= 10000) {
            loyaltyTier = new BronzeTier();
        } else if (loyaltyPoints >= 10001 && loyaltyPoints <= 50000) {
            loyaltyTier = new SilverTier();
        } else if (loyaltyPoints >= 50001 && loyaltyPoints <= 100000) {
            loyaltyTier = new GoldTier();
        } else {
            loyaltyTier = new PlatinumTier();
        }
    }

    @Override
    public String toString() {
        return id + "," + name + "," + email + "," + phone + "," + mileagePoints + "," + loyaltyPoints;
    }
}
