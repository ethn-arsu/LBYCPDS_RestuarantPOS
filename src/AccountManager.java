import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;

public class AccountManager {

    private static final String FILE_PATH = "data/customers.csv";
    private static final int ITERATIONS = 120000;
    private static final int KEY_LENGTH = 256;

    // Arrays for customer account records
    private Customer[] customers = new Customer[10];
    private String[] passwordHashes = new String[10];
    private String[] passwordSalts = new String[10];

    private int accountCount = 0;

    private final SecureRandom random = new SecureRandom();

    // Constructor: load existing accounts
    public AccountManager() {
        loadAccounts();
    }

    // Increase array capacity when full
    private void expandArrays() {

        int newSize = customers.length * 2;

        Customer[] newCustomers = new Customer[newSize];
        String[] newHashes = new String[newSize];
        String[] newSalts = new String[newSize];

        for (int i = 0; i < accountCount; i++) {
            newCustomers[i] = customers[i];
            newHashes[i] = passwordHashes[i];
            newSalts[i] = passwordSalts[i];
        }

        customers = newCustomers;
        passwordHashes = newHashes;
        passwordSalts = newSalts;
    }

    // Linear search for an email address
    public int linearSearch(String email) {

        for (int i = 0; i < accountCount; i++) {

            if (customers[i].getEmail()
                    .equalsIgnoreCase(email.trim())) {

                return i;
            }
        }

        return -1;
    }

    // Check for existing email
    public boolean accountExists(String email) {
        return linearSearch(email) != -1;
    }

    // Generate a sequential customer ID
    private String generateCustomerId() {

        int highestId = 0;

        for (int i = 0; i < accountCount; i++) {

            try {
                int id = Integer.parseInt(
                        customers[i].getCustomerId()
                );

                if (id > highestId) {
                    highestId = id;
                }

            } catch (NumberFormatException e) {
                // Ignore invalid IDs
            }
        }

        return String.format("%05d", highestId + 1);
    }

    // Create a salted password hash
    private String hashPassword(
            String password, byte[] salt) {

        PBEKeySpec spec = new PBEKeySpec(
                password.toCharArray(),
                salt,
                ITERATIONS,
                KEY_LENGTH
        );

        try {
            SecretKeyFactory factory =
                    SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");

            byte[] hash = factory
                    .generateSecret(spec)
                    .getEncoded();

            return Base64.getEncoder()
                    .encodeToString(hash);

        } catch (Exception e) {
            throw new IllegalStateException("Password hashing failed.", e);

        } finally {
            spec.clearPassword();
        }
    }

    // Register a new customer
    public boolean registerAccount(
            Customer customer, String password) {

        if (customer == null ||
                customer.getEmail() == null ||
                customer.getEmail().isBlank() ||
                password == null ||
                password.isBlank()) {

            return false;
        }

        if (accountExists(customer.getEmail())) {
            return false;
        }

        if (accountCount == customers.length) {
            expandArrays();
        }

        String oldId = customer.getCustomerId();
        customer.setCustomerId(generateCustomerId());

        byte[] salt = new byte[16];
        random.nextBytes(salt);

        String encodedSalt = Base64.getEncoder().encodeToString(salt);
        String passwordHash = hashPassword(password, salt);

        customers[accountCount] = customer;
        passwordHashes[accountCount] = passwordHash;
        passwordSalts[accountCount] = encodedSalt;

        accountCount++;

        try {
            saveAccounts();
            return true;

        } catch (IOException e) {

            // Undo insertion if saving fails
            accountCount--;

            customers[accountCount] = null;
            passwordHashes[accountCount] = null;
            passwordSalts[accountCount] = null;

            customer.setCustomerId(oldId);

            System.out.println("Unable to save the customer account.");

            return false;
        }
    }

    // Log in to an existing account
    public Customer login(
            String email, String password) {

        int index = linearSearch(email);

        if (index == -1 || password == null) {
            return null;
        }

        try {
            byte[] salt = Base64.getDecoder()
                    .decode(passwordSalts[index]);

            String enteredHash = hashPassword(
                    password, salt
            );

            if (java.security.MessageDigest.isEqual(
                    Base64.getDecoder().decode(enteredHash),
                    Base64.getDecoder().decode(
                            passwordHashes[index]))) {

                return customers[index];
            }

        } catch (IllegalArgumentException e) {
            return null;
        }

        return null;
    }

    // Escape a value for CSV storage
    private String csvValue(String value) {

        if (value == null) {
            value = "";
        }

        return "\"" + value.replace("\"", "\"\"") + "\"";
    }

    // Parse a CSV record
    private List<String> parseCSV(String line) {

        List<String> fields = new ArrayList<>();
        StringBuilder field = new StringBuilder();

        boolean quoted = false;

        for (int i = 0; i < line.length(); i++) {

            char ch = line.charAt(i);

            if (ch == '"') {

                if (quoted && i + 1 < line.length()
                        && line.charAt(i + 1) == '"') {

                    field.append('"');
                    i++;

                } else {
                    quoted = !quoted;
                }

            } else if (ch == ',' && !quoted) {

                fields.add(field.toString());
                field.setLength(0);

            } else {
                field.append(ch);
            }
        }

        fields.add(field.toString());
        return fields;
    }

    // Save customer records to CSV
    public void saveAccounts() throws IOException {

        Path path = Paths.get(FILE_PATH);

        Files.createDirectories(path.getParent());

        try (BufferedWriter writer =
                     Files.newBufferedWriter(
                             path,
                             StandardCharsets.UTF_8)) {

            writer.write("customerId,name,email,phone,address,salt,passwordHash");
            writer.newLine();

            for (int i = 0; i < accountCount; i++) {

                Customer c = customers[i];

                String record = String.join(",",
                        csvValue(c.getCustomerId()),
                        csvValue(c.getName()),
                        csvValue(c.getEmail()),
                        csvValue(c.getPhone()),
                        csvValue(c.getAddress()),
                        csvValue(passwordSalts[i]),
                        csvValue(passwordHashes[i])
                );

                writer.write(record);
                writer.newLine();
            }
        }
    }

    // Load saved accounts from CSV
    public void loadAccounts() {

        Path path = Paths.get(FILE_PATH);

        if (!Files.exists(path)) {
            return;
        }

        try (BufferedReader reader =
                     Files.newBufferedReader(
                             path,
                             StandardCharsets.UTF_8)) {

            String line = reader.readLine(); // Header
            int lineNumber = 1;

            while ((line = reader.readLine()) != null) {

                lineNumber++;

                if (line.isBlank()) {
                    continue;
                }

                List<String> data = parseCSV(line);

                if (data.size() != 7) {
                    throw new IOException("Invalid CSV record at line " + lineNumber);
                }

                if (accountCount == customers.length) {
                    expandArrays();
                }

                Customer customer = new Customer(
                        data.get(1),
                        data.get(0),
                        data.get(2),
                        data.get(4),
                        data.get(3)
                );

                customers[accountCount] = customer;
                passwordSalts[accountCount] = data.get(5);
                passwordHashes[accountCount] = data.get(6);

                accountCount++;
            }

        } catch (IOException e) {
            throw new IllegalStateException("Unable to load customer accounts.", e);
        }
    }
}