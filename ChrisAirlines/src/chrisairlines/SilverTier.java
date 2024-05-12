package chrisairlines;

import java.util.Arrays;
import java.util.List;

public class SilverTier extends LoyaltyTier {
    public SilverTier() {
        super("Silver Tier", 10001, 50000);
    }
    
    @Override
    public double calculateBonusMileage(double totalAmount) {
        return totalAmount * 0.1;
    }

    @Override
    public List<String> getPerks() {
        return Arrays.asList("10% bonus mileage points on flights", "More baggage allowance");
    }
}
