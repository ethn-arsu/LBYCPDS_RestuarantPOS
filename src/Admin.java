import java.time.LocalDateTime;
import java.util.Scanner;

public class Admin {

    private static final int CONSOLE_WIDTH = 120;
    private static final int CONTENT_WIDTH = 60;

    private static String padding() {
        return " ".repeat((CONSOLE_WIDTH - CONTENT_WIDTH) / 2);
    }

    private static void printLine(String text) {
        System.out.println(padding() + text);
    }

    private static void printCentered(String text) {
        int spaces = Math.max(0,
                (CONTENT_WIDTH - text.length()) / 2);

        System.out.println(padding() + " ".repeat(spaces) + text);
    }

    private static void clearConsole() {
        System.out.print("\033[H\033[2J\033[3J");
        System.out.flush();
    }

    // Verify administrator credentials
    public boolean adminLogin(Scanner input) {

        String envUser = System.getenv("PMK_ADMIN_USER");
        String envPass = System.getenv("PMK_ADMIN_PASS");

        String adminUser = (envUser != null && !envUser.isEmpty()) ? envUser : "admin";
        String adminPass = (envPass != null && !envPass.isEmpty()) ? envPass : "admin123";

        clearConsole();

        printLine("=".repeat(CONTENT_WIDTH));
        printCentered("ADMIN LOGIN");
        printLine("=".repeat(CONTENT_WIDTH));
        System.out.println();

        if (adminUser == null || adminPass == null || adminUser.isEmpty() || adminPass.isEmpty()) {
            printLine("Admin credentials are not configured.");
            return false;
        }

        printLine("Enter administrator credentials.");
        System.out.println();

        System.out.print(padding() + "Username: ");
        String username = input.nextLine().trim();

        System.out.print(padding() + "Password: ");
        String password = input.nextLine();

        if (adminUser.equals(username) && adminPass.equals(password)) {

            printCentered("Admin login successful!");
            return true;
        }

        printLine("Invalid username or password.");
        return false;
    }

    // Display admin menu
    public void displayAdminMenu(Scanner input, OrderQueue orderQueue,
                                 Reports reports, Menu menu,
                                 TransactionList transactions, Algorithm algorithm) {

        while (true) {
            clearConsole();

            printLine("=".repeat(CONTENT_WIDTH));
            printCentered("ADMIN DASHBOARD");
            printLine("=".repeat(CONTENT_WIDTH));

            System.out.println();
            printLine("[1] View Pending Orders (Queue: " + orderQueue.getSize() + ")");
            printLine("[2] Process Next Order");
            printLine("[3] View and Manage Inventory");
            printLine("[4] View and Manage Transaction Records");
            printLine("[5] View Sales Report");
            printLine("[6] Logout");

            System.out.println();
            System.out.print(padding() + "Select option: ");

            String choice = input.nextLine().trim();

            switch (choice) {
                case "1": {
                    clearConsole();
                    monitorQueue(orderQueue);
                    break;
                }
                case "2": {
                    processOrders(orderQueue, transactions, menu, input);
                    break;
                }
                case "3": {
                    manageInventory(menu, algorithm, input);
                    break;
                }
                case "4": {
                    manageTransactions(transactions, input);
                    break;
                }
                case "5": {
                    clearConsole();
                    reports.displayReportsMenu(input, menu, transactions);
                    break;
                }
                case "6": {
                    System.out.println();
                    printCentered("Admin logged out successfully...");
                    System.out.println();
                    System.out.println(padding() + "Press Enter to return to Main Menu...");
                    input.nextLine();
                    return;
                }
                default: {
                    printLine("Invalid option. Please try again.");
                }
            }

            System.out.println();
            System.out.print(padding() + "Press Enter to continue...");
            input.nextLine();

        }
    }

    //
    public void displayAdminMenu(Scanner scanner, OrderQueue orderQueue) {
        displayAdminMenu(scanner, orderQueue, new Reports(), new Menu(), new TransactionList(), new Algorithm());
    }

    // Monitor customer order queue
    public void monitorQueue(OrderQueue orderQueue) {
        orderQueue.displayQueue();
    }

