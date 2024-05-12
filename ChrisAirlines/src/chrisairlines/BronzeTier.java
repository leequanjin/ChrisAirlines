package chrisairlines;

import java.util.ArrayList;
import java.util.Arrays;

public class BronzeTier extends LoyaltyTier {
    public BronzeTier() {
        super("Bronze Tier", 0, 10000, initializePerks());
    }
    
    @Override
    public double calculateBonusMileage(double totalAmount) {
        return 0;
    }
    
    private static ArrayList<String> initializePerks() {
        return new ArrayList<>(Arrays.asList("Standard mileage points accrual for flights"));
    }
}
