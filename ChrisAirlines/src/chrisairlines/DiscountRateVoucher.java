package chrisairlines;

public class DiscountRateVoucher extends Voucher {
    private double discountRate;
    
    public DiscountRateVoucher(String code, String description, int pointsRequired, double discountRate) {
        super(code, description, pointsRequired);
        this.discountRate = discountRate;
    }

    public double getDiscountRate() {
        return discountRate;
    }

    public void setDiscountRate(double discountRate) {
        this.discountRate = discountRate;
    }
}
