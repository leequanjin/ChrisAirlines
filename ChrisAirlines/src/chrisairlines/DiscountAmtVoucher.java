package chrisairlines;

public class DiscountAmtVoucher extends Voucher {
    private double discountAmount;
    
    public DiscountAmtVoucher(String code, String description, int pointsRequired, double discountAmount) {
        super(code, description, pointsRequired);
        this.discountAmount = discountAmount;
    }

    public double getDiscountAmount() {
        return discountAmount;
    }

    public void setDiscountAmount(double discountAmount) {
        this.discountAmount = discountAmount;
    }
}
