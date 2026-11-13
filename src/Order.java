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

    // Clear console
    public void clearConsole() {
        System.out.print("\033[H\033[2J");
        System.out.flush();
    }

    // Pause before changing screens
    public void pauseScreen() {
        System.out.print("\nPress Enter to continue...");
        input.nextLine();
        input.nextLine();
    }

    // Select food items
    public void selectItems() {
        int itemCount = 0;
        char categoryChoice;
        int foodChoice;

        while (true) {
            clearConsole();

            menu.displayMenu(' ');
            System.out.print("\nSelect category: ");
            categoryChoice = input.next().toUpperCase().charAt(0);

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
                    System.out.println("Invalid choice.");
                    pauseScreen();
                    continue;
            }

            while (true) {
                clearConsole();

                menu.displayMenu(categoryChoice);
                System.out.print("\nSelect food number or X: ");
                String choice = input.next();

                if (choice.equalsIgnoreCase("X")) {
                    break;
                }

                // Validate numeric input
                try {
                    foodChoice = Integer.parseInt(choice);
                }
                catch (NumberFormatException e) {
                    System.out.println("Invalid food selection.");
                    pauseScreen();
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

                        System.out.println("Item added to your order.");
                    }

                    else {
                        System.out.println("Maximum items reached.");
                    }
                }

                pauseScreen();
            }
        }

        inputQuantity();
    }

    // Enter quantities
    public void inputQuantity() {

        for (int i = 0; i < selectedItems.length; i++) {

            if (selectedItems[i] != -1) {
                clearConsole();

                int itemIndex = selectedItems[i];

                System.out.println("\n[FOOD QUANTITY]");
                System.out.println("Food: " + menu.foodNames[itemIndex]);

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

                totalAmount += menu.prices[itemIndex] * quantities[i];
            }
        }
    }

    // Display order summary
    public void displayOrderSummary() {
        clearConsole();

        System.out.println("\n[ORDER SUMMARY]");

        for (int i = 0; i < selectedItems.length; i++) {

            if (selectedItems[i] != -1) {
                int itemIndex = selectedItems[i];

                System.out.println(menu.foodNames[itemIndex] + " x " + quantities[i] + " = PHP " + (menu.prices[itemIndex] * quantities[i]));
            }
        }

        System.out.println("-----------------------------------------");
        System.out.println("Total Amount: PHP " + totalAmount);
    }
}