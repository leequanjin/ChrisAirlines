package chrisairlines;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

public class Customer {

    private String id;
    private String name;
    private String email;
    private String phone;
    private int mileagePoints;
    private int prevloyaltyPoints;
    private int newloyaltyPoints;
    private LoyaltyTier loyaltyTier;
    private ArrayList<VoucherDetails> redeemedVouchers;
    private ArrayList<BookingDetails> bookedFlights;
    private LocalDateTime accountCreationDate;
    private LocalDateTime lastActivityDate;

    public Customer() {

    }

    public Customer(String id, String name, String email, String phone) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.mileagePoints = 0;
        this.prevloyaltyPoints = 0;
        this.newloyaltyPoints = 0;
        updateLoyaltyTier();
        this.redeemedVouchers = new ArrayList<>();
        this.bookedFlights = new ArrayList<>();
        this.accountCreationDate = LocalDateTime.now();
        this.lastActivityDate = LocalDateTime.now();
    }

    public Customer(String id, String name, String email, String phone, int mileagePoints, int newloyaltyPoints) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.mileagePoints = mileagePoints;
        this.prevloyaltyPoints = 0;
        this.newloyaltyPoints = newloyaltyPoints;
        updateLoyaltyTier();
        this.redeemedVouchers = new ArrayList<>();
        this.bookedFlights = new ArrayList<>();
        this.accountCreationDate = LocalDateTime.now();
        this.lastActivityDate = LocalDateTime.now();
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

    public int getprevLoyaltyPoints() {
        return prevloyaltyPoints;
    }

    public int getnewLoyaltyPoints() {
        return newloyaltyPoints;
    }

    public void setprevLoyaltyPoints(int prevloyaltyPoints) {
        this.prevloyaltyPoints = prevloyaltyPoints;
        updateLoyaltyTier();
    }

    public void setnewLoyaltyPoints(int newloyaltyPoints) {
        this.newloyaltyPoints = newloyaltyPoints;
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

    public LocalDateTime getAccountCreationDate() {
        return accountCreationDate;
    }

    public LocalDateTime getLastActivityDate() {
        return lastActivityDate;
    }

    public void setAccountCreationDate(LocalDateTime accountCreationDate) {
        this.accountCreationDate = accountCreationDate;
    }

    public void setLastActivityDate(LocalDateTime lastActivityDate) {
        this.lastActivityDate = lastActivityDate;
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
        this.newloyaltyPoints += pointsEarned;
        updateLoyaltyTier();
    }

    public final void updateLoyaltyTier() {
        if (newloyaltyPoints > prevloyaltyPoints) {
            if (newloyaltyPoints >= 0 && newloyaltyPoints <= 10000) {
                loyaltyTier = new BronzeTier();
            } else if (newloyaltyPoints >= 10001 && newloyaltyPoints <= 50000) {
                loyaltyTier = new SilverTier();
            } else if (newloyaltyPoints >= 50001 && newloyaltyPoints <= 100000) {
                loyaltyTier = new GoldTier();
            } else {
                loyaltyTier = new PlatinumTier();
            }
        } else {
            if (prevloyaltyPoints >= 0 && prevloyaltyPoints <= 10000) {
                loyaltyTier = new BronzeTier();
            } else if (prevloyaltyPoints >= 10001 && prevloyaltyPoints <= 50000) {
                loyaltyTier = new SilverTier();
            } else if (prevloyaltyPoints >= 50001 && prevloyaltyPoints <= 100000) {
                loyaltyTier = new GoldTier();
            } else {
                loyaltyTier = new PlatinumTier();
            }
        }
    }

    public void checkMileagePointValidity(LocalDateTime lastActivityDate) {
        if (lastActivityDate.until(LocalDateTime.now(), ChronoUnit.YEARS) > 2) {
            setMileagePoints(0);
        } else {
            getMileagePoints();
        }
    }

    public void yearlyLoyaltyPointReset() {
        if (LocalDateTime.now().getMonthValue() == 5 && LocalDateTime.now().getDayOfMonth() == 14) {
            setprevLoyaltyPoints(getnewLoyaltyPoints());
            setnewLoyaltyPoints(0);
        }
    }

    @Override
    public String toString() {
        //+ loyaltyTier + ","
        return id + "," + name + "," + email + "," + phone + "," + mileagePoints + "," + prevloyaltyPoints + "," + newloyaltyPoints + "," + accountCreationDate + "," + lastActivityDate;
    }
}
