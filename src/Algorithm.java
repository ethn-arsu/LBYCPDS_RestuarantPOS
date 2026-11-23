public class Algorithm {
    private static final int CONSOLE_WIDTH = 120;
    private static final int CONTENT_WIDTH = 60;

    public void clearConsole() {
        System.out.print("\033[H\033[2J\033[3J");
        System.out.flush();
    }

    // Left margin of content area
    private String getPadding() {
        int padding = Math.max(0, (CONSOLE_WIDTH - CONTENT_WIDTH) / 2);
        return " ".repeat(padding);
    }


    // Center text
    private void printCentered(String text) {
        int padding = Math.max(0, (CONTENT_WIDTH - text.length()) / 2);
        System.out.println(getPadding() + " ".repeat(padding) + text);
    }
    // TODO: Implement linear search
    public void linearSearch() {

    }

    // Calculate the total quantity sold for each menu item.
    // The TransactionList should contain completed transactions only.
    public int[] calculateItemSales(Menu menu, TransactionList transactions) {

        if (menu == null || menu.foodNames == null) {
            return new int[0];
        }

        int[] itemSales = new int[menu.foodNames.length];

        if (transactions == null) {
            return itemSales;
        }

        TransactionList.Node current = transactions.head;

        while (current != null) {

            Order order = current.data;

            if (order != null && order.selectedItems != null && order.quantities != null) {

                for (int i = 0; i < order.itemCount; i++) {

                    int itemIndex = order.selectedItems[i];
                    int quantity = order.quantities[i];

                    if (itemIndex >= 0 && itemIndex < itemSales.length && quantity > 0) {

                        itemSales[itemIndex] += quantity;
                    }
                }
            }

            current = current.next;
        }

        return itemSales;
    }

    // Sort menu item indexes by quantity sold (highest to lowest).
    // Selection Sort is used so the menu's original arrays are not rearranged.
    public int[] sortItems(int[] itemSales) {

        if (itemSales == null) {
            return new int[0];
        }

        int[] sortedIndexes = new int[itemSales.length];

        // Initially, indexes follow the menu's original order.
        for (int i = 0; i < sortedIndexes.length; i++) {
            sortedIndexes[i] = i;
        }

        // Selection Sort: select the highest-selling item for each position.
        for (int i = 0; i < sortedIndexes.length - 1; i++) {

            int highestIndex = i;

            for (int j = i + 1; j < sortedIndexes.length; j++) {

                int candidate = sortedIndexes[j];
                int currentHighest = sortedIndexes[highestIndex];

                if (itemSales[candidate] > itemSales[currentHighest]
                        || (itemSales[candidate] == itemSales[currentHighest]
                        && candidate < currentHighest)) {

                    highestIndex = j;
                }
            }

            // Swap the selected highest-selling index into its position.
            int temporary = sortedIndexes[i];
            sortedIndexes[i] = sortedIndexes[highestIndex];
            sortedIndexes[highestIndex] = temporary;
        }

        return sortedIndexes;
    }

    // Print a line within the project's centered console layout.
    private void printLine(String text) {
        int padding = Math.max(0, (120 - 60) / 2);
        System.out.println(" ".repeat(padding) + text);
    }

    // Display the complete item sales ranking from highest to lowest.
    public void rankItems(Menu menu, TransactionList transactions) {

        if (menu == null || menu.foodNames == null) {
            System.out.println("Menu data is unavailable.");
            return;
        }

        int[] itemSales = calculateItemSales(menu, transactions);
        int totalUnitsSold = 0;

        for (int quantity : itemSales) {
            totalUnitsSold += quantity;
        }

        final int CONTENT_WIDTH = 60;
        String border = "=".repeat(CONTENT_WIDTH);
        String separator = "-".repeat(CONTENT_WIDTH);

        printLine(border);
        printCentered("ITEM SALES RANKING");
        printLine(border);
        System.out.println();

        if (totalUnitsSold == 0) {
            System.out.println("No completed sales recorded yet.");
            System.out.println(border);
            return;
        }

        int[] sortedIndexes = sortItems(itemSales);
        printLine(String.format("%-6s %-39s %10s", "Rank", "Item", "Units Sold"));
        printLine(separator);

        for (int rank = 0; rank < sortedIndexes.length; rank++) {

            int itemIndex = sortedIndexes[rank];
            printLine(String.format("%-6d %-39s %10d", rank + 1, menu.foodNames[itemIndex], itemSales[itemIndex]));
        }

        printLine(separator);
        printLine(String.format("%-46s %10d", "TOTAL UNITS SOLD:", totalUnitsSold));
        printLine(border);
    }
}