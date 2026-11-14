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

        String adminUser = System.getenv("PMK_ADMIN_USER");
        String adminPass = System.getenv("PMK_ADMIN_PASS");

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
    public void displayAdminMenu(Scanner input) {

        while (true) {
            clearConsole();

            printLine("=".repeat(CONTENT_WIDTH));
            printCentered("ADMIN DASHBOARD");
            printLine("=".repeat(CONTENT_WIDTH));

            System.out.println();
            printLine("[1] View Pending Orders");
            printLine("[2] Process Next Order");
            printLine("[3] View Inventory");
            printLine("[4] View Transaction Records");
            printLine("[5] View Sales Report");
            printLine("[6] Logout");

            System.out.println();
            System.out.print(padding() + "Select option: ");

            String choice = input.nextLine().trim();

            if (choice.equals("6")) {
                printLine("Logging out...");
                return;
            }

            if (choice.matches("[1-5]")) {
                printLine("Feature under development.");
            } else {
                printLine("Invalid option.");
            }

            System.out.println();
            System.out.print(padding() + "Press Enter to continue...");
            input.nextLine();
        }
    }

    // Existing method retained for future integration
    public void displayAdminMenu() {
        // TODO: Connect completed administrative features
    }

    // TODO: Monitor customer order queue
    public void monitorQueue() {

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
}
