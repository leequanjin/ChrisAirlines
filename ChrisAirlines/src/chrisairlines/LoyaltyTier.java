package chrisairlines;

import java.util.ArrayList;

public abstract class LoyaltyTier {

    private String tierName;
    private int minPoints;
    private int maxPoints;
    private ArrayList<String> perks;

    public LoyaltyTier(String tierName, int minPoints, int maxPoints, ArrayList<String> perks) {
        this.tierName = tierName;
        this.minPoints = minPoints;
        this.maxPoints = maxPoints;
        this.perks = perks;
    }

    public String getTierName() {
        return tierName;
    }

    public void setTierName(String tierName) {
        this.tierName = tierName;
    }

    public int getMinPoints() {
        return minPoints;
    }

    public void setMinPoints(int minPoints) {
        this.minPoints = minPoints;
    }

    public int getMaxPoints() {
        return maxPoints;
    }

    public void setMaxPoints(int maxPoints) {
        this.maxPoints = maxPoints;
    }

    public ArrayList<String> getPerks() {
        return perks;
    }

    public void setPerks(ArrayList<String> perks) {
        this.perks = perks;
    }

    public abstract double calculateBonusMileage(double totalAmount);
}
