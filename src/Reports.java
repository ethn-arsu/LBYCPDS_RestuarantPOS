import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;

public class Reports {

    // Inventory movements are kept in memory for the current session only.
    private static class InventoryMovement {
        String itemName;
        int quantityChange; // positive = restock; negative = sales deduction

        InventoryMovement(String itemName, int quantityChange) {
            this.itemName = itemName;
            this.quantityChange = quantityChange;
        }
    }

    private final List<InventoryMovement> inventoryMovements = new ArrayList<>();

    public void recordInventoryAdjustment(String itemName, int quantityChange) {
        if (itemName == null || itemName.trim().isEmpty() || quantityChange == 0) {
            return;
        }
        inventoryMovements.add(new InventoryMovement(itemName.trim(), quantityChange));
    }

    private static final int CONSOLE_WIDTH = 120;
    private static final int CONTENT_WIDTH = 60;

    private String padding() {
        return " ".repeat(Math.max(0, (CONSOLE_WIDTH - CONTENT_WIDTH) / 2));
    }

    private void printLine(String text) {
        System.out.println(padding() + text);
    }

    private void printCentered(String text) {
        int innerPadding = Math.max(0, (CONTENT_WIDTH - text.length()) / 2);
        System.out.println(padding() + " ".repeat(innerPadding) + text);
    }

    private void clearConsole() {
        System.out.print("\033[H\033[2J\033[3J");
        System.out.flush();
    }

    private void pause(Scanner input) {
        System.out.println();
        printLine("Press Enter to continue...");
        input.nextLine();
    }

    // Count total quantity of all items sold
    public int calculateItemsSold(Menu menu, TransactionList transactions) {

        Algorithm algorithm = new Algorithm();

        int[] salesByItem = algorithm.calculateItemSales(menu, transactions);
        int totalItemsSold = 0;

        for (int quantity : salesByItem) {
            totalItemsSold += quantity;
        }

        return totalItemsSold;
    }

    // Calculate quantities sold per menu category
    // Categories retain their original menu order
    public Map<String, Integer> calculateCategorySales(Menu menu, TransactionList transactions) {

        Map<String, Integer> categorySales = new LinkedHashMap<>();

        if (menu == null || menu.categories == null) {
            return categorySales;
        }

        // Initialize each category with zero sales
        for (String category : menu.categories) {

            if (category != null) {
                categorySales.putIfAbsent(category, 0);
            }
        }

        Algorithm algorithm = new Algorithm();

        int[] itemSales = algorithm.calculateItemSales(menu, transactions);

        // Add item sales to their respective categories
        for (int i = 0; i < itemSales.length; i++) {

            String category = menu.categories[i];

            if (category != null) {
                categorySales.put(category, categorySales.getOrDefault(category, 0) + itemSales[i]);
            }
        }

        return categorySales;
    }

    // Count unique customers
    public int calculateCustomers(
            TransactionList transactions) {

        if (transactions == null) {
            return 0;
        }

        Set<String> customerIdentifiers = new HashSet<>();
        TransactionList.Node current = transactions.head;

        while (current != null) {
            Order order = current.data;
            if (order != null) {
                Customer customer = order.customer;
                String identifier = null;
                if (customer != null) {
                    identifier = customer.getCustomerId();
                    if (identifier == null
                            || identifier.trim().isEmpty()) {
                        identifier = customer.getEmail();
                    }
                }

                // Count the order separately if customer
                // information is unavailable
                if (identifier == null || identifier.trim().isEmpty()) {

                    identifier = "ORDER-" + order.orderID;
                }
                customerIdentifiers.add(identifier.trim().toLowerCase());
            }

            current = current.next;
        }

        return customerIdentifiers.size();
    }

