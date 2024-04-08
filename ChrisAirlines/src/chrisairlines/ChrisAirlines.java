package chrisairlines;

public class ChrisAirlines {

  public static void main(String[] args) {
    Membership newMember = MembershipCreation.createMembership();

    if (newMember != null) {
      System.out.println("New member created successfully!");
      // You can now access and potentially save the newMember object
    } else {
      System.out.println("Failed to create new membership.");
    }
    
    System.out.println("\nYour UID: " + newMember.getMembership_id());
    System.out.println("Your Name : " + newMember.getMember_name());
    System.out.println("Your Contact Info (Phone) : " + newMember.getContact_info());
    System.out.println("Your Membership Level : " + newMember.getMembership_level());
    System.out.println("Your Points Accumulated : " + newMember.getPoints_accumulated());
    System.out.println("Your Membership Status : " + newMember.getMembership_status());
  }
}
