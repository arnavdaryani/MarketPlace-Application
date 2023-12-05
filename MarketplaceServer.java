import java.io.*;
import java.net.*;
import java.util.ArrayList;

import javax.swing.JOptionPane;

public class MarketplaceServer {

    public static ArrayList<Product> products = new ArrayList<>();
    public static final int PORT = 5000;
    public static File f = new File("users.txt");
    public static File p = new File("products.txt");
    public static Object user;
    public static Object gatekeeper;

    public static ArrayList<Product> readFromFile() {
        ArrayList<Product> availableProducts = new ArrayList<>();
        try (BufferedReader bfr = new BufferedReader(new FileReader(p))) {
            String line = bfr.readLine();
            while (line != null) {
                String[] productInfo = line.split(",");
                Product product = new Product(productInfo[0], productInfo[1], productInfo[2],
                        Double.parseDouble(productInfo[3]), Integer.parseInt(productInfo[4]));
                availableProducts.add(product);
                line = bfr.readLine();
            }
        } catch (IOException e) {
            System.out.println("No products are currently listed!\n");
        }
        return availableProducts;
    }

    public static ShoppingCart getUserShoppingCart(Customer customer) {
        ArrayList<Product> prods = new ArrayList<>();
        File customerFile = new File(customer.getFileName());
        try (BufferedReader bfr = new BufferedReader(new FileReader(customerFile))) {
            String line = bfr.readLine();
            if (line == null || line.isEmpty()) {
                return new ShoppingCart();
            }
            while (line != null) {
                String[] productInfo = line.split(",");
                Product prod = new Product(productInfo[0], productInfo[1], productInfo[2],
                        Double.parseDouble(productInfo[3]), Integer.parseInt(productInfo[4]));
                prods.add(prod);
                line = bfr.readLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new ShoppingCart(prods);
    }

    public static Product viewItemPage(String itemName, String store) {
        products = readFromFile();
        for (Product prod : products) {
            if (prod.getProductName().equalsIgnoreCase(itemName) && prod.getStoreName().equalsIgnoreCase(store)) {
                return prod;
            }
        }
        return null;
    }

    public static double shoppingCartTotal(Customer customer) {
        ShoppingCart sc = customer.getShoppingCart();
        double total = 0;
        if (sc.getListOfProducts().isEmpty()) {
            return total;
        }
        for (Product p : sc.getListOfProducts()) {
            total += p.getQuantity() * p.getPrice();
        }
        return total;
    }

    public static void saveProductsToFile() {
        try (PrintWriter pw = new PrintWriter(new FileWriter(p, true))) {
            for (Product prod : products) {
                pw.println(prod.listInShoppingCart());
                pw.flush();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static String viewPreviousPurchases(Customer customer) {
        String previousProducts = "";
        File previousPurchases = new File(customer.getUsername() + " _purchases.txt");
        try (BufferedReader bfr = new BufferedReader(new FileReader(previousPurchases))) {
            //bfr.readLine();
            String line = bfr.readLine();
            if (line == null || line.isEmpty()) {
            }
            while (line != null) {
                if (!(line.contains("Previously Purchased Items: "))) {
                    previousProducts = previousProducts + line + ",";
                }
                line = bfr.readLine();
            }
        } catch (IOException e) {
            System.out.println("You have no previous purchases!");
        }
        return previousProducts;
    }

    public static void main(String[] args) {
            try {
                ServerSocket ss = new ServerSocket(PORT);
                Socket socket = ss.accept();
                BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                PrintWriter writer = new PrintWriter(socket.getOutputStream(), true);

                BufferedReader userReader = new BufferedReader(new FileReader(f)); 
                PrintWriter userWriter = new PrintWriter(new FileWriter(f, true));

                // Login or Signup process
                String command = reader.readLine();
                String userInfo = null;
                if (command.equals("Login")) {
                    String username = reader.readLine();
                    String password = reader.readLine();

                    boolean loggedIn = false;
                    try (BufferedReader bfr = new BufferedReader(new FileReader("users.txt"))) {
                        String line;
                        while ((line = bfr.readLine()) != null) {
                            String[] parts = line.split(",");
                            if (parts[0].equals(username) && parts[1].equals(password)) {
                                loggedIn = true;
                                userInfo = line;
                                break;
                            }
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    if (loggedIn) {
                        writer.write("user found");
                        writer.println();
                        writer.flush();
                        writer.write(userInfo);
                        writer.println();
                        writer.flush();
                    } else {
                        writer.write("user not found");
                        writer.println();
                        writer.flush();
                    }
                    
                } else {
                    while (true) {
                        String username = reader.readLine();
                        synchronized (gatekeeper) {
                            String line = userReader.readLine();
                            while (line != null) {
                                String[] userData = line.split(",");
                                if (userData[0].equals(username)) {
                                    writer.write("true");
                                    writer.println();
                                    writer.flush();
                                    break;
                                }
                                line = userReader.readLine();
                            }
                        }
                        writer.write("false");
                        writer.println();
                        writer.flush();
                        break;
                    }

                    String userData = reader.readLine();
                    userWriter.write(userData);
                    userWriter.println();
                    userWriter.flush(); 
                }

                userReader.close();
                userWriter.close();

                String username = reader.readLine();
                String password = reader.readLine();
                String userType = reader.readLine();
                if (userType.equals("Customer")) {
                    user = new Customer(username, password);
                    synchronized (gatekeeper) {
                        ShoppingCart shoppingCart = getUserShoppingCart((Customer) user);
                        user = new Customer(username, password, shoppingCart);
                    }
                } else {
                    user = new Seller(username, password);
                }

                if (user instanceof Customer) {
                    // Customer processing
                    while (true) {
                        String selection = reader.readLine();
                        switch (selection) {
                            case "Search for a product":
                                synchronized (gatekeeper) {
                                    products = readFromFile();
                                }
                                String selectionSearch = reader.readLine();
                                if (selectionSearch.equals("Search for a product by store name")) {
                                    String nameOfIt = reader.readLine();
                                    ArrayList<Product> storeItems = Customer.readProductsFullyFromFile(nameOfIt);
                                    if (storeItems != null) {
                                        writer.write("Store found");
                                        writer.println();  
                                        writer.flush();
                                        String[] newAgain = new String[storeItems.size()];
                                        int go = 0;
                                        for (Product prod : storeItems) {
                                            newAgain[go] = prod.toString();
                                            go++;
                                        }
                                        String searchTerm = reader.readLine();
                                        ArrayList<String> correctOnes = new ArrayList<String>();
                                        for (Product lookInside : storeItems) {
                                            if (lookInside.getProductName().contains(searchTerm)) {
                                                correctOnes.add(lookInside.toString());
                                            }
                                        }
                                        String[] productToDisplay = new String[correctOnes.size()];
                                        int counters = 0;
                                        for (String i : correctOnes) {
                                            productToDisplay[counters] = i;
                                            counters++;
                                        }
                                        if (productToDisplay.length == 0) {
                                            writer.write("Products not found");
                                            writer.println();
                                            writer.flush();
                                        } else {
                                            writer.write("Products found");
                                            writer.println();
                                            writer.flush();
                                            writer.write(String.valueOf(productToDisplay.length));
                                            writer.println();
                                            writer.flush();
                                            for (String j : productToDisplay) {
                                                writer.write(j);
                                                writer.println();
                                                writer.flush();
                                            }
                                        }
                                    } else {
                                        writer.write("false");
                                        writer.println();
                                        writer.flush();
                                    }
                                } else {
                                    String searchTerm = reader.readLine();
                                    ArrayList<String> correctOnes = new ArrayList<String>();
                                    synchronized (gatekeeper) {
                                        for (Product lookInside : products) {
                                            if (lookInside.getProductName().contains(searchTerm)) {
                                                correctOnes.add(lookInside.toString());
                                            }
                                        }
                                    }
                                    String[] productToDisplay = new String[correctOnes.size()];
                                    int counters = 0;
                                    for (String i : correctOnes) {
                                        productToDisplay[counters] = i;
                                        counters++;
                                    }
                                    if (productToDisplay.length == 0) {
                                        writer.write("Products not found");
                                        writer.println();
                                        writer.flush();
                                    } else {
                                        writer.write("Products found");
                                        writer.println();
                                        writer.flush();
                                        writer.write(String.valueOf(productToDisplay.length));
                                        writer.println();
                                        writer.flush();
                                        for (String j : productToDisplay) {
                                            writer.write(j);
                                            writer.println();
                                            writer.flush();
                                        }
                                    }
                                }
                                synchronized (gatekeeper) {
                                    saveProductsToFile();
                                }
                                break;
                            case "Sort products by price":
                                String[] sortedProducts;
                                synchronized (gatekeeper) {
                                    products = readFromFile();
                                    products.sort(new SortByPrice());
                                    sortedProducts = new String[products.size()];
                                    int i = 0;
                                    for (Product prod : products) {
                                        sortedProducts[i] = prod.toString();
                                        i++;
                                    }
                                }
                                writer.write(String.valueOf(sortedProducts.length));
                                writer.println();
                                writer.flush();
                                for (String prod : sortedProducts) {
                                    writer.write(prod);
                                    writer.println();
                                    writer.flush();
                                }
                                synchronized (gatekeeper) {
                                    saveProductsToFile();
                                }
                                break;
                            case "Sort products by quantity available":
                                synchronized (gatekeeper) {
                                    products = readFromFile();
                                    products.sort(new SortByQuantity());
                                    sortedProducts = new String[products.size()];
                                    int i = 0;
                                    for (Product prod : products) {
                                        sortedProducts[i] = prod.toString();
                                        i++;
                                    }
                                }
                                writer.write(String.valueOf(sortedProducts.length));
                                writer.println();
                                writer.flush();
                                for (String prod : sortedProducts) {
                                    writer.write(prod);
                                    writer.println();
                                    writer.flush();
                                }
                                synchronized (gatekeeper) {
                                    saveProductsToFile();
                                }
                                break;
                            case "View product page to purchase":
                                String productName = reader.readLine();
                                String storeName = reader.readLine();
                                synchronized (gatekeeper) {
                                    Product product = viewItemPage(productName, storeName);
                                    if (product == null) {
                                        writer.write("Product not found");
                                        writer.println();
                                        writer.flush();
                                }   else {
                                        writer.write("Product found");
                                        writer.println();
                                        writer.flush();
                                        String page1 = product.returnProductPage();
                                        writer.write(page1);
                                        writer.println();
                                        writer.flush();
                                        String selection1 = reader.readLine();
                                        if (selection1.equals("Add to cart")) {
                                                int quantity = Integer.parseInt(reader.readLine());
                                                if (quantity > product.getQuantity()) {
                                                    writer.write("Quantity greater than available");
                                                    writer.println();
                                                    writer.flush();
                                                } else {
                                                    writer.write("Added to cart");
                                                    writer.println();
                                                    writer.flush();
                                                }
                                                synchronized (gatekeeper) {
                                                    ((Customer) user).getShoppingCart().addProduct(product, quantity);
                                                    ((Customer) user).saveShoppingCart();
                                                }
                                        } else {
                                            continue;
                                        }
                                    }
                                }
                                synchronized (gatekeeper) {
                                    saveProductsToFile();
                                }
                                break;
                            case "View shopping cart":
                                String productString = "";
                                int iiii = 0;
                                ArrayList<Product> list11;
                                synchronized (gatekeeper) {
                                    list11 = getUserShoppingCart((Customer) user).getListOfProducts();
                                }
                                String[] values1 = new String[list11.size()];
                                for (Product iii : list11) {
                                    productString = iii.getProductName();
                                    values1[iiii] = productString;
                                    iiii++;
                                }
                                writer.write(String.valueOf(shoppingCartTotal((Customer) user)));
                                writer.println();
                                writer.flush();
                                writer.write(String.valueOf(values1.length));
                                writer.println();
                                writer.flush();
                                for (String i : values1) {
                                    writer.write(i);
                                    writer.println();
                                    writer.flush();
                                }
                                synchronized (gatekeeper) {
                                    ((Customer) user).saveShoppingCart();
                                }
                                String decision = reader.readLine();
                                if (decision.equals("Checkout $" + shoppingCartTotal((Customer) user))) {
                                    synchronized (gatekeeper) {
                                        ((Customer) user).checkout();
                                        ((Customer) user).previouslyPurchasedFile();
                                        ((Customer) user).saveShoppingCart();
                                    }
                                }
                                if (decision.equals("Remove Item")) {
                                    String pickIt = reader.readLine();
                                    Product productToRemove1 = new Product(pickIt, "", "", 0, 0);
                                    synchronized (gatekeeper) {
                                        for (Product product111 : ((Customer) user).getShoppingCart().getListOfProducts()) {
                                            if (product111.getProductName().equalsIgnoreCase(pickIt)) {
                                                productToRemove1 = product111;
                                                ((Customer) user).getShoppingCart().deleteProduct(productToRemove1);
                                                ((Customer) user).previouslyPurchasedFile();
                                                ((Customer) user).saveShoppingCart();
                                                break;
                                            }
                                        }
                                    }
                                }
                                if (decision.equals("Go Back")) {
                                    synchronized (gatekeeper) {
                                        ((Customer) user).saveShoppingCart();
                                    }
                                    continue;
                                }
                                break;
                            case "View purchase history":
                                synchronized (gatekeeper) {
                                    String list = viewPreviousPurchases((Customer) user);
                                    String[] splitOnce = list.split(",");
                                    writer.write(String.valueOf(splitOnce.length));
                                    writer.println();
                                    writer.flush();
                                    for (String i : splitOnce) {
                                        writer.write(i);
                                        writer.println();
                                        writer.flush();
                                    }
                                }
                                break;
                            case "Export purchase history":
                                synchronized (gatekeeper) {
                                    ((Customer) user).previouslyPurchasedFile();
                                }
                                break;
                            case "Logout and Exit":
                                synchronized (gatekeeper) {
                                    ((Customer) user).saveShoppingCart();
                                    saveProductsToFile();
                                }
                    }
                }
            }
            } catch (IOException e) {
                e.printStackTrace();
            }

    }
    
}