    // Process customer orders using FIFO
    public void processOrders(OrderQueue orderQueue, TransactionList transactions, Menu menu, Scanner input) {
        String border = "=".repeat(CONTENT_WIDTH);
        printLine(border);
        printCentered("PROCESS NEXT ORDER");
        printLine(border);
        System.out.println();

        if(orderQueue.isEmpty()){
            printCentered("No pending orders in the queue.");
            System.out.println();
            printLine(border);
            return;
        }
        Order nextOrder = orderQueue.peek();
        String custName = (nextOrder.customer!=null)? nextOrder.customer.getName(): "Guest";

        printLine("Next Order in queue (FIFO): ");
        printLine("Order ID: " + nextOrder.orderID);
        printLine("Customer: " + custName);
        printLine(String.format("Total:        PHP %.2f", nextOrder.totalAmount));
        System.out.println();

        for(int i = 0; i < nextOrder.itemCount; i++){
            int itemIndex = nextOrder.selectedItems[i];
            if(itemIndex != -1){
                printLine(String.format("  - %-34s x%02d (Stock: %d)",
                        menu.foodNames[itemIndex], nextOrder.quantities[i], menu.stocks[itemIndex]));
            }
        }

        System.out.println();
        System.out.print(padding() + "Complete this order and record receipt? (Y/N): ");
        String choice = input.nextLine().trim();
        if(choice.equalsIgnoreCase("Y") || choice.equalsIgnoreCase("YES")) {
            Order processed = orderQueue.dequeue();
            processed.finalizeOrder();
            processed.completedAt = LocalDateTime.now();
            processed.status = "COMPLETED";

            transactions.addTransaction(processed);
            transactions.saveReceiptToCSV(processed);

            System.out.println();
            printCentered("Order #" + processed.orderID + " processed!");
            printLine("Inventory updated & receipt saved to CSV.");
        }else {
            System.out.println();
            printLine("Order remains at front of the queue");
        }
    }

    // TODO: Update inventory stocks
    public void updateInventory() {

    }

    // TODO: Display inventory information
    public void displayInventory() {

    }

    public void manageTransactions(TransactionList transactionList, Scanner input) {
        while(true){
            clearConsole();
            transactionList.displayTransactions();
            System.out.println();
            printLine("[1] Remove a Transaction by Order ID");
            printLine("[2] Remove Oldest Transaction");
            printLine("[3] Back to Admin Dashboard");
            System.out.println();
            System.out.print(padding() + "Select option: ");
            String choice = input.nextLine().trim();
            switch (choice) {
                case "1": {
                    if(transactionList.isEmpty()){
                        printLine("No transaction records to remove.");
                    }else {
                        System.out.print(padding() + "Enter Order ID to remove: ");
                        String idStr = input.nextLine().trim();
                        try {
                            int orderId = Integer.parseInt(idStr);
                            Order removed = transactionList.removeIdTransaction(orderId);
                            if(removed !=null){
                                printLine("Transaction #" + orderId + "removed.");
                            }else{
                                printLine("Transaction #" + orderId + " not found.");
                            }
                        }catch(NumberFormatException e){
                            printLine("Invalid Order ID.");
                        }
                    }
                    break;
                }
                case "2": {
                    Order removed = transactionList.removeTransaction();
                    if(removed !=null){
                        printLine("Removed oldest transaction (#" + removed.orderID + ").");
                    }else{
                        printLine("No transaction records to remove.");
                    }
                    break;
                }
                case "3": {
                    return;
                }
                default: {
                    printLine("Invalid option. Please try again.");
                }
            }
            System.out.println();
            System.out.print(padding() + "Press Enter to continue...");
            input.nextLine();
        }
    }
    public void manageInventory(Menu menu, Algorithm algorithm, Scanner input) {
        clearConsole();
        printLine("=".repeat(CONTENT_WIDTH));
        printCentered("INVENTORY MANAGEMENT");
        printLine("=".repeat(CONTENT_WIDTH));
        System.out.println();
        printLine("Inventory management module is being integrated by Member 2.");
        System.out.println();
        System.out.print(padding() + "Press Enter to return to Admin Dashboard...");
        input.nextLine();
    }
}
