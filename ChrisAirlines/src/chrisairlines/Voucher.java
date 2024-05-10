package chrisairlines;

public class Voucher {
    private String code;
    private String type;
    private String description;
    private int pointsRequired;
    private int stock;
    
    public Voucher(){
        
    }
    
    public Voucher(String code, String type, String description, int pointsRequired, int stock) {
        this.code = code;
        this.type = type;
        this.description = description;
        this.pointsRequired = pointsRequired;
        this.stock = stock;
    }
    
    public Voucher(String code, String type, String description) {
        this.code = code;
        this.type = type;
        this.description = description;
    }
    
    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
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

    public int getStock() {
        return stock;
    }

    public void setStock(int stock) {
        this.stock = stock;
    }

    @Override
    public String toString() {
        return code + "," + type + "," + description + "," + pointsRequired + "," + stock;
    }
}
