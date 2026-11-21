import java.time.Duration;
import java.util.HashSet;
import java.util.Set;

public class Reports {

    private static final int CONTENT_WIDTH = 60;


    // Print a line aligned with the project's centered console layout.
    private void printLine(String text) {
        int padding = Math.max(0, (120 - CONTENT_WIDTH) / 2);
        System.out.println(" ".repeat(padding) + text);
    }

    // Print a centered heading.
    private void printCentered(String text) {
        int outerPadding = Math.max(0, (120 - CONTENT_WIDTH) / 2);
        int innerPadding = Math.max(0, (CONTENT_WIDTH - text.length()) / 2);
        System.out.println(" ".repeat(outerPadding) + " ".repeat(innerPadding) + text);
    }

    // Count the total quantity of all items sold in completed transactions.
    public int calculateItemsSold(Menu menu, TransactionList transactions) {

        Algorithm algorithm = new Algorithm();
        int[] salesByItem = algorithm.calculateItemSales(menu, transactions);
        int totalItemsSold = 0;

        for (int quantity : salesByItem) {
            totalItemsSold += quantity;
        }

        return totalItemsSold;
    }

    // Count unique customers represented in the transaction list.
    public int calculateCustomers(TransactionList transactions) {

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

                    if (identifier == null || identifier.trim().isEmpty()) {
                        identifier = customer.getEmail();
                    }
                }

                // If customer information is missing, treat each transaction
                // as a separate customer record for counting purposes.
                if (identifier == null || identifier.trim().isEmpty()) {
                    identifier = "ORDER-" + order.orderID;
                }

                customerIdentifiers.add(identifier.trim().toLowerCase());
            }

            current = current.next;
        }

        return customerIdentifiers.size();
    }

    // Calculate average service time in minutes.
    // Returns -1 if completed-order timestamps are unavailable.
    public double calculateAverageTime(TransactionList transactions) {

        if (transactions == null) {
            return -1;
        }

        long totalServiceSeconds = 0;
        int ordersWithTimes = 0;
        TransactionList.Node current = transactions.head;

        while (current != null) {

            Order order = current.data;

            if (order != null && order.placedAt != null
                    && order.completedAt != null) {

                long serviceSeconds = Duration.between(
                        order.placedAt, order.completedAt).getSeconds();

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

    // Generate the end-of-session report from completed transactions.
    public void generateSessionReport(Menu menu, TransactionList transactions) {

        final String border = "=".repeat(CONTENT_WIDTH);
        final String separator = "-".repeat(CONTENT_WIDTH);


        int transactionCount = 0;
        double grossSales = 0;
        double totalDiscounts = 0;
        double totalVat = 0;
        double netSales = 0;

        if (transactions != null) {

            TransactionList.Node current = transactions.head;

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

        System.out.println();
        printLine(border);
        printCentered("END-OF-SESSION REPORT");
        printLine(border);
        System.out.println();

        printLine(String.format("%-36s %20d", "Completed Transactions:", transactionCount));
        printLine(String.format("%-36s %20d", "Customers Served:", customersServed));
        printLine(String.format("%-36s %20d", "Total Items Sold:", itemsSold));
        printLine(separator);
        printLine(String.format("%-36s %20s", "Gross Sales:", String.format("PHP %.2f", grossSales)));
        printLine(String.format("%-36s %20s", "Discounts Applied:", String.format("PHP %.2f", totalDiscounts)));
        printLine(String.format("%-36s %20s", "VAT Collected:", String.format("PHP %.2f", totalVat)));
        printLine(String.format("%-36s %20s", "Net Sales:", String.format("PHP %.2f", netSales)));

        if (averageServiceMinutes >= 0) {
            printLine(String.format("%-36s %19.2f min", "Average Service Time:", averageServiceMinutes));
        } else {
            printLine(String.format("%-36s %20s", "Average Service Time:", "N/A"));
        }

        printLine(border);
        System.out.println();

        Algorithm algorithm = new Algorithm();
        algorithm.rankItems(menu, transactions);
    }

    // TODO: Receipt generation
    public void generateReceipt() {

    }
}