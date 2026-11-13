import java.util.Scanner;
public class Customer {
    private String name;
    private String customerId;
    private String email;
    private String phone;
    private String address;


    public Customer(String name,  String customerId, String email, String address, String phone) {
        this.name = name;
        this.customerId = customerId;
        this.email = email;
        this.address = address;
        this.phone = phone;
    }

    public static Customer customerRegistration() {
        Scanner input = new Scanner(System.in);
        System.out.println("\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t============================");
        System.out.println("\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t    ACCOUNT REGISTRATION    ");
        System.out.println("\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t============================");

        System.out.print("\t\t\t\t\t\t\t\t\t\t\t\t\t\t\tEnter your full name: ");
        String name = input.nextLine();
        while (name.isEmpty()) {
            System.out.print("\t\t\t\t\t\t\t\t\t\t\t\t\t\t\tEnter your full name: ");
            name = input.nextLine();
        }
        System.out.print("\t\t\t\t\t\t\t\t\t\t\t\t\t\t\tEnter your email: ");
        String email = input.nextLine();
        while (email.isEmpty()) {
            System.out.print("\t\t\t\t\t\t\t\t\t\t\t\t\t\t\tEnter your email: ");
            email = input.nextLine();
        }
        System.out.print("\t\t\t\t\t\t\t\t\t\t\t\t\t\t\tEnter your phone number: ");
        String phone = input.nextLine();
        while (phone.isEmpty()) {
            System.out.print("\t\t\t\t\t\t\t\t\t\t\t\t\t\t\tEnter your phone number: ");
            phone = input.nextLine();
        }
        System.out.print("\t\t\t\t\t\t\t\t\t\t\t\t\t\t\tEnter your address: ");
        String address = input.nextLine();
        while (address.isEmpty()) {
            System.out.print("\t\t\t\t\t\t\t\t\t\t\t\t\t\t\tEnter your address: ");
            address = input.nextLine();
        }

        String customerId ="00" + (int) (Math.random() *10);

        System.out.println("\t\t\t\t\t\t\t\t\t\t\t\t\t\t\tSuccessfully created account!");
        return new Customer(name, customerId, email, address, phone);
    }

    public void customerDisplayProfile() {
        System.out.println("\n\n\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t============================");
        System.out.println("\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t      CUSTOMER PROFILE      ");
        System.out.println("\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t============================");
        System.out.println("\t\t\t\t\t\t\t\t\t\t\t\t\t\t\tCustomer ID : " + customerId);
        System.out.println("\t\t\t\t\t\t\t\t\t\t\t\t\t\t\tFull Name   : " + name);
        System.out.println("\t\t\t\t\t\t\t\t\t\t\t\t\t\t\tEmail       : " + email);
        System.out.println("\t\t\t\t\t\t\t\t\t\t\t\t\t\t\tAddress     : " + address);
        System.out.println("\t\t\t\t\t\t\t\t\t\t\t\t\t\t\tContact No. : " + phone);
    }

    public String getName() {
        return name;
    }
    public String getCustomerId() {
        return customerId;
    }
    public String getEmail() {
        return email;
    }
    public String getPhone() {
        return phone;
    }
    public String getAddress() {
        return address;
    }
    public void setName(String name) {
        this.name = name;
    }
    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }
    public void setEmail(String email) {
        this.email = email;
    }
    public void setPhone(String phone) {
        this.phone = phone;
    }
    public void setAddress(String address) {
        this.address = address;
    }
}