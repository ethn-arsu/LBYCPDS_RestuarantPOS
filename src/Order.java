import java.util.*;
public class Order {

    int orderID;
    int[] selectedItems;
    int[] quantities;
    double totalAmount;
    Menu menu;
    Scanner input = new Scanner(System.in);

    // TODO: Constructor
    public Order(Menu menu) {
        this.menu = menu;

        selectedItems = new int[10];
        quantities = new int[10];

        for (int i = 0; i < selectedItems.length; i++) {
            selectedItems[i] = -1;
        }

        totalAmount = 0;
    }

    // TODO: Select food items
    public void selectItems() {

    }

    // TODO: Enter quantities
    public void inputQuantity() {

    }

    // TODO: Calculate total cost
    public void calculateTotal() {

    }

    // TODO: Display order summary
    public void displayOrderSummary() {

    }
}
