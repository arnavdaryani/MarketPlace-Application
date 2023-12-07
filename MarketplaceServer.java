import java.io.*;
import java.net.*;
import java.util.ArrayList;

public class MarketplaceServer {

    public static ArrayList<Product> products = new ArrayList<>();
    public static final int PORT = 4242;
    public static File f = new File("users.txt");
    public static File p = new File("products.txt");
    public static Object user;
    public static Object gatekeeper = new Object();

    public static ArrayList<Product> readFromFile() {
        ArrayList<Product> availableProducts = new ArrayList<>();
        try (BufferedReader bfr = new BufferedReader(new FileReader(p))) {
            String line = bfr.readLine();
            while (line != null) {
                String[] productInfo = line.split(",");
                Product product = new Product(productInfo[0], productInfo[1], productInfo[2],
                        Double.parseDouble(productInfo[3]), Integer.parseInt(productInfo[4]),
                        Integer.parseInt(productInfo[5]));
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
                pw.println(prod.listInFile());
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

    public static void addProductToStore(String storeName, Seller seller, BufferedReader reader, PrintWriter writer) {
        try (PrintWriter pw = new PrintWriter(new FileWriter(storeName + ".txt", true), true)) {
            writer.write("No Error");
            writer.println();
            writer.flush();
            String productDetails = reader.readLine();
            pw.println(productDetails);
            String[] detailsArray = productDetails.split(",");
            if (detailsArray.length == 5) {
                String name = detailsArray[0];
                String storeNames = detailsArray[1];
                String description = detailsArray[2];
                double price = Double.parseDouble(detailsArray[3]);
                int quantity = Integer.parseInt(detailsArray[4]);
                int quantitySold = 0;
                Product newProduct = new Product(name, storeNames, description, price, quantity, quantitySold);
                products.add(newProduct);
            }
        } catch (IOException e) {
            e.printStackTrace();
            writer.write("Error");
            writer.println();
            writer.flush();
        }
    }

    public static ArrayList<Product> searchFProducts(String searchTerm) {
        ArrayList<Product> correctOnes = new ArrayList<Product>();
        for (Product lookInside : products) {
            if (lookInside.getProductName().contains(searchTerm)) {
                correctOnes.add(lookInside);
            }
        }
        return correctOnes;
    }

    public static ArrayList<Product> searchFProductsByStore(String searchTerm) {
        ArrayList<Product> correctOnes = new ArrayList<Product>();
        for (Product lookInside : products) {
            if (lookInside.getStoreName().contains(searchTerm)) {
                correctOnes.add(lookInside);
            }
        }
        return correctOnes;
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
                    synchronized (gatekeeper) {
                        products = readFromFile();
                    }
                    switch (selection) {
                        case "Search for a product":
                            if (reader.readLine().equals("Search for a product by store name")) {
                                String nameOfIt = reader.readLine();
                                ArrayList<Product> storeItems = new ArrayList<>();
                                synchronized (gatekeeper) {
                                    storeItems = searchFProductsByStore(nameOfIt);
                                }
                                if (storeItems.size() > 0) {
                                    writer.write("Store found");
                                    writer.println();
                                    writer.flush();
                                    String[] storeProducts = new String[storeItems.size()];
                                    int go = 0;
                                    for (Product prod : storeItems) {
                                        storeProducts[go] = prod.toString();
                                        go++;
                                    }
                                    writer.write(String.valueOf(storeProducts.length));
                                    writer.println();
                                    writer.flush();
                                    for (String i : storeProducts) {
                                        writer.write(i);
                                        writer.flush();
                                    }
                                    String productChoice = reader.readLine();
                                    System.out.println(productChoice);
                                    String[] parts = productChoice.split("\\|");
                                    String itemName = parts[0].split(":")[1].trim();
                                    String storeName = parts[1].split(":")[1].trim();
                                    for (Product prod : products) {
                                        if (prod.getProductName().equalsIgnoreCase(itemName)
                                                && prod.getStoreName().equalsIgnoreCase(storeName)) {
                                            String productPage = prod.returnProductPage();
                                            writer.write(productPage);
                                            writer.println();
                                            writer.flush();
                                            break;
                                        }
                                    }

                                } else {
                                    writer.write("Store not found");
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
                                        writer.flush();
                                    }
                                    String productChoice = reader.readLine();
                                    System.out.println(productChoice);
                                    String[] parts = productChoice.split("\\|");
                                    String itemName = parts[0].split(":")[1].trim();
                                    String storeName = parts[1].split(":")[1].trim();
                                    for (Product prod : products) {
                                        if (prod.getProductName().equalsIgnoreCase(itemName)
                                                && prod.getStoreName().equalsIgnoreCase(storeName)) {
                                            String productPage = prod.returnProductPage();
                                            writer.write(productPage);
                                            writer.println();
                                            writer.flush();
                                            break;
                                        }
                                    }
                                }
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
                                writer.flush();
                            }
                            String productChoice = reader.readLine();
                            System.out.println(productChoice);
                            String[] parts = productChoice.split("\\|");
                            String itemName = parts[0].split(":")[1].trim();
                            String storeName = parts[1].split(":")[1].trim();
                            for (Product prod : products) {
                                if (prod.getProductName().equalsIgnoreCase(itemName)
                                        && prod.getStoreName().equalsIgnoreCase(storeName)) {
                                    String productPage = prod.returnProductPage();
                                    writer.write(productPage);
                                    writer.println();
                                    writer.flush();
                                    break;
                                }
                            }
                            break;
                        case "Sort products by quantity available":
                            String[] sortedProductsQuantity;
                            synchronized (gatekeeper) {
                                products = readFromFile();
                                products.sort(new SortByQuantity());
                                sortedProductsQuantity = new String[products.size()];
                                int i = 0;
                                for (Product prod : products) {
                                    sortedProductsQuantity[i] = prod.toString();
                                    i++;
                                }
                            }
                            writer.write(String.valueOf(sortedProductsQuantity.length));
                            writer.println();
                            writer.flush();
                            for (String prod : sortedProductsQuantity) {
                                writer.write(prod);
                                writer.flush();
                            }
                            productChoice = reader.readLine();
                            System.out.println(productChoice);
                            parts = productChoice.split("\\|");
                            itemName = parts[0].split(":")[1].trim();
                            storeName = parts[1].split(":")[1].trim();
                            for (Product prod : products) {
                                if (prod.getProductName().equalsIgnoreCase(itemName)
                                        && prod.getStoreName().equalsIgnoreCase(storeName)) {
                                    String productPage = prod.returnProductPage();
                                    writer.write(productPage);
                                    writer.println();
                                    writer.flush();
                                    break;
                                }
                            }
                            break;
                        case "View product page to purchase":
                            String productName = reader.readLine();
                            storeName = reader.readLine();
                            synchronized (gatekeeper) {
                                Product product = viewItemPage(productName, storeName);
                                if (product == null) {
                                    writer.write("Product not found");
                                    writer.println();
                                    writer.flush();
                                } else {
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
                                            saveProductsToFile();
                                        }
                                    } else {
                                        continue;
                                    }
                                }
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
                                productString = iii.getProductName() + " -- " + iii.getQuantity() + " x " + iii.getPrice()
                                        + " = " + iii.getPrice() * iii.getQuantity();
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
                                    for (Product prod : ((Customer) user).getShoppingCart().getListOfProducts()) {
                                        for (Product product : products) {
                                            if (prod.equals(product)) {
                                                product.setQuantitySold(product.getQuantitySold() + prod.getQuantity());
                                            }
                                        }
                                    }
                                    ((Customer) user).checkout();
                                    ((Customer) user).previouslyPurchasedFile();
                                    ((Customer) user).saveShoppingCart();
                                }
                            }
                            if (decision.equals("Remove Item")) {
                                String pickIt = reader.readLine();
                                synchronized (gatekeeper) {
                                    for (Product product : ((Customer) user).getShoppingCart().getListOfProducts()) {
                                        if (product.getProductName().equalsIgnoreCase(pickIt)) {
                                            int quantity = product.getQuantity();
                                            for (Product prod : products) {
                                                if (prod.equals(product)) {
                                                    prod.setQuantity(prod.getQuantity() + quantity);
                                                    break;
                                                }
                                            }
                                            saveProductsToFile();
                                            ((Customer) user).getShoppingCart().deleteProduct(product);
                                            ((Customer) user).saveShoppingCart();
                                            break;
                                        }
                                    }
                                }
                            }
                            if (decision.equals("Go Back")) {
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
                            break;
                    }
                }
            } else {
                boolean valid = true;
                while (valid) {
                    String selection = reader.readLine();
                    switch (selection) {
                        case "Create a new store":
                            synchronized (gatekeeper) {
                                String storeName = reader.readLine();
                                ((Seller) user).createStore(storeName);
                                addStoreToSellerFile(storeName, (Seller) user);
                            }
                            break;
                        case "Delete an existing store":
                            synchronized (gatekeeper) {
                                String storeToRemove = reader.readLine();
                                String username1 = reader.readLine();
                                ArrayList<String> storeNames = Seller.readStoreNamesFromFile(username1);
                                if (storeNames.contains(storeToRemove)) {
                                    storeNames.remove(storeToRemove);
                                    Seller.writeStoreNamesToFile(username, storeNames);// rewrite stores to the file
                                    writer.write("D");
                                    writer.println();
                                    writer.flush();
                                } else {
                                    writer.write("DNE");
                                    writer.println();
                                    writer.flush();
                                }
                            }
                            break;

                        case "Edit a product":
                            synchronized (gatekeeper) {
                                String prodName = reader.readLine();
                                String store1 = reader.readLine();
                                Seller.editProduct(prodName, store1);
                                //saveProductsToFile();
                            }
                            break;

                         case "Add product":
                            synchronized (gatekeeper) {
                                boolean isValid = true;
                                do {
                                    String m = reader.readLine();
                                    if (m.equals("False")) {
                                        isValid = false;
                                    } else if (m.equals("E")) {
                                        String product = reader.readLine();
                                        writer.write("a");
                                        writer.println();
                                        writer.flush();
                                        addProductToStore(product, (Seller) user, reader, writer);
                                        saveProductsToFile();
                                    }
                                } while (!isValid);
                            }
                            break;
                            
                        case "Delete product":
                            synchronized (gatekeeper) {
                                String store = reader.readLine();
                                String removeProduct = reader.readLine();
                                ArrayList<String> productNames = Seller.readProductsFromFile(store);
                                if (productNames.contains(removeProduct)) {
                                    productNames.remove(removeProduct);
                                    writer.write("Removed");
                                    writer.println();
                                    writer.flush();
                                    Seller.writeProductNamesToFile(store, productNames); // rewrite product names to file
                                } else {
                                    writer.write("False");
                                    writer.println();
                                    writer.flush();
                                }
                                ArrayList<String> productName = Seller.readProductsFromFiles();
                                if (productName.contains(removeProduct)) {
                                    productName.remove(removeProduct);
                                    Seller.writeProductNamesToFiles(productName);
                                }
                            }
                            break;

                        case "View Sales":
                            synchronized (gatekeeper) {
                                String sales = reader.readLine();
                                double revenue = 0;
                                try (BufferedReader bfr = new BufferedReader(new FileReader(p))) {
                                    String line;
                                    while ((line = bfr.readLine()) != null) {
                                        String[] contents = line.split(",");
                                        int quantitySold = Integer.parseInt(contents[5]);
                                        double price = Double.parseDouble(contents[3]);
                                        if (contents[1].equals(sales)) {
                                            revenue += quantitySold * price;
                                        }
                                    }
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                                writer.write(String.valueOf(revenue));
                                writer.println();
                                writer.flush();
                            }
                            break;

                        case "View number of products currently in customer shopping carts":
                            synchronized (gatekeeper) {
                                String userWanted = reader.readLine();
                                try {
                                    writer.write("No Error");
                                    writer.println();
                                    writer.flush();
                                    BufferedReader bfr = new BufferedReader(new FileReader(userWanted + " _info.txt"));
                                    String line;
                                    while ((line = bfr.readLine()) != null) {
                                        System.out.println(line);
                                    }
                                    bfr.close();
                                } catch (IOException e) {
                                    e.printStackTrace();
                                    writer.write("Error");
                                    writer.println();
                                    writer.flush();
                                }
                            }
                            break;

                        case "Import products to store(csv file)":
                            synchronized (gatekeeper) {
                                String imports = reader.readLine();
                                ((Seller) user).importProductsFromCSV(imports);
                            }
                            break;

                        case "Export products to store(csv file)":
                            synchronized (gatekeeper) {
                                String exportFilePath = reader.readLine();
                                try {
                                    ((Seller) user).exportProductsToCSV(exportFilePath);
                                } catch (IOException e) {
                                    throw new RuntimeException(e);
                                }
                            }
                            break;

                        case "View products currently in each store":
                            synchronized (gatekeeper) {
                                String storeWanted = reader.readLine();
                                String line;
                                try {
                                    BufferedReader bfr = new BufferedReader(new FileReader(storeWanted + ".txt"));
                                    while ((line = bfr.readLine()) != null) {
                                        System.out.println(line);
                                    }
                                    bfr.close();
                                } catch (IOException e) {
                                    throw new RuntimeException(e);
                                }
                            }
                            break;

                        case "Logout and Exit":
                            synchronized (gatekeeper) {
                                valid = false;
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
