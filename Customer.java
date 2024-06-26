import java.io.*;
import java.util.ArrayList;
import java.util.Objects;

public class Customer {
    private ShoppingCart shoppingCart;
    private String username;
    private String password;
    private String fileName;
    private ArrayList<String> purchasedProducts;

    public Customer(String username, String password) {
        this.shoppingCart = new ShoppingCart();
        this.username = username;
        this.password = password;
        this.fileName = username + " _info.txt";
        this.purchasedProducts = new ArrayList<>();
    }
    public static ArrayList<String> readStoreNamesFromFile(String username) {
        ArrayList<String> storeNames = new ArrayList<>();
        String fileName = username + "_stores.txt";
        try (BufferedReader reader = new BufferedReader(new FileReader(fileName))) {
            String storeName;
            while ((storeName = reader.readLine()) != null) {
                storeNames.add(storeName);
            }
        } catch (IOException e) {
            // Handle file reading errors, if any
            e.printStackTrace();
        }
        return storeNames;
    }

    public static ArrayList<Product> readProductsFullyFromFile(String storeName) {
        ArrayList<Product> productNames = new ArrayList<>();
        String[] details;
        String fileName = storeName + ".txt";
        try {
            BufferedReader reader = new BufferedReader(new FileReader(fileName));
            String line = null;

            while ((line = reader.readLine()) != null) {
                details = line.split(",");
                productNames.add(new Product(details[0], details[1], details[2], Double.parseDouble(details[3]), Integer.parseInt(details[4])));
            }

        } catch (IOException e) {
            // Handle file reading errors, if any
            return null;
        }
        return productNames;
    }

    public Customer(String username, String password, ShoppingCart shoppingCart) {
        this.shoppingCart = shoppingCart;
        this.username = username;
        this.password = password;
        this.fileName = username + " _info.txt";
        this.purchasedProducts = new ArrayList<>();
    }

    public ShoppingCart getShoppingCart() {
        return shoppingCart;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getFileName() {
        return fileName;
    }

    public ArrayList<String> getPurchasedProducts() {
        return purchasedProducts;
    }

    public void setShoppingCart(ShoppingCart shoppingCart) {
        this.shoppingCart = shoppingCart;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }
    public void saveFileInfo() {
        try {
            PrintWriter pw = new PrintWriter(new BufferedWriter(new FileWriter(fileName)));
            pw.println(shoppingCart.toString());
            pw.close();
        } catch(IOException e) {
            e.printStackTrace();
        }
    }
    public void addToShoppingCart(Product product) {
        shoppingCart.getListOfProducts().add(product);
        saveFileInfo();
    }

    public void checkout() {
        for (Product prod : shoppingCart.getListOfProducts()) {
            purchasedProducts.add(prod.getProductName() + " | " + prod.getQuantity());
        }
        for (int i = 0; i < shoppingCart.getSize(); i++) {
            this.shoppingCart.getListOfProducts().remove(shoppingCart.getListOfProducts().get(i));
        }
    }

    public void previouslyPurchasedFile() {
        try {
            String newFile = username + " _purchases.txt";
            PrintWriter pw = new PrintWriter(new BufferedWriter(new FileWriter((newFile), true)));
            //pw.println("Previously Purchased Items: ");
            for (String s : purchasedProducts) {
                pw.println(s);
            }
            pw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void saveShoppingCart() {
        File f = new File(this.fileName);
        try (PrintWriter pw = new PrintWriter(new FileWriter(f))) {
            for (Product p : this.shoppingCart.getListOfProducts()) {
                pw.println(p.listInShoppingCart());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
