package chrisairlines;

import java.time.LocalDateTime;

public class VoucherDetails {
    private String id;
    private String customerId;
    private Voucher voucher;
    private LocalDateTime redeemedDateTime;
    private LocalDateTime expiryDateTime;
    private String status;
    private double discountAmount;
    private double discountRate;
    private String reward;
    
    public VoucherDetails(String id, String customerId, Voucher voucher, LocalDateTime redeemedDateTime, double discountAmount, double discountRate, String reward) {
        this.id = id;
        this.customerId = customerId;
        this.voucher = voucher;
        this.redeemedDateTime = redeemedDateTime;
        this.expiryDateTime = this.redeemedDateTime.plusMonths(1);
        this.status = "Valid";
        this.discountAmount = discountAmount;
        this.discountRate = discountRate;
        this.reward = reward;
    }
    
    //used by loadRedeemedVouchersByCustomerID from DatabaseHandler class
    public VoucherDetails(String id, String customerId, Voucher voucher, LocalDateTime redeemedDateTime, LocalDateTime exipryDateTime, String status, double discountAmount, double discountRate, String reward) {
        this.id = id;
        this.customerId = customerId;
        this.voucher = voucher;
        this.redeemedDateTime = redeemedDateTime;
        this.expiryDateTime = exipryDateTime;
        this.status = status;
        this.discountAmount = discountAmount;
        this.discountRate = discountRate;
        this.reward = reward;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
    
    public Voucher getVoucher() {
        return voucher;
    }

    public void setVoucher(Voucher voucher) {
        this.voucher = voucher;
    }

    public LocalDateTime getRedeemedDateTime() {
        return redeemedDateTime;
    }

    public void setRedeemedDateTime(LocalDateTime redeemedDateTime) {
        this.redeemedDateTime = redeemedDateTime;
    }

    public LocalDateTime getExpiryDateTime() {
        return expiryDateTime;
    }

    public void setExpiryDateTime(LocalDateTime expiryDateTime) {
        this.expiryDateTime = expiryDateTime;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    public double getDiscountAmount() {
        return discountAmount;
    }

    public void setDiscountAmount(double discountAmount) {
        this.discountAmount = discountAmount;
    }

    public double getDiscountRate() {
        return discountRate;
    }

    public void setDiscountRate(double discountRate) {
        this.discountRate = discountRate;
    }

    public String getReward() {
        return reward;
    }

    public void setReward(String reward) {
        this.reward = reward;
    }
    
    @Override
    public String toString() {
        return id + "," +
                customerId + "," +
                voucher.getCode() + "," +
                voucher.getType() + "," +
                voucher.getDescription() + "," +
                redeemedDateTime.toString() + "," +
                expiryDateTime.toString() + "," +
                status + "," +
                discountAmount + "," +
                discountRate + "," +
                reward;
    }
}
