package chrisairlines;

public class DiscountRateVoucher extends Voucher {
    private double discountRate;
    
    public DiscountRateVoucher() {
    
    }
    
    public DiscountRateVoucher(String code, String type, String description, double discountRate) {
        super(code, type, description);
        this.discountRate = discountRate;
    }
    
    public DiscountRateVoucher(String code, String type, String description, int pointsRequired, int stock, double discountRate) {
        super(code, type, description, pointsRequired, stock);
        this.discountRate = discountRate;
    }

    public double getDiscountRate() {
        return discountRate;
    }

    public void setDiscountRate(double discountRate) {
        this.discountRate = discountRate;
    }
    
    @Override
    public String toString() {
        return super.toString() + "," + discountRate;
    }
}
