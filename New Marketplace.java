import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.Socket;
import java.nio.Buffer;
import java.util.ArrayList;
import java.util.Scanner;

public class Marketplace extends JComponent implements Runnable{
    // STILL NEED TO MAKE SURE THE USER CAN PRESS CANCEL OR RED X BUTTON AT ANY TIME DURING THE PROGRAM
    public static ArrayList<Product> products = new ArrayList<>();
    private String userType;
    public static File f = new File("users.txt");
    public static File p = new File("products.txt");

    public static void main(String[] args) {
        Thread[] threads = new Thread[10];
        /*for (int i = 0; i < 10; i++) {
            threads[i] = new Thread(new Marketplace());
            threads[i].start();
        }*/
        new Marketplace().run();
    }

    public static String createUser(String userType, BufferedReader reader, PrintWriter writer) {  
        String line = null;
        String username;
        String password;
        String confirmPassword;

        do {
            username = JOptionPane.showInputDialog(null, "Enter username:","Marketplace", JOptionPane.QUESTION_MESSAGE);
            writer.write(username);
            writer.println();
            writer.flush();
            String taken = "";
            try {
                taken = reader.readLine();
            } catch (IOException e) {
                e.printStackTrace();
            }

            if (taken.equals("true")) {
                JOptionPane.showMessageDialog(null, "This username is already taken. Please try again!",
                        "Marketplace", JOptionPane.INFORMATION_MESSAGE);
            } else {
                break;
            }
    
        } while (true);

        do {
            password = JOptionPane.showInputDialog(null, "Enter password:",
                    "Marketplace", JOptionPane.QUESTION_MESSAGE);
            confirmPassword = JOptionPane.showInputDialog(null, "Please re-enter your password:",
                    "Marketplace", JOptionPane.QUESTION_MESSAGE);
            if (!password.equals(confirmPassword)) {
                JOptionPane.showMessageDialog(null, "Your passwords did not match. Please try again!",
                        "Marketplace", JOptionPane.INFORMATION_MESSAGE);
            }
        } while (!password.equals(confirmPassword));
        JOptionPane.showMessageDialog(null, "Your account was successfully created!");
        if (userType.equals("Customer")) {
            line = String.format("%s,%s,%s", username, password, "Customer");
            writer.write(line);
            writer.println();
            writer.flush();
        } else {
            line = String.format("%s,%s,%s", username, password, "Seller");
            writer.write(line);
            writer.println();
            writer.flush();
        }

        return line;
    } 

