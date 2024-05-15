package chrisairlines;

import java.util.ArrayList;
import java.util.Arrays;

public class GoldTier extends LoyaltyTier {

    public GoldTier() {
        super("Gold Tier", 50001, 100000, initializePerks());
    }

    @Override
    public double calculateBonusMileage(double totalAmount) {
        return totalAmount * 0.15;
    }

    private static ArrayList<String> initializePerks() {
        return new ArrayList<>(Arrays.asList(
                "15% bonus mileage points on flights",
                "Priority boarding",
                "Access to airport lounges",
                "Extra baggage allowance"
        ));
    }
}
