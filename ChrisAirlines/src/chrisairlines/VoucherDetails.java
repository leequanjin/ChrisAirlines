package chrisairlines;

import java.time.LocalDateTime;

public class VoucherDetails {
    private Voucher voucher;
    private String id;
    private LocalDateTime redeemedDateTime;
    private LocalDateTime expiryDateTime;
    private String status;
    static int index = 1;

    public VoucherDetails(Voucher voucher, LocalDateTime redeemedDateTime) {
        this.id = String.format("%s%03d", "C", index);
        this.voucher = voucher;
        this.redeemedDateTime = redeemedDateTime;
        this.expiryDateTime = this.redeemedDateTime.plusMonths(1);
        this.status = "Valid";
        index++;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public static int getIndex() {
        return index;
    }

    public static void setIndex(int index) {
        VoucherDetails.index = index;
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
}
