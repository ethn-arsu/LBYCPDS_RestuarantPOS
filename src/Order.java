
import java.util.*;

public class Order {

    int orderID;
    int[] selectedItems;
    int[] quantities;
    double totalAmount;
    Menu menu;
    Scanner input = new Scanner(System.in);

    // Constructor
    public Order(Menu menu) {
        this.menu = menu;

        selectedItems = new int[menu.foodNames.length];
        quantities = new int[menu.foodNames.length];

        for (int i = 0; i < selectedItems.length; i++) {
            selectedItems[i] = -1;
        }

        totalAmount = 0;
    }

    // Clear terminal screen
    public void clearConsole() {
        System.out.print("\033[H\033[2J\033[3J");
        System.out.flush();
    }

    // Select food items
    public void selectItems() {
        int itemCount = 0;
        boolean ordering = true;

        clearConsole();
        System.out.println("\n========== ORDER TAKING ==========");
        menu.displayMenu(' ');

        while (ordering) {

            System.out.print("\nSelect category or X to finish: ");
            char categoryChoice =
                    input.next().toUpperCase().charAt(0);

            if (categoryChoice == 'X') {
                break;
            }

            String selectedCategory = "";

            switch (categoryChoice) {
                case 'A':
                    selectedCategory = "CHICKEN";
                    break;
                case 'B':
                    selectedCategory = "BURGERS";
                    break;
                case 'C':
                    selectedCategory = "MEALS";
                    break;
                case 'D':
                    selectedCategory = "SIDES";
                    break;
                case 'E':
                    selectedCategory = "DESSERTS";
                    break;
                case 'F':
                    selectedCategory = "DRINKS";
                    break;
                default:
                    System.out.println("Invalid category.");
                    continue;
            }

            // Display selected category
            clearConsole();
            menu.displayMenu(categoryChoice);

            while (true) {

                System.out.print("\nEnter food number, B to change category, or X to finish: ");

                String choice = input.next();

                if (choice.equalsIgnoreCase("B")) {
                    clearConsole();
                    menu.displayMenu(' ');
                    break;
                }

                if (choice.equalsIgnoreCase("X")) {
                    ordering = false;
                    break;
                }

                int foodChoice;

                try {
                    foodChoice = Integer.parseInt(choice);
                } catch (NumberFormatException e) {
                    System.out.println("Invalid food selection.");
                    continue;
                }

                int itemIndex = foodChoice - 1;

                if (itemIndex < 0 ||
                        itemIndex >= menu.foodNames.length) {

                    System.out.println("Invalid food selection.");
                }

                else if (!menu.categories[itemIndex].equals(selectedCategory)) {

                    System.out.println("Please select an item from this category.");
                }

                else if (menu.stocks[itemIndex] <= 0) {
                    System.out.println("This item is out of stock.");
                }

                else {
                    boolean alreadySelected = false;

                    for (int i = 0; i < itemCount; i++) {
                        if (selectedItems[i] == itemIndex) {
                            alreadySelected = true;
                            break;
                        }
                    }

                    if (alreadySelected) {
                        System.out.println("Item already selected.");
                    }

                    else if (itemCount < selectedItems.length) {
                        selectedItems[itemCount] = itemIndex;
                        itemCount++;

                        System.out.println(menu.foodNames[itemIndex] + " added to your order.");
                    }

                    else {
                        System.out.println("Maximum items reached.");
                    }
                }
            }
        }

        inputQuantity();
    }

    // Enter quantities
    public void inputQuantity() {

        clearConsole();
        System.out.println("\n========== FOOD QUANTITY ==========");

        for (int i = 0; i < selectedItems.length; i++) {

            if (selectedItems[i] != -1) {
                int itemIndex = selectedItems[i];

                System.out.println(
                        "\nFood: " + menu.foodNames[itemIndex]
                );

                System.out.print("Enter quantity: ");
                quantities[i] = input.nextInt();
            }
        }

        calculateTotal();
    }

    // Calculate total cost
    public void calculateTotal() {
        totalAmount = 0;

        for (int i = 0; i < selectedItems.length; i++) {

            if (selectedItems[i] != -1) {
                int itemIndex = selectedItems[i];

                totalAmount +=
                        menu.prices[itemIndex] * quantities[i];
            }
        }
    }

    // Display order summary
    public void displayOrderSummary() {

        clearConsole();
        System.out.println("\n========== ORDER SUMMARY ==========");

        boolean hasItems = false;

        for (int i = 0; i < selectedItems.length; i++) {

            if (selectedItems[i] != -1) {
                int itemIndex = selectedItems[i];
                hasItems = true;

                System.out.println(
                        menu.foodNames[itemIndex] +
                                " x " + quantities[i] +
                                " = PHP " +
                                (menu.prices[itemIndex] * quantities[i])
                );
            }
        }

        if (!hasItems) {
            System.out.println("No items ordered.");
        }

        System.out.println("-----------------------------------------");
        System.out.println("Total Amount: PHP " + totalAmount);
    }
}