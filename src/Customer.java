
import java.util.Scanner;

public class Customer {

    private String name;
    private String customerId;
    private String email;
    private String phone;
    private String address;

    // Console formatting
    private static final int CONSOLE_WIDTH = 120;
    private static final int CONTENT_WIDTH = 44;

    // Constructor
    public Customer(String name, String customerId,
                    String email, String address, String phone) {
        this.name = name;
        this.customerId = customerId;
        this.email = email;
        this.address = address;
        this.phone = phone;
    }

    // Left margin for centered content
    private static String getPadding() {
        int padding = Math.max(0, (CONSOLE_WIDTH - CONTENT_WIDTH) / 2);
        return " ".repeat(padding);
    }

    private static void printCentered(String text) {
        int padding = Math.max(0, (CONTENT_WIDTH - text.length()) / 2);
        System.out.println(getPadding() + " ".repeat(padding) + text);
    }

    // Display input prompt
    private static void printPrompt(String text) {
        System.out.print(getPadding() + text);
    }

    // Customer registration
    public static Customer customerRegistration() {

        Scanner input = new Scanner(System.in);

        System.out.println();
        printCentered("=================================");
        printCentered("ACCOUNT REGISTRATION");
        printCentered("=================================");

        printPrompt("Enter your full name: ");
        String name = input.nextLine();

        while (name.isEmpty()) {
            printPrompt("Enter your full name: ");
            name = input.nextLine();
        }

        printPrompt("Enter your email: ");
        String email = input.nextLine();

        while (email.isEmpty()) {
            printPrompt("Enter your email: ");
            email = input.nextLine();
        }

        printPrompt("Enter your phone number: ");
        String phone = input.nextLine();

        while (phone.isEmpty()) {
            printPrompt("Enter your phone number: ");
            phone = input.nextLine();
        }

        printPrompt("Enter your address: ");
        String address = input.nextLine();

        while (address.isEmpty()) {
            printPrompt("Enter your address: ");
            address = input.nextLine();
        }

        String customerId = "00" + (int) (Math.random() * 10);

        System.out.println();
        printCentered("Successfully created account!");

        return new Customer(name, customerId, email, address, phone);
    }

    // Display customer profile
    public void customerDisplayProfile() {

        System.out.println();
        printCentered("============================");
        printCentered("CUSTOMER PROFILE");
        printCentered("============================");

        String padding = getPadding();

        System.out.println(padding + "Customer ID : " + customerId);
        System.out.println(padding + "Full Name   : " + name);
        System.out.println(padding + "Email       : " + email);
        System.out.println(padding + "Address     : " + address);
        System.out.println(padding + "Contact No. : " + phone);
    }

    // Getters
    public String getName() {
        return name;
    }

    public String getCustomerId() {
        return customerId;
    }

    public String getEmail() {
        return email;
    }

    public String getPhone() {
        return phone;
    }

    public String getAddress() {
        return address;
    }

    // Setters
    public void setName(String name) {
        this.name = name;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}