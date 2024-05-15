package chrisairlines;

import java.util.ArrayList;
import java.util.Arrays;

public class PlatinumTier extends LoyaltyTier {

    public PlatinumTier() {
        super("Platinum Tier", 100001, 999999, initializePerks());
    }

    @Override
    public double calculateBonusMileage(double totalAmount) {
        return totalAmount * 0.20;
    }

    private static ArrayList<String> initializePerks() {
        return new ArrayList<>(Arrays.asList(
                "20% bonus mileage points on flights",
                "Exclusive access to premium customer service",
                "Complimentary seat upgrades, subject to availability",
                "Exclusive invitations to Chris Airlines events"
        ));
    }
}
