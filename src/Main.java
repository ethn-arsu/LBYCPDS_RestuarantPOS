public class Main {

    public static void main(String[] args) {
        // TODO: Initialize program
        // TODO: Display menu
        // TODO: Handle customer and admin choice
        // TODO: Loop until user exits program

        Menu menu = new Menu();

        menu.initializeMenu();

        Order order = new Order(menu);

        order.selectItems();

        order.displayOrderSummary();
    }
}
