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
        int itemCount = 0;
        char categoryChoice;
        int foodChoice;

        while (true) {
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

                default:
                    System.out.println("Invalid choice.");
                    continue;
            }

            while (true) {
                menu.displayMenu(categoryChoice);
                System.out.print("\nSelect food number or X: ");
                String choice = input.next();

                if (choice.equalsIgnoreCase("X")) {
                    break;
                }

                foodChoice = Integer.parseInt(choice);
                int itemIndex = foodChoice - 1;

                if (itemIndex < 0 || itemIndex >= menu.foodNames.length) {
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
            }
        }
        inputQuantity();
    }

    // TODO: Enter quantities
    public void inputQuantity() {
        System.out.println("\n[FOOD QUANTITY]");
        for (int i = 0; i < selectedItems.length; i++) {
            if (selectedItems[i] != -1) {
                int itemIndex = selectedItems[i];
                System.out.println("Food: " + menu.foodNames[itemIndex]);
                System.out.print("Enter quantity: ");
                quantities[i] = input.nextInt();
            }
        }
        calculateTotal();
    }

    // TODO: Calculate total cost
    public void calculateTotal() {
        totalAmount = 0;
        for (int i = 0; i < selectedItems.length; i++) {
            if (selectedItems[i] != -1) {
                int itemIndex = selectedItems[i];
                totalAmount += menu.prices[itemIndex] * quantities[i];
            }
        }
    }

    // TODO: Display order summary
    public void displayOrderSummary() {

    }
}
