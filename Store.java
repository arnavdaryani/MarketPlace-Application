import java.io.*;
import java.util.ArrayList;
/**
 * A store class with store methods
 *
 * methods for sellers to add, modify, or delete stores via files
 *
 * @author Neha Jain
 *
 * @version November 12th, 2023
 *
 */

public class Store implements Serializable {
    private int storeId;
    private String storeName;
    private ArrayList<Product> products;
    public Store(String storeName) {
        this.storeName = storeName;
        this.products = new ArrayList<>();
    }

    public Store(String storeName, ArrayList<Product> products) {
        this.storeName = storeName;
        this.products = products;
    }

    public String getStoreName() {
        return storeName;
    }

    public void setStoreName(String storeName) {
        this.storeName = storeName;
    }

    public ArrayList<Product> getProducts() {
        return products;
    }

    public void addProduct(Product product) {
        products.add(product);
        saveProductsToFile(); // Save products to file after adding a new product
    }

    /**
     * this method will save products to the store file
     */
    private void saveProductsToFile() {
        try (PrintWriter writer = new PrintWriter(new FileWriter(storeName + ".txt"))) {
            for (Product product : products) {
                // Convert product to a formatted string and write it to the file
                String productString = product.getProductName() + "," + product.getDescription() + "," +
                        product.getQuantity() + "," + product.getPrice();
                writer.println(productString);
            }
        } catch (IOException e) {
            // Handle file write errors
        }
    }
}

