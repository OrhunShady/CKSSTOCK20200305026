import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;
import java.util.function.BiConsumer;

public class StockManagement {
    private static final String USERNAME = "Orhun";
    private static final String PASSWORD = "1234";
    private static final String PRODUCTS_FILE = "products.dat";

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        boolean loginSuccessful = false;

        do {
            loginSuccessful = login(scanner);

            if (!loginSuccessful) {
                System.out.println("Login failed. Please try again.");
            }
        } while (!loginSuccessful);

        System.out.println("Welcome to the ÇKS Stock Market.");

        List<Product<Double>> products = loadProducts();

        displayStockLevels(products);

        while (true) {
            System.out.println("\nSelect an operation:");
            System.out.println("1. Add Stock");
            System.out.println("2. Sell Stock");
            System.out.println("3. List Stock Levels");
            System.out.println("4. Add New Product");
            System.out.println("5. Update Price");
            System.out.println("6. Exit");

            int choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice) {
                case 1:
                    performStockOperation(products, "Add", (product, quantity) -> product.addStock(quantity));
                    break;
                case 2:
                    performStockOperation(products, "Sell", (product, quantity) -> product.sell(quantity));
                    break;
                case 3:
                    displayStockLevels(products);
                    break;
                case 4:
                    addNewProduct(products);
                    break;
                case 5:
                    updateProductPrice(products);
                    break;
                case 6:
                    saveProducts(products);
                    System.out.println("Exiting Stock Management System.");
                    System.exit(0);
                default:
                    System.out.println("Invalid choice. Please enter a valid option.");
            }
        }
    }

    private static boolean login(Scanner scanner) {
        System.out.println("Enter username:");
        String username = scanner.nextLine();

        System.out.println("Enter password:");
        String password = scanner.nextLine();

        return username.equals(USERNAME) && password.equals(PASSWORD);
    }

    private static void performStockOperation(List<Product<Double>> products, String operationType, BiConsumer<Product<Double>, Integer> operation) {
        Scanner scanner = new Scanner(System.in);

        System.out.println("Enter the product name:");
        String productName = scanner.nextLine();

        System.out.println("Enter the quantity:");
        int quantity = scanner.nextInt();
        scanner.nextLine();

        Optional<Product<Double>> foundProduct = products.stream()
                .filter(product -> product.getName().equalsIgnoreCase(productName))
                .findFirst();

        foundProduct.ifPresent(product -> {
            System.out.println("Performing " + operationType + " operation for " + productName);
            operation.accept(product, quantity);
        });

        if (!foundProduct.isPresent()) {
            System.out.println("Product not found.");
        }
    }

    private static void updateProductPrice(List<Product<Double>> products) {
        Scanner scanner = new Scanner(System.in);

        System.out.println("Enter the product name:");
        String productName = scanner.nextLine();

        System.out.println("Enter the new price for " + productName + ": $");
        double newPrice = scanner.nextDouble();
        scanner.nextLine();

        Optional<Product<Double>> foundProduct = products.stream()
                .filter(product -> product.getName().equalsIgnoreCase(productName))
                .findFirst();

        foundProduct.ifPresent(product -> {
            product.updatePrice(newPrice);
        });

        if (!foundProduct.isPresent()) {
            System.out.println("Product not found.");
        }
    }

    private static void displayStockLevels(List<Product<Double>> products) {
        System.out.println("\nCurrent Stock Levels:");
        products.forEach(product -> System.out.println(product.getName() + ": " + product.getStock() + " - Price: $" + product.getPrice()));
    }

    private static void addNewProduct(List<Product<Double>> products) {
        Scanner scanner = new Scanner(System.in);

        System.out.println("Enter the new product name:");
        String newProductName = scanner.nextLine();

        // Check if the product already exists
        if (products.stream().anyMatch(product -> product.getName().equalsIgnoreCase(newProductName))) {
            System.out.println("Product already exists.");
        } else {
            System.out.println("Enter the price for " + newProductName + ": $");
            double price = scanner.nextDouble();
            System.out.println("Enter the initial quantity for " + newProductName + ":");
            int quantity = scanner.nextInt();
            scanner.nextLine();

            Product<Double> newProduct = new Product<>(newProductName, price);
            newProduct.addStock(quantity);
            products.add(newProduct);
            System.out.println("New product added: " + newProductName);
        }
    }

    private static void saveProducts(List<Product<Double>> products) {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(PRODUCTS_FILE))) {
            oos.writeObject(products);
            System.out.println("Products saved to file.");
        } catch (IOException e) {
            System.out.println("Error saving products to file: " + e.getMessage());
        }
    }

    @SuppressWarnings("unchecked")
    private static List<Product<Double>> loadProducts() {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(PRODUCTS_FILE))) {
            return (List<Product<Double>>) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            return new ArrayList<>();
        }
    }
}