    // Calculate average service time in minutes
    // Returns -1 if timestamps are unavailable
    public double calculateAverageTime(TransactionList transactions) {

        if (transactions == null) {
            return -1;
        }

        long totalServiceSeconds = 0;
        int ordersWithTimes = 0;

        TransactionList.Node current = transactions.head;

        while (current != null) {

            Order order = current.data;

            if (order != null && order.placedAt != null && order.completedAt != null) {

                long serviceSeconds = Duration.between(order.placedAt, order.completedAt).getSeconds();

                if (serviceSeconds >= 0) {
                    totalServiceSeconds += serviceSeconds;
                    ordersWithTimes++;
                }
            }
            current = current.next;
        }

        if (ordersWithTimes == 0) {
            return -1;
        }

        return (totalServiceSeconds / 60.0) / ordersWithTimes;
    }

    // Calculate total session duration
    // Earliest order placement to latest order completion
    // Returns -1 if timestamps are unavailable
    public long calculateSessionDurationMinutes(TransactionList transactions) {

        if (transactions == null) {
            return -1;
        }

        LocalDateTime earliestPlaced = null;
        LocalDateTime latestCompleted = null;

        TransactionList.Node current = transactions.head;

        while (current != null) {

            Order order = current.data;

            if (order != null
                    && order.placedAt != null
                    && order.completedAt != null
                    && !order.completedAt.isBefore(
                    order.placedAt)) {

                if (earliestPlaced == null || order.placedAt.isBefore(earliestPlaced)) {
                    earliestPlaced = order.placedAt;
                }

                if (latestCompleted == null || order.completedAt.isAfter(latestCompleted)) {
                    latestCompleted = order.completedAt;
                }
            }
            current = current.next;
        }

        if (earliestPlaced == null || latestCompleted == null) {
            return -1;
        }

        return Duration.between(earliestPlaced, latestCompleted).toMinutes();
    }

    // Format session duration
    private String formatDuration(long totalMinutes) {

        if (totalMinutes < 0) {
            return "N/A";
        }

        long hours = totalMinutes / 60;
        long minutes = totalMinutes % 60;

        if (hours > 0) {
            return hours + "h " + minutes + "m";
        }

        return minutes + "m";
    }

    // Summarize stock additions and deductions recorded during the session.
    private void displayInventoryAdjustments() {
        String separator = "-".repeat(CONTENT_WIDTH);
        printLine(separator);
        printLine("INVENTORY ADJUSTMENTS");

        if (inventoryMovements.isEmpty()) {
            printLine("No inventory movements recorded.");
            return;
        }

        Map<String, Integer> restockedByItem = new LinkedHashMap<>();
        Map<String, Integer> soldByItem = new LinkedHashMap<>();
        int totalRestocked = 0;
        int totalDeducted = 0;

        for (InventoryMovement movement : inventoryMovements) {
            if (movement.quantityChange > 0) {
                restockedByItem.put(movement.itemName,
                        restockedByItem.getOrDefault(movement.itemName, 0) + movement.quantityChange);
                totalRestocked += movement.quantityChange;
            } else {
                int deducted = -movement.quantityChange;
                soldByItem.put(movement.itemName,
                        soldByItem.getOrDefault(movement.itemName, 0) + deducted);
                totalDeducted += deducted;
            }
        }

        printLine(String.format("%-28s %8s %8s %8s", "Item", "Added", "Sold", "Net"));
        printLine(separator);

        for (String itemName : restockedByItem.keySet()) {
            int added = restockedByItem.getOrDefault(itemName, 0);
            int sold = soldByItem.getOrDefault(itemName, 0);
            printLine(String.format("%-28s %8d %8d %8d", itemName, added, sold, added - sold));
        }
        for (String itemName : soldByItem.keySet()) {
            if (!restockedByItem.containsKey(itemName)) {
                int sold = soldByItem.getOrDefault(itemName, 0);
                printLine(String.format("%-28s %8d %8d %8d", itemName, 0, sold, -sold));
            }
        }

        printLine(separator);
        printLine(String.format("%-28s %8d %8d %8d", "TOTAL", totalRestocked,
                totalDeducted, totalRestocked - totalDeducted));
    }

