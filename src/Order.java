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

    // Select food items
    public void selectItems() {
        int itemCount = 0;
        boolean ordering = true;

        clearConsole();
        System.out.println("\n\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t========== ORDER TAKING ==========");
        menu.displayMenu(' ');

        while (ordering) {

            System.out.print("\n\t\t\t\t\t\t\t\t\t\t\t\t\t\t\tSelect category or X to finish: ");
            char categoryChoice =
                    input.next().toUpperCase().charAt(0);

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
                    System.out.println("\t\t\t\t\t\t\t\t\t\t\t\t\t\t\tInvalid category.");
                    continue;
            }

            // Display selected category
            clearConsole();
            menu.displayMenu(categoryChoice);

            while (true) {

                System.out.print("\n\t\t\t\t\t\t\t\t\t\t\t\t\t\t\tEnter food number, B to change category, or X to finish: ");

                String choice = input.next();

                if (choice.equalsIgnoreCase("B")) {
                    clearConsole();
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
                    System.out.println("\t\t\t\t\t\t\t\t\t\t\t\t\t\t\tInvalid food selection.");
                    continue;
                }

                int itemIndex = foodChoice - 1;

                if (itemIndex < 0 ||
                        itemIndex >= menu.foodNames.length) {

                    System.out.println("\t\t\t\t\t\t\t\t\t\t\t\t\t\t\tInvalid food selection.");
                }

                else if (!menu.categories[itemIndex].equals(selectedCategory)) {

                    System.out.println("\t\t\t\t\t\t\t\t\t\t\t\t\t\t\tPlease select an item from this category.");
                }

                else if (menu.stocks[itemIndex] <= 0) {
                    System.out.println("\t\t\t\t\t\t\t\t\t\t\t\t\t\t\tThis item is out of stock.");
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
                        System.out.println("\t\t\t\t\t\t\t\t\t\t\t\t\t\t\tItem already selected.");
                    }

                    else if (itemCount < selectedItems.length) {
                        selectedItems[itemCount] = itemIndex;
                        itemCount++;

                        System.out.println("\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t" + menu.foodNames[itemIndex] + " added to your order.");
                    }

                    else {
                        System.out.println("\t\t\t\t\t\t\t\t\t\t\t\t\t\t\tMaximum items reached.");
                    }
                }
            }
        }

        inputQuantity();
    }

    // Enter quantities
    public void inputQuantity() {

        clearConsole();
        System.out.println("\n\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t========== FOOD QUANTITY ==========");

        for (int i = 0; i < selectedItems.length; i++) {

            if (selectedItems[i] != -1) {
                int itemIndex = selectedItems[i];

                System.out.println("\n\t\t\t\t\t\t\t\t\t\t\t\t\t\t\tFood: " + menu.foodNames[itemIndex]
                );

                System.out.print("\t\t\t\t\t\t\t\t\t\t\t\t\t\t\tEnter quantity: ");
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

                totalAmount +=
                        menu.prices[itemIndex] * quantities[i];
            }
        }
    }

    // Display order summary
    public void displayOrderSummary() {

        clearConsole();
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("MM-dd-yyyy HH:mm:ss");
        String formattedDate = dtf.format(now);

        String custName = customer.getName();
        String custPhone = customer.getPhone();
        String custAddress = customer.getAddress();
        String custEmail = customer.getEmail();


        double totalVat = totalAmount - (totalAmount/1.12);

        System.out.println("\n\n\t\t\t\t\t\t\t\t\t\t\t\t=======================================================");
        System.out.println("\t\t\t\t\t\t\t\t\t\t\t\t                PINRITONG MANOK NG KENTUCKY            ");
        System.out.println("\t\t\t\t\t\t\t\t\t\t\t\t                     OFFICIAL E-RECEIPT                ");
        System.out.println("\t\t\t\t\t\t\t\t\t\t\t\t=======================================================");
        System.out.printf("\t\t\t\t\t\t\t\t\t\t\t\t%-24s %27s%n", "Date:\t", formattedDate);
        System.out.printf("\t\t\t\t\t\t\t\t\t\t\t\t%-24s %27s%n", "Customer:\t", custName);
        System.out.printf("\t\t\t\t\t\t\t\t\t\t\t\t%-24s %27s%n", "Contact No.:\t", custPhone);
        System.out.printf("\t\t\t\t\t\t\t\t\t\t\t\t%-24s %27s%n", "Email:\t", custEmail);
        System.out.printf("\t\t\t\t\t\t\t\t\t\t\t\t%-24s %27s%n", "Delivery Address:\t", custAddress);
        System.out.println("\t\t\t\t\t\t\t\t\t\t\t\t-------------------------------------------------------");
        System.out.printf("\t\t\t\t\t\t\t\t\t\t\t\t%-31s %6s %16s%n", "Description:" , "QTY" , "Price:");
        System.out.println("\t\t\t\t\t\t\t\t\t\t\t\t-------------------------------------------------------");

        boolean hasItems = false;

        for (int i = 0; i < selectedItems.length; i++) {
            if (selectedItems[i] != -1) {
                int itemIndex = selectedItems[i];
                hasItems = true;

                String itemName = menu.foodNames[itemIndex];
                String qty = String.format("t%02d", quantities[i]);
                double lineTotal = menu.prices[itemIndex] * quantities[i];
                String price = String.format("PHP %.2f", lineTotal);

                System.out.printf("\t\t\t\t\t\t\t\t\t\t\t\t%-31s %6s %16s%n", itemName, qty, price);
            }
        }

        if (!hasItems) {
            System.out.println("\t\t\t\t\t\t\t\t\t\t\t\t\t\t\tNo items ordered.");
        }

        System.out.println("\t\t\t\t\t\t\t\t\t\t\t\t-------------------------------------------------------");
        System.out.printf("\t\t\t\t\t\t\t\t\t\t\t\t%38s %16s%n" , "Total:" ,  String.format("PHP %.2f", totalAmount));
        System.out.printf("\t\t\t\t\t\t\t\t\t\t\t\t%38s %16s%n" ,  "Total VAT:" ,  String.format("PHP %.2f", totalVat));
        System.out.println("\t\t\t\t\t\t\t\t\t\t\t\t=======================================================");
        System.out.printf("\t\t\t\t\t\t\t\t\t\t\t\t                  4-1234-%d-1296%n", orderID);
        System.out.println("\t\t\t\t\t\t\t\t\t\t\t\t------------------------------------------------------");
    }
}