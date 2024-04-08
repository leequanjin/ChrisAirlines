package chrisairlines;

import java.util.Scanner;

public class MembershipCreation {

  public static Membership createMembership() {
    Scanner scan = new Scanner(System.in);
    int count = 0;

    // Get User info member name, contact info, password
    System.out.print("Enter Member Name : ");
    String name = scan.nextLine();
    System.out.print("Enter Contact Info (Phone) : ");
    String contact = scan.next();
    System.out.print("Enter Password : ");
    String password = scan.next();
    String re_password;

    do {
      System.out.print("Re-enter Password : ");
      re_password = scan.next();
      count++; // Increment count on each iteration
    } while (!password.equals(re_password) && count < 3); // Only loop if passwords don't match and attempts are less than 3

    if (count == 3) {
      System.err.println("Maximum password attempt reached. Exiting program.");
      return null; // Indicate failure if attempts reach the limit
    }

    return new Membership(name, contact, password); // Create and return a new Membership object
  }
}
