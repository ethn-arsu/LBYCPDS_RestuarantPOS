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
                "Chocolate Sundae",
                "Water",
                "Iced Tea",
                "Orange Juice",
                "Coca Cola",
                "Pepsi",
        };

        prices = new double[]{
                99.00, 105.00, 145.00, 125.00, 85.00,
                115.00, 55.00, 45.00, 45.00, 50.00,
                50.00, 55.00, 55.00, 60.00, 60.00
        };

        categories = new String[]{
                "CHICKEN", "CHICKEN", "BURGERS", "CHICKEN",
                "CHICKEN", "MEALS", "SIDES", "SIDES",
                "SIDES", "DESSERTS", "DRINKS", "DRINKS",
                "DRINKS", "DRINKS", "DRINKS"
        };

        stocks = new int[]{
                20, 20, 20, 20, 20, 20, 20, 20, 20, 20, 20, 20, 20, 20, 20
        };
    }

    // TODO: Display categorized food menu
    public void displayMenu(char choice) {
        if (choice == 'X') {
            return;
        }

        if (choice == ' ') {
            System.out.println("\n\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t[PMK MENU]");
            System.out.println("\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t[A] Chicken");
            System.out.println("\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t[B] Burgers");
            System.out.println("\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t[C] Meals");
            System.out.println("\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t[D] Sides");
            System.out.println("\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t[E] Desserts");
            System.out.println("\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t[F] Drinks");
            System.out.println("\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t[X] Finish Order");
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
                case 'F':
                    selectedCategory = "DRINKS";
                    break;
                default:
                    System.out.println("\t\t\t\t\t\t\t\t\t\t\t\t\t\t\tInvalid choice.");
                    return;
            }

            System.out.println("\n\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t[" + selectedCategory + "]");
            for (int i = 0; i < foodNames.length; i++) {
                if (categories[i].equals(selectedCategory)) {
                    System.out.println("\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t[" + (i + 1) + "] " + foodNames[i] + " - PHP " + prices[i] + " | Stock: " + stocks[i]);
                }
            }

            System.out.println("\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t[X] Back to Categories");
        }
    }

    // TODO: Display most and least ordered items
    public void displayPopularItems() {

    }

    // TODO: Check food availability
    public void checkStock() {

    }
}
