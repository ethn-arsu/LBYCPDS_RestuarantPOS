import java.util.Scanner;

public class Customer {

    private String name;
    private String customerId;
    private String email;
    private String phone;
    private String address;

    // Console formatting
    private static final int CONSOLE_WIDTH = 120;
    private static final int CONTENT_WIDTH = 60;

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

    // Print text aligned to the content area
    private static void printLine(String text) {
        System.out.println(getPadding() + text);
    }

    // Center headings within the content area
    private static void printCentered(String text) {
        int padding = Math.max(0, (CONTENT_WIDTH - text.length()) / 2);

        System.out.println(getPadding() + " ".repeat(padding) + text);
    }

    // Display input prompt
    private static void printPrompt(String text) {
        System.out.print(getPadding() + text);
    }

    // Require a nonempty input
    private static String readRequiredInput(
            Scanner input, String prompt) {

        String value;

        do {
            printPrompt(prompt);
            value = input.nextLine().trim();

            if (value.isEmpty()) {
                printLine("This field cannot be empty.");
            }

        } while (value.isEmpty());

        return value;
    }

    // Compatibility with the current Main.java
    public static Customer customerRegistration() {
        AccountManager manager = new AccountManager();
        Scanner input = new Scanner(System.in);
        return customerRegistration(manager, input);
    }

    // Register a new customer account
    public static Customer customerRegistration(
            AccountManager manager, Scanner input) {

        String border = "=".repeat(CONTENT_WIDTH);

        System.out.println();
        printLine(border);
        printCentered("ACCOUNT REGISTRATION");
        printLine(border);
        System.out.println();

        String name = readRequiredInput(input, "Enter your full name: ");

        // Verify that the email is not already registered
        String email;

        while (true) {
            email = readRequiredInput(input, "Enter your email: ");

            if (manager.accountExists(email)) {
                printLine("This email is already registered.");
                printLine("Please use another email or log in.");
            } else {
                break;
            }
        }

        String phone = readRequiredInput(input, "Enter your phone number: ");

        String address = readRequiredInput(input, "Enter your address: ");

        // Create password
        String password;

        while (true) {
            printPrompt("Create password (minimum 8 characters): ");
            password = input.nextLine();

            if (password.length() < 8) {
                printLine(
                        "Password must contain at least 8 characters."
                );
                continue;
            }

            printPrompt("Confirm password: ");
            String confirmation = input.nextLine();

            if (!password.equals(confirmation)) {
                printLine("Passwords do not match. Try again.");
                continue;
            }

            break;
        }

        // AccountManager assigns a unique customer ID
        Customer newCustomer = new Customer(name, "", email, address, phone);

        // Add the account and save to CSV
        boolean saved = manager.registerAccount(newCustomer, password);

        if (!saved) {
            System.out.println();
            printLine("Account registration could not be completed.");
            return null;
        }

        System.out.println();
        printCentered("Account registered successfully!");
        printLine("Customer ID: " + newCustomer.getCustomerId());

        return newCustomer;
    }

    // Display customer profile
    public void customerDisplayProfile() {

        String border = "=".repeat(CONTENT_WIDTH);
        String separator = "-".repeat(CONTENT_WIDTH);

        System.out.println();
        printLine(border);
        printCentered("CUSTOMER PROFILE");
        printLine(border);
        System.out.println();

        printLine(String.format("%-20s %s", "Customer ID:", customerId));
        printLine(String.format("%-20s %s", "Full Name:", name));
        printLine(String.format("%-20s %s", "Email:", email));
        printLine(String.format("%-20s %s", "Address:", address));
        printLine(String.format("%-20s %s", "Contact No.:", phone));

        System.out.println();
        printLine(separator);
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