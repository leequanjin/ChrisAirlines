package chrisairlines;

public class Membership {
  private static int membership_id_count = 100000;  
    
  private int membership_id;
  private String member_name;
  private String contact_info;
  private MembershipLevel membership_level; // enum for Bronze, Silver, Gold etc.
  private int points_accumulated;
  private MembershipStatus membership_status; // enum for Active, Inactive etc.
  private String password; // hashed password for secure storage


  public Membership(String member_name, String contact_info, String password) {
    this.membership_id = membership_id_count;
    this.member_name = member_name;
    this.contact_info = contact_info;
    this.membership_level = MembershipLevel.BRONZE;
    this.points_accumulated = 0;
    this.membership_status = MembershipStatus.ACTIVE;
    this.password = password;
    membership_id_count++;
  }

    public int getMembership_id() {
        return membership_id;
    }

    public String getMember_name() {
        return member_name;
    }

    public String getContact_info() {
        return contact_info;
    }

    public MembershipLevel getMembership_level() {
        return membership_level;
    }

    public int getPoints_accumulated() {
        return points_accumulated;
    }

    public MembershipStatus getMembership_status() {
        return membership_status;
    }

    public String getPassword() {
        return password;
    }

    @Override
    public String toString() {
        return "Membership{" + "membership_id=" + membership_id + ", member_name=" + member_name + ", contact_info=" + contact_info + ", membership_level=" + membership_level + ", points_accumulated=" + points_accumulated + ", membership_status=" + membership_status + ", password=" + password + '}';
    }
  
    enum MembershipLevel {
        BRONZE,
        SILVER,
        GOLD,
        PLATINUM
    }

    enum MembershipStatus {
      ACTIVE,
      INACTIVE
    }

}




