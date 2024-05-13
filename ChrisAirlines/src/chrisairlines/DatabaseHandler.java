package chrisairlines;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class DatabaseHandler {

    // used to append new customer object to customer_details txt file
    public static void saveCustomerDetailsToFile(String filename, Customer customer) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filename, true))) {
            writer.write(customer.toString() + "\n");
            System.out.println("Customer details saved to file successfully.");
        } catch (IOException e) {
            System.err.println("Error writing to file: " + e.getMessage());
        }
    }

    // used to append new voucher object to voucher_details txt file
    public static void saveVoucherDetailsToFile(String filename, Voucher voucher) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filename, true))) {
            writer.write(voucher.toString() + "\n");
            System.out.println("Voucher details saved to file successfully.");
        } catch (IOException e) {
            System.err.println("Error writing to file: " + e.getMessage());
        }
    }

    // used to append new voucher details object and associated customer id to redeemed_vouchers txt file
    public static void saveRedeemedVoucherToFile(String filename, VoucherDetails voucherDetails) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filename, true))) {
            writer.write(voucherDetails.toString() + "\n");
        } catch (IOException e) {
            System.err.println("Error writing to file: " + e.getMessage());
        }
    }
    
    // used to append new booking details object and associated customer id to booked_flights txt file
    public static void saveBookingDetailToFile(String customerId, BookingDetails bookingDetails) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("booked_flights.txt", true))) {
            writer.write(customerId + "," + bookingDetails.toString() + "\n");
        } catch (IOException e) {
            System.err.println("Error writing to file: " + e.getMessage());
        }
    }

    // used to generate a new customer id
    public static int generateNewId(String filename) {
        int newId = 1;
        try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                int id = Integer.parseInt(parts[0]);
                if (newId <= id) {
                    newId = ++id;
                }
            }
        } catch (IOException e) {
            System.err.println("Error reading file: " + e.getMessage());
        }
        return newId;
    }

    // used to modify a single customer record in the customer_details txt file
    public static void updateCustomerInfo(String filename, Customer selectedCustomer) {
        List<Customer> customers = readCustomersFromFile(filename);

        for (Customer customer : customers) {
            if (customer.getId().equals(selectedCustomer.getId())) {
                customer.setName(selectedCustomer.getName());
                customer.setEmail(selectedCustomer.getEmail());
                customer.setPhone(selectedCustomer.getPhone());
                customer.setMileagePoints(selectedCustomer.getMileagePoints());
                customer.setLoyaltyPoints(selectedCustomer.getLoyaltyPoints());
                customer.setAccountCreationDate(selectedCustomer.getAccountCreationDate());
                customer.setLastActivityDate(selectedCustomer.getLastActivityDate());
                break;
            }
        }
        writeCustomersToFile(filename, customers);
    }

    // used to modify a single voucher record in the voucher_details txt file
    public static void updateVoucherDetails(String filename, Voucher selectedVoucher) {
        List<Voucher> vouchers = readVouchersFromFile(filename);

        for (Voucher voucher : vouchers) {
            if (voucher.getCode().equals(selectedVoucher.getCode())) {
                voucher.setDescription(selectedVoucher.getDescription());
                voucher.setPointsRequired(selectedVoucher.getPointsRequired());
                voucher.setStock(selectedVoucher.getStock());
                switch (voucher) {
                    case DiscountRateVoucher originalVoucher -> {
                        DiscountRateVoucher selectedDiscountRateVoucher = (DiscountRateVoucher) selectedVoucher;
                        originalVoucher.setDiscountRate(selectedDiscountRateVoucher.getDiscountRate());
                    }
                    case DiscountAmtVoucher originalVoucher -> {
                        DiscountAmtVoucher selectedDiscountAmtVoucher = (DiscountAmtVoucher) selectedVoucher;
                        originalVoucher.setDiscountAmount(selectedDiscountAmtVoucher.getDiscountAmount());
                    }
                    case RewardVoucher originalVoucher -> {
                        RewardVoucher selectedRewardVoucher = (RewardVoucher) selectedVoucher;
                        originalVoucher.setReward(selectedRewardVoucher.getReward());
                    }
                    default -> {
                    }
                }
                break;
            }
        }
        writeVouchersToFile(filename, vouchers);
    }
    
    // used to modify a single redeemed voucher record in the redeemed_vouchers txt file
    public static void updateRedeemedVouchers(String filename, VoucherDetails redeemedVoucher) {
        List<VoucherDetails> redeemedVouchers = readRedeemedVouchersFromFile(filename);

        for (VoucherDetails voucherDetails : redeemedVouchers) {
            if (voucherDetails.getId().equals(redeemedVoucher.getId())) {
                voucherDetails.setStatus(redeemedVoucher.getStatus());
                break;
            }
        }
        writeRedeemedVouchersToFile(filename, redeemedVouchers);
    }

    // used to read all customer records from customer_details txt and store in an arraylist for data modification
    public static List<Customer> readCustomersFromFile(String filename) {
        List<Customer> customers = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                Customer customer = new Customer();
                customer.setId(parts[0]);
                customer.setName(parts[1]);
                customer.setEmail(parts[2]);
                customer.setPhone(parts[3]);
                customer.setMileagePoints(Integer.parseInt(parts[4]));
                customer.setLoyaltyPoints(Integer.parseInt(parts[5]));
                customer.setAccountCreationDate(LocalDateTime.parse(parts[6]));
                customer.setLastActivityDate(LocalDateTime.parse(parts[7]));
                customers.add(customer);
            }
        } catch (IOException e) {
            System.err.println("Error reading file: " + e.getMessage());
        }
        return customers;
    }

    // used to read all vouchers from voucher_details txt and store in an arraylist for data modification
    public static List<Voucher> readVouchersFromFile(String filename) {
        List<Voucher> vouchers = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");

                String type = parts[1];
                switch (type) {
                    case "Flat Discount Amount Voucher" -> {
                        DiscountAmtVoucher voucher = new DiscountAmtVoucher();
                        voucher.setCode(parts[0]);
                        voucher.setType(parts[1]);
                        voucher.setDescription(parts[2]);
                        voucher.setPointsRequired(Integer.parseInt(parts[3]));
                        voucher.setStock(Integer.parseInt(parts[4]));
                        voucher.setDiscountAmount(Double.parseDouble(parts[5]));
                        vouchers.add(voucher);
                    }
                    case "Percentage Discount Rate Voucher" -> {
                        DiscountRateVoucher voucher = new DiscountRateVoucher();
                        voucher.setCode(parts[0]);
                        voucher.setType(parts[1]);
                        voucher.setDescription(parts[2]);
                        voucher.setPointsRequired(Integer.parseInt(parts[3]));
                        voucher.setStock(Integer.parseInt(parts[4]));
                        voucher.setDiscountRate(Double.parseDouble(parts[5]));
                        vouchers.add(voucher);
                    }

                    case "Additional Rewards or Services Voucher" -> {
                        RewardVoucher voucher = new RewardVoucher();
                        voucher.setCode(parts[0]);
                        voucher.setType(parts[1]);
                        voucher.setDescription(parts[2]);
                        voucher.setPointsRequired(Integer.parseInt(parts[3]));
                        voucher.setStock(Integer.parseInt(parts[4]));
                        voucher.setReward(parts[5]);
                        vouchers.add(voucher);
                    }
                    default -> {

                    }
                }
            }
        } catch (IOException e) {
            System.err.println("Error reading file: " + e.getMessage());
        }
        return vouchers;
    }

    // used to read all redeemed voucher records from redeemed_voucher txt and store in an arraylist for data modification
    public static List<VoucherDetails> readRedeemedVouchersFromFile(String filename) {
        List<VoucherDetails> redeemedVouchers = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                String voucherType = parts[3];
                    switch (voucherType) {
                        case "Flat Discount Amount Voucher" -> {
                            VoucherDetails voucherDetails = new VoucherDetails(
                            parts[0], parts[1],
                            new DiscountAmtVoucher(parts[2], parts[3], parts[4], Double.parseDouble(parts[8])),
                            LocalDateTime.parse(parts[5]),
                            LocalDateTime.parse(parts[6]),
                            parts[7], Double.parseDouble(parts[8]), Double.parseDouble(parts[9]), parts[10]
                            );
                            redeemedVouchers.add(voucherDetails);
                        }
                        case "Percentage Discount Rate Voucher" -> {
                            VoucherDetails voucherDetails = new VoucherDetails(
                            parts[0], parts[1],
                            new DiscountRateVoucher(parts[2], parts[3], parts[4], Double.parseDouble(parts[9])),
                            LocalDateTime.parse(parts[5]),
                            LocalDateTime.parse(parts[6]),
                            parts[7], Double.parseDouble(parts[8]), Double.parseDouble(parts[9]), parts[10]
                            );
                            redeemedVouchers.add(voucherDetails);
                        }
                        case "Additional Rewards or Services Voucher" -> {
                            VoucherDetails voucherDetails = new VoucherDetails(
                            parts[0], parts[1],
                            new RewardVoucher(parts[2], parts[3], parts[4], parts[10]),
                            LocalDateTime.parse(parts[5]),
                            LocalDateTime.parse(parts[6]),
                            parts[7], Double.parseDouble(parts[8]), Double.parseDouble(parts[9]), parts[10]
                            );
                            redeemedVouchers.add(voucherDetails);
                        }
                        default -> {
                        }
                    }
            }
        } catch (IOException e) {
            System.err.println("Error reading file: " + e.getMessage());
        }
        return redeemedVouchers;
    }
    
    // used to rewrite all contents into customer_details txt file after modifying customers array list
    public static void writeCustomersToFile(String filename, List<Customer> customers) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filename))) {
            for (Customer customer : customers) {
                writer.write(customer.toString() + "\n");
            }
        } catch (IOException e) {
            System.err.println("Error writing to file: " + e.getMessage());
        }
    }
    
    // used to rewrite all contents into voucher_details txt file after modifying vouchers array list
    public static void writeVouchersToFile(String filename, List<Voucher> vouchers) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filename))) {
            for (Voucher voucher : vouchers) {
                writer.write(voucher.toString() + "\n");
            }
        } catch (IOException e) {
            System.err.println("Error writing to file: " + e.getMessage());
        }
    }
    
    // used to rewrite all contents into voucher_details txt file after modifying vouchers array list
    public static void writeRedeemedVouchersToFile(String filename, List<VoucherDetails> redeemedVouchers) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filename))) {
            for (VoucherDetails redeemedVoucher : redeemedVouchers) {
                writer.write(redeemedVoucher.toString() + "\n");
            }
        } catch (IOException e) {
            System.err.println("Error writing to file: " + e.getMessage());
        }
    }

    // used to load all voucher objects associated with a cusstomer ID cufrom redeemed_vouchers txt file and store in an arraylist for data modification
    public static List<VoucherDetails> loadRedeemedVouchersByCustomerId(String customerId, String filename) {
        List<VoucherDetails> redeemedVouchers = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts[1].equals(customerId)) {
                    String voucherType = parts[3];
                    switch (voucherType) {
                        case "Flat Discount Amount Voucher" -> {
                            VoucherDetails voucherDetails = new VoucherDetails(
                            parts[0], parts[1],
                            new DiscountAmtVoucher(parts[2], parts[3], parts[4], Double.parseDouble(parts[8])),
                            LocalDateTime.parse(parts[5]),
                            LocalDateTime.parse(parts[6]),
                            parts[7], Double.parseDouble(parts[8]), Double.parseDouble(parts[9]), parts[10]
                            );
                            redeemedVouchers.add(voucherDetails);
                        }
                        case "Percentage Discount Rate Voucher" -> {
                            VoucherDetails voucherDetails = new VoucherDetails(
                            parts[0], parts[1],
                            new DiscountRateVoucher(parts[2], parts[3], parts[4], Double.parseDouble(parts[9])),
                            LocalDateTime.parse(parts[5]),
                            LocalDateTime.parse(parts[6]),
                            parts[7], Double.parseDouble(parts[8]), Double.parseDouble(parts[9]), parts[10]
                            );
                            redeemedVouchers.add(voucherDetails);
                        }
                        case "Additional Rewards or Services Voucher" -> {
                            VoucherDetails voucherDetails = new VoucherDetails(
                            parts[0], parts[1],
                            new RewardVoucher(parts[2], parts[3], parts[4], parts[10]),
                            LocalDateTime.parse(parts[5]),
                            LocalDateTime.parse(parts[6]),
                            parts[7], Double.parseDouble(parts[8]), Double.parseDouble(parts[9]), parts[10]
                            );
                            redeemedVouchers.add(voucherDetails);
                        }
                        default -> {
                        }
                    }
                }
            }
        } catch (IOException e) {
            System.err.println("Error writing to file: " + e.getMessage());
        }
        return redeemedVouchers;
    }
    
    // used to load all booking details objects associated with a cusstomer ID  from booking_details txt and store in an arraylist for data modification
    public static List<BookingDetails> loadBookingsByCustomerId(String customerId, String filename) {
        List<BookingDetails> bookedFlights = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts[0].equals(customerId)) {
                    Flight flight = new Flight(parts[1], 
                            Double.parseDouble(parts[2]), 
                            parts[3], parts[4], parts[5], parts[6]);
                    BookingDetails bookedFlight = new BookingDetails(flight, 
                            Integer.parseInt(parts[7]), 
                            Double.parseDouble(parts[8]), 
                            LocalDateTime.parse(parts[9]), 
                            Double.parseDouble(parts[10]), 
                            parts[11]);
                    bookedFlights.add(bookedFlight);
                }
            }
        } catch (IOException e) {
            System.err.println("Error reading file: " + e.getMessage());
        }
        return bookedFlights;
    }
}
