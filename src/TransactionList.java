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

    public boolean isEmpty(){
        return head == null;
    }

    // TODO: Add completed transaction
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

    // TODO: Remove transaction record
    public Order removeTransaction() {
        if(head==null){
            return null;
        }
        Order removed = head.data;
        head = head.next;
        return removed;
    }

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

    // TODO: Traverse and display records
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
        }
    }
}
