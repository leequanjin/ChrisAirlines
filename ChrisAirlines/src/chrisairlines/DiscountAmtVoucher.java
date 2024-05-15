package chrisairlines;

public class DiscountAmtVoucher extends Voucher {

    private double discountAmount;

    public DiscountAmtVoucher() {

    }

    public DiscountAmtVoucher(String code, String type, String description, double discountAmount) {
        super(code, type, description);
        this.discountAmount = discountAmount;
    }

    public DiscountAmtVoucher(String code, String type, String description, int pointsRequired, int stock, double discountAmount) {
        super(code, type, description, pointsRequired, stock);
        this.discountAmount = discountAmount;
    }

    public double getDiscountAmount() {
        return discountAmount;
    }

    public void setDiscountAmount(double discountAmount) {
        this.discountAmount = discountAmount;
    }

    @Override
    public String toString() {
        return super.toString() + "," + discountAmount;
    }
}
