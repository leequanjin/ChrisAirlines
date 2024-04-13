package chrisairlines;

import chrisairlines.Customer;
import java.util.Scanner;

public class ChrisAirlines {
    public static void main(String[] args) {
        Scanner scan = new Scanner(System.in);
        int choice;
        boolean validChoice = false;
        
        while (!validChoice) {
            System.out.println("You wanna login or signup? \n1. Login \n2. Signup");
            choice = scan.nextInt();

            switch (choice) {
                case 1 -> {
                    Customer.login();
                    validChoice = true;
                }
                case 2 -> {
                    Customer.signUp();
                    validChoice = true;
                }
                default -> System.out.println("Invalid choice. Please enter 1 for login or 2 for signup.");
            }
        }
    }
}