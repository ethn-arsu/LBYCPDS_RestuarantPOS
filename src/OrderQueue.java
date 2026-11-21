import java.time.format.DateTimeFormatter;

public class OrderQueue {

    Order[] queue;
    int front;
    int rear;
    int size;
    private static final int CONSOLE_WIDTH = 120;
    private static final int CONTENT_WIDTH = 60;
    private static final int DEFAULT_CAPACITY = 50;

    public OrderQueue() {
        initializeQueue();
    }

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

    public void initializeQueue() {
        queue = new Order[DEFAULT_CAPACITY];
        front = 0;
        rear = -1;
        size = 0;
    }

    public boolean isEmpty(){
        return size == 0;
    }

    public boolean isFull(){
        return size == queue.length;
    }
    public int getSize(){
        return size;
    }


    public void expandQueue(){
        Order[] newQueue = new Order[queue.length * 2];
        for(int i = 0; i < size; i++){
            newQueue[i] = queue[(front+1)%queue.length];
        }
        queue = newQueue;
        front = 0;
        rear = size - 1;
    }
    public void enqueue(Order order) {
        int no;
        if(order == null) {
            return;
        }
        if(isFull()){
            expandQueue();
        }
        rear = (rear + 1) % queue.length;
        queue[rear] = order;
        size++;
    }

    public Order dequeue() {
        if(isEmpty()){
            return null;
        }
        Order nextOrder= queue[front];
        queue[front] = null;
        front = (front + 1) % queue.length;
        size--;
        return nextOrder;
    }

    public Order peek() {
        if(isEmpty()){
            return null;
        }
        return queue[front];
    }

    public void displayQueue() {
        String border = "=".repeat(CONTENT_WIDTH);
        String separate = "-".repeat(CONTENT_WIDTH);

        printLine(border);
        printCentered("PENDING CUSTOMER ORDER QUEUE (FIFO)");
        printLine(border);
        System.out.println();

        if(isEmpty()){
            printCentered("No pending orders...");
            System.out.println();
            printLine(border);
            return;
        }

        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
        printLine("Total Pending Orders: " + size);
        printLine(separate);

        for(int i = 0; i < size; i++){
            int index = (front + i) % queue.length;
            Order order = queue[index];
            String tag = (i==0) ? " [NEXT ORDER TO PROCESS]" : "";
            String custName = (order.customer != null)? order.customer.getName(): "Guest";
            String timePlaced = (order.placedAt != null) ? dtf.format(order.placedAt) : "N/A";

            printLine(String.format("#%d | Order ID: %d%s", i+1, order.orderID, tag));
            printLine(String.format("    Customer: %-22s Time: %s", custName, timePlaced));

            for(int j=0;j<order.itemCount; j++){
                int itemIndex = order.selectedItems[j];
                if(itemIndex != -1){
                    printLine(String.format("      - %-34s x%02d", order.menu.foodNames[itemIndex], order.quantities[j]));
                }
            }
            printLine(String.format("    Total Amount: PHP %.2f", order.totalAmount));
            printLine(separate);
        }
    }

    // TODO: Check if queue is empty
    public void checkEmpty() {
        if(isEmpty()){
            printLine("Order queue is currently empty.");
        }else{
            printLine("Order queue has " + size + " pending  orders.");
        }
    }

    public int getReservedQuantity(int itemIndex){
        int reserved =0;
        for(int i=0;i<size;i++) {
            int index = (front + i) % queue.length;
            Order order = queue[index];
            for (int j = 0; j < order.itemCount; j++) {
                if (order.selectedItems[j] == itemIndex) {
                    reserved += order.quantities[j];
                }
            }
        }
        return reserved;
    }
}
