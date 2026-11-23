import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class Menu {

    // Inventory file for stocks
    private static final String INVENTORY_FILE_PATH = "data/inventory.csv";

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

                // A. CHICKEN
                "Original Recipe Chicken",
                "Hot and Crispy Chicken",
                "Chicken Shots",
                "Hot Chicken Shots",
                "Chicken Nuggets",

                // B. BURGERS
                "PMK Original Burger",
                "Zinger Burger",
                "Cheesy Burger",
                "Double Burger",
                "Chicken Burger",

                // C. MEALS
                "Famous Bowl",
                "Chicken Rice Meal",
                "Spaghetti Chicken Meal",
                "Chicken Fillet Meal",
                "2-Piece Chicken Meal",

                // D. SIDES
                "Regular Fries",
                "Mashed Potato",
                "Coleslaw",
                "Macaroni Salad",
                "Buttered Corn",

                // E. DESSERTS
                "Chocolate Sundae",
                "Vanilla Sundae",
                "Caramel Sundae",
                "Choco Brownie",
                "Butterscotch Brownie",

                // F. DRINKS
                "Water",
                "Iced Tea",
                "Orange Juice",
                "Coca Cola",
                "Pepsi"
        };

        prices = new double[]{
                // A. CHICKEN
                99.00, 105.00, 95.00, 99.00, 85.00,

                // B. BURGERS
                99.00, 145.00, 115.00, 165.00, 105.00,

                // C. MEALS
                115.00, 135.00, 155.00, 125.00, 185.00,

                // D. SIDES
                55.00, 45.00, 45.00, 50.00, 40.00,

                // E. DESSERTS
                50.00, 50.00, 50.00, 45.00, 45.00,

                // F. DRINKS
                25.00, 50.00, 50.00, 60.00, 60.00
        };

        categories = new String[]{
                "CHICKEN", "CHICKEN", "CHICKEN", "CHICKEN", "CHICKEN",
                "BURGERS", "BURGERS", "BURGERS", "BURGERS", "BURGERS",
                "MEALS", "MEALS", "MEALS", "MEALS", "MEALS",
                "SIDES", "SIDES", "SIDES", "SIDES", "SIDES",
                "DESSERTS", "DESSERTS", "DESSERTS", "DESSERTS", "DESSERTS",
                "DRINKS", "DRINKS", "DRINKS", "DRINKS", "DRINKS"
        };

        stocks = new int[]{
                20, 20, 20, 20, 20,
                20, 20, 20, 20, 20,
                20, 20, 20, 20, 20,
                20, 20, 20, 20, 20,
                20, 20, 20, 20, 20,
                20, 20, 20, 20, 20
        };
    }

    // Save current inventory records to CSV
    public boolean saveInventory() {
        Path path = Paths.get(INVENTORY_FILE_PATH);

        try {
            Files.createDirectories(path.getParent());
            try (BufferedWriter writer = Files.newBufferedWriter(path, StandardCharsets.UTF_8)) {
                writer.write("foodName,stock");
                writer.newLine();

                for (int i = 0; i < foodNames.length; i++) {
                    writer.write(foodNames[i] + "," + stocks[i]);
                    writer.newLine();
                }
            }
            return true;

        }

        catch (IOException e) {
            System.out.println("Unable to save inventory.");
            return false;
        }
    }

    // Load inventory records from CSV
    public void loadInventory() {

        Path path = Paths.get(INVENTORY_FILE_PATH);
        if (!Files.exists(path)) {
            saveInventory();
            return;
        }

        try {
            if (Files.size(path) == 0) {
                saveInventory();
                return;
            }

            try (BufferedReader reader = Files.newBufferedReader(path, StandardCharsets.UTF_8)) {
                String header = reader.readLine();
                if (header == null || !header.trim().equalsIgnoreCase("foodName,stock")) {
                    System.out.println("Invalid inventory.csv format.");
                    return;
                }

                String line;
                while ((line = reader.readLine()) != null) {
                    if (line.isBlank()) {
                        continue;
                    }

                    String[] data = line.split(",", 2);
                    if (data.length != 2) {
                        continue;
                    }

                    String itemName = data[0].trim();
                    try {
                        int savedStock = Integer.parseInt(data[1].trim());
                        if (savedStock < 0) {
                            continue;
                        }

                        for (int i = 0; i < foodNames.length; i++) {
                            if (foodNames[i].equalsIgnoreCase(itemName)) {
                                stocks[i] = savedStock;
                                break;
                            }
                        }

                    }

                    catch (NumberFormatException e) {
                        System.out.println("Invalid stock value for " + itemName);
                    }
                }
            }
        }

        catch (IOException e) {
            System.out.println("Unable to load inventory.");
        }
    }

    // Display categorized food menu
    public void displayMenu(char choice) {
        displayMenu(choice, null);
    }

    // Display the menu, with session sales data when available
    public void displayMenu(char choice, TransactionList transactions) {

        if (choice == 'X') {
            return;
        }

        if (choice == 'G') {
            displayPopularItems(transactions);
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
            printLine("[G] Most and Least Ordered Items");
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

    // Display the three most and least ordered items based on completed orders.
    // Items with zero sales are included in the least-ordered section.
    public void displayPopularItems(TransactionList transactions) {
        Algorithm algorithm = new Algorithm();
        int[] itemSales = algorithm.calculateItemSales(this, transactions);
        int totalSales = 0;
        for (int quantity : itemSales) {
            totalSales += quantity;
        }

        String border = "=".repeat(CONTENT_WIDTH);
        String separator = "-".repeat(CONTENT_WIDTH);
        System.out.println();
        printLine(border);
        printCentered("MOST AND LEAST ORDERED ITEMS");
        printLine(border);
        System.out.println();

        if (totalSales == 0) {
            printLine("No completed sales yet.");
            printLine("Popularity statistics will appear after orders are processed.");
            printLine(border);
            return;
        }

        int[] sortedIndexes = algorithm.sortItems(itemSales);
        printLine("MOST ORDERED ITEMS");
        printLine(String.format("%-5s %-38s %10s", "Rank", "Item", "Units Sold"));
        printLine(separator);

        int shown = 0;
        int previousSales = -1;
        int rank = 0;
        for (int i = 0; i < sortedIndexes.length && shown < 3; i++) {
            int itemIndex = sortedIndexes[i];
            int quantity = itemSales[itemIndex];
            if (quantity <= 0) {
                break;
            }
            if (quantity != previousSales) {
                rank = i + 1;
                previousSales = quantity;
            }
            printLine(String.format("%-5d %-38s %10d", rank, foodNames[itemIndex], quantity));
            shown++;
        }

        System.out.println();
        printLine("LEAST ORDERED ITEMS");
        printLine(String.format("%-5s %-38s %10s", "Rank", "Item", "Units Sold"));
        printLine(separator);

        shown = 0;
        previousSales = -1;
        rank = 0;
        for (int i = sortedIndexes.length - 1; i >= 0 && shown < 3; i--) {
            int itemIndex = sortedIndexes[i];
            int quantity = itemSales[itemIndex];
            if (quantity != previousSales) {
                rank = sortedIndexes.length - i;
                previousSales = quantity;
            }
            printLine(String.format("%-5d %-38s %10d", rank, foodNames[itemIndex], quantity));
            shown++;
        }
        printLine(border);
    }

    // Compatibility method for callers that do not have session transactions.
    public void displayPopularItems() {
        displayPopularItems(null);
    }
}