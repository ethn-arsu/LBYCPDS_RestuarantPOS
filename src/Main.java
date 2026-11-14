import java.util.Scanner;

public class Main {

    // Console formatting
    private static final int CONSOLE_WIDTH = 120;
    private static final int CONTENT_WIDTH = 60;

    public static void clearConsole() {
        System.out.print("\033[H\033[2J\033[3J");
        System.out.flush();
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

    // Center text within the content area
    private static void printCentered(String text) {
        int padding = Math.max(0, (CONTENT_WIDTH - text.length()) / 2);

        System.out.println(getPadding() + " ".repeat(padding) + text);
    }

    // Display input prompt
    private static void printPrompt(String text) {
        System.out.print(getPadding() + text);
    }

    public static void main(String[] args) {

        Scanner scanner = new Scanner(System.in);

        // Initialize menu and saved customer accounts
        Menu menu = new Menu();
        menu.initializeMenu();

        AccountManager accountManager = new AccountManager();
        Customer currentCustomer = null;

        // Login / Register / Exit menu
        while (currentCustomer == null) {

            clearConsole();

            printLine("=".repeat(CONTENT_WIDTH));
            printCentered("PINRITONG MANOK NG KENTUCKY");
            printCentered("CUSTOMER ACCESS");
            printLine("=".repeat(CONTENT_WIDTH));

            System.out.println();
            printLine("[1] Login");
            printLine("[2] Register");
            printLine("[3] Exit");

            System.out.println();
            printPrompt("Select option: ");

            String choice = scanner.nextLine().trim();

            switch (choice) {

                case "1":
                    // Login to an existing account
                    clearConsole();

                    printLine("=".repeat(CONTENT_WIDTH));
                    printCentered("CUSTOMER LOGIN");
                    printLine("=".repeat(CONTENT_WIDTH));

                    System.out.println();

                    printPrompt("Enter your email: ");
                    String email = scanner.nextLine().trim();

                    printPrompt("Enter your password: ");
                    String password = scanner.nextLine();

                    currentCustomer = accountManager.login(
                            email, password
                    );

                    System.out.println();

                    if (currentCustomer != null) {
                        printCentered("Login successful!");
                    } else {
                        printLine("Invalid email or password.");
                    }

                    System.out.println();
                    printPrompt("Press Enter to continue...");
                    scanner.nextLine();
                    break;

                case "2":
                    // Register a new customer
                    clearConsole();

                    currentCustomer = Customer.customerRegistration(accountManager);

                    System.out.println();
                    printPrompt("Press Enter to continue...");
                    scanner.nextLine();
                    break;

                case "3":
                    // Exit the application
                    clearConsole();
                    printCentered("Thank you for using Pinritong Manok ng Kentucky!");
                    return;

                default:
                    printLine("Invalid option. Please try again.");

                    System.out.println();
                    printPrompt("Press Enter to continue...");
                    scanner.nextLine();
                    break;
            }
        }

        // Display customer profile after login/registration
        clearConsole();
        currentCustomer.customerDisplayProfile();

        System.out.println();
        printCentered("Press [Enter] to proceed to the menu.");
        scanner.nextLine();

        // Begin the existing ordering process
        Order order = new Order(menu, currentCustomer);
        order.selectItems();
        order.displayOrderSummary();
    }
}