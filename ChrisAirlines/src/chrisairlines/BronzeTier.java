package chrisairlines;

import java.util.Arrays;
import java.util.List;

public class BronzeTier extends LoyaltyTier {
    public BronzeTier() {
        super("Bronze Tier", 0, 10000);
    }
    
    @Override
    public double calculateBonusMileage(double totalAmount) {
        return 0;
    }

    @Override
    public List<String> getPerks() {
        return Arrays.asList("Standard mileage points accrual for flights");
    }
}
