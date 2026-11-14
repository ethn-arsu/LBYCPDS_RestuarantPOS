import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Scanner;

public class Order {

    int orderID;
    int[] selectedItems;
    int[] quantities;
    double totalAmount;
    Menu menu;
    Customer customer;
    Scanner input = new Scanner(System.in);

    // Console formatting
    private static final int CONSOLE_WIDTH = 120;
    private static final int CONTENT_WIDTH = 60;

    // Constructor
    public Order(Menu menu, Customer currentCustomer) {
        this.menu = menu;
        this.customer = currentCustomer;
        this.orderID = (int) (Math.random() * 90000) + 10000;

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

    // Left margin of content area
    private String getPadding() {
        int padding = Math.max(0, (CONSOLE_WIDTH - CONTENT_WIDTH) / 2);

        return " ".repeat(padding);
    }

    // Print left-aligned text
    private void printLine(String text) {
        System.out.println(getPadding() + text);
    }

    // Center text
    private void printCentered(String text) {
        int padding = Math.max(0, (CONTENT_WIDTH - text.length()) / 2);

        System.out.println(getPadding() + " ".repeat(padding) + text);
    }

    // Print an input prompt
    private void printPrompt(String text) {
        System.out.print(getPadding() + text);
    }

    // Format receipt columns
    private void printReceiptRow(String format, Object... args) {
        printLine(String.format(format, args));
    }

    // Format receipt heading
    private void printReceiptHeading(String text) {
        printCentered(text);
    }

    // Pause before changing screens
    private void pauseScreen() {
        printPrompt("Press Enter to continue...");
        input.nextLine();
    }

    // Select food items
    public void selectItems() {

        int itemCount = 0;
        boolean ordering = true;

        while (ordering) {

            clearConsole();
            printCentered("========== ORDER TAKING ==========");
            menu.displayMenu(' ');

            printPrompt("Select category or X to finish: ");
            String categoryInput = input.next().toUpperCase();
            char categoryChoice = categoryInput.charAt(0);

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
                    printLine("Invalid category.");
                    input.nextLine();
                    pauseScreen();
                    continue;
            }

            boolean inCategory = true;

            while (inCategory && ordering) {

                clearConsole();
                menu.displayMenu(categoryChoice);

                printPrompt("Select food number: ");
                String choice = input.next();

                // Return to category selection
                if (choice.equalsIgnoreCase("B")) {
                    break;
                }

                // Finish ordering
                if (choice.equalsIgnoreCase("X")) {
                    ordering = false;
                    break;
                }

                int foodChoice;

                try {
                    foodChoice = Integer.parseInt(choice);
                } catch (NumberFormatException e) {
                    printLine("Invalid food selection.");
                    input.nextLine();
                    pauseScreen();
                    continue;
                }

                int itemIndex = foodChoice - 1;

                if (itemIndex < 0 ||
                        itemIndex >= menu.foodNames.length) {

                    printLine("Invalid food selection.");
                    input.nextLine();
                    pauseScreen();
                    continue;
                }

                if (!menu.categories[itemIndex].equals(selectedCategory)) {

                    printLine("Please select an item from this category.");
                    input.nextLine();
                    pauseScreen();
                    continue;
                }

                if (menu.stocks[itemIndex] <= 0) {
                    printLine("This item is out of stock.");
                    input.nextLine();
                    pauseScreen();
                    continue;
                }

                // Check whether item is already in the order
                int existingIndex = -1;

                for (int i = 0; i < itemCount; i++) {
                    if (selectedItems[i] == itemIndex) {
                        existingIndex = i;
                        break;
                    }
                }

                // Enter quantity immediately
                clearConsole();

                printLine("=".repeat(CONTENT_WIDTH));
                printCentered("FOOD QUANTITY");
                printLine("=".repeat(CONTENT_WIDTH));

                System.out.println();
                printLine("Food: " + menu.foodNames[itemIndex]);
                printLine(String.format("Price: PHP %.2f", menu.prices[itemIndex]));
                printLine("Available stock: " + menu.stocks[itemIndex]);

                int quantity;

                while (true) {

                    printPrompt("Enter quantity: ");
                    String quantityInput = input.next();

                    try {
                        quantity = Integer.parseInt(quantityInput);
                    } catch (NumberFormatException e) {
                        printLine("Please enter a valid number.");
                        continue;
                    }

                    if (quantity <= 0) {
                        printLine("Quantity must be greater than zero.");
                        continue;
                    }

                    int currentQuantity = 0;

                    if (existingIndex != -1) {
                        currentQuantity = quantities[existingIndex];
                    }

                    if (currentQuantity + quantity >
                            menu.stocks[itemIndex]) {

                        printLine("Requested quantity exceeds available stock.");
                        continue;
                    }

                    break;
                }

                // Add or update selected item
                if (existingIndex != -1) {

                    quantities[existingIndex] += quantity;

                } else if (itemCount < selectedItems.length) {

                    selectedItems[itemCount] = itemIndex;
                    quantities[itemCount] = quantity;
                    itemCount++;

                } else {

                    printLine("Maximum number of items reached.");
                    input.nextLine();
                    pauseScreen();
                    continue;
                }

                calculateTotal();

                // Show the current order
                clearConsole();

                printCentered("ITEM ADDED SUCCESSFULLY!");
                System.out.println();

                displayCurrentOrder(itemCount);

                // Navigation after adding an item
                while (true) {

                    System.out.println();
                    printLine("What would you like to do next?");
                    System.out.println();

                    printLine("[1] Add another item");
                    printLine("[2] Change food category");
                    printLine("[3] Finish order");

                    System.out.println();
                    printPrompt("Select option: ");

                    String nextChoice = input.next();

                    if (nextChoice.equals("1")) {
                        // Return to the current food category
                        break;
                    }

                    else if (nextChoice.equals("2")) {
                        inCategory = false;
                        break;
                    }

                    else if (nextChoice.equals("3")) {
                        ordering = false;
                        inCategory = false;
                        break;
                    }

                    else {
                        printLine("Invalid option. Please try again.");
                    }
                }
            }
        }

        calculateTotal();
    }

