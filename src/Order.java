
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

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
    private static final int RECEIPT_WIDTH = 60;

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

    // Center a line of text
    public void printCentered(String text) {
        int padding = Math.max(
                0, (CONSOLE_WIDTH - text.length()) / 2
        );

        System.out.println(" ".repeat(padding) + text);
    }

    // Center an input prompt
    public void printCenteredPrompt(String text) {
        int padding = Math.max(
                0, (CONSOLE_WIDTH - text.length()) / 2
        );

        System.out.print(" ".repeat(padding) + text);
    }

    // Left-align content within the centered receipt
    public void printReceiptLine(String text) {
        int padding = Math.max(
                0, (CONSOLE_WIDTH - RECEIPT_WIDTH) / 2
        );

        System.out.println(" ".repeat(padding) + text);
    }

    // Print a formatted receipt row
    public void printReceiptRow(String format, Object... args) {
        String line = String.format(format, args);
        printReceiptLine(line);
    }

    // Select food items
    public void selectItems() {
        int itemCount = 0;
        boolean ordering = true;

        clearConsole();
        printCentered("========== ORDER TAKING ==========");
        menu.displayMenu(' ');

        while (ordering) {

            printCenteredPrompt("Select category or X to finish: ");

            char categoryChoice = input.next().toUpperCase().charAt(0);

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
                    printCentered("Invalid category.");
                    continue;
            }

            // Display selected category
            clearConsole();
            menu.displayMenu(categoryChoice);

            while (true) {

                printCenteredPrompt("Enter food number, B to change category, or X to finish: ");

                String choice = input.next();

                if (choice.equalsIgnoreCase("B")) {
                    clearConsole();
                    printCentered("========== ORDER TAKING ==========");
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
                    printCentered("Invalid food selection.");
                    continue;
                }

                int itemIndex = foodChoice - 1;

                if (itemIndex < 0 ||
                        itemIndex >= menu.foodNames.length) {

                    printCentered("Invalid food selection.");
                }

                else if (!menu.categories[itemIndex].equals(selectedCategory)) {

                    printCentered("Please select an item from this category.");
                }

                else if (menu.stocks[itemIndex] <= 0) {
                    printCentered("This item is out of stock.");
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
                        printCentered("Item already selected.");
                    }

                    else if (itemCount < selectedItems.length) {
                        selectedItems[itemCount] = itemIndex;
                        itemCount++;

                        printCentered(menu.foodNames[itemIndex] + " added to your order.");
                    }

                    else {
                        printCentered("Maximum items reached.");
                    }
                }
            }
        }

        inputQuantity();
    }

    // Enter quantities
    public void inputQuantity() {

        clearConsole();
        printCentered("========== FOOD QUANTITY ==========");

        for (int i = 0; i < selectedItems.length; i++) {

            if (selectedItems[i] != -1) {
                int itemIndex = selectedItems[i];

                System.out.println();
                printCentered("Food: " + menu.foodNames[itemIndex]);

                printCenteredPrompt("Enter quantity: ");
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

        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter dtf =
                DateTimeFormatter.ofPattern("MM-dd-yyyy HH:mm:ss");

        String formattedDate = dtf.format(now);

        String custName = customer.getName();
        String custPhone = customer.getPhone();
        String custAddress = customer.getAddress();
        String custEmail = customer.getEmail();

        double totalVat = totalAmount - (totalAmount / 1.12);

        String border = "=".repeat(RECEIPT_WIDTH);
        String separator = "-".repeat(RECEIPT_WIDTH);

        System.out.println();
        System.out.println();

        printReceiptLine(border);

        printReceiptRow("%" + RECEIPT_WIDTH + "s", centerReceiptText("PINRITONG MANOK NG KENTUCKY"));
        printReceiptRow("%" + RECEIPT_WIDTH + "s", centerReceiptText("OFFICIAL E-RECEIPT"));
        printReceiptLine(border);
        printReceiptRow("%-20s %39s", "Date:", formattedDate);
        printReceiptRow("%-20s %39s", "Customer:", custName);
        printReceiptRow("%-20s %39s", "Contact No.:", custPhone);
        printReceiptRow("%-20s %39s", "Email:", custEmail);
        printReceiptLine("Delivery Address:");
        printReceiptLine(custAddress);
        printReceiptLine(separator);
        printReceiptRow("%-32s %6s %20s", "Description", "QTY", "Price");
        printReceiptLine(separator);

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
            printReceiptLine("No items ordered.");
        }

        printReceiptLine(separator);
        printReceiptRow("%-39s %20s", "Total:", String.format("PHP %.2f", totalAmount));
        printReceiptRow("%-39s %20s", "Total VAT:", String.format("PHP %.2f", totalVat));
        printReceiptLine(border);
        printReceiptLine(centerReceiptText("4-1234-" + orderID + "-1296"));
        printReceiptLine(separator);
    }

    // Center text within the receipt width
    public String centerReceiptText(String text) {

        if (text.length() >= RECEIPT_WIDTH) {
            return text;
        }

        int leftPadding =
                (RECEIPT_WIDTH - text.length()) / 2;

        return " ".repeat(leftPadding) + text;
    }
}