    public static String login(BufferedReader reader, PrintWriter writer) {
        boolean loggedIn = false;
        String line = null;
        do {
            try (BufferedReader bfr = new BufferedReader(new FileReader("users.txt"))) {
                String username = JOptionPane.showInputDialog(null, "Enter your username:");
                String password = JOptionPane.showInputDialog(null, "Enter your password:");

                writer.write(username);
                writer.println();
                writer.flush();
                writer.write(password);
                writer.println();
                writer.flush();

                String userFound = reader.readLine();

                if (userFound.equals("user not found")) {
                    JOptionPane.showMessageDialog(null, "Either your username or password is wrong, " +
                            "or there is no account associated with this username!", "Marketplace", JOptionPane.ERROR_MESSAGE);
                } else {
                    loggedIn = true;
                    line = reader.readLine();
                    JOptionPane.showMessageDialog(null,"Success! You are now logged in!",
                                    "Marketplace", JOptionPane.INFORMATION_MESSAGE);
                    break;
                }
            } catch (IOException e) {
                JOptionPane.showMessageDialog(null,
                        "Please create an account first!", "Marketplace", JOptionPane.ERROR_MESSAGE);
                break;
            }
        } while (!loggedIn);

        return line;
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
        }
        return new ShoppingCart(prods);
    }

    
    public static void addStoreToSellerFile(String storeName, Seller seller) {
        try (PrintWriter pw = new PrintWriter(new FileWriter(seller.getFileName(), true))) {
            pw.println(storeName);
            seller.getStores().add(storeName);
            //  storeNames.add(storeName);

            File storeFile = new File(storeName + ".txt");
            storeFile.createNewFile();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    
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
            JOptionPane.showMessageDialog(null, "No products are currently listed!",
                    "Marketplace", JOptionPane.ERROR_MESSAGE);
        }
        return availableProducts;
    }

    public static void addProductToStore(String storeName, Seller seller) {
        try (PrintWriter pw = new PrintWriter(new FileWriter(storeName + ".txt", true), true)) {
            String productDetails = JOptionPane.showInputDialog(null, "Enter product details separated by commas (name,store name,description,price,quantity):",
                    "Marketplace", JOptionPane.QUESTION_MESSAGE);
            pw.println(productDetails);
            JOptionPane.showMessageDialog(null, "Product added successfully!",
                    "Marketplace", JOptionPane.INFORMATION_MESSAGE);
            String[] detailsArray = productDetails.split(",");
            if (detailsArray.length == 5) {
                String name = detailsArray[0];
                String storeNames = detailsArray[1];
                String description = detailsArray[2];
                double price = Double.parseDouble(detailsArray[3]);
                int quantity = Integer.parseInt(detailsArray[4]);
                Product newProduct = new Product(name, storeNames, description, price, quantity);
                products.add(newProduct);
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "Error occurred while adding the product.",
                    "Marketplace", JOptionPane.ERROR_MESSAGE);
        }
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

    public static ArrayList<Product> searchForStore(String store) {
        ArrayList<Product> searchResults = new ArrayList<>();
        for (Product prod : products) {
            if (prod.getStoreName().equalsIgnoreCase(store)) {
                searchResults.add(prod);
            }
        }
        return searchResults;
    }
    public static ArrayList<Product> searchForItemInStore(ArrayList<Product> firstList, String itemSearch) {
        ArrayList<Product> searchResults = new ArrayList<>();
        if (firstList.isEmpty()) {
            JOptionPane.showMessageDialog(null, "There are no items to search from!",
                    "Marketplace", JOptionPane.ERROR_MESSAGE);
        } else {
            for (Product prod : products) {
                if (prod.getProductName().contains(itemSearch) || prod.getDescription().contains(itemSearch)
                        || prod.getStoreName().contains(itemSearch)) {
                    searchResults.add(prod);
                }
            }
        }
        return searchResults;
    }



    public void run() {
        BufferedReader reader = null;
        PrintWriter writer = null; 
        try  {
            Socket socket = new Socket("localhost", 5000);
            reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            writer = new PrintWriter(socket.getOutputStream(), true);

            Object user = null;
            String username = "";
            String password = "";
            Font customFont = new Font("Arial", Font.BOLD, 24);
            JLabel label = new JLabel("Welcome to Marketplace!");
            label.setFont(customFont);
            //Image icon = new BufferedImage(1, 1, BufferedImage.TYPE_INT_ARGB_PRE);
            // IS THERE A WAY TO GET RID OF THE ICON?
            BufferedImage transparentImage = new BufferedImage(1, 1, BufferedImage.TYPE_INT_ARGB_PRE);
            ImageIcon icon = new ImageIcon(transparentImage);

            JOptionPane.showMessageDialog(null, label, "Marketplace", JOptionPane.INFORMATION_MESSAGE, icon);


            // Ask the user to choose between logging in or creating an account
            String[] options = {"Login", "Create Account"};
            int choice = JOptionPane.showOptionDialog(null,
                    "Would you like to login or create an account?",
                    "Marketplace", JOptionPane.YES_NO_OPTION,
                    JOptionPane.QUESTION_MESSAGE, null, options, options[0]);

            // Process the user's choice
            if (choice == JOptionPane.YES_OPTION) {
                writer.write("Login");
                writer.println();
                writer.flush();
                String userInfo = login(reader, writer);
                String[] userData = userInfo.split(",");
                username = userData[0];
                password = userData[1];
                writer.write(username);
                writer.println();
                writer.flush();
                writer.write(password);
                writer.println();
                writer.flush();
                writer.write(userData[2]);
                writer.println();
                writer.flush();               
                if (userData[2].equals("Customer")) {
                    user = new Customer(username, password);
                    ShoppingCart shoppingCart = getUserShoppingCart((Customer) user);
                    user = new Customer(username, password, shoppingCart);
                } else {
                    user = new Seller(username, password);
                }
            } else if (choice == JOptionPane.NO_OPTION) {
                writer.write("Signup");
                writer.println();
                writer.flush();
                String[] custOrSell = {"Customer", "Seller"};
                userType = (String) JOptionPane.showInputDialog(null,"Are you a customer or seller?", "Marketplace",
                            JOptionPane.PLAIN_MESSAGE, null, custOrSell, null);
                String userInfo = createUser(userType, reader, writer);
                String[] userData = userInfo.split(",");
                if (userData.length >= 2) {
                    username = userData[0];
                    password = userData[1];
                    writer.write(username);
                    writer.println();
                    writer.flush();
                    writer.write(password);
                    writer.println();
                    writer.flush();
                    writer.write(userData[2]);
                    writer.println();
                    writer.flush();
                    if (userData[2].equals("Customer")) {
                        user = new Customer(username, password);
                        ShoppingCart shoppingCart = getUserShoppingCart((Customer) user);
                        user = new Customer(username, password, shoppingCart);
                    } else {
                        user = new Seller(username, password);
                    }
                } else {
                    JOptionPane.showMessageDialog(null, "Invalid user data format.", "Marketplace", JOptionPane.ERROR_MESSAGE);
                }
            } else {
                return;
            }
             if (user instanceof Customer) {
            // customer implementation
            user = new Customer(username, password);
            writer.write("Customer");
            writer.println();
            writer.flush();
            boolean continueNow = true;
            while (continueNow) {
                String[] customerMenu = {"Search for a product", "Sort products by price",
                        "Sort products by quantity available", "View shopping cart",
                        "View product page to purchase", "View purchase history",
                        "Export purchase history", "Logout and Exit"};
                String selection = (String) JOptionPane.showInputDialog(null, "Pick Below", "Marketplace",
                        JOptionPane.PLAIN_MESSAGE, null, customerMenu, null);
                switch (selection) {
                    case "Search for a product":
                        writer.write("Search for a product");
                        writer.println();
                        writer.flush();
                        products = readFromFile();
                        String[] selectionMenu = {"Search for a product by store name", "Search all products"};
                        String selectionSearch = (String) JOptionPane.showInputDialog(null, "Pick an Option Below", "Marketplace",
                                JOptionPane.PLAIN_MESSAGE, null, selectionMenu, null);
                        writer.write(selectionSearch);
                        writer.println();
                        writer.flush();
                        if (selectionSearch.equals("Search for a product by store name")) {
                            String nameOfIt = JOptionPane.showInputDialog(null, "What is the EXACT name of the store?",
                                    "Marketplace", JOptionPane.QUESTION_MESSAGE);
                            writer.write(nameOfIt);
                            writer.println();
                            writer.flush();
                            if (reader.readLine().equals("Store found")) {
                                String searchTerm = JOptionPane.showInputDialog(null, "What would you like to search",
                                        "Marketplace", JOptionPane.QUESTION_MESSAGE);
                                writer.write(searchTerm);
                                writer.println();
                                writer.flush();
                                if (reader.readLine().equals("Products not found")) {
                                    JOptionPane.showMessageDialog(null, "No products contained that search term!",
                                            "Marketplace", JOptionPane.ERROR_MESSAGE);
                                } else {
                                    int length = Integer.parseInt(reader.readLine());
                                    String[] productToDisplay = new String[length];
                                    for (int i = 0; i < length; i++) {
                                        productToDisplay[i] = reader.readLine();
                                    }
                                    JOptionPane.showInputDialog(null, "Products containing the search term", "Marketplace",
                                            JOptionPane.PLAIN_MESSAGE, null, productToDisplay, null);
                                }
                            } else {
                                JOptionPane.showMessageDialog(null, "There is no store with this name",
                                        "Marketplace", JOptionPane.ERROR_MESSAGE);
                            }
                        } else {
                            writer.write("Search all products");
                            writer.println();
                            writer.flush();
                            String searchTerm = JOptionPane.showInputDialog(null, "What would you like to search",
                                    "Marketplace", JOptionPane.QUESTION_MESSAGE);
                            writer.write(searchTerm);
                            writer.println();
                            writer.flush();

                            // Check if the productToDisplay array is empty before displaying
                            if (reader.readLine().equals("Products not found")) {
                                JOptionPane.showMessageDialog(null, "No products contained that search term!",
                                        "Marketplace", JOptionPane.ERROR_MESSAGE);
                            } else {
                                int length = Integer.parseInt(reader.readLine());
                                String[] productToDisplay = new String[length];
                                for (int i = 0; i < length; i++) {
                                    productToDisplay[i] = reader.readLine();
                                }
                                JOptionPane.showInputDialog(null, "Products containing the search term", "Marketplace",
                                        JOptionPane.PLAIN_MESSAGE, null, productToDisplay, null);
                            }
                        }
                        break;
                    case "Sort products by price":
                        writer.write("Sort products by price");
                        writer.println();
                        writer.flush();

                        int length = Integer.parseInt(reader.readLine());
                        String[] sortedProducts = new String[length];
                        for (int i = 0; i < length; i++) {
                            sortedProducts[i] = reader.readLine();
                        }   
                        JOptionPane.showInputDialog(null, "Sorted by Price", "Marketplace",
                                JOptionPane.PLAIN_MESSAGE, null, sortedProducts, null);
                        break;
                    case "Sort products by quantity available":
                        writer.write("Sort products by quantity");
                        writer.println();
                        writer.flush();

                        length = Integer.parseInt(reader.readLine());
                        sortedProducts = new String[length];
                        for (int i = 0; i < length; i++) {
                            sortedProducts[i] = reader.readLine();
                        } 
                        JOptionPane.showInputDialog(null, "Sorted by Quantity Available", "Marketplace",
                                JOptionPane.PLAIN_MESSAGE, null, sortedProducts, null);
                        break;
                    case "View product page to purchase":
                        writer.write("View product page to purchase");
                        writer.println();
                        writer.flush();
                        String nameOfIt = JOptionPane.showInputDialog(null, "Enter the EXACT name of the product.",
                                "Marketplace", JOptionPane.QUESTION_MESSAGE);
                        String nameOfIt1 = JOptionPane.showInputDialog(null, "Enter the EXACT store name of the product.",
                                "Marketplace", JOptionPane.QUESTION_MESSAGE);
                        writer.write(nameOfIt);
                        writer.println();
                        writer.flush();
                        writer.write(nameOfIt1);
                        writer.println();
                        writer.flush();
                        if (reader.readLine().equals("Product not found")) {
                            JOptionPane.showMessageDialog(null, "The item cannot be found",
                                    "Marketplace", JOptionPane.INFORMATION_MESSAGE);
                            break;
                        }
                        String[] cartOption = {"Add to cart", "Go Back"};
                        String page1 = reader.readLine();
                        String selection1 = (String) JOptionPane.showInputDialog(null, page1, "Marketplace",
                                JOptionPane.PLAIN_MESSAGE, null, cartOption, null);
                        writer.write(selection1);
                        writer.println();
                        writer.flush();
                        int numbers1;
                        switch (selection1) {
                            case "Add to cart":
                                String numbers = JOptionPane.showInputDialog(null, "Enter the quantity of the product you would like to purchase.",
                                        "Marketplace", JOptionPane.QUESTION_MESSAGE);
                                try {
                                    numbers1 = Integer.parseInt(numbers);
                                    writer.write(String.valueOf(numbers1));
                                    writer.println();
                                    writer.flush();
                                } catch (Exception e) {
                                    JOptionPane.showMessageDialog(null, "Please enter an integer",
                                            "Marketplace", JOptionPane.ERROR_MESSAGE);
                                }
                                if (reader.readLine().equals("Quantity greater than available")) {
                                    JOptionPane.showMessageDialog(null, "The quantity you would like to purchase is greater than the current value. Please try again!",
                                            "Marketplace", JOptionPane.ERROR_MESSAGE);
                                    return;
                                }
                                JOptionPane.showMessageDialog(null, "The product was added!",
                                        "Marketplace", JOptionPane.INFORMATION_MESSAGE);
                            case "Go Back":
                                break;
                        }
                        break;
                    case ("View shopping cart"):
                        writer.write("View shopping cart");
                        writer.println();
                        writer.flush();
                        double total = Double.parseDouble(reader.readLine());
                        String[] values2 = {"Checkout $" + total, "Remove Item", "Go Back"};
                        length = Integer.parseInt(reader.readLine());
                        String[] values1 = new String[length];
                        for (int i = 0; i < length; i++) {
                            values1[i] = reader.readLine();
                        }
                        JOptionPane.showInputDialog(null,
                                "The Shopping Cart", "",
                                JOptionPane.PLAIN_MESSAGE, null, values1, null);
                        String decision = (String) JOptionPane.showInputDialog(null,
                                "Options", "",
                                JOptionPane.PLAIN_MESSAGE, null, values2, null);
                        if (decision == null) {
                            return;
                        }
                        writer.write(decision);
                        writer.println();
                        writer.flush();
                        if (decision.equals("Remove Item")) {
                            String pickIt = (String) JOptionPane.showInputDialog(null,
                                    "Pick the item you want to remove", "",
                                    JOptionPane.PLAIN_MESSAGE, null, values1, null);
                            writer.write(pickIt);
                            writer.println();
                            writer.flush();
                        }
                        if (decision.equals("Go Back")) {
                           continue; 
                        }
                        break;
                    case "View purchase history":
                        writer.write("View purchase history");
                        writer.println();
                        writer.flush();
                        
                        length = Integer.parseInt(reader.readLine());
                        String[] splitOnce = new String[length];
                        for (int i = 0; i < length; i++) {
                            splitOnce[i] = reader.readLine();
                        }
                        JOptionPane.showInputDialog(null,
                                "The Purchase History (name then quantity)", "",
                                JOptionPane.PLAIN_MESSAGE, null, splitOnce, null);
                        break;
                    case "Export purchase history":
                        writer.write("Export purchase history");
                        writer.println();
                        writer.flush();

                        JOptionPane.showMessageDialog(null, 
                        "Your purchase history has been exported to the file <your username> _purchases.txt!",
                                "Marketplace", JOptionPane.INFORMATION_MESSAGE);
                        break;
                    case ("Logout and Exit"):
                        writer.write("Logout and Exit");
                        writer.println();
                        writer.flush();
                        JOptionPane.showMessageDialog(null, "Thank you for using the marketplace!",
                                "Marketplace", JOptionPane.INFORMATION_MESSAGE);
                        continueNow = false;
                        return;
                }
            }
        } else {
            boolean valid = true;
            while (valid) {
                user = new Seller(username, password);
                String[] sellerMenu = {"Create a new store", "Delete an existing store", "Edit a product", "Add product",
                        "Delete product", "View Sales", "View number of products currently in customer shopping carts",
                        "Import products to store(csv file)", "Export products to store(csv file)",
                        "View products currently in each store", "Logout and Exit"};
                String selection = (String) JOptionPane.showInputDialog(null, "What would you like to do", "Marketplace",
                        JOptionPane.PLAIN_MESSAGE, null, sellerMenu, null);
                if (selection == null) {
                    return;
                }
                switch (selection) {
                    case "Create a new store":
                        String storeName = JOptionPane.showInputDialog(null, "What is the name of the store you would like to create?",
                                "Marketplace", JOptionPane.QUESTION_MESSAGE);
                        ((Seller) user).createStore(storeName);
                        addStoreToSellerFile(storeName, (Seller) user);
                        if (storeName == null) {
                            return;
                        }
                        break;
                    case "Delete an existing store":
                        String storeToRemove = JOptionPane.showInputDialog(null, "What is the name of the store you would like to delete?",
                                "Marketplace", JOptionPane.QUESTION_MESSAGE);
                        if (storeToRemove == null) {
                            return;
                        }
                        String username1 = JOptionPane.showInputDialog(null, "Please re-enter your username",
                                "Marketplace", JOptionPane.QUESTION_MESSAGE);
                        if (username1 == null) {
                            return;
                        }
                        ArrayList<String> storeNames = Seller.readStoreNamesFromFile(username1);
                        if (storeNames.contains(storeToRemove)) {
                            storeNames.remove(storeToRemove);
                            Seller.writeStoreNamesToFile(username, storeNames);  // rewrite stores to the file
                        } else {
                            JOptionPane.showMessageDialog(null, "Sorry this store does not exist",
                                    "Marketplace", JOptionPane.ERROR_MESSAGE);
                            return;
                        }

                        // Delete the corresponding store file
                        File storeFile1 = new File(storeToRemove + ".txt");
                        if (storeFile1.exists() && storeFile1.delete()) {
                            JOptionPane.showMessageDialog(null, "Store removed successfully.",
                                    "Marketplace", JOptionPane.INFORMATION_MESSAGE);
                        } else {
                            JOptionPane.showMessageDialog(null, "Failed to remove the store.",
                                    "Marketplace", JOptionPane.ERROR_MESSAGE);
                        }
                        break;

                    case "Edit a product":
                        String prodName = JOptionPane.showInputDialog(null, "Enter the name of the product you would like to edit:",
                                "Marketplace", JOptionPane.QUESTION_MESSAGE);
                        if (prodName == null) {
                            return;
                        }
                        String store1 = JOptionPane.showInputDialog(null, "Enter the store name that contains the product you would like to edit:",
                                "Marketplace", JOptionPane.QUESTION_MESSAGE);
                        if (store1 == null) {
                            return;
                        }
                        Seller.editProduct(prodName, store1);
                        //saveProductsToFile();

                        break;
                    case "Add product":
                        boolean isValidStore = true;
                        do {
                            String product = JOptionPane.showInputDialog(null, "What store would you like to add a product to?",
                                    "Marketplace", JOptionPane.QUESTION_MESSAGE);
                            if (product == null) {
                                return;
                            }
                            File storeName1 = new File(product + ".txt");
                            if (!storeName1.exists()) {
                                JOptionPane.showMessageDialog(null, "Error this store does not exist",
                                        "Marketplace", JOptionPane.ERROR_MESSAGE);
                                isValidStore = false;
                            } else {
                                addProductToStore(product, (Seller) user);
                                saveProductsToFile();
                            }
                        } while (!isValidStore);
                        break;
                    case "Delete product":
                        String store = JOptionPane.showInputDialog(null, "What EXACT store has the product you would like to delete?",
                                "Marketplace", JOptionPane.QUESTION_MESSAGE);
                        if (store == null) {
                            return;
                        }
                        String removeProduct = JOptionPane.showInputDialog(null, "What is the EXACT name of the product you would like to delete?",
                                "Marketplace", JOptionPane.QUESTION_MESSAGE);
                        if (removeProduct == null) {
                            return;
                        }
                        // Read existing product names from the file
                        ArrayList<String> productNames = Seller.readProductsFromFile(store);
                        if (productNames.contains(removeProduct)) {
                            productNames.remove(removeProduct);
                            JOptionPane.showMessageDialog(null, "Product successfully removed!",
                                    "Marketplace", JOptionPane.INFORMATION_MESSAGE);
                            Seller.writeProductNamesToFile(store, productNames); // rewrite product names to file
                        } else {
                            JOptionPane.showMessageDialog(null, "Sorry this product does not exist",
                                    "Marketplace", JOptionPane.ERROR_MESSAGE);
                        }
                        ArrayList<String> productName = Seller.readProductsFromFiles();
                        if (productName.contains(removeProduct)) {
                            productName.remove(removeProduct);
                            Seller.writeProductNamesToFiles(productName);
                        }
                        break;
                    case "View Sales":
                        // NOT SURE IF THIS METHOD IS PROPERLY WORKING... CAN WE DOUBLE CHECK?
                        String sales = JOptionPane.showInputDialog(null, "What store would you like to view the sales for?",
                                "Marketplace", JOptionPane.QUESTION_MESSAGE);
                        if (sales == null) {
                            return;
                        }
                        double revenue = 0;
                        try {
                            BufferedReader bfr = new BufferedReader(new FileReader(p));
                            String line;
                            while ((line = bfr.readLine()) != null) {
                                String[] contents = line.split(",");
                                int quantitySold = Integer.parseInt(contents[4]);
                                double price = Double.parseDouble(contents[3]);
                                if (contents[1].equals(sales)) {
                                    revenue += quantitySold * price;
                                }
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        JOptionPane.showMessageDialog(null, "The total revenue for the store is " + revenue,
                                "Marketplace", JOptionPane.INFORMATION_MESSAGE);
                        break;
                    case "View number of products currently in customer shopping carts":
                        // allows the seller to view the shopping cart of a specific user
                        String userWanted = JOptionPane.showInputDialog(null, "Please enter the EXACT username of the user whose shopping cart you would like to view",
                                "Marketplace", JOptionPane.QUESTION_MESSAGE);
                        if (userWanted == null) {
                            return;
                        }
                        try {
                            BufferedReader bfr = new BufferedReader(new FileReader(userWanted + " _info.txt"));
                            String line;
                            while ((line = bfr.readLine()) != null) {
                                System.out.println(line);
                            }
                        } catch (IOException e) {
                            JOptionPane.showMessageDialog(null, "Error this customer does not exist",
                                    "Marketplace", JOptionPane.INFORMATION_MESSAGE);
                        }
                        break;
                    case "Import products to store(csv file)":
                        String imports = JOptionPane.showInputDialog(null, "Enter the path to the CSV file for importing products: (MUST BE IN FORMAT AS FOLLOWS: PRODUCT NAME,STORE NAME,DESCRIPTION,PRICE,QUANTITY",
                                "Marketplace", JOptionPane.QUESTION_MESSAGE);
                        if (imports == null) {
                            return;
                        }
                        ((Seller) user).importProductsFromCSV(imports);
                        break;
                    case "Export products to store(csv file)":
                        String exportFilePath = JOptionPane.showInputDialog(null, "Enter the path to the CSV file for exporting products: (MUST BE IN .CSV FORMAT, THIS WILL EXPORT ALL PRODUCTS TO CSV FILE",
                                "Marketplace", JOptionPane.QUESTION_MESSAGE);
                        if (exportFilePath == null) {
                            return;
                        }
                        try {
                            ((Seller) user).exportProductsToCSV(exportFilePath);
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                        break;
                    case "View products currently in each store":
                        String storeWanted = JOptionPane.showInputDialog(null, "What store would you like to view the products for?",
                                "Marketplace", JOptionPane.QUESTION_MESSAGE);
                        if (storeWanted == null) {
                            return;
                        }
                        String line;
                        try {
                            BufferedReader bfr = new BufferedReader(new FileReader(storeWanted + ".txt"));
                            while ((line = bfr.readLine()) != null) {
                                System.out.println(line);
                            }
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                        break;
                    case "Logout and Exit":
                        JOptionPane.showMessageDialog(null, "Thank you for using the marketplace!",
                                "Marketplace", JOptionPane.INFORMATION_MESSAGE);
                        valid = false;
                        break;
                    default:
                        JOptionPane.showMessageDialog(null, "Error", "Marketplace", JOptionPane.ERROR_MESSAGE);
                }
            }
        }
    } catch (IOException ioe) {
        ioe.printStackTrace();
    }
}
}
