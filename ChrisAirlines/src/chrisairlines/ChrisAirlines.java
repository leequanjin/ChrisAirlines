package chrisairlines;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ChrisAirlines {

    //Colours
    static String Black = "\u001b[30m";
    static String Red = "\u001b[31m";
    //static String Green = "\u001b[32;2m";
    static String Yellow = "\u001b[33m";
    //static String Blue = "\u001b[34m";
    //static String Magenta = "\u001b[35m";
    static String Cyan = "\u001b[36m";
    //static String CyanBg = "\u001b[46m";
    //static String White = "\u001b[37;3m";
    static String Silver = "\u001b[37;2m";
    //static String BrightBlack = "\u001b[30;3m";
    static String BrightRed = "\u001b[31;1m";
    static String BrightRed2 = "\u001b[31;2m";
    //static String BrightGreen = "\u001b[32;2m";
    static String BrightYellow = "\u001b[33;2m";
    static String BrightBlue = "\u001b[34;1m";
    //static String BrightMagenta = "\u001b[35;1m";
    static String BrightCyan = "\u001b[36;2m";
    //static String BrightWhite = "\u001b[37;1m";
    static String BrightWhiteBg = "\u001b[47;2m";
    static String BrightWhiteBgLight = "\u001b[47;3m";
    static String Reset = "\u001b[0m";

    //Example use case
    //System.out.println(Black + "Black Text" + Reset);
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        Booking booking = new Booking();

        List<Customer> customers = DatabaseHandler.readCustomersFromFile("customer_details.txt");
        for (Customer customer : customers) {
            customer.yearlyLoyaltyPointReset();
            DatabaseHandler.updateCustomerInfo("customer_details.txt", customer);
        }

        int selection;
        do {
            displayMenu();
            selection = getValidIntegerInput(scanner, "Enter selection: ", 1, 11);

            switch (selection) {
                // add customer
                case 1 -> {
                    Customer customer = enterCustomerDetails();

                    DatabaseHandler.saveCustomerDetailsToFile("customer_details.txt", customer);
                }
                // add voucher
                case 2 -> {
                    Voucher voucher = enterVoucherDetails();

                    DatabaseHandler.saveVoucherDetailsToFile("voucher_details.txt", voucher);
                }
                // book ticket
                case 3 -> {
                    Customer selectedCustomer = promptCustomerID();

                    if (selectedCustomer != null) {
                        selectedCustomer.checkMileagePointValidity(selectedCustomer.getLastActivityDate());
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

                        String flightCode = getValidFlightCode(scanner, booking.getAvailableFlights());
                        if ("0".equals(flightCode)) {
                            System.out.println("Booking cancelled, returning to main menu...");
                            break;
                        }

                        Flight selectedFlight = booking.getFlight(flightCode);

                        int quantity = getValidIntegerInput(scanner, "Select ticket quantity (0 to exit): ");

                        if (quantity == 0) {
                            System.out.println("Booking cancelled, returning to main menu...");
                            break;
                        }

                        String useVoucher = getValidConfirmation(scanner, "Use Voucher?(Y/N): ");
                        if (useVoucher.equals("Y")) {
                            List<VoucherDetails> redeemedVouchers = DatabaseHandler.loadRedeemedVouchersByCustomerId(selectedCustomer.getId(), "redeemed_vouchers.txt");
                            updateVoucherStatus(redeemedVouchers);
                            DatabaseHandler.writeRedeemedVouchersToFile("redeemed_vouchers.txt", redeemedVouchers);
                            selectedCustomer.setRedeemedVouchers(redeemedVouchers);

                            boolean voucherExists = false;
                            for (VoucherDetails redeemedVoucher : selectedCustomer.getRedeemedVouchers()) {
                                if (redeemedVoucher.getStatus().equals("Valid")) {
                                    voucherExists = true;
                                    break;
                                }
                            }

                            if (!voucherExists) {
                                System.out.println("\nSorry, you don't have any valid vouchers in your account.");
                                System.out.println("Booking cancelled, returning to main menu...");
                                break;
                            }

                            System.out.println("\nAvailable Vouchers:");
                            System.out.printf("%-15s%-15s%-50s%15s%n", "Voucher ID", "Voucher Code", "Description", "Expiry Date");
                            System.out.println("----------------------------------------------------------------------------------------------------------------");

                            for (VoucherDetails redeemedVoucher : selectedCustomer.getRedeemedVouchers()) {
                                if (redeemedVoucher.getStatus().equals("Valid")) {
                                    Voucher voucher = redeemedVoucher.getVoucher();
                                    String formattedExipryDateTime = redeemedVoucher.getExpiryDateTime().format(formatter);
                                    System.out.printf("%-15s%-15s%-50s%15s%n",
                                            redeemedVoucher.getId(), voucher.getCode(), voucher.getDescription(), formattedExipryDateTime);
                                }
                            }

                            System.out.println("----------------------------------------------------------------------------------------------------------------");

                            String selectedVoucher = getValidRedeemedVoucherId(scanner, redeemedVouchers, "Which voucher would you like to use? Enter voucher ID (0 to exit): ");

                            if ("0".equals(selectedVoucher)) {
                                System.out.println("Booking cancelled, returning to main menu...");
                                break;
                            }

                            for (VoucherDetails redeemedVoucher : selectedCustomer.getRedeemedVouchers()) {
                                if (redeemedVoucher.getId().equals(selectedVoucher) && redeemedVoucher.getStatus().equals("Valid")) {
                                    Voucher voucher = redeemedVoucher.getVoucher();
                                    String type = voucher.getType();
                                    LocalDateTime bookingDateTime = LocalDateTime.now();
                                    switch (type) {
                                        case "Flat Discount Amount Voucher" -> {
                                            DiscountAmtVoucher discountAmtVoucher = (DiscountAmtVoucher) voucher;
                                            BookingDetails bookingDetails = selectedCustomer.bookFlightWithDiscountAmount(selectedCustomer.getId(), selectedFlight, quantity, bookingDateTime, discountAmtVoucher.getDiscountAmount());
                                            DatabaseHandler.saveBookingDetailToFile(selectedCustomer.getId(), bookingDetails);
                                            String formattedBookingDateTime = bookingDateTime.format(formatter);

                                            System.out.println("\nBooking Date: " + formattedBookingDateTime);
                                            System.out.println(quantity + " " + selectedFlight.getFlightCode() + " flight tickets booked successfully for customer: " + selectedCustomer.getName());
                                            System.out.printf("%s%.2f%s%n", "Discount amount of RM", bookingDetails.getDiscount(), " applied successfully!");
                                            System.out.printf("%s%.2f%n", "Total Amount Due: RM", bookingDetails.getTotalAmount());
                                        }
                                        case "Percentage Discount Rate Voucher" -> {
                                            DiscountRateVoucher discountRateVoucher = (DiscountRateVoucher) voucher;
                                            BookingDetails bookingDetails = selectedCustomer.bookFlightWithDiscountRate(selectedCustomer.getId(), selectedFlight, quantity, bookingDateTime, discountRateVoucher.getDiscountRate());
                                            DatabaseHandler.saveBookingDetailToFile(selectedCustomer.getId(), bookingDetails);
                                            String formattedBookingDateTime = bookingDateTime.format(formatter);

                                            System.out.println("\nBooking Date: " + formattedBookingDateTime);
                                            System.out.println(quantity + " " + selectedFlight.getFlightCode() + " flight tickets booked successfully for customer: " + selectedCustomer.getName());
                                            System.out.printf("%s%.2f%s%n", "Discount amount of RM", bookingDetails.getDiscount(), " applied successfully!");
                                            System.out.printf("%s%.2f%n", "Total Amount Due: RM", bookingDetails.getTotalAmount());
                                        }
                                        case "Additional Rewards or Services Voucher" -> {
                                            RewardVoucher rewardVoucher = (RewardVoucher) voucher;
                                            BookingDetails bookingDetails = selectedCustomer.bookFlightWithReward(selectedCustomer.getId(), selectedFlight, quantity, bookingDateTime, rewardVoucher.getReward());
                                            DatabaseHandler.saveBookingDetailToFile(selectedCustomer.getId(), bookingDetails);
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
                                    System.err.println("Invalid voucher");
                                }
                            }

                        } else {
                            LocalDateTime bookingDateTime = LocalDateTime.now();
                            BookingDetails bookingDetails = selectedCustomer.bookFlight(selectedCustomer.getId(), selectedFlight, quantity, bookingDateTime);
                            DatabaseHandler.saveBookingDetailToFile(selectedCustomer.getId(), bookingDetails);
                            String formattedBookingDateTime = bookingDateTime.format(formatter);

                            System.out.println("\nBooking Date: " + formattedBookingDateTime);
                            System.out.println(quantity + " " + selectedFlight.getFlightCode() + " flight tickets booked successfully for customer: " + selectedCustomer.getName());
                            System.out.printf("%s%.2f%n", "Total Amount Due: RM", bookingDetails.getTotalAmount());
                        }

                        selectedCustomer.setLastActivityDate(LocalDateTime.now());
                        DatabaseHandler.updateCustomerInfo("customer_details.txt", selectedCustomer);

                    } else {
                        System.out.println(Red + "Customer not found" + Reset + ", returning to main menu...");
                    }
                }
                // redeem voucher
                case 4 -> {
                    Customer selectedCustomer = promptCustomerID();

                    if (selectedCustomer != null) {

                        List<Voucher> vouchers = DatabaseHandler.readVouchersFromFile("voucher_details.txt");
                        selectedCustomer.checkMileagePointValidity(selectedCustomer.getLastActivityDate());

                        System.out.println("\nREDEEM VOUCHER");
                        System.out.println("--------------");
                        displayVoucherList();

                        System.out.println("Available mileage points: " + selectedCustomer.getMileagePoints());
                        
                        String voucherCode = getValidVoucherCode(scanner, vouchers, "Select voucher code (0 to exit): ");
                        if("0".equals(voucherCode)){
                            System.out.println("Process cancelled, returning to main menu...");
                            break;
                        }
                        
                        Voucher selectedVoucher = null;
                        for (Voucher voucher : vouchers) {
                            if (voucher.getCode().equals(voucherCode)) {
                                selectedVoucher = voucher;
                                break;
                            }
                        }

                        if (selectedVoucher != null && selectedCustomer.getMileagePoints() >= selectedVoucher.getPointsRequired() && selectedVoucher.getStock() >= 1) {
                            int newId = DatabaseHandler.generateNewId("redeemed_vouchers.txt");
                            String id = String.valueOf(newId);
                            LocalDateTime redemptionDateTime = LocalDateTime.now();

                            VoucherDetails voucherDetails = selectedCustomer.redeemVoucher(id, selectedVoucher.getPointsRequired(), selectedVoucher, redemptionDateTime);

                            selectedCustomer.setLastActivityDate(LocalDateTime.now());
                            DatabaseHandler.updateCustomerInfo("customer_details.txt", selectedCustomer);
                            DatabaseHandler.saveRedeemedVoucherToFile("redeemed_vouchers.txt", voucherDetails);
                            DatabaseHandler.updateVoucherDetails("voucher_details.txt", selectedVoucher);

                            String formattedRedemptionDateTime = redemptionDateTime.format(formatter);
                            System.out.println("Voucher " + selectedVoucher.getCode() + " redeemed successfully for customer: " + selectedCustomer.getName());
                            System.out.println("Redemption Date: " + formattedRedemptionDateTime);
                        } else {
                            System.err.println("Invalid voucher code or insufficient mileage points for redemption or out of stock.");
                        }
                    } else {
                        System.out.println(Red + "Customer not found" + Reset + ", returning to main menu...");
                    }
                }
                // view profile
                case 5 -> {
                    Customer selectedCustomer = promptCustomerID();

                    if (selectedCustomer != null) {
                        displayProfileDetails(selectedCustomer);
                    } else {
                        System.out.println(Red + "Customer not found" + Reset + ", returning to main menu...");
                    }
                }
                // view booking history
                case 6 -> {
                    Customer selectedCustomer = promptCustomerID();

                    if (selectedCustomer != null) {
                        displayBookingHistory(selectedCustomer);
                    } else {
                        System.out.println(Red + "Customer not found" + Reset + ", returning to main menu...");
                    }
                }
                // view all customers in system
                case 7 -> {
                    displayCustomerList();
                }
                // view all vouchers in system
                case 8 -> {
                    System.out.println("\nVOUCHER LIST");
                    System.out.println("------------");
                    displayVoucherList();
                }
                // view all loyalty tier perks
                case 9 -> {
                    displayLoyaltyTierPerks();
                }

                case 10 -> {
                    displayPolicies();
                }

                default -> {
                    System.out.println("\nTerminating Session...");
                }
            }
        } while (selection >= 1 && selection <= 10);
    }

    public static void displayMenu() {
        System.out.println("\nMAIN MENU");
        System.out.println("---------");
        System.out.println("1. Add Customer to system");
        System.out.println("2. Add Voucher to system");
        System.out.println("3. Book Ticket for Customer");
        System.out.println("4. Redeem Voucher for Customer");
        System.out.println("5. View Customer Profile");
        System.out.println("6. View Customer Booking History");
        System.out.println("7. View All Customers in System");
        System.out.println("8. View All Vouchers in System");
        System.out.println("9. View Loyalty Tier Perks");
        System.out.println("10. View Policies");
        System.out.println("11. Quit\n");
    }

    private static int getValidIntegerInput(Scanner scanner, String prompt, int minValue, int maxValue) {
        int value;
        while (true) {
            System.out.print(prompt);
            try {
                value = Integer.parseInt(scanner.nextLine());
                if (value < minValue || value > maxValue) {
                    System.err.println("Invalid input! Please enter an integer between " + minValue + " and " + maxValue + ".");
                } else {
                    break;
                }
            } catch (NumberFormatException e) {
                System.err.println("Invalid input! Please enter a valid integer.");
            }
        }
        return value;
    }

    private static double getValidDoubleInput(Scanner scanner, String prompt, double minValue, double maxValue) {
        double value;
        while (true) {
            System.out.print(prompt);
            try {
                value = Double.parseDouble(scanner.nextLine());
                if (value < minValue || value > maxValue) {
                    System.err.println("Invalid input! Please enter an number between " + minValue + " and " + maxValue + ".");
                } else {
                    break;
                }
            } catch (NumberFormatException e) {
                System.err.println("Invalid input! Please enter a valid number.");
            }
        }
        return value;
    }

    private static int getValidIntegerInput(Scanner scanner, String prompt) {
        int value;
        while (true) {
            System.out.print(prompt);
            try {
                value = Integer.parseInt(scanner.nextLine());
                if (value < 0) {
                    System.err.println("Value cannot be negative. Please enter a valid integer.");
                } else {
                    break;
                }
            } catch (NumberFormatException e) {
                System.err.println("Invalid input! Please enter a valid integer.");
            }
        }
        return value;
    }

    private static double getValidDoubleInput(Scanner scanner, String prompt) {
        double value;
        while (true) {
            System.out.print(prompt);
            try {
                value = Double.parseDouble(scanner.nextLine());
                if (value < 0) {
                    System.err.println("Value cannot be negative. Please enter a valid number.");
                } else {
                    break;
                }
            } catch (NumberFormatException e) {
                System.err.println("Invalid input! Please enter a valid number.");
            }
        }
        return value;
    }

    public static String getValidConfirmation(Scanner scanner, String prompt) {
        String confirmation;
        while (true) {
            System.out.print(prompt);
            confirmation = scanner.nextLine().trim().toUpperCase();
            if (confirmation.equals("Y") || confirmation.equals("N")) {
                break;
            } else {
                System.err.println("Invalid input! Please enter 'Y' for Yes or 'N' for No.");
            }
        }
        return confirmation;
    }

    public static Customer enterCustomerDetails() {
        Scanner scanner = new Scanner(System.in);

        System.out.println("\nEnter customer details:");

        int newId = DatabaseHandler.generateNewId("customer_details.txt");
        String id = String.valueOf(newId);

        String name;
        Pattern namePattern = Pattern.compile("^[A-Za-z ]+$");
        do {
            System.out.print("Name: ");
            name = scanner.nextLine();
            Matcher matcher = namePattern.matcher(name);
            if (!matcher.matches() || name.isEmpty()) {
                System.err.println("Invalid name. Only alphabets and spaces are allowed. Please enter a valid name.");
            }
        } while (!namePattern.matcher(name).matches() || name.isEmpty());

        String email;
        Pattern emailPattern = Pattern.compile("^[A-Za-z0-9+_.-]+@(.+)$");
        do {
            System.out.print("Email: ");
            email = scanner.nextLine();
            Matcher matcher = emailPattern.matcher(email);
            if (!matcher.matches()) {
                System.err.println("Invalid email format. Please enter a valid email.");
            }
        } while (!emailPattern.matcher(email).matches());

        String phone;
        Pattern phonePattern = Pattern.compile("^(\\d{10})$");
        do {
            System.out.print("Phone: ");
            phone = scanner.nextLine();
            Matcher matcher = phonePattern.matcher(phone);
            if (!matcher.matches()) {
                System.err.println("Invalid phone number format. Please enter a valid phone number (e.g.1234567890).");
            }
        } while (!phonePattern.matcher(phone).matches());

        int mileagePoints = getValidIntegerInput(scanner, "Mileage Points: ");
        int loyaltyPoints = getValidIntegerInput(scanner, "Loyalty Points: ");

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
        System.out.println("3. Additional Rewards or Services Voucher");

        int selection = getValidIntegerInput(scanner, "Enter voucher type: ", 1, 3);

        switch (selection) {
            case 1 -> {
                double discountAmount = getValidDoubleInput(scanner, "Discount Amount (RM): ");

                String type = "Flat Discount Amount Voucher";
                String formattedDiscountAmount = String.format("%.2f", discountAmount);
                String description = "RM" + formattedDiscountAmount + " discount on next flight booking";

                int pointsRequired = getValidIntegerInput(scanner, "Points Required to Redeem: ");
                int stock = getValidIntegerInput(scanner, "Available Stock: ");

                return new DiscountAmtVoucher(id, type, description, pointsRequired, stock, discountAmount);
            }
            case 2 -> {
                double discountRate = getValidDoubleInput(scanner, "Discount Rate (eg.0.2):", 0, 1);

                String type = "Percentage Discount Rate Voucher";
                String formattedDiscountRate = String.format("%.0f", discountRate * 100);
                String description = formattedDiscountRate + "% off on next flight booking";

                int pointsRequired = getValidIntegerInput(scanner, "Points Required to Redeem: ");
                int stock = getValidIntegerInput(scanner, "Available Stock: ");

                return new DiscountRateVoucher(id, type, description, pointsRequired, stock, discountRate);
            }
            case 3 -> {
                System.out.print("Reward: ");
                String reward = scanner.nextLine();

                String type = "Additional Rewards or Services Voucher";
                String description = reward;

                int pointsRequired = getValidIntegerInput(scanner, "Points Required to Redeem: ");
                int stock = getValidIntegerInput(scanner, "Available Stock: ");

                return new RewardVoucher(id, type, description, pointsRequired, stock, reward);
            }

            default -> {
                return null;
            }
        }
    }

    public static Customer promptCustomerID() {
        List<Customer> customers = DatabaseHandler.readCustomersFromFile("customer_details.txt");

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

        return (selectedCustomer);
    }

    public static String getValidFlightCode(Scanner scanner, Map<String, Flight> availableFlights) {
        String flightCode;
        while (true) {
            System.out.print("Select flight code (0 to exit): ");
            flightCode = scanner.nextLine();
            if (availableFlights.containsKey(flightCode)) {
                break;
            } else if ("0".equals(flightCode)) {
                break;
            } else {
                System.err.println("Invalid flight code! Please select a flight code from the available flights.");
            }
        }
        return flightCode;
    }

    public static String getValidRedeemedVoucherId(Scanner scanner, List<VoucherDetails> redeemedVouchers, String prompt) {
        String selectedVoucherId;
        while (true) {
            System.out.print(prompt);
            selectedVoucherId = scanner.nextLine();
            boolean isValid = false;
            if ("0".equals(selectedVoucherId)) {
                isValid = true;
            } else {
                for (VoucherDetails voucherDetails : redeemedVouchers) {
                    if (voucherDetails.getId().equals(selectedVoucherId) && voucherDetails.getStatus().equals("Valid")) {
                        isValid = true;
                        break;
                    }
                }
            }
            if (isValid) {
                break;
            } else {
                System.err.println("Invalid voucher ID! Please select a voucher ID from the available vouchers.");
            }
        }
        return selectedVoucherId;
    }
    
    public static String getValidVoucherCode(Scanner scanner, List<Voucher> vouchers, String prompt) {
        String selectedVoucherCode;
        while (true) {
            System.out.print(prompt);
            selectedVoucherCode = scanner.nextLine();
            boolean isValid = false;
            if ("0".equals(selectedVoucherCode)) {
                isValid = true;
            } else {
                for (Voucher voucher : vouchers) {
                    if (voucher.getCode().equals(selectedVoucherCode)) {
                        isValid = true;
                        break;
                    }
                }
            }
            if (isValid) {
                break;
            } else {
                System.err.println("Invalid voucher code! Please select a voucher code from the available vouchers.");
            }
        }
        return selectedVoucherCode;
    }

    public static void displayProfileDetails(Customer selectedCustomer) {
        System.out.println("\nVIEW PROFILE");
        System.out.println("------------");
        System.out.println("Customer ID: " + selectedCustomer.getId());
        System.out.println("Name: " + selectedCustomer.getName());
        System.out.println("Email: " + selectedCustomer.getEmail());
        System.out.println("Phone: " + selectedCustomer.getPhone());
        selectedCustomer.checkMileagePointValidity(selectedCustomer.getLastActivityDate());
        DatabaseHandler.updateCustomerInfo("customer_details.txt", selectedCustomer);
        System.out.println("Mileage Points: " + selectedCustomer.getMileagePoints());
        System.out.println("Loyalty Points: " + selectedCustomer.getnewLoyaltyPoints());

        if ("Bronze Tier".equals(selectedCustomer.getLoyaltyTier().getTierName())) {
            System.out.println("Loyalty Tier: " + Reset + BrightYellow + selectedCustomer.getLoyaltyTier().getTierName() + Reset);
        } else if ("Silver Tier".equals(selectedCustomer.getLoyaltyTier().getTierName())) {
            System.out.println("Loyalty Tier: " + Reset + Silver + selectedCustomer.getLoyaltyTier().getTierName() + Reset);
        } else if ("Gold Tier".equals(selectedCustomer.getLoyaltyTier().getTierName())) {
            System.out.println("Loyalty Tier: " + Reset + Yellow + selectedCustomer.getLoyaltyTier().getTierName() + Reset);
        } else if ("Platinum Tier".equals(selectedCustomer.getLoyaltyTier().getTierName())) {
            System.out.println("Loyalty Tier: " + Reset + BrightBlue + selectedCustomer.getLoyaltyTier().getTierName() + Reset);
        } else {
            System.err.println("ERROR!");
        }

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        String formattedAccountCreationDate = selectedCustomer.getAccountCreationDate().format(formatter);
        String formattedLastActivityDate = selectedCustomer.getLastActivityDate().format(formatter);
        System.out.println("Account Creation Date: " + formattedAccountCreationDate);
        System.out.println("Last Activity Date: " + formattedLastActivityDate);

        System.out.println("\nYour Vouchers:\n");
        System.out.printf("%-15s%-15s%-50s%-20s%-20s%-20s%n", "Voucher ID", "Voucher Code", "Description", "Redeemed Date", "Expiry Date", "Status");
        System.out.println("-------------------------------------------------------------------------------------------------------------------------------");

        List<VoucherDetails> redeemedVouchers = DatabaseHandler.loadRedeemedVouchersByCustomerId(selectedCustomer.getId(), "redeemed_vouchers.txt");

        updateVoucherStatus(redeemedVouchers);
        DatabaseHandler.writeRedeemedVouchersToFile("redeemed_vouchers.txt", redeemedVouchers);
        selectedCustomer.setRedeemedVouchers(redeemedVouchers);

        for (VoucherDetails redeemedVoucher : selectedCustomer.getRedeemedVouchers()) {
            Voucher voucher = redeemedVoucher.getVoucher();
            String formattedRedeemedDateTime = redeemedVoucher.getRedeemedDateTime().format(formatter);
            String formattedExpiryDateTime = redeemedVoucher.getExpiryDateTime().format(formatter);
            System.out.printf("%-15s%-15s%-50s%-20s%-20s%-20s%n",
                    redeemedVoucher.getId(), voucher.getCode(), voucher.getDescription(), formattedRedeemedDateTime, formattedExpiryDateTime, redeemedVoucher.getStatus());
        }
        System.out.println("-------------------------------------------------------------------------------------------------------------------------------");
    }

    public static void displayBookingHistory(Customer selectedCustomer) {
        System.out.println("\nVIEW BOOKING HISTORY");
        System.out.println("--------------------");
        System.out.println(selectedCustomer.getName() + "'s Booked Flights:\n");
        System.out.printf("%-15s%-15s%-15s%18s%18s%16s%10s%16s%15s%19s%35s%n", "Flight Code", "Origin", "Destination", "Departure Time", "Arrival Time", "Fare(RM)", "Quantity", "Discount(RM)", "Total(RM)", "Booking Date", "Additional Notes");
        System.out.println("------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------");
        for (BookingDetails bookedFlight : DatabaseHandler.loadBookingsByCustomerId(selectedCustomer.getId(), "booked_flights.txt")) {
            Flight flight = bookedFlight.getFlight();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
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
    }

    public static void displayCustomerList() {
        List<Customer> customers = DatabaseHandler.readCustomersFromFile("customer_details.txt");

        System.out.println("\nLIST OF CUSTOMERS\n");
        System.out.printf("%-15s%-50s%n", "Customer ID", "Customer Name");
        System.out.println("---------------------------------------------------------------------------");
        for (Customer customer : customers) {
            System.out.printf("%-15s%-50s%n", customer.getId(), customer.getName());
        }
        System.out.println("---------------------------------------------------------------------------");
    }

    public static void displayVoucherList() {
        List<Voucher> vouchers = DatabaseHandler.readVouchersFromFile("voucher_details.txt");
        System.out.println("LIST OF VOUCHERS\n");
        System.out.printf("%-15s%-50s%15s%15s%n", "Voucher Code", "Description", "Points Required", "Stock Left");
        System.out.println("-----------------------------------------------------------------------------------------------");
        for (Voucher voucher : vouchers) {
            System.out.printf("%-15s%-50s%15d%15d%n", voucher.getCode(), voucher.getDescription(), voucher.getPointsRequired(), voucher.getStock());
        }
        System.out.println("-----------------------------------------------------------------------------------------------");
    }

    public static void displayLoyaltyTierPerks() {
        LoyaltyTier[] loyaltyTiers = new LoyaltyTier[4];

        loyaltyTiers[0] = new BronzeTier();
        loyaltyTiers[1] = new SilverTier();
        loyaltyTiers[2] = new GoldTier();
        loyaltyTiers[3] = new PlatinumTier();

        System.out.println("\nLOYALTY TIER PERKS");
        System.out.println("------------------");

        for (LoyaltyTier tier : loyaltyTiers) {
            if ("Bronze Tier".equals(tier.getTierName())) {
                System.out.println(Reset + BrightWhiteBgLight + BrightYellow + tier.getTierName() + Reset + displayRequiredPoints(tier));
                displayPerks(tier);
                System.out.println("");
            } else if ("Silver Tier".equals(tier.getTierName())) {
                System.out.println(Reset + BrightWhiteBgLight + Silver + tier.getTierName() + Reset + displayRequiredPoints(tier));
                displayPerks(tier);
                System.out.println("");
            } else if ("Gold Tier".equals(tier.getTierName())) {
                System.out.println(Reset + BrightWhiteBgLight + Yellow + tier.getTierName() + Reset + displayRequiredPoints(tier));
                displayPerks(tier);
                System.out.println("");
            } else if ("Platinum Tier".equals(tier.getTierName())) {
                System.out.println(Reset + BrightWhiteBgLight + BrightBlue + tier.getTierName() + Reset + displayRequiredPoints(tier));
                displayPerks(tier);
                System.out.println("");
            } else {
                System.err.println("ERROR!");
            }
//            System.out.println(tier.getTierName() + displayRequiredPoints(tier));
//            displayPerks(tier);
//            System.out.println("");
        }
    }

    public static void displayPolicies() {
        System.out.println("\nPolicies");
        System.out.println("-----------");
        System.out.println(BrightWhiteBg + "Mileage Points Terms" + Reset);
        System.out.println("Mileage Points are earned based on the fare of " + BrightCyan + "flight (RM1 = 1pt)." + Reset);
        System.out.println("Mileage Points can be multiplied  by the bonuses provided by either special promotions or loyalty tiers.");
        System.out.println("Mileage Points can be exchanged for vouchers that can be redeemed for flight add-ons such as hotel stays, vacation packages, and merchandise.");
        System.out.println("Mileage Points will not expire as long as there is " + BrightRed + " qualifying account activity at least once every 24 months." + Reset);
        System.out.println("Qualifying activities include earning or redeeming Mileage Points for flights.");

        System.out.println(BrightWhiteBg + "Loyalty Points Terms" + Reset);
        System.out.println("Loyalty Points are earned based on the fare of " + BrightCyan + "flight (RM1 = 1pt)." + Reset);
        System.out.println("Loyalty Points will not be affected by bonuses.");
        System.out.println("Loyalty Points will be evaluated based on the loyalty points you earned that year.");
        System.out.println("Loyalty Tiers status last for the remainder of the calendar year in which you achieve it as well as the following calendar year.");
        System.out.println("If the qualification criteria is not met in the following year, the Loyalty Tier will be downgraded based on their accumulated Loyalty Points.");
        System.out.println("Loyalty Points will revert back to " + BrightRed + "zero" + Reset + " after the qualification period regardless of what Loyalty Tier the user has achieved.");

    }

    private static void displayPerks(LoyaltyTier loyaltyTier) {
        List<String> perks = loyaltyTier.getPerks();
        for (String perk : perks) {
            System.out.println("- " + perk);
        }
    }

    public static String displayRequiredPoints(LoyaltyTier loyaltyTier) {
        int minPoints = loyaltyTier.getMinPoints();
        int maxPoints = loyaltyTier.getMaxPoints();
        return (" (" + minPoints + " - " + maxPoints + ")");
    }

    //update the status of voucher details
    public static List<VoucherDetails> updateVoucherStatus(List<VoucherDetails> redeemedVouchers) {
        for (VoucherDetails redeemedVoucher : redeemedVouchers) {
            if (redeemedVoucher.getStatus().equals("Used")) {

            } else if (redeemedVoucher.getExpiryDateTime().isBefore(LocalDateTime.now())) {
                redeemedVoucher.setStatus(BrightRed2 + "Expired");
            }
        }
        return redeemedVouchers;
    }
}