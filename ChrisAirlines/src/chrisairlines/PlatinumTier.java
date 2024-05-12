package chrisairlines;

import java.util.Arrays;
import java.util.List;

public class PlatinumTier extends LoyaltyTier {
    public PlatinumTier() {
        super("Platinum Tier", 100001, 999999);
    }
    
    @Override
    public double calculateBonusMileage(double totalAmount) {
        return totalAmount * 0.20;
    }

    @Override
    public List<String> getPerks() {
        return Arrays.asList("20% bonus mileage points on flights", "Exclusive access to premium customer service", "Complimentary seat upgrades, subject to availability", "Exclusive invitations to Chris Airlines events");
    }
}
