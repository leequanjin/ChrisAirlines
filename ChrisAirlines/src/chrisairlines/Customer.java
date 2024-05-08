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
    

    public Customer(){
    
    }
    
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
    
    public BookingDetails bookFlight(String customerId, Flight flight, int quantity, LocalDateTime bookingDateTime) {
        double discount = 0;
        double totalAmount = flight.getFare() * quantity - discount;
        earnMileagePoints(totalAmount);
        earnLoyaltyPoints(totalAmount);
        BookingDetails bookingDetails = new BookingDetails(flight, quantity, totalAmount, bookingDateTime, 0, "-");
        DatabaseHandler.saveBookingDetailToFile(customerId, bookingDetails);
        return bookingDetails;
    }
    
    public BookingDetails bookFlightWithDiscountAmount(String customerId, Flight flight, int quantity, LocalDateTime bookingDateTime, double discountAmount) {
        double discount = discountAmount;
        double totalAmount = flight.getFare() * quantity - discount;
        earnMileagePoints(totalAmount);
        earnLoyaltyPoints(totalAmount);
        BookingDetails bookingDetails = new BookingDetails(flight, quantity, totalAmount, bookingDateTime, discount, "-");
        DatabaseHandler.saveBookingDetailToFile(customerId, bookingDetails);
        return bookingDetails;
    }

    public BookingDetails bookFlightWithDiscountRate(String customerId, Flight flight, int quantity, LocalDateTime bookingDateTime, double discountRate) {
        double subTotal = flight.getFare() * quantity;
        double discount = subTotal * (discountRate);
        double totalAmount = subTotal - discount;
        earnMileagePoints(totalAmount);
        earnLoyaltyPoints(totalAmount);
        BookingDetails bookingDetails = new BookingDetails(flight, quantity, totalAmount, bookingDateTime, discount, "-");
        DatabaseHandler.saveBookingDetailToFile(customerId, bookingDetails);
        return bookingDetails;
    }
    
    public BookingDetails bookFlightWithReward(String customerId, Flight flight, int quantity, LocalDateTime bookingDateTime, String reward) {
        double subTotal = flight.getFare() * quantity;
        double discount = 0;
        double totalAmount = subTotal - discount;
        earnMileagePoints(totalAmount);
        earnLoyaltyPoints(totalAmount);
        BookingDetails bookingDetails = new BookingDetails(flight, quantity, totalAmount, bookingDateTime, 0, reward);
        DatabaseHandler.saveBookingDetailToFile(customerId, bookingDetails);
        return bookingDetails;
    }

    public List<VoucherDetails> getRedeemedVouchers() {
        return redeemedVouchers;
    }

    public void setRedeemedVouchers(List<VoucherDetails> redeemedVouchers) {
        this.redeemedVouchers = (ArrayList<VoucherDetails>) redeemedVouchers;
        updateVoucherStatus();
    }
    
    public void redeemVoucher(String id, int requiredPoints, Voucher voucher, LocalDateTime redeemedDateTime) {
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
        DatabaseHandler.updateCustomerInfo("customer_details.txt", this);
        DatabaseHandler.saveRedeemedVoucherToFile(voucherDetails, "redeemed_vouchers.txt");
        DatabaseHandler.updateVoucherDetails("voucher_details.txt", voucher);
    }
    
    public void earnMileagePoints(double totalAmount) {
        double bonusPercentage = PointCalculation.getBonusPercentage(loyaltyTier);
        int pointsEarned = PointCalculation.calculateMileagePoints(totalAmount, bonusPercentage);
        this.mileagePoints += pointsEarned;
        DatabaseHandler.updateCustomerInfo("customer_details.txt", this);
    }

    public void earnLoyaltyPoints(double totalAmount) {
        int pointsEarned = PointCalculation.calculateLoyaltyPoints(totalAmount);
        this.loyaltyPoints += pointsEarned;
        updateLoyaltyTier();
        DatabaseHandler.updateCustomerInfo("customer_details.txt", this);
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
    
    public void updateVoucherStatus() {
        for (VoucherDetails redeemedVoucher : redeemedVouchers) {
            if (redeemedVoucher.getStatus().equals("Used")){
                break;
            } else if(redeemedVoucher.getExpiryDateTime().isBefore(LocalDateTime.now())){
                redeemedVoucher.setStatus("Expired");
            } 
            DatabaseHandler.updateRedeemedVouchers("redeemed_vouchers.txt", redeemedVoucher);
        }  
    }

    @Override
    public String toString() {
        return id + "," + name + "," + email + "," + phone + "," + mileagePoints + "," + loyaltyPoints;
    }
}
