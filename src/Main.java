import java.util.Scanner;
public class Main {

    /**
     * For the clear screen feature to work, the program should
     * run in Windows PowerShell via Terminal since running the program
     * directly in IntelliJ does not properly clear the screen.
     */

    public static void clearConsole() {
        System.out.print("\033[H\033[2J");
        System.out.flush();
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        Menu menu = new Menu();
        menu.initializeMenu();

        clearConsole();
        Customer currentCustomer = Customer.customerRegistration();

        clearConsole();
        currentCustomer.customerDisplayProfile();
        System.out.println("\n\t\t\t\t\t\t\t\t\t\t\t\t\t\t\tPress [Enter] to process to the menu.");
        scanner.nextLine();

        Order order = new Order(menu, currentCustomer);

        order.selectItems();
        order.displayOrderSummary();
    }
}
