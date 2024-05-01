package chrisairlines;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class ChrisAirlines {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        // <editor-fold defaultstate="collapsed" desc="Dummy Customer Data">
//        Customer c = new Customer("0001", "Chris", "chris@gmail.com", "018-5200888");
        Customer c = new Customer("0002", "Danny", "danny@gmail.com", "016-2240520", 200000, 100000);
        Voucher v1 = new DiscountRateVoucher("V001", "10% off on next flight booking", 10000, 0.1);
        Voucher v2 = new DiscountAmtVoucher("V004", "RM20 discount on next flight booking", 2000, 20);
        Voucher v3 = new RewardVoucher("V007", "Free upgrade to business class", 20000, "Business Class Upgrade");
        
        VoucherDetails vd1 = new VoucherDetails(v1, LocalDateTime.of(2024, 4, 30, 10, 30, 34));
        VoucherDetails vd2 = new VoucherDetails(v2, LocalDateTime.of(2024, 4, 20, 11, 20, 24));
        VoucherDetails vd3 = new VoucherDetails(v3, LocalDateTime.of(2024, 4, 10, 12, 10, 14));
        VoucherDetails vd4 = new VoucherDetails(v1, LocalDateTime.of(2024, 3, 30, 10, 30, 34));
        VoucherDetails vd5 = new VoucherDetails(v2, LocalDateTime.of(2024, 2, 20, 11, 20, 24));
        VoucherDetails vd6 = new VoucherDetails(v3, LocalDateTime.of(2024, 3, 10, 12, 10, 14));
        
        List<VoucherDetails> redeemedVouchers = new ArrayList<>();
        redeemedVouchers.add(vd1);
        redeemedVouchers.add(vd2);
        redeemedVouchers.add(vd3);
        redeemedVouchers.add(vd4);
        redeemedVouchers.add(vd5);
        redeemedVouchers.add(vd6);

        c.setRedeemedVouchers(redeemedVouchers);
        // </editor-fold>
        
        Booking booking = new Booking();        
        VoucherCatalog voucherCatalog = new VoucherCatalog();

        int selection;
        do {
            selection = menu();

            switch (selection) {
                case 1 -> {
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

                        c.updateVoucherStatus();
                        for (VoucherDetails redeemedVoucher : c.getRedeemedVouchers()) {
                            if (redeemedVoucher.getStatus().equals("Valid")){
                                Voucher voucher = redeemedVoucher.getVoucher();
                                String formattedExipryDateTime = redeemedVoucher.getExpiryDateTime().format(formatter);
                                System.out.printf("%-15s%-15s%-50s%15s%n",
                                    redeemedVoucher.getId(), voucher.getCode(), voucher.getDescription(), formattedExipryDateTime);
                            }
                        }
                        
                        System.out.println("\nSWhich voucher would you like to use?");
                        System.out.print("Enter voucher ID(C001):");
                        String selectedVoucher = scanner.nextLine();
                        
                        for (VoucherDetails redeemedVoucher : c.getRedeemedVouchers()) {
                            
                            if (redeemedVoucher.getId().equals(selectedVoucher) && redeemedVoucher.getStatus().equals("Valid")) {
                                Voucher voucher = redeemedVoucher.getVoucher();
                                LocalDateTime bookingDateTime = LocalDateTime.now();
                                switch (voucher) {
                                    case DiscountAmtVoucher discountAmtVoucher -> {
                                        BookingDetails bookingDetails = c.bookFlightWithDiscountAmount(selectedFlight, quantity, bookingDateTime, discountAmtVoucher.getDiscountAmount());
                                        String formattedBookingDateTime = bookingDateTime.format(formatter);
                                        
                                        System.out.println("\nBooking Date: " + formattedBookingDateTime);
                                        System.out.println(quantity + " " + selectedFlight.getFlightCode() + " flight tickets booked successfully for customer: " + c.getName());
                                        System.out.printf("%s%.2f%s%n", "Discount amount of RM", bookingDetails.getDiscount(), " applied successfully!");
                                        System.out.printf("%s%.2f%n", "Total Amount Due: RM", bookingDetails.getTotalAmount());
                                    }
                                    case DiscountRateVoucher discountRateVoucher -> {
                                        BookingDetails bookingDetails = c.bookFlightWithDiscountRate(selectedFlight, quantity, bookingDateTime, discountRateVoucher.getDiscountRate());
                                        String formattedBookingDateTime = bookingDateTime.format(formatter);
                                        
                                        System.out.println("\nBooking Date: " + formattedBookingDateTime);
                                        System.out.println(quantity + " " + selectedFlight.getFlightCode() + " flight tickets booked successfully for customer: " + c.getName());
                                        System.out.printf("%s%.2f%s%n", "Discount amount of RM", bookingDetails.getDiscount(), " applied successfully!");
                                        System.out.printf("%s%.2f%n", "Total Amount Due: RM", bookingDetails.getTotalAmount());
                                    }
                                    case RewardVoucher rewardVoucher -> {
                                        BookingDetails bookingDetails = c.bookFlightWithReward(selectedFlight, quantity, bookingDateTime, rewardVoucher.getReward());
                                        String formattedBookingDateTime = bookingDateTime.format(formatter);
                                        
                                        System.out.println("\nBooking Date: " + formattedBookingDateTime);
                                        System.out.println(quantity + " " + selectedFlight.getFlightCode() + " flight tickets booked successfully for customer: " + c.getName());
                                        System.out.printf("%s%s%s%n", "Reward \"", bookingDetails.getReward(), "\" claimed successfully!");
                                        System.out.printf("%s%.2f%n", "Total Amount Due: RM", bookingDetails.getTotalAmount());
                                    }
                                    default -> {
                                        
                                    }
                                }

                                redeemedVoucher.setStatus("Used");
                                break;
                            }
                        }
                    } else {
                        LocalDateTime bookingDateTime = LocalDateTime.now();
                        BookingDetails bookingDetails = c.bookFlight(selectedFlight, quantity, bookingDateTime);
                        String formattedBookingDateTime = bookingDateTime.format(formatter);

                        System.out.println("\nBooking Date: " + formattedBookingDateTime);
                        System.out.println(quantity + " " + selectedFlight.getFlightCode() + " flight tickets booked successfully for customer: " + c.getName());
                        System.out.printf("%s%.2f%n", "Total Amount Due: RM", bookingDetails.getTotalAmount());
                    }

                }

                case 2 -> {
                    System.out.println("\nREDEEM VOUCHER");
                    System.out.println("--------------");
                    System.out.println("Available Vouchers:\n");
                    System.out.printf("%-15s%-50s%15s%n", "Voucher Code", "Description", "Points Required");
                    System.out.println("--------------------------------------------------------------------------------");
                    for (Voucher voucher : voucherCatalog.getAllVouchers().values()) {
                        System.out.printf("%-15s%-50s%15d%n", voucher.getCode(), voucher.getDescription(), voucher.getPointsRequired());
                    }
                    System.out.println("--------------------------------------------------------------------------------");
                    System.out.println("Available mileage points: " + c.getMileagePoints());
                    
                    System.out.print("\nSelect voucher code(V001):");
                    String voucher = scanner.nextLine();

                    LocalDateTime bookingDateTime = LocalDateTime.now();
                    Voucher selectedVoucher = voucherCatalog.getVoucher(voucher);
                    if(c.getMileagePoints() >= selectedVoucher.getPointsRequired()){
                        c.redeemVoucher(selectedVoucher.getPointsRequired(), selectedVoucher, bookingDateTime);
                        String formattedBookingDateTime = bookingDateTime.format(formatter);
                        System.out.println("Voucher " + selectedVoucher.getCode() + " redeemed successfully for customer: " + c.getName());
                        System.out.println("Redemption Date: " + formattedBookingDateTime);
                    } else {
                        System.out.println("Insufficient mileage points for redemption.");
                    }
                }

                case 3 -> {
                    System.out.println("\nVIEW PROFILE");
                    System.out.println("------------");
                    System.out.println("Customer ID: " + c.getId());
                    System.out.println("Name: " + c.getName());
                    System.out.println("Email: " + c.getEmail());
                    System.out.println("Phone: " + c.getPhone());
                    System.out.println("Mileage Points: " + c.getMileagePoints());
                    System.out.println("Loyalty Points: " + c.getLoyaltyPoints());
                    System.out.println("Loyalty Tier: " + c.getLoyaltyTier());

                    System.out.println("\nYour Vouchers:\n");
                    System.out.printf("%-15s%-15s%-50s%-20s%-20s%-20s%n","Voucher ID" , "Voucher Code", "Description", "Redeemed Date", "Expiry Date", "Status");
                    System.out.println("-------------------------------------------------------------------------------------------------------------------------------");
                    
                    c.updateVoucherStatus();
                    for (VoucherDetails redeemedVoucher : c.getRedeemedVouchers()) {
                        Voucher voucher = redeemedVoucher.getVoucher();
                        String formattedRedeemedDateTime = redeemedVoucher.getRedeemedDateTime().format(formatter);
                        String formattedExipryDateTime = redeemedVoucher.getExpiryDateTime().format(formatter);
                        System.out.printf("%-15s%-15s%-50s%-20s%-20s%-20s%n",
                                redeemedVoucher.getId(), voucher.getCode(), voucher.getDescription(), formattedRedeemedDateTime, formattedExipryDateTime, redeemedVoucher.getStatus());
                    }  
                    System.out.println("-------------------------------------------------------------------------------------------------------------------------------");
                }

                case 4 -> {
                    System.out.println("\nVIEW BOOKING HISTORY");
                    System.out.println("--------------------");
                    System.out.println("Customer's Booked Flights:\n");
                    System.out.printf("%-15s%-15s%-15s%18s%18s%16s%10s%16s%15s%19s%30s%n", "Flight Code", "Origin", "Destination", "Departure Time", "Arrival Time", "Fare(RM)", "Quantity", "Discount(RM)", "Total(RM)", "Booking Date", "Additional Notes");
                    System.out.println("-------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------");
                    for (BookingDetails bookedFlight : c.getBookedFlights()) {
                        Flight flight = bookedFlight.getFlight();
                        String formattedBookingDateTime = bookedFlight.getBookingDateTime().format(formatter);
                        System.out.printf("%-15s%-15s%-15s%18s%18s%15.2f%11d%15.2f%15.2f%20s%30s%n",
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
                    System.out.println("-------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------");
                }

                default -> {
                    System.out.println("\nTerminating Session...");
                }
            }
        } while (selection >= 1 && selection <= 4);
    }

    public static int menu() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("\nMAIN MENU");
        System.out.println("---------");
        System.out.println("1. Book Ticket");
        System.out.println("2. Redeem Voucher");
        System.out.println("3. View Profile");
        System.out.println("4. View Booking History");
        System.out.println("5. Quit");
        System.out.print("\nEnter your selection: ");
        int selection = scanner.nextInt();
        return selection;
    }   
}
