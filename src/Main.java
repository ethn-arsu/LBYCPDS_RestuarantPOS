
import java.util.Scanner;

public class Main {

    // Console formatting
    private static final int CONSOLE_WIDTH = 120;

    /**
     * For the clear screen feature to work, the program should
     * run in Windows PowerShell via Terminal since running the
     * program directly in IntelliJ does not properly clear the screen.
     */

    public static void clearConsole() {
        System.out.print("\033[H\033[2J\033[3J");
        System.out.flush();
    }

    // Center text horizontally
    public static void printCentered(String text) {
        int padding = Math.max(
                0, (CONSOLE_WIDTH - text.length()) / 2
        );

        System.out.println(" ".repeat(padding) + text);
    }

    public static void main(String[] args) {

        Scanner scanner = new Scanner(System.in);

        Menu menu = new Menu();
        menu.initializeMenu();

        // Customer registration
        clearConsole();
        Customer currentCustomer = Customer.customerRegistration();

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
