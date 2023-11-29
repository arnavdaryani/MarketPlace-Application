import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.ArrayList;
import java.util.Scanner;

public class Marketplace extends JComponent implements Runnable{
    // STILL NEED TO MAKE SURE THE USER CAN PRESS CANCEL OR RED X BUTTON AT ANY TIME DURING THE PROGRAM
    public static ArrayList<Product> products = new ArrayList<>();
    private String userType;
    public static File f = new File("users.txt");
    public static File p = new File("products.txt");

    public static void main(String[] args) {

        SwingUtilities.invokeLater(new Marketplace());
    }

    public static String createUser(String userType) {
        String line = null;
        try (PrintWriter pw = new PrintWriter(new FileWriter(f, true), true);
             BufferedReader bfr = new BufferedReader(new FileReader(f))) {
            String username;
            String password;
            String confirmPassword;

            do {
                username = JOptionPane.showInputDialog(null, "Enter username:","Marketplace", JOptionPane.QUESTION_MESSAGE);
                line = bfr.readLine();
                boolean taken = false;
                while (line != null) {
                    String[] userData = line.split(",");
                    if (userData[0].equals(username)) {
                        JOptionPane.showMessageDialog(null, "This username is already taken! Please try again.",
                                "Marketplace", JOptionPane.ERROR_MESSAGE);
                        taken = true;
                        break;
                    }
                    line = bfr.readLine();
                }
                if (!taken) {
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
                pw.printf(line);
                pw.println();
            } else {
                line = String.format("%s,%s,%s", username, password, "Seller");
                pw.printf(line);
                pw.println();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return line;
    }

    public static String login() {
        String line = null;
        boolean loggedIn = false;

        do {
            try (BufferedReader bfr = new BufferedReader(new FileReader("users.txt"))) {
                String username = JOptionPane.showInputDialog(null, "Enter your username:");

                String password = JOptionPane.showInputDialog(null, "Enter your password:");
                line = bfr.readLine();

                while (line != null) {
                    String[] userData = line.split(",");
                    String storedUsername = userData[0];
                    String storedPassword = userData[1];

                    if (username.equals(storedUsername) && password.equals(storedPassword)) {
                        JOptionPane.showMessageDialog(null,"Success! You are now logged in!",
                                "Marketplace", JOptionPane.INFORMATION_MESSAGE);
                        loggedIn = true;
                        break;
                    }
                    line = bfr.readLine();
                }

                if (!loggedIn) {
                    JOptionPane.showMessageDialog(null, "Either your username or password is wrong, " +
                            "or there is no account associated with this username!", "Marketplace", JOptionPane.ERROR_MESSAGE);
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



    public void run() {
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
            String userInfo = login();
            String[] userData = userInfo.split(",");
            username = userData[0];
            password = userData[1];
            if (userData[2].equals("Customer")) {
                user = new Customer(username, password);
                ShoppingCart shoppingCart = getUserShoppingCart((Customer) user);
                user = new Customer(username, password,shoppingCart);
            } else {
                user = new Seller(username, password);
            }
        } else if (choice == JOptionPane.NO_OPTION) {
            String[] custOrSell = {"Customer", "Seller"};
            userType = (String) JOptionPane.showInputDialog(null,"Are you a customer or seller?", "Marketplace",
                     JOptionPane.PLAIN_MESSAGE, null, custOrSell, null);
            String userInfo = createUser(userType);
            String[] userData = userInfo.split(",");
            if (userData.length >= 2) {
                username = userData[0];
                password = userData[1];
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
                switch (selection) {
                    case "Create a new store":
                        String storeName = JOptionPane.showInputDialog(null, "What is the name of the store you would like to create?",
                                "Marketplace", JOptionPane.QUESTION_MESSAGE);
                        ((Seller) user).createStore(storeName);
                        addStoreToSellerFile(storeName, (Seller) user);
                        break;
                    case "Delete an existing store":
                        String storeToRemove = JOptionPane.showInputDialog(null, "What is the name of the store you would like to delete?",
                                "Marketplace", JOptionPane.QUESTION_MESSAGE);
                        String username1 = JOptionPane.showInputDialog(null, "Please re-enter your username",
                                "Marketplace", JOptionPane.QUESTION_MESSAGE);
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
                        String store1 = JOptionPane.showInputDialog(null, "Enter the store name that contains the product you would like to edit:",
                                "Marketplace", JOptionPane.QUESTION_MESSAGE);
                        Seller.editProduct(prodName, store1);
                        //saveProductsToFile();
                        break;
                    case "Add product":
                        boolean isValidStore = true;
                        do {
                            String product = JOptionPane.showInputDialog(null, "What store would you like to add a product to?",
                                    "Marketplace", JOptionPane.QUESTION_MESSAGE);
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
                        String removeProduct = JOptionPane.showInputDialog(null, "What is the EXACT name of the product you would like to delete?",
                                "Marketplace", JOptionPane.QUESTION_MESSAGE);

                        // Read existing product names from the file
                        ArrayList<String> productNames = Seller.readProductsFromFile(store);
                        if (productNames.contains(removeProduct)) {
                            productNames.remove(removeProduct);
                            JOptionPane.showMessageDialog(null, "Product successfully removed!",
                                    "Marketplace", JOptionPane.INFORMATION_MESSAGE);
                            Seller.writeProductNamesToFile(store, productNames); // rewrite product names to file
                        } else {
                            System.out.println("Sorry this product does not exist");
                        }
                        ArrayList<String> productName = Seller.readProductsFromFiles();
                        if (productName.contains(removeProduct)) {
                            productName.remove(removeProduct);
                            Seller.writeProductNamesToFiles(productName);
                        }
                        break;
                    case "View Sales":
                        // NEED TO MAKE THIS METHOD BETTER, IS THERE SOMETHING IN CUSTOMER IMPLEMENTATION WE CAN USE?
                        break;
                    case "View number of products currently in customer shopping carts":
                        break;
                    case "Import products to store(csv file)":
                        break;
                    case "Export products to store(csv file)":
                        break;
                    case "View products currently in each store":
                        break;
                    case "Logout and Exit":
                        JOptionPane.showMessageDialog(null, "Thank you for using the marketplace!",
                                "Marketplace", JOptionPane.INFORMATION_MESSAGE);
                        valid = false;
                        break;
                    default:
                        JOptionPane.showMessageDialog(null, "Error", "Marketplace", JOptionPane.ERROR_MESSAGE);
                }

                //openSellerFrame((Seller) user);

            }
        }

    }

}

//    private void openSellerFrame(Seller sellerInstance) {
//        SwingUtilities.invokeLater(() -> {
//            SellerFrame sellerFrame = new SellerFrame(sellerInstance);
//            sellerFrame.setVisible(true);
//            sellerFrame.setLocationRelativeTo(null);
//        });
//    }
