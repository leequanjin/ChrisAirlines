package chrisairlines;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Map;
import java.util.Scanner;

public class ChrisAirlines {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

//        Customer c = new Customer("0001", "Chris", "chris@gmail.com", "018-5200888");
        Customer c = new Customer("0002", "Danny", "danny@gmail.com", "016-2240520", 200000, 100000);
        
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

                    System.out.print("\nSelect flight code(F001): ");
                    String flight = scanner.nextLine();

                    LocalDateTime bookingDateTime = LocalDateTime.now();
                    Flight selectedFlight = booking.getFlight(flight);
                    c.bookFlight(selectedFlight, bookingDateTime);

                    String formattedBookingDateTime = bookingDateTime.format(formatter);
                    System.out.println("Flight " + selectedFlight.getFlightCode() + " booked successfully for customer: " + c.getName());
                    System.out.println("Booking Date and Time: " + formattedBookingDateTime);

                }

                case 2 -> {
                    System.out.println("\nREDEEM VOUCHER");
                    System.out.println("--------------");
                    System.out.println("Available Vouchers:");
                    System.out.printf("%-15s%-50s%15s%n", "Voucher Code", "Description", "Points Required");
                    System.out.println("--------------------------------------------------------------------------------");
                    for (Voucher voucher : voucherCatalog.getAllVouchers().values()) {
                        System.out.printf("%-15s%-50s%15d%n", voucher.getCode(), voucher.getDescription(), voucher.getPointsRequired());
                    }
                    
                    System.out.print("\nSelect voucher code(V001): ");
                    String voucher = scanner.nextLine();

                    LocalDateTime bookingDateTime = LocalDateTime.now();
                    Voucher selectedVoucher = voucherCatalog.getVoucher(voucher);
                    c.redeemVoucher(selectedVoucher, bookingDateTime);

//                    String formattedBookingDateTime = bookingDateTime.format(formatter);
//                    System.out.println("Flight " + selectedFlight.getFlightCode() + " booked successfully for customer: " + c.getName());
//                    System.out.println("Booking Date and Time: " + formattedBookingDateTime);
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

                    System.out.println("\nAvailable Vouchers:\n");
                    System.out.printf("%-15s%-50s%-20s%n", "Voucher Code", "Description", "Date Redeemed");
                    System.out.println("------------------------------------------------------------------------------------");
                    
                    for (VoucherDetails redeemedVoucher : c.getRedeemedVouchers()) {
                        Voucher voucher = redeemedVoucher.getVoucher();
                        String formattedBookingDateTime = redeemedVoucher.getBookingDateTime().format(formatter);
                        System.out.printf("%-15s%-50s%-20s%n",
                                voucher.getCode(), voucher.getDescription(), formattedBookingDateTime);
                    }    
                }

                case 4 -> {
                    System.out.println("\nVIEW BOOKING HISTORY");
                    System.out.println("--------------------");
                    System.out.println("Customer's Booked Flights:\n");
                    System.out.printf("%-15s%-15s%-15s%18s%18s%11s%23s%n", "Flight Code", "Origin", "Destination", "Departure Time", "Arrival Time", "Fare(RM)", "Booking Date");
                    System.out.println("-------------------------------------------------------------------------------------------------------------------");
                    for (BookingDetails bookedFlight : c.getBookedFlights()) {
                        Flight flight = bookedFlight.getFlight();
                        String formattedBookingDateTime = bookedFlight.getBookingDateTime().format(formatter);
                        System.out.printf("%-15s%-15s%-15s%18s%18s%10s%24s%n",
                                flight.getFlightCode(), flight.getOrigin(), flight.getDestination(),
                                flight.getDepartureTime(), flight.getArrivalTime(), flight.getFare(), formattedBookingDateTime);
                    }
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
