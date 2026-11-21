import java.time.LocalDateTime;
import java.util.Scanner;

public class Main {

    private static final int CONSOLE_WIDTH = 120;
    private static final int CONTENT_WIDTH = 60;

    public static void clearConsole() {
        System.out.print("\033[H\033[2J\033[3J");
        System.out.flush();
    }

    private static String getPadding() {
        int padding = Math.max(0, (CONSOLE_WIDTH - CONTENT_WIDTH) / 2);
        return " ".repeat(padding);
    }

    private static void printLine(String text) {
        System.out.println(getPadding() + text);
    }

    private static void printCentered(String text) {
        int padding = Math.max(0, (CONTENT_WIDTH - text.length()) / 2);

        System.out.println(getPadding() + " ".repeat(padding) + text);
    }

    private static void printPrompt(String text) {
        System.out.print(getPadding() + text);
    }

    private static void pause(Scanner scanner) {
        System.out.println();
        printPrompt("Press Enter to continue...");
        scanner.nextLine();
    }

    public static void main(String[] args) {

        Scanner scanner = new Scanner(System.in);

        Menu menu = new Menu();
        menu.initializeMenu();

        OrderQueue orderQueue = new OrderQueue();
        TransactionList transactionList = new TransactionList();
        Reports reports = new Reports();
        Algorithm algorithm = new Algorithm();

        AccountManager accountManager = new AccountManager();
        Admin admin = new Admin();

        while (true) {

            clearConsole();

            printLine("=".repeat(CONTENT_WIDTH));
            printCentered("PINRITONG MANOK NG KENTUCKY");
            printCentered("MAIN MENU");
            printLine("=".repeat(CONTENT_WIDTH));

            System.out.println();
            printLine("[1] Admin Login");
            printLine("[2] Customer Login");
            printLine("[3] Create Account");
            printLine("[4] Exit");

            System.out.println();
            printPrompt("Select option: ");

            String choice = scanner.nextLine().trim();
            Customer currentCustomer = null;

            switch (choice) {

                case "1":
                    // Admin login
                    boolean loggedIn = admin.adminLogin(scanner);

                    if (loggedIn) {
                        pause(scanner);
                        admin.displayAdminMenu(scanner, orderQueue, reports, menu, transactionList, algorithm);
                    } else {
                        pause(scanner);
                    }
                    continue;

                case "2":
                    // Existing customer login
                    clearConsole();

                    printLine("=".repeat(CONTENT_WIDTH));
                    printCentered("CUSTOMER LOGIN");
                    printLine("=".repeat(CONTENT_WIDTH));

                    System.out.println();

                    printPrompt("Enter your email: ");
                    String email = scanner.nextLine().trim();

                    printPrompt("Enter your password: ");
                    String password = scanner.nextLine();

                    currentCustomer = accountManager.login(email, password);

                    if (currentCustomer == null) {
                        printLine("Invalid email or password.");
                        pause(scanner);
                        continue;
                    }

                    printCentered("Login successful!");
                    pause(scanner);
                    break;

                case "3":
                    // Register new customer
                    clearConsole();

                    currentCustomer = Customer.customerRegistration(accountManager);

                    pause(scanner);

                    if (currentCustomer == null) {
                        continue;
                    }
                    break;

                case "4":
                    clearConsole();
                    reports.generateSessionReport(menu, transactionList);
                    if(!orderQueue.isEmpty()) {
                        System.out.println();
                        printPrompt("Note: " + orderQueue.getSize() + " unprocessed orders remain.");
                    }
                    System.out.println();
                    printCentered("Thank you for using Pinritong Manok ng Kentucky!");
                    return;

                default:
                    printLine("Invalid option. Please try again.");
                    pause(scanner);
                    continue;
            }

            // Customer profile
            boolean customerLoggedIn = true;
            while (customerLoggedIn) {
                clearConsole();
                currentCustomer.customerDisplayProfile();

                System.out.println();
                printLine("[1] Place Order");
                printLine("[2] Logout");
                System.out.println();
                printPrompt("Select option: ");

                String sessionChoice = scanner.nextLine().trim();

                if(sessionChoice.equals("2")) {
                    currentCustomer = null;
                    System.out.println();
                    printCentered("Logged out successfully.");
                    pause(scanner);
                    break;
                }else if(!sessionChoice.equals("1")) {
                    printLine("Invalid option. Please try again.");
                    pause(scanner);
                    continue;
                }

            boolean newTransaction = true;
            while (newTransaction) {
                Order order = new Order(menu, currentCustomer);
                boolean orderConfirmed = false;
                boolean orderCancelled = false;

                while (!orderConfirmed && !orderCancelled) {
                    order.selectItems();

                    if (!order.hasItems()) {
                        clearConsole();
                        printLine("=".repeat(CONTENT_WIDTH));
                        printCentered("NO ITEMS SELECTED");
                        printLine("=".repeat(CONTENT_WIDTH));
                        System.out.println();
                        printLine("Your cart is currently empty.");
                        printLine("[1] Return to menu to order");
                        printLine("[2] Cancel transaction");
                        System.out.println();

                        while (true) {
                            printPrompt("Select option: ");
                            String emptyChoice = scanner.nextLine().trim();
                            if (emptyChoice.equals("1")) {
                                break;
                            } else if (emptyChoice.equals("2")) {
                                orderCancelled = true;
                                break;
                            } else printLine("Invalid option. Please try again.");
                        }
                    } else {
                        int confirm = order.confirmFinalOrder();
                        if (confirm == 1) {
                            orderConfirmed = true;
                        } else if (confirm == 2) {
                            continue;
                        } else orderCancelled = true;
                    }
                }
                if (orderConfirmed) {
                    order.pwdDiscount();
                    order.placedAt = LocalDateTime.now();
                    order.status = "PENDING";
                    orderQueue.enqueue(order);
                    order.displayOrderSummary();
                    System.out.println();
                    printCentered("Order added to queue! Queue Position: #" + orderQueue.getSize());
                } else {
                    clearConsole();
                    printLine("=".repeat(CONTENT_WIDTH));
                    printCentered("Order cancelled. No e-receipt generated.");
                    printLine("=".repeat(CONTENT_WIDTH));
                }

                System.out.println();
                printLine("What would you like to do next?");
                printLine("[1] Place another order");
                printLine("[2] Return to Customer Profile");
                printLine("[3] Logout");
                System.out.println();

                while (true) {
                    printPrompt("Select option: ");
                    String another = scanner.nextLine().trim();
                    if (another.equals("1")) {
                        newTransaction = true;
                        break;
                    } else if (another.equals("2")) {
                        newTransaction = false;
                        break;
                    } else if (another.equals("3")) {
                        newTransaction = false;
                        customerLoggedIn = false;
                        currentCustomer = null;
                        System.out.println();
                        printCentered("Logged out successfully.");
                        pause(scanner);
                        break;
                    }else printLine("Invalid option. Please try again.");
                    }
                }
            }
        }
    }
}