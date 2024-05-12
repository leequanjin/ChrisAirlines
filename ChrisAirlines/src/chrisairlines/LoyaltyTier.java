package chrisairlines;

import java.util.List;

public abstract class LoyaltyTier {
    private String tierName;
    private int minPoints;
    private int maxPoints;

    public LoyaltyTier(String tierName, int minPoints, int maxPoints) {
        this.tierName = tierName;
        this.minPoints = minPoints;
        this.maxPoints = maxPoints;
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
    
    public abstract double calculateBonusMileage(double totalAmount);

    public abstract List<String> getPerks();
}
