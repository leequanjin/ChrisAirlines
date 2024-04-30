package chrisairlines;

import java.util.HashMap;
import java.util.Map;

public class VoucherCatalog {
    private HashMap<String, Voucher> vouchers;

    public VoucherCatalog() {
        this.vouchers = new HashMap<>();
        vouchers.put("V001", new Voucher("V001", "10% off on next flight booking", 10000));
        vouchers.put("V002", new Voucher("V002", "20% off on next flight booking", 20000));
        vouchers.put("V003", new Voucher("V003", "25% off on next flight booking", 25000));
        vouchers.put("V004", new Voucher("V004", "RM20 discount on next flight booking", 2000));
        vouchers.put("V005", new Voucher("V005", "RM50 discount on next flight booking", 4000));
        vouchers.put("V006", new Voucher("V006", "RM100 discount on next flight booking", 7500));
        vouchers.put("V007", new Voucher("V007", "Free upgrade to business class", 20000));
        vouchers.put("V008", new Voucher("V008", "Complimentary airport lounge access", 18000));
    }
    
    public void addVoucher(Voucher voucher) {
        vouchers.put(voucher.getCode(), voucher);
    }
    
    public Voucher getVoucher(String code) {
        return vouchers.get(code);
    }
    
    public void removeVoucher(String code) {
        vouchers.remove(code);
    }
    
    public boolean hasVoucher(String code) {
        return vouchers.containsKey(code);
    }
    
    public Map<String, Voucher> getAllVouchers() {
        return vouchers;
    }
}