    // Display current order and running total
    public void displayCurrentOrder(int itemCount) {

        String border = "=".repeat(CONTENT_WIDTH);
        String separator = "-".repeat(CONTENT_WIDTH);

        printLine(border);
        printCentered("CURRENT ORDER");
        printLine(border);
        System.out.println();

        for (int i = 0; i < itemCount; i++) {

            int itemIndex = selectedItems[i];

            if (itemIndex != -1) {

                String itemDescription = menu.foodNames[itemIndex] + " x " + quantities[i];

                double itemTotal = menu.prices[itemIndex] * quantities[i];

                printReceiptRow(
                        "%-42s %17s",
                        itemDescription,
                        String.format("PHP %.2f", itemTotal)
                );
            }
        }

        System.out.println();
        printLine(separator);

        printReceiptRow(
                "%-42s %17s",
                "Running Total:",
                String.format("PHP %.2f", totalAmount)
        );
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

    // Display final receipt
    public void displayOrderSummary() {

        clearConsole();

        LocalDateTime now = LocalDateTime.now();

        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("MM-dd-yyyy HH:mm:ss");

        String formattedDate = dtf.format(now);

        String custName = customer.getName();
        String custPhone = customer.getPhone();
        String custAddress = customer.getAddress();
        String custEmail = customer.getEmail();

        double totalVat =
                totalAmount - (totalAmount / 1.12);

        String border = "=".repeat(CONTENT_WIDTH);
        String separator = "-".repeat(CONTENT_WIDTH);

        System.out.println();

        printLine(border);
        printReceiptHeading("PINRITONG MANOK NG KENTUCKY");
        printReceiptHeading("OFFICIAL E-RECEIPT");
        printLine(border);

        System.out.println();

        printReceiptRow("%-20s %39s", "Date:", formattedDate);
        printReceiptRow("%-20s %39s", "Customer:", custName);
        printReceiptRow("%-20s %39s", "Contact No.:", custPhone);
        printReceiptRow("%-20s %39s", "Email:", custEmail);

        printLine("Delivery Address:");
        printLine(custAddress);

        System.out.println();
        printLine(separator);

        printReceiptRow("%-32s %6s %20s", "Description", "QTY", "Price");

        printLine(separator);

        boolean hasItems = false;

        for (int i = 0; i < selectedItems.length; i++) {

            if (selectedItems[i] != -1) {

                int itemIndex = selectedItems[i];
                hasItems = true;

                String itemName = menu.foodNames[itemIndex];
                String qty = String.format("%02d", quantities[i]);

                double lineTotal = menu.prices[itemIndex] * quantities[i];

                String price = String.format("PHP %.2f", lineTotal);

                printReceiptRow("%-32s %6s %20s", itemName, qty, price);
            }
        }

        if (!hasItems) {
            printLine("No items ordered.");
        }

        printLine(separator);

        printReceiptRow("%-39s %20s", "Total:", String.format("PHP %.2f", totalAmount));
        printReceiptRow("%-39s %20s", "Total VAT:", String.format("PHP %.2f", totalVat));

        printLine(border);

        printReceiptHeading("4-1234-" + orderID + "-1296");

        printLine(separator);
    }
}