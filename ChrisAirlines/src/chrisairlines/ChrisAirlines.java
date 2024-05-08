package chrisairlines;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Scanner;

public class ChrisAirlines {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        Customer c = new Customer();
        
        Booking booking = new Booking();        

        int selection;
        do {
            selection = menu();

            switch (selection) {
                // add customer
                case 1-> {
                    Customer customer = enterCustomerDetails();
                    
                    DatabaseHandler.saveCustomerDetailsToFile(customer, "customer_details.txt");
                }
                // add voucher
                case 2-> {
                    Voucher voucher = enterVoucherDetails();
                    
                    DatabaseHandler.saveVoucherDetailsToFile(voucher, "voucher_details.txt");
                }
                // book ticket
                case 3 -> {
                    Customer selectedCustomer = promptCustomerID();
                    
                    if (selectedCustomer != null) {
                        System.out.println("\nBOOK TICKET");
                        System.out.println("-----------");
                        System.out.println("Available Flights:\n");
                        System.out.printf("%-15s%-15s%-15s%-20s%-20s%11s%n", "Flight Code", "Origin", "Destination", "Departure Time", "Arrival Time", "Fare(RM)");
                        System.out.println("------------------------------------------------------------------------------------------------");
                        for (Flight flight : booking.getAvailableFlights().values()) {
                            System.out.printf("%-15s%-15s%-15s%-20s%-20s%10s%n",
                                    flight.getFlightCode(), 
                                    flight.getOrigin(), 
                                    flight.getDestination(),
                                    flight.getDepartureTime(), 
                                    flight.getArrivalTime(), 
                                    flight.getFare());
                        }
                        System.out.println("------------------------------------------------------------------------------------------------");

                        System.out.print("\nSelect flight code(F001): ");
                        String flight = scanner.nextLine();
                        Flight selectedFlight = booking.getFlight(flight);
                        System.out.print("Select ticket quantity(4): ");
                        int quantity = scanner.nextInt();
                        scanner.nextLine();

                        System.out.print("Use Voucher?(Y/N): ");
                        String useVoucher = scanner.nextLine();
                        if (useVoucher.equals("Y")){
                            System.out.println("\nAvailable Vouchers:");
                            System.out.printf("%-15s%-15s%-50s%15s%n", "Voucher ID", "Voucher Code", "Description", "Expiry Date");
                            System.out.println("----------------------------------------------------------------------------------------------------------------");

                            
                            List<VoucherDetails> redeemedVouchers = DatabaseHandler.loadRedeemedVouchersByCustomerID(selectedCustomer.getId(), "redeemed_vouchers.txt");
                            selectedCustomer.setRedeemedVouchers(redeemedVouchers);
                            for (VoucherDetails redeemedVoucher : selectedCustomer.getRedeemedVouchers()) {
                                if (redeemedVoucher.getStatus().equals("Valid")){
                                    Voucher voucher = redeemedVoucher.getVoucher();
                                    String formattedExipryDateTime = redeemedVoucher.getExpiryDateTime().format(formatter);
                                    System.out.printf("%-15s%-15s%-50s%15s%n",
                                        redeemedVoucher.getId(), voucher.getCode(), voucher.getDescription(), formattedExipryDateTime);
                                }
                            }

                            System.out.println("\nWhich voucher would you like to use?");
                            System.out.print("Enter voucher ID: ");
                            String selectedVoucher = scanner.nextLine();

                            for (VoucherDetails redeemedVoucher : selectedCustomer.getRedeemedVouchers()) {
                                if (redeemedVoucher.getId().equals(selectedVoucher) && redeemedVoucher.getStatus().equals("Valid")) {
                                    Voucher voucher = redeemedVoucher.getVoucher();
                                    String type = voucher.getType();
                                    LocalDateTime bookingDateTime = LocalDateTime.now();
                                    switch (type) {
                                        case "Flat Discount Amount Voucher" -> {
                                            DiscountAmtVoucher discountAmtVoucher = (DiscountAmtVoucher) voucher;
                                            BookingDetails bookingDetails = selectedCustomer.bookFlightWithDiscountAmount(selectedCustomer.getId(), selectedFlight, quantity, bookingDateTime, discountAmtVoucher.getDiscountAmount());
                                            String formattedBookingDateTime = bookingDateTime.format(formatter);

                                            System.out.println("\nBooking Date: " + formattedBookingDateTime);
                                            System.out.println(quantity + " " + selectedFlight.getFlightCode() + " flight tickets booked successfully for customer: " + selectedCustomer.getName());
                                            System.out.printf("%s%.2f%s%n", "Discount amount of RM", bookingDetails.getDiscount(), " applied successfully!");
                                            System.out.printf("%s%.2f%n", "Total Amount Due: RM", bookingDetails.getTotalAmount());
                                        }
                                        case "Percentage Discount Rate Voucher" -> {
                                            DiscountRateVoucher discountRateVoucher = (DiscountRateVoucher)voucher;
                                            BookingDetails bookingDetails = selectedCustomer.bookFlightWithDiscountRate(selectedCustomer.getId(), selectedFlight, quantity, bookingDateTime, discountRateVoucher.getDiscountRate());
                                            String formattedBookingDateTime = bookingDateTime.format(formatter);

                                            System.out.println("\nBooking Date: " + formattedBookingDateTime);
                                            System.out.println(quantity + " " + selectedFlight.getFlightCode() + " flight tickets booked successfully for customer: " + selectedCustomer.getName());
                                            System.out.printf("%s%.2f%s%n", "Discount amount of RM", bookingDetails.getDiscount(), " applied successfully!");
                                            System.out.printf("%s%.2f%n", "Total Amount Due: RM", bookingDetails.getTotalAmount());
                                        }
                                        case "Additional Rewards or Services Voucher" -> {
                                            RewardVoucher rewardVoucher = (RewardVoucher)voucher;
                                            BookingDetails bookingDetails = selectedCustomer.bookFlightWithReward(selectedCustomer.getId(), selectedFlight, quantity, bookingDateTime, rewardVoucher.getReward());
                                            String formattedBookingDateTime = bookingDateTime.format(formatter);

                                            System.out.println("\nBooking Date: " + formattedBookingDateTime);
                                            System.out.println(quantity + " " + selectedFlight.getFlightCode() + " flight tickets booked successfully for customer: " + selectedCustomer.getName());
                                            System.out.printf("%s%s%s%n", "Reward \"", bookingDetails.getReward(), "\" claimed successfully!");
                                            System.out.printf("%s%.2f%n", "Total Amount Due: RM", bookingDetails.getTotalAmount());
                                        }
                                        default -> {
                                            System.out.println("Voucher not recognized, returning to main menu...\n");
                                        }
                                    }

                                    redeemedVoucher.setStatus("Used");
                                    DatabaseHandler.updateRedeemedVouchers("redeemed_vouchers.txt", redeemedVoucher);
                                    
                                    break;
                                } else {
                                    System.out.println("Invalid voucher");
                                }
                            }
                        } else {
                            LocalDateTime bookingDateTime = LocalDateTime.now();
                            BookingDetails bookingDetails = selectedCustomer.bookFlight(selectedCustomer.getId(), selectedFlight, quantity, bookingDateTime);
                            String formattedBookingDateTime = bookingDateTime.format(formatter);

                            System.out.println("\nBooking Date: " + formattedBookingDateTime);
                            System.out.println(quantity + " " + selectedFlight.getFlightCode() + " flight tickets booked successfully for customer: " + selectedCustomer.getName());
                            System.out.printf("%s%.2f%n", "Total Amount Due: RM", bookingDetails.getTotalAmount());
                        }
                        
                        
                    } else {
                        System.out.println("Customer not found.");
                    }
                }
                // redeem voucher
                case 4 -> {
                    Customer selectedCustomer = promptCustomerID();

                    if (selectedCustomer != null) {
                        List<Voucher> vouchers = DatabaseHandler.readVouchersFromFile("voucher_details.txt");
                        
                        System.out.println("\nREDEEM VOUCHER");
                        System.out.println("--------------");
                        System.out.println("Available Vouchers:\n");
                        System.out.printf("%-15s%-50s%15s%15s%n", "Voucher Code", "Description", "Points Required", "Stock Left");
                        System.out.println("-----------------------------------------------------------------------------------------------");
                        for (Voucher voucher : vouchers) {
                            System.out.printf("%-15s%-50s%15d%15d%n", voucher.getCode(), voucher.getDescription(), voucher.getPointsRequired(), voucher.getStock());
                        }
                        System.out.println("-----------------------------------------------------------------------------------------------");
                        System.out.println("Available mileage points: " + selectedCustomer.getMileagePoints());

                        System.out.print("\nSelect voucher code (eg.1): ");
                        String voucherId = scanner.nextLine();

                        Voucher selectedVoucher = null;
                        for (Voucher voucher : vouchers) {
                            if (voucher.getCode().equals(voucherId)) {
                                selectedVoucher = voucher;
                            break;
                            }
                        }

                        if (selectedVoucher != null && selectedCustomer.getMileagePoints() >= selectedVoucher.getPointsRequired() && selectedVoucher.getStock() >= 1) {
                            int newId = DatabaseHandler.generateNewId("redeemed_vouchers.txt");
                            String id = String.valueOf(newId);
                            LocalDateTime redemptionDateTime = LocalDateTime.now();

                            selectedCustomer.redeemVoucher(id, selectedVoucher.getPointsRequired(), selectedVoucher, redemptionDateTime);
                            
                            String formattedRedemptionDateTime = redemptionDateTime.format(formatter);
                            System.out.println("Voucher " + selectedVoucher.getCode() + " redeemed successfully for customer: " + selectedCustomer.getName());
                            System.out.println("Redemption Date: " + formattedRedemptionDateTime);
                        } else {
                            System.out.println("Invalid voucher code or insufficient mileage points for redemption or out of stock.");
                        }
                    } else {
                        System.out.println("Customer not found.");
                    }
                }
                // view profile
                case 5 -> {
                    Customer selectedCustomer = promptCustomerID();
                    
                    if (selectedCustomer != null) {
                        System.out.println("\nVIEW PROFILE");
                        System.out.println("------------");
                        System.out.println("Customer ID: " + selectedCustomer.getId());
                        System.out.println("Name: " + selectedCustomer.getName());
                        System.out.println("Email: " + selectedCustomer.getEmail());
                        System.out.println("Phone: " + selectedCustomer.getPhone());
                        System.out.println("Mileage Points: " + selectedCustomer.getMileagePoints());
                        System.out.println("Loyalty Points: " + selectedCustomer.getLoyaltyPoints());
                        System.out.println("Loyalty Tier: " + selectedCustomer.getLoyaltyTier());

                        System.out.println("\nYour Vouchers:\n");
                        System.out.printf("%-15s%-15s%-50s%-20s%-20s%-20s%n","Voucher ID" , "Voucher Code", "Description", "Redeemed Date", "Expiry Date", "Status");
                        System.out.println("-------------------------------------------------------------------------------------------------------------------------------");

                        List<VoucherDetails> redeemedVouchers = DatabaseHandler.loadRedeemedVouchersByCustomerID(selectedCustomer.getId(), "redeemed_vouchers.txt");
                        selectedCustomer.setRedeemedVouchers(redeemedVouchers);
                        for (VoucherDetails redeemedVoucher : selectedCustomer.getRedeemedVouchers()) {
                            Voucher voucher = redeemedVoucher.getVoucher();
                            String formattedRedeemedDateTime = redeemedVoucher.getRedeemedDateTime().format(formatter);
                            String formattedExpiryDateTime = redeemedVoucher.getExpiryDateTime().format(formatter);
                            System.out.printf("%-15s%-15s%-50s%-20s%-20s%-20s%n",
                                    redeemedVoucher.getId(), voucher.getCode(), voucher.getDescription(), formattedRedeemedDateTime, formattedExpiryDateTime, redeemedVoucher.getStatus());
                        }  
                        System.out.println("-------------------------------------------------------------------------------------------------------------------------------");
                    } else {
                        System.out.println("Customer not found.");
                    }
                }
                // view booking history
                case 6 -> {
                    Customer selectedCustomer = promptCustomerID();
                    
                    if (selectedCustomer != null) {
                        System.out.println("\nVIEW BOOKING HISTORY");
                    System.out.println("--------------------");
                    System.out.println("Customer's Booked Flights:\n");
                    System.out.printf("%-15s%-15s%-15s%18s%18s%16s%10s%16s%15s%19s%35s%n", "Flight Code", "Origin", "Destination", "Departure Time", "Arrival Time", "Fare(RM)", "Quantity", "Discount(RM)", "Total(RM)", "Booking Date", "Additional Notes");
                    System.out.println("------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------");
                        for (BookingDetails bookedFlight : DatabaseHandler.loadBookingsFromFile(selectedCustomer.getId(), "booked_flights.txt")) {
                            Flight flight = bookedFlight.getFlight();
                            String formattedBookingDateTime = bookedFlight.getBookingDateTime().format(formatter);
                            System.out.printf("%-15s%-15s%-15s%18s%18s%15.2f%11d%15.2f%15.2f%20s%35s%n",
                                    flight.getFlightCode(), 
                                    flight.getOrigin(), 
                                    flight.getDestination(),
                                    flight.getDepartureTime(), 
                                    flight.getArrivalTime(), 
                                    flight.getFare(),
                                    bookedFlight.getQuantity(), 
                                    bookedFlight.getDiscount(),
                                    bookedFlight.getTotalAmount(),
                                    formattedBookingDateTime,
                                    bookedFlight.getReward());
                        }
                        System.out.println("------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------");
                    } else {
                        System.out.println("Customer not found.");
                    }
                }

                default -> {
                    System.out.println("\nTerminating Session...");
                }
            }
        } while (selection >= 1 && selection <= 7);
    }

    public static int menu() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("\nMAIN MENU");
        System.out.println("---------");
        System.out.println("1. Add Customer to system");
        System.out.println("2. Add Voucher to system");
        System.out.println("3. Book Ticket for Customer");
        System.out.println("4. Redeem Voucher for Customer");
        System.out.println("5. View Customer Profile");
        System.out.println("6. View Customer Booking History");
        System.out.println("7. Quit");
        System.out.print("\nEnter your selection: ");
        int selection = scanner.nextInt();
        return selection;
    }
    
    public static Customer enterCustomerDetails() {
        Scanner scanner = new Scanner(System.in);
        
        System.out.println("\nEnter customer details:");

        int newId = DatabaseHandler.generateNewId("customer_details.txt");
        String id = String.valueOf(newId);
        
        System.out.print("Name: ");
        String name = scanner.nextLine();
        
        System.out.print("Email: ");
        String email = scanner.nextLine();
        
        System.out.print("Phone: ");
        String phone = scanner.nextLine();
        
        System.out.print("Mileage Points: ");
        int mileagePoints = scanner.nextInt();
        scanner.nextLine();
        
        System.out.print("Loyalty Points: ");
        int loyaltyPoints = scanner.nextInt();
        scanner.nextLine();
        
        return new Customer(id, name, email, phone, mileagePoints, loyaltyPoints);
    }
    
    public static Voucher enterVoucherDetails() {
        Scanner scanner = new Scanner(System.in);

        int newId = DatabaseHandler.generateNewId("voucher_details.txt");
        String id = String.valueOf(newId);

        System.out.println("\nVOUCHER TYPES");
        System.out.println("-------------");
        System.out.println("1. Flat Discount Amount Voucher");
        System.out.println("2. Percentage Discount Rate Voucher");
        System.out.println("3. Additional Rewards or Services Voucher\n");
        System.out.print("Select voucher type: ");
        
        int selection = scanner.nextInt();
        scanner.nextLine();
        
        switch (selection) {
            case 1 -> {
                System.out.print("Discount Amount: RM");
                double discountAmount = scanner.nextDouble();
                scanner.nextLine();
                
                String type = "Flat Discount Amount Voucher";
                String formattedDiscountAmount = String.format("%.2f", discountAmount);
                String description = "RM" + formattedDiscountAmount + " discount on next flight booking";
                
                System.out.print("Points Required to Redeem: ");
                int pointsRequired = scanner.nextInt();
                scanner.nextLine();
                
                System.out.print("Available Stock: ");
                int stock = scanner.nextInt();
                scanner.nextLine();
                
                return new DiscountAmtVoucher(id, type, description, pointsRequired, stock, discountAmount);
            }
            case 2 -> {
                System.out.print("Discount Rate (eg.0.2): ");
                double discountRate = scanner.nextDouble();
                scanner.nextLine();
                
                String type = "Percentage Discount Rate Voucher";
                String formattedDiscountRate = String.format("%.0f", discountRate * 100);
                String description = formattedDiscountRate + "% off on next flight booking";
                
                System.out.print("Points Required to Redeem: ");
                int pointsRequired = scanner.nextInt();
                scanner.nextLine();
                
                System.out.print("Available Stock: ");
                int stock = scanner.nextInt();
                scanner.nextLine();
                
                return new DiscountRateVoucher(id, type, description, pointsRequired, stock, discountRate);
            }
            case 3 -> {
                System.out.print("Reward: ");
                String reward = scanner.nextLine();
                
                String type = "Additional Rewards or Services Voucher";
                String description = reward;
                
                System.out.print("Points Required to Redeem: ");
                int pointsRequired = scanner.nextInt();
                scanner.nextLine();
                
                System.out.print("Available Stock: ");
                int stock = scanner.nextInt();
                scanner.nextLine();
                
                return new RewardVoucher(id, type, description, pointsRequired, stock, reward);
            }
            
            default -> {
                return null;
            }
        }
    }
    
    public static Customer promptCustomerID(){
        List<Customer> customers = DatabaseHandler.readCustomersFromFile("customer_details.txt");

        System.out.println("LIST OF CUSTOMERS");
        System.out.println("-----------------");
        for (Customer customer : customers) {
            System.out.println("Customer ID: " + customer.getId());
            System.out.println("Name: " + customer.getName());
            System.out.println("-----------------");
        }
        
        System.out.print("Enter the ID of the customer you want to view details for: ");
        Scanner scanner = new Scanner(System.in);
        String customerId = scanner.nextLine();

        Customer selectedCustomer = null;
        for (Customer customer : customers) {
            if (customer.getId().equals(customerId)) {
                selectedCustomer = customer;
                break;
            }
        }
        
        return(selectedCustomer);
    }
}
