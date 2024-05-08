package chrisairlines;

public class RewardVoucher extends Voucher{
    private String reward;

    RewardVoucher() {
    
    }
    
    public RewardVoucher(String code, String type, String description, String reward) {
        super(code, type, description);
        this.reward = reward;
    }
    
    public RewardVoucher(String code, String type, String description, int pointsRequired, int stock, String reward) {
        super(code, type, description, pointsRequired, stock);
        this.reward = reward;
    }

    public String getReward() {
        return reward;
    }

    public void setReward(String reward) {
        this.reward = reward;
    }
    
    @Override
    public String toString() {
        return super.toString() + "," + reward;
    }
}
