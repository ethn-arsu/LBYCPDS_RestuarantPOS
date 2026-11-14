
public class Menu {

    // Arrays for menu information
    String[] foodNames;
    double[] prices;
    String[] categories;
    int[] stocks;

    // Console formatting
    private static final int CONSOLE_WIDTH = 120;
    private static final int CONTENT_WIDTH = 60;

    // Left margin for the content area
    private String getPadding() {
        int padding = Math.max(0, (CONSOLE_WIDTH - CONTENT_WIDTH) / 2);

        return " ".repeat(padding);
    }

    // Print text aligned to the content area
    private void printLine(String text) {
        System.out.println(getPadding() + text);
    }

    // Center a heading within the content area
    private void printCentered(String text) {
        int padding = Math.max(0, (CONTENT_WIDTH - text.length()) / 2);
        System.out.println(getPadding() + " ".repeat(padding) + text);
    }

    // Menu items. Add more as needed.
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
                "Pepsi"
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
                20, 20, 20, 20, 20,
                20, 20, 20, 20, 20,
                20, 20, 20, 20, 20
        };
    }

    // Display categorized food menu
    public void displayMenu(char choice) {

        if (choice == 'X') {
            return;
        }

        String border = "=".repeat(CONTENT_WIDTH);
        String separator = "-".repeat(CONTENT_WIDTH);

        System.out.println();
        printLine(border);

        if (choice == ' ') {

            printCentered("PMK MENU");
            printLine(border);
            System.out.println();

            printLine("[A] Chicken");
            printLine("[B] Burgers");
            printLine("[C] Meals");
            printLine("[D] Sides");
            printLine("[E] Desserts");
            printLine("[F] Drinks");
            printLine("[X] Finish Order");

            System.out.println();
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
                    printLine("Invalid choice.");
                    return;
            }

            printCentered(selectedCategory);
            printLine(border);
            System.out.println();

            for (int i = 0; i < foodNames.length; i++) {

                if (categories[i].equals(selectedCategory)) {

                    String item = String.format("[%d] %s", i + 1, foodNames[i]);

                    String price = String.format("PHP %.2f", prices[i]);

                    String stock = String.format("Stock: %d", stocks[i]);

                    System.out.printf("%s%-32s %11s | %s%n", getPadding(), item, price, stock);
                }
            }

            System.out.println();
            printLine(separator);
            printLine("[B] Back to Categories");
            printLine("[X] Finish Order");
            System.out.println();
        }
    }

    // TODO: Display most and least ordered items
    public void displayPopularItems() {

    }

    // TODO: Check food availability
    public void checkStock() {

    }
}