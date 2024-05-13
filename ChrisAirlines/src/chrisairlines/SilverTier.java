package chrisairlines;

import java.util.ArrayList;
import java.util.Arrays;

public class SilverTier extends LoyaltyTier {
    public SilverTier() {
        super("Silver Tier", 10001, 50000, initializePerks());
    }
    
    @Override
    public double calculateBonusMileage(double totalAmount) {
        return totalAmount * 0.1;
    }

    private static ArrayList<String> initializePerks() {
        return new ArrayList<>(Arrays.asList(
                "10% bonus mileage points on flights", 
                "More baggage allowance"
        ));
    }
}
