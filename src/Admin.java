import java.util.Scanner;

public class Admin {

    private static final int CONSOLE_WIDTH = 120;
    private static final int CONTENT_WIDTH = 60;

    private static String padding() {
        return " ".repeat((CONSOLE_WIDTH - CONTENT_WIDTH) / 2);
    }

    private static void printLine(String text) {
        System.out.println(padding() + text);
    }

    private static void printCentered(String text) {
        int spaces = Math.max(0,
                (CONTENT_WIDTH - text.length()) / 2);

        System.out.println(padding() + " ".repeat(spaces) + text);
    }

    private static void clearConsole() {
        System.out.print("\033[H\033[2J\033[3J");
        System.out.flush();
    }

    // Verify administrator credentials
    public boolean adminLogin(Scanner input) {

        String envUser = System.getenv("PMK_ADMIN_USER");
        String envPass = System.getenv("PMK_ADMIN_PASS");

        String adminUser = (envUser != null && !envUser.isEmpty()) ? envUser : "admin";
        String adminPass = (envPass != null && !envPass.isEmpty()) ? envPass : "admin123";

        clearConsole();

        printLine("=".repeat(CONTENT_WIDTH));
        printCentered("ADMIN LOGIN");
        printLine("=".repeat(CONTENT_WIDTH));
        System.out.println();

        if (adminUser == null || adminPass == null || adminUser.isEmpty() || adminPass.isEmpty()) {
            printLine("Admin credentials are not configured.");
            return false;
        }

        printLine("Enter administrator credentials.");
        System.out.println();

        System.out.print(padding() + "Username: ");
        String username = input.nextLine().trim();

        System.out.print(padding() + "Password: ");
        String password = input.nextLine();

        if (adminUser.equals(username) && adminPass.equals(password)) {

            printCentered("Admin login successful!");
            return true;
        }

        printLine("Invalid username or password.");
        return false;
    }

    // Display admin menu
    public void displayAdminMenu(Scanner input, OrderQueue orderQueue,
                                 Reports reports, Menu menu,
                                 TransactionList transactions) {

        while (true) {
            clearConsole();

            printLine("=".repeat(CONTENT_WIDTH));
            printCentered("ADMIN DASHBOARD");
            printLine("=".repeat(CONTENT_WIDTH));

            System.out.println();
            printLine("[1] View Pending Orders (Queue: " + orderQueue.getSize() + ")");
            printLine("[2] Process Next Order");
            printLine("[3] View and Manage Inventory");
            printLine("[4] View and Manage Transaction Records");
            printLine("[5] View Sales Report");
            printLine("[6] Logout");

            System.out.println();
            System.out.print(padding() + "Select option: ");

            String choice = input.nextLine().trim();

            switch (choice) {
                case "1": {
                    clearConsole();
                    monitorQueue(orderQueue);
                    break;
                }
                case "2": {
                    processOrders();
                    break;
                }
                case "3": {
                    break;
                }
                case "4": {
                    break;
                }
                case "5": {
                    reports.displayReportsMenu(input, menu, transactions);
                    break;
                }
            }

            if (choice.equals("6")) {
                printLine("Logging out...");
                return;
            }

            if (choice.matches("[1-4]")) {
                printLine("Feature under development.");

                System.out.println();
                System.out.print(padding() + "Press Enter to continue...");
                input.nextLine();

            } else if (!choice.equals("5")) {
                printLine("Invalid option.");

                System.out.println();
                System.out.print(padding() + "Press Enter to continue...");
                input.nextLine();
            }
        }
    }

    // Existing method retained for future integration

    // TODO: Connect completed administrative features
    public void displayAdminMenu(Scanner scanner) {
    }

    // TODO: Monitor customer order queue
    public void monitorQueue(OrderQueue orderQueue) {

    }

    // TODO: Process customer orders
    public void processOrders() {

    }

    // TODO: Update inventory stocks
    public void updateInventory() {

    }

    // TODO: Display inventory information
    public void displayInventory() {

    }

    public void manageInventory() {
    }
    public void manageTransactions(TransactionList transactionList, Scanner input) {
        while(true){
            clearConsole();
            transactionList.displayTransactions();
            System.out.println();
            printLine("[1] Remove a Transaction by Order ID");
            printLine("[2] Remove Oldest Transaction");
            printLine("[3] Back to Admin Dashboard");
            System.out.println();
            System.out.print(padding() + "Select option: ");
            String choice = input.nextLine().trim();
            switch (choice) {
                case "1": {
                    if(transactionList.isEmpty()){
                        printLine("No transaction records to remove.");
                    }
                }
            }
        }
    }
}
