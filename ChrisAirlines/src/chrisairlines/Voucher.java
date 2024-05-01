package chrisairlines;

public class Voucher {
    private String code;
    private String description;
    private int pointsRequired;
    

    public Voucher(String code, String description, int pointsRequired) {
        this.code = code;
        this.description = description;
        this.pointsRequired = pointsRequired;
    }
    
    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getPointsRequired() {
        return pointsRequired;
    }

    public void setPointsRequired(int pointsRequired) {
        this.pointsRequired = pointsRequired;
    } 
}
