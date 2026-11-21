import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;


public class TransactionList {

    private static final String RECEIPTS_FILE_PATH = "data/receipts.csv";
    private static final int CONSOLE_WIDTH = 120;
    private static final int CONTENT_WIDTH = 60;

    // Node structure for linked list
    static class Node {

        Order data;
        Node next;
        Node(Order data) {
            this.data = data;
            this.next = null;
        }
    }

    Node head;


    private static String getPadding() {
        int padding = Math.max(0, (CONSOLE_WIDTH - CONTENT_WIDTH) / 2);
        return " ".repeat(padding);
    }

    private static void printLine(String text) {
        System.out.println(getPadding() + text);
    }

    private static void printCentered(String text) {
        int padding = Math.max(0, (CONTENT_WIDTH - text.length()) / 2);
        System.out.println(getPadding() + " ".repeat(padding) + text);
    }

    // Check if the transaction list has no recorded order
    public boolean isEmpty(){
        return head == null;
    }

    // Append a completed order to the end of the linked list
    public void addTransaction(Order order) {
        if(order==null){
            return;
        }
        Node node = new Node(order);
        if(head == null){
            head = node;
            return;
        }
        Node curr = head;
        while(curr.next!=null){
            curr = curr.next;
        }
        curr.next = node;
    }

    // Remove and return the oldest transaction from the front of the list
    public Order removeTransaction() {
        if(head==null){
            return null;
        }
        Order removed = head.data;
        head = head.next;
        return removed;
    }

    // Remove and return the oldest transaction from its order ID
    public Order removeIdTransaction(int orderID) {
        if(head==null){
            return null;
        }
        if(head.data.orderID == orderID){
            Order removed = head.data;
            head = head.next;
            return removed;
        }
        Node curr = head;
        while(curr.next!=null){
            if(curr.next.data.orderID == orderID){
                Order removed = curr.next.data;
                curr.next = curr.next.next;
                return removed;
            }
            curr = curr.next;
        }
        return null;
    }

    // Traverse the linked list and display completed transactions
    public void displayTransactions() {
        String border = "=".repeat((CONTENT_WIDTH));
        String separate = "-".repeat((CONTENT_WIDTH));

        printLine(border);
        printCentered("COMPLETED TRANSACTION RECORDS");
        printLine(border);
        System.out.println();

        if(head==null){
            printCentered("No completed transactions");
            System.out.println();
            printLine(border);
            return;
        }

        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        Node curr = head;
        int count = 0;

        while(curr!=null){
            count++;
            Order order = curr.data;
            String custName = (order.customer != null) ? order.customer.getName() : "Guest";
            String completeTime = (order.completedAt !=null) ? dtf.format(order.completedAt) : "N/A";

            printLine(String.format("[%d] Order ID: %d | Customer: %s", count, order.orderID, custName));
            printLine("   Completed: " + completeTime + (order.hasPwdDiscount ? " (PWD Discount)" : ""));

            for(int i=0; i<order.itemCount; i++){
                int itemIndex = order.selectedItems[i];
                if(itemIndex != -1){
                    double lineTotal = order.menu.prices[itemIndex] * order.quantities[i];
                    printLine(String.format("      - %-30s x%02d  PHP %8.2f", order.menu.foodNames[itemIndex], order.quantities[i], lineTotal));
                }
            }
            printLine(String.format("    Total Paid: PHP %.2f", order.totalAmount));
            printLine(separate);
            curr = curr.next;
        }
    }
   private String csvValue(String value){
        if(value==null){
            return "";
        }
        return "\""+value.replace("\"", "\"\"") + "\"";
   }

    // Save completed order e-receipt to csv file
   public void saveReceiptToCSV(Order order){
        if(order==null){
            return;
        }
        Path path = Paths.get(RECEIPTS_FILE_PATH);

        try{
            if(path.getParent() != null){
                Files.createDirectories(path.getParent());
            }
            boolean writeHeader = !Files.exists(path) || Files.size(path) ==0;
            LocalDateTime timeStamp = (order.completedAt != null) ? order.completedAt : LocalDateTime.now();
            DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            String formattedDate = dtf.format(timeStamp);

            String custId = (order.customer != null) ? order.customer.getCustomerId() : "GUEST";
            String custName = (order.customer != null) ? order.customer.getName() : "Guest";

            StringBuilder sb = new StringBuilder();
            for(int i=0; i<order.itemCount; i++){
                int itemIndex = order.selectedItems[i];
                if(itemIndex != -1){
                    if(sb.length()>0){
                        sb.append("; ");
                    }
                    sb.append(order.menu.foodNames[itemIndex]).append(" x").append(order.quantities[i]);
                }
            }
            try (BufferedWriter writer = Files.newBufferedWriter(path, StandardCharsets.UTF_8, StandardOpenOption.CREATE, StandardOpenOption.APPEND)){
                if(writeHeader){
                    writer.write("orderId,dateTime,customerId,customerName,item,subtotal,pwdDiscount,vat,totalAmount");
                    writer.newLine();
                }
                String record = String.join(",",
                        csvValue(String.valueOf(order.orderID)),
                        csvValue(formattedDate),
                        csvValue(custId),
                        csvValue(custName),
                        csvValue(sb.toString()),
                        csvValue(String.format("%.2f", order.subtotal)),
                        csvValue(String.format("%.2f", order.totalDiscount)),
                        csvValue(String.format("%.2f", order.totalVat)),
                        csvValue(String.format("%.2f", order.totalAmount)));

                writer.write(record);
                writer.newLine();
            }
        }catch(IOException e){
            printLine("Unable to save receipt to CSV (" + e.getMessage() + ").");
        }
   }
}
