package chrisairlines;

public class RewardVoucher extends Voucher{
    private String reward;

    public RewardVoucher(String code, String description, int pointsRequired, String reward) {
        super(code, description, pointsRequired);
        this.reward = reward;
    }

    public String getReward() {
        return reward;
    }

    public void setReward(String reward) {
        this.reward = reward;
    }
}
