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
    public void displayMenu() {
    }

    // TODO: Display most and least ordered items
    public void displayPopularItems() {

    }

    // TODO: Check food availability
    public void checkStock() {

    }
}
