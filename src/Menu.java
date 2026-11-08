import java.util.*;
public class Menu {

    // arrays for menu information
    String[] foodNames;
    double[] prices;
    String[] categories;
    int[] stocks;

    // TODO: Initialize menu arrays
    public void initializeMenu() {
        foodNames = new String[]{
                "Original Recipe Chicken",
                "Hot and Crispy Chicken",
                "Zinger Burger",
                "Chicken Shots",
                "Chicken Nuggets",
                "Famous Bowl",
                "Regular Fries",
                "Mashed Potato",
                "Coleslaw",
                "Chocolate Sundae"
        };

        prices = new double[]{
                99.00, 105.00, 145.00, 125.00, 85.00,
                115.00, 55.00, 45.00, 45.00, 50.00
        };

        categories = new String[]{
                "CHICKEN", "CHICKEN", "BURGERS", "CHICKEN",
                "CHICKEN", "MEALS", "SIDES", "SIDES",
                "SIDES", "DESSERTS"
        };

        stocks = new int[]{
                20, 20, 20, 20, 20, 20, 20, 20, 20, 20
        };
    }

    // TODO: Display categorized food menu
    public void displayMenu(char choice) {
        if (choice == 'X') {
            return;
        }

        if (choice == ' ') {
            System.out.println("\n[PMK MENU]");
            System.out.println("[A] Chicken");
            System.out.println("[B] Burgers");
            System.out.println("[C] Meals");
            System.out.println("[D] Sides");
            System.out.println("[E] Desserts");
            System.out.println("[X] Finish Order");
        }

        else {
            String selectedCategory = "";
            switch (choice) {
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
                    return;
            }

            System.out.println("\n[" + selectedCategory + "]");
            for (int i = 0; i < foodNames.length; i++) {
                if (categories[i].equals(selectedCategory)) {
                    System.out.println("[" + (i + 1) + "] " + foodNames[i] + " - PHP " + prices[i] + " | Stock: " + stocks[i]);
                }
            }

            System.out.println("[X] Back to Categories");
        }
    }

    // TODO: Display most and least ordered items
    public void displayPopularItems() {

    }

    // TODO: Check food availability
    public void checkStock() {

    }
}
