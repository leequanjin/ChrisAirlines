package chrisairlines;

import java.util.Arrays;
import java.util.List;

public class GoldTier extends LoyaltyTier {
    public GoldTier() {
        super("Gold Tier", 50001, 100000);
    }
    
    @Override
    public double calculateBonusMileage(double totalAmount) {
        return totalAmount * 0.15;
    }

    @Override
    public List<String> getPerks() {
        return Arrays.asList("15% bonus mileage points on flights", "Priority boarding", "Access to airport lounges", "Extra baggage allowance");
    }
}
