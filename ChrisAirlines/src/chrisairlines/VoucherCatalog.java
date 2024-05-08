//package chrisairlines;
//
//import java.util.HashMap;
//import java.util.Map;
//
//public class VoucherCatalog {
//    private HashMap<String, Voucher> vouchers;
//
//    public VoucherCatalog() {
//        this.vouchers = new HashMap<>();
//        vouchers.put("V001", new DiscountRateVoucher("V001", "10% off on next flight booking", 10000, 10, 0.1));
//        vouchers.put("V002", new DiscountRateVoucher("V002", "20% off on next flight booking", 20000, 10, 0.2));
//        vouchers.put("V003", new DiscountRateVoucher("V003", "25% off on next flight booking", 25000, 10, 0.25));
//        vouchers.put("V004", new DiscountAmtVoucher("V004", "RM20 discount on next flight booking", 2000, 10, 20));
//        vouchers.put("V005", new DiscountAmtVoucher("V005", "RM50 discount on next flight booking", 4000, 10, 50));
//        vouchers.put("V006", new DiscountAmtVoucher("V006", "RM100 discount on next flight booking", 7500, 10, 100));
//        vouchers.put("V007", new RewardVoucher("V007", "Free upgrade to business class", 20000, 10, "Business Class Upgrade"));
//        vouchers.put("V008", new RewardVoucher("V008", "Complimentary airport lounge access", 18000, 10, "Complimentary Airport Lounge Access"));
//    }
//    
//    public void addVoucher(Voucher voucher) {
//        vouchers.put(voucher.getId(), voucher);
//    }
//    
//    public Voucher getVoucher(String code) {
//        return vouchers.get(code);
//    }
//    
//    public void removeVoucher(String code) {
//        vouchers.remove(code);
//    }
//    
//    public Map<String, Voucher> getAllVouchers() {
//        return vouchers;
//    }
//}
