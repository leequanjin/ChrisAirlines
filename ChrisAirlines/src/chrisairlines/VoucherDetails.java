package chrisairlines;

import java.time.LocalDateTime;

public class VoucherDetails {
    private Voucher voucher;
    private LocalDateTime bookingDateTime;

    public VoucherDetails(Voucher voucher, LocalDateTime bookingDateTime) {
        this.voucher = voucher;
        this.bookingDateTime = bookingDateTime;
    }

    public Voucher getVoucher() {
        return voucher;
    }

    public void setVoucher(Voucher voucher) {
        this.voucher = voucher;
    }

    public LocalDateTime getBookingDateTime() {
        return bookingDateTime;
    }

    public void setBookingDateTime(LocalDateTime bookingDateTime) {
        this.bookingDateTime = bookingDateTime;
    }  
}
