import javax.swing.*;
import java.io.*;
import java.util.ArrayList;

public class Product implements Comparable<Product> {
    private String productName;
    private String storeName;
    private String description;
    private int quantity;
    private double price;
    private int quantityInCart;
    private int quantitySold;
    private boolean inCart;
    private ArrayList<Customer> listOfCustomers;

    @Override
    public int compareTo(Product product) {
        return Double.compare(price, product.getPrice());
    }

    public Product(String productName, String storeName, String description, double price, int quantity) {
        this.productName = productName;
        this.storeName = storeName;
        this.description = description;
        this.price = price;
        this.quantity = quantity;
        this.inCart = false;
        this.quantityInCart = 0;
        this.quantitySold = 0;
        this.listOfCustomers = new ArrayList<Customer>();
    }

    public Product(String fileName) {
        listOfCustomers = new ArrayList<Customer>();
        try {
            FileReader readerOne = new FileReader(new File(fileName));
            BufferedReader bufferedOne = new BufferedReader(readerOne);
            String eachLine = bufferedOne.readLine();
            while (eachLine != null) {
                String[] productSplit = eachLine.split(",");
                new Product(productSplit[0], productSplit[1], productSplit[2],
                        Double.parseDouble(productSplit[3]), Integer.parseInt(productSplit[4]));
                eachLine = bufferedOne.readLine();
            }
            bufferedOne.close();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }
    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getStoreName() {
        return storeName;
    }

    public void setStoreName(String storeName) {
        this.storeName = storeName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public int getQuantityInCart() {
        return quantityInCart;
    }

    public void setQuantityInCart(int quantityInCart) {
        this.quantityInCart = quantityInCart;
    }

    public int getQuantitySold() {
        return quantitySold;
    }

    public void setQuantitySold(int quantitySold) {
        this.quantitySold = quantitySold;
    }

    public boolean isInCart() {
        return inCart;
    }

    public void setInCart(boolean inCart) {
        this.inCart = inCart;
    }

    public ArrayList<Customer> getListOfCustomers() {
        return listOfCustomers;
    }

    public void setListOfCustomers(ArrayList<Customer> listOfCustomers) {
        this.listOfCustomers = listOfCustomers;
    }

    public Product duplicate(Product p) {
        return new Product(p.getProductName(), p.getStoreName(), p.getDescription(), p.getPrice(), p.getQuantity());
    }
    public String listInShoppingCart() {
        return(String.format("%s,%s,%s,%.2f,%d", productName, storeName, description, price, quantity));
    }

    public void displayProductPage() {
        System.out.printf("Name: %s\nStore Name: %s\nDescription: %s\nPrice: %.2f\nQuantity Available: %d\n",
                productName, storeName, description, price, quantity);
        System.out.println();
    }
    public String returnProductPage() {
        return ("Name: " + productName + "\nStore Name: " + storeName + "\nDescription: " + description + "\nPrice: " + price + "\nQuantity Available: " + quantity);
    }
    public String toString() {
        return("Name: " + productName + "\nStore Name: " + storeName + "\nPrice: " + price + "\n");
    }

}
