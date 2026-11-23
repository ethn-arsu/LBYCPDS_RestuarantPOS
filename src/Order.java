import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Scanner;

public class Order {

    int orderID;
    int itemCount;
    int[] selectedItems;
    int[] quantities;
    double totalAmount;
    double subtotal;
    double totalDiscount;
    double totalVat;
    boolean hasPwdDiscount;
    String status;
    LocalDateTime placedAt;
    LocalDateTime completedAt;
    Menu menu;
    Customer customer;
    OrderQueue orderQueue;
    TransactionList transactions;
    Scanner input;

    // Console formatting
    private static final int CONSOLE_WIDTH = 120;
    private static final int CONTENT_WIDTH = 60;

    // Constructor retained for compatibility with existing callers.
    public Order(Menu menu, Customer currentCustomer, OrderQueue orderQueue, Scanner input) {
        this(menu, currentCustomer, orderQueue, input, null);
    }

    // Constructor with access to completed session transactions.
    public Order(Menu menu, Customer currentCustomer, OrderQueue orderQueue,
                 Scanner input, TransactionList transactions) {
        this.menu = menu;
        this.customer = currentCustomer;
        this.orderQueue = orderQueue;
        this.transactions = transactions;
        this.input = input;
        this.orderID = (int) (Math.random() * 90000) + 10000;
        this.itemCount = 0;

        selectedItems = new int[menu.foodNames.length];
        quantities = new int[menu.foodNames.length];

        for (int i = 0; i < selectedItems.length; i++) {
            selectedItems[i] = -1;
        }

        totalAmount = 0;
        subtotal = 0;
        totalDiscount = 0;
        totalVat = 0;
        hasPwdDiscount = false;
        status = "PENDING";
        placedAt = LocalDateTime.now();
        completedAt = null;
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
    public boolean hasItems(){
        return itemCount>0;
    }

    // Select food items
    public void selectItems() {

        boolean ordering = true;

        while (ordering) {

            clearConsole();
            printCentered("========== ORDER TAKING ==========");
            menu.displayMenu(' ', transactions);

            printPrompt("Select category or X to finish: ");
            String categoryInput = input.nextLine().trim().toUpperCase();
            if (categoryInput.length() != 1) {
                printLine("Invalid category.");
                pauseScreen();
                continue;
            }
            char categoryChoice = categoryInput.charAt(0);

            if (categoryChoice == 'X') {
                break;
            }

            if (categoryChoice == 'G') {
                clearConsole();
                menu.displayPopularItems(transactions);
                pauseScreen();
                continue;
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
                String choice = input.nextLine().trim();

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
                    pauseScreen();
                    continue;
                }

                int itemIndex = foodChoice - 1;

                if (itemIndex < 0 ||
                        itemIndex >= menu.foodNames.length) {

                    printLine("Invalid food selection.");
                    pauseScreen();
                    continue;
                }

                if (!menu.categories[itemIndex].equals(selectedCategory)) {

                    printLine("Please select an item from this category.");
                    pauseScreen();
                    continue;
                }

                if (menu.stocks[itemIndex] <= 0) {
                    printLine("This item is out of stock.");
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

                int currentQuantity = (existingIndex == -1) ? 0 : quantities[existingIndex];
                int reservedQuantity = orderQueue.getReservedQuantity(itemIndex);
                int availableForAdditional = menu.stocks[itemIndex]
                        - reservedQuantity - currentQuantity;

                System.out.println();
                printLine("Food: " + menu.foodNames[itemIndex]);
                printLine(String.format("Price: PHP %.2f", menu.prices[itemIndex]));
                printLine("Available quantity for this order: " + availableForAdditional);

                int quantity;

                while (true) {

                    printPrompt("Enter quantity to add: ");
                    String quantityInput = input.nextLine().trim();

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

                    if (currentQuantity + quantity + reservedQuantity >
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

                displayCurrentOrder();

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

                    String nextChoice = input.nextLine().trim();

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
    //Checks if customer is done ordering
    public int confirmFinalOrder(){

        while (true) {
            clearConsole();
            calculateTotal();

            printCentered("REVIEW FINAL ORDER");
            System.out.println();
            displayCurrentOrder();

            System.out.println();
            printLine("Proceed to E-receipt?");
            printLine("[1] Yes, confirm order");
            printLine("[2] Add more items");
            printLine("[3] Remove items");
            printLine("[4] Edit items");
            printLine("[5] No, cancel order");
            System.out.println();

            printPrompt("Select option: ");
            String choice = input.nextLine().trim();

            switch (choice) {
                case "1": {return 1;}
                case "2": {return 2;}
                case "3":
                {
                    if (hasItems()) {
                        removeItem();
                    }
                    else {
                        printLine("There are no items to remove.");
                    }
                    break;
                }
                case "4":
                {
                    if (hasItems()) {
                        editItem();
                    }
                    else {
                        printLine("There are no items to edit.");
                    }

                    break;
                }
                case "5": {return 5;}
                default: { printLine("Invalid option. Please try again."); }
            }
        }
    }

    // Remove an item from the current order
    private void removeItem() {

        clearConsole();

        printLine("=".repeat(CONTENT_WIDTH));
        printCentered("REMOVE ORDER ITEM");
        printLine("=".repeat(CONTENT_WIDTH));
        System.out.println();

        // Display selected items
        for (int i = 0; i < itemCount; i++) {
            int itemIndex = selectedItems[i];
            printLine(String.format("[%d] %s x%d", i + 1, menu.foodNames[itemIndex], quantities[i]));
        }

        System.out.println();
        printLine("[0] Cancel");
        System.out.println();

        printPrompt("Enter item number to remove: ");
        String choice = input.nextLine().trim();

        int itemPosition;
        try {
            itemPosition = Integer.parseInt(choice);
        } catch (NumberFormatException e) {
            printLine("Invalid item number.");
            pauseScreen();
            return;
        }

        if (itemPosition == 0) {
            return;
        }

        if (itemPosition < 1 || itemPosition > itemCount) {
            printLine("Invalid item number.");
            pauseScreen();
            return;
        }

        int removeIndex = itemPosition - 1;
        String removedItem = menu.foodNames[selectedItems[removeIndex]];

        // Shift remaining items to fill the empty position
        for (int i = removeIndex; i < itemCount - 1; i++) {
            selectedItems[i] = selectedItems[i + 1];
            quantities[i] = quantities[i + 1];
        }

        // Clear the last position
        itemCount--;
        selectedItems[itemCount] = -1;
        quantities[itemCount] = 0;

        calculateTotal();

        System.out.println();
        printLine(removedItem + " has been removed from your order.");

        pauseScreen();
    }

    // Edit the quantity of an item in the current order
    private void editItem() {

        clearConsole();

        printLine("=".repeat(CONTENT_WIDTH));
        printCentered("EDIT ORDER ITEM");
        printLine("=".repeat(CONTENT_WIDTH));
        System.out.println();

        // Display selected items
        for (int i = 0; i < itemCount; i++) {
            int itemIndex = selectedItems[i];

            printLine(String.format("[%d] %s x%d",
                    i + 1, menu.foodNames[itemIndex], quantities[i]));
        }

        System.out.println();
        printLine("[0] Cancel");
        System.out.println();

        printPrompt("Enter item number to edit: ");
        String choice = input.nextLine().trim();

        int itemPosition;

        try {
            itemPosition = Integer.parseInt(choice);
        } catch (NumberFormatException e) {
            printLine("Invalid item number.");
            pauseScreen();
            return;
        }

        if (itemPosition == 0) {
            return;
        }

        if (itemPosition < 1 || itemPosition > itemCount) {
            printLine("Invalid item number.");
            pauseScreen();
            return;
        }

        int orderIndex = itemPosition - 1;
        int itemIndex = selectedItems[orderIndex];

        System.out.println();
        printLine("Selected Item: " + menu.foodNames[itemIndex]);
        printLine("Current Quantity: " + quantities[orderIndex]);

        // Check stock reserved by other pending orders
        int reservedQuantity = orderQueue.getReservedQuantity(itemIndex);
        int availableStock = menu.stocks[itemIndex] - reservedQuantity;
        printLine("Maximum Available Quantity: " + availableStock);
        System.out.println();

        printPrompt("Enter new quantity (0 to cancel): ");
        String quantityInput = input.nextLine().trim();

        int newQuantity;
        try {
            newQuantity = Integer.parseInt(quantityInput);
        } catch (NumberFormatException e) {
            printLine("Invalid quantity.");
            pauseScreen();
            return;
        }

        if (newQuantity == 0) {
            return;
        }

        if (newQuantity < 0) {
            printLine("Quantity must be greater than zero.");
            pauseScreen();
            return;
        }

        // Check if the new quantity fits the available stock
        if (newQuantity > availableStock) {
            printLine("Requested quantity exceeds available stock.");
            pauseScreen();
            return;
        }

        // Update the selected item's quantity
        quantities[orderIndex] = newQuantity;
        calculateTotal();

        System.out.println();
        printLine("Item quantity updated successfully!");
        printLine("New Quantity: " + quantities[orderIndex]);
        printLine(String.format("New Item Total: PHP %.2f", menu.prices[itemIndex] * newQuantity));

        pauseScreen();
    }

    // Ask if user wants to avail PWD discount
    public void pwdDiscount(){
        clearConsole();
        String border = "=".repeat(CONTENT_WIDTH);
        printLine(border);
        printCentered("PWD DISCOUNT");
        printLine(border);
        System.out.println();

        while (true) {
            printPrompt("Do you want to avail the PWD Discount? (Y/N): ");
            String choice = input.nextLine().trim();
            if (choice.equalsIgnoreCase("Y") || choice.equalsIgnoreCase("yes")) {
                hasPwdDiscount = true;
                System.out.println();
                printCentered("20% PWD DISCOUNT AND VAT EXEMPTION SUCCESSFULLY APPLIED!");
                System.out.println();
                pauseScreen();
                break;
            }else if (choice.equalsIgnoreCase("N") || choice.equalsIgnoreCase("no")) {
                hasPwdDiscount = false;
                break;
            }else printLine("Invalid option. Please try again.");
        }
        calculateTotal();
    }

    // Deduct stock when the admin processes the order from the queue
    public void finalizeOrder(){
        calculateTotal();
        for(int i=0; i<itemCount; i++){
            if(selectedItems[i] != -1){
                int itemIndex = selectedItems[i];
                menu.stocks[itemIndex] -= quantities[i];
            }
        }
    }
    // Display current order and running total
    public void displayCurrentOrder() {

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

    // Calculate subtotal, PWD discount, VAT, final total
    public void calculateTotal() {
        subtotal = 0;
        totalAmount = 0;

        for (int i = 0; i < selectedItems.length; i++) {

            if (selectedItems[i] != -1) {

                int itemIndex = selectedItems[i];

                subtotal += menu.prices[itemIndex] * quantities[i];
            }
        }
        if(hasPwdDiscount && subtotal>0){
            double vatExemptSales = subtotal/1.12;
            totalDiscount = vatExemptSales * 0.20;
            totalVat = 0.0;
            totalAmount = vatExemptSales - totalDiscount;
        }else{
            totalDiscount = 0.0;
            totalVat = subtotal - (subtotal/1.12);
            totalAmount = subtotal;
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

        printReceiptRow("%-6s %-32s %20s", "QTY", "Description", "Price");

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

                printReceiptRow("%-6s %-32s %20s", qty, itemName, price);
            }
        }

        if (!hasItems) {
            printLine("No items ordered.");
        }

        printLine(separator);

        printReceiptRow("%-39s %20s", "Subtotal:", String.format("PHP %.2f", subtotal));

        if(hasPwdDiscount) {
            printReceiptRow("%-39s %20s", "PWD Discount (20%):", String.format("-PHP %.2f", totalDiscount));
            printReceiptRow("%-39s %20s", "Total VAT (Exempt):", "PHP 0.00");
        }else {
            printReceiptRow("%-39s %20s", "PWD Discount:", "PHP 0.00");
            printReceiptRow("%-39s %20s", "Total VAT (12%):", String.format("PHP %.2f", totalVat));
        }
        printLine(border);
        printReceiptRow("%-39s %20s", "TOTAL AMOUNT:", String.format("PHP %.2f", totalAmount));
        printLine(border);
        printReceiptHeading("4-1234-" + orderID + "-1296");

        printLine(separator);
    }
}