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
                        admin.displayAdminMenu(scanner);
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
                    printCentered("Thank you for using Pinritong Manok ng Kentucky!");
                    return;

                default:
                    printLine("Invalid option. Please try again.");
                    pause(scanner);
                    continue;
            }

            // Customer profile
            clearConsole();
            currentCustomer.customerDisplayProfile();

            System.out.println();
            printCentered("Press Enter to proceed to the menu.");
            scanner.nextLine();

            boolean newTransaction = true;
            while(newTransaction) {
                Order order = new Order(menu, currentCustomer);
                boolean orderConfirmed = false;
                boolean orderCancelled = false;
                while (!orderConfirmed && !orderCancelled) {
                    order.selectItems();

                    if(!order.hasItems()){
                        clearConsole();
                        printLine("=".repeat(CONTENT_WIDTH));
                        printCentered("NO ITEMS SELECTED");
                        printLine("=".repeat(CONTENT_WIDTH));
                        System.out.println();
                        printLine("Your cart is currently empty.");
                        printLine("[1] Return to menu to order");
                        printLine("[2] Cancel transaction");
                        System.out.println();

                        while(true){
                            printPrompt("Select option: ");
                            String emptyChoice = scanner.nextLine().trim();
                            if (emptyChoice.equals("1")) {
                                break;
                            }else if (emptyChoice.equals("2")) {
                                orderCancelled = true;
                                break;
                            }else   printLine("Invalid option. Please try again.");
                        }
                    }else{
                        int confirm = order.confirmFinalOrder();
                        if(confirm == 1){
                            orderConfirmed = true;
                        }else if(confirm == 2){
                            continue;
                        }else orderCancelled = true;
                    }
                }
            }

            // Existing order process
            Order order = new Order(menu, currentCustomer);

            order.selectItems();
            order.displayOrderSummary();

            // End the current customer session
            // Other transaction features will be integrated later
            return;
        }
    }
}