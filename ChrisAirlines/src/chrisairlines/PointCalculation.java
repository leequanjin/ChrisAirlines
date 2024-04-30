package chrisairlines;

public class PointCalculation {
    public static int calculateMileagePoints(double spentAmount, double bonusPercentage) {
        return (int) (spentAmount * bonusPercentage);
    }

    public static double getBonusPercentage(String loyaltyTier) {
        double baseBonus = 0.0;
        switch (loyaltyTier) {
            case "Bronze" -> baseBonus = 0.0;
            case "Silver" -> baseBonus = 0.1;
            case "Gold" -> baseBonus = 0.15;
            case "Platinum" -> baseBonus = 0.20;
        }
        return 1.0 + baseBonus;
    }

    public static int calculateLoyaltyPoints(double spentAmount) {
        return (int) spentAmount;
    }
}
