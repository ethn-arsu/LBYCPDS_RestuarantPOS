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

        Menu menu = new Menu();
        menu.initializeMenu();

        Order order = new Order(menu);

        order.selectItems();
        order.displayOrderSummary();
    }
}
