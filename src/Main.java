
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
        int padding = Math.max(
                0, (CONSOLE_WIDTH - CONTENT_WIDTH) / 2
        );

        return " ".repeat(padding);
    }

    // Center text within the content area
    private static void printCentered(String text) {
        int padding = Math.max(
                0, (CONTENT_WIDTH - text.length()) / 2
        );

        System.out.println(
                getPadding() + " ".repeat(padding) + text
        );
    }

    public static void main(String[] args) {

        Scanner scanner = new Scanner(System.in);

        Menu menu = new Menu();
        menu.initializeMenu();

        // Customer registration
        clearConsole();
        Customer currentCustomer =
                Customer.customerRegistration();

        // Display customer profile
        clearConsole();
        currentCustomer.customerDisplayProfile();

        System.out.println();
        printCentered("Press [Enter] to proceed to the menu.");
        scanner.nextLine();

        // Begin order taking
        Order order = new Order(menu, currentCustomer);

        order.selectItems();
        order.displayOrderSummary();
    }
}