    // Generate end-of-session report
    // TransactionList should contain completed transactions only
    public void generateSessionReport(Menu menu, TransactionList transactions) {

        String border = "=".repeat(CONTENT_WIDTH);
        String separator = "-".repeat(CONTENT_WIDTH);

        int transactionCount = 0;

        double grossSales = 0;
        double totalDiscounts = 0;
        double totalVat = 0;
        double netSales = 0;

        if (transactions != null) {

            TransactionList.Node current =
                    transactions.head;

            while (current != null) {
                Order order = current.data;
                if (order != null) {
                    transactionCount++;
                    grossSales += order.subtotal;
                    totalDiscounts += order.totalDiscount;
                    totalVat += order.totalVat;
                    netSales += order.totalAmount;
                }
                current = current.next;
            }
        }

        int customersServed = calculateCustomers(transactions);
        int itemsSold = calculateItemsSold(menu, transactions);
        double averageServiceMinutes = calculateAverageTime(transactions);
        long sessionDurationMinutes = calculateSessionDurationMinutes(transactions);
        Map<String, Integer> categorySales = calculateCategorySales(menu, transactions);

        System.out.println();
        printLine(border);
        printCentered("END-OF-SESSION REPORT");
        printLine(border);
        System.out.println();
        printLine(String.format("%-36s %20d", "Completed Transactions:", transactionCount));
        printLine(String.format("%-36s %20d", "Customers Served:", customersServed));
        printLine(String.format("%-36s %20d", "Total Items Sold:", itemsSold));

        // Category sales breakdown
        printLine(separator);
        printLine("SALES BY CATEGORY");

        for (Map.Entry<String, Integer> entry : categorySales.entrySet()) {
            printLine(String.format("%-36s %20d", entry.getKey() + ":", entry.getValue()));
        }

        // Financial summary
        printLine(separator);
        printLine(String.format("%-36s %20s", "Gross Sales:", String.format("PHP %.2f", grossSales)));
        printLine(String.format("%-36s %20s", "Discounts Applied:", String.format("PHP %.2f", totalDiscounts)));
        printLine(String.format("%-36s %20s", "VAT Collected:", String.format("PHP %.2f", totalVat)));
        printLine(String.format("%-36s %20s", "Net Sales:", String.format("PHP %.2f", netSales)));

        // Inventory movement summary (sales deductions and restocks)
        displayInventoryAdjustments();

        // Average service time
        if (averageServiceMinutes >= 0) {
            printLine(String.format("%-36s %19.2f min", "Average Service Time:", averageServiceMinutes));
        } else {
            printLine(String.format("%-36s %20s", "Average Service Time:", "N/A"));
        }

        // Total session duration
        printLine(String.format("%-36s %20s", "Total Session Duration:", formatDuration(sessionDurationMinutes)));

        printLine(border);

        System.out.println();

        // Display the most and least ordered items, then the complete ranking.
        if (menu != null) {
            menu.displayPopularItems(transactions);
        }
        new Algorithm().rankItems(menu, transactions);
    }

    // Sales report menu
    // This can be called from the Admin menu
    public void displayReportsMenu(Scanner input, Menu menu, TransactionList transactions) {

        while (true) {

            clearConsole();

            String border = "=".repeat(CONTENT_WIDTH);

            printLine(border);
            printCentered("SALES REPORTS");
            printLine(border);

            System.out.println();

            printLine("[1] Item Sales Ranking");
            printLine("[2] End-of-Session Report");
            printLine("[3] Back to Admin Menu");

            System.out.println();
            System.out.print(padding() + "Select option: ");

            String choice = input.nextLine().trim();

            switch (choice) {

                case "1":
                    clearConsole();
                    new Algorithm().rankItems(menu, transactions);
                    pause(input);
                    break;
                case "2":
                    clearConsole();
                    generateSessionReport(menu, transactions);
                    pause(input);
                    break;
                case "3":
                    return;
                default:
                    printLine("Invalid option. Please try again.");
                    pause(input);
            }
        }
    }
}