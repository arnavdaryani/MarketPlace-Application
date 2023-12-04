import javax.swing.*;
import java.io.*;
import java.net.*;
import java.util.Scanner;

public class SellerClient {
    public static void main(String[] args) {
        Scanner scan = new Scanner(System.in);
        Socket socket = null;
        try {
            Object user = null;
            String username = "";
            String password = "";

            socket = new Socket("localhost", 4242);
            BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            PrintWriter writer = new PrintWriter(socket.getOutputStream());
            boolean valid = true;
            while (valid) {
                user = new Seller(username, password);
                String[] sellerMenu = {"Create a new store", "Delete an existing store", "Edit a product", "Add product",
                        "Delete product", "View Sales", "View number of products currently in customer shopping carts",
                        "Import products to store(csv file)", "Export products to store(csv file)",
                        "View products currently in each store", "Logout and Exit"};
                String selection = (String) JOptionPane.showInputDialog(null, "What would you like to do", "Marketplace",
                        JOptionPane.PLAIN_MESSAGE, null, sellerMenu, null);
                writer.write(selection);
                writer.println();
                writer.flush();
                if (selection == null) {
                    return;
                }
                switch (selection) {
                    case "Create a new store":
                        String storeName = JOptionPane.showInputDialog(null, "What is the name of the store you would like to create?",
                                "Marketplace", JOptionPane.QUESTION_MESSAGE);
                        writer.write(storeName);
                        writer.println();
                        writer.flush();
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
                        writer.write(username1);
                        writer.println();
                        writer.flush();
                        if (username1 == null) {
                            return;
                        }
                        String message = reader.readLine();
                        if (message.equals("DNE")) {
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
                        writer.write(prodName);
                        writer.println();
                        writer.flush();
                        if (prodName == null) {
                            return;
                        }
                        String store1 = JOptionPane.showInputDialog(null, "Enter the store name that contains the product you would like to edit:",
                                "Marketplace", JOptionPane.QUESTION_MESSAGE);
                        writer.write(store1);
                        writer.println();
                        writer.flush();
                        if (store1 == null) {
                            return;
                        }
                        break;

                    case "Add Product":
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
                                writer.write("False");
                                writer.println();
                                writer.flush();
                            } else {
                                writer.write("E");
                                writer.println();
                                writer.flush();
                                writer.write(product);
                                writer.println();
                                writer.flush();
                                String m = reader.readLine();
                                if (m.equals("Error")) {
                                    JOptionPane.showMessageDialog(null, "Error occurred while adding the product.",
                                            "Marketplace", JOptionPane.ERROR_MESSAGE);
                                } else {
                                    String productDetails = JOptionPane.showInputDialog(null, "Enter product details separated by commas (name,store name,description,price,quantity):",
                                            "Marketplace", JOptionPane.QUESTION_MESSAGE);
                                    writer.write(productDetails);
                                    writer.println();
                                    writer.flush();
                                    JOptionPane.showMessageDialog(null, "Product added successfully!",
                                            "Marketplace", JOptionPane.INFORMATION_MESSAGE);
                                }

                            }
                        } while (!isValidStore);
                        break;

                    case "Delete Product":
                        String store = JOptionPane.showInputDialog(null, "What EXACT store has the product you would like to delete?",
                                "Marketplace", JOptionPane.QUESTION_MESSAGE);
                        writer.write(store);
                        writer.println();
                        writer.flush();
                        if (store == null) {
                            return;
                        }
                        String removeProduct = JOptionPane.showInputDialog(null, "What is the EXACT name of the product you would like to delete?",
                                "Marketplace", JOptionPane.QUESTION_MESSAGE);
                        writer.write(removeProduct);
                        writer.println();
                        writer.flush();
                        if (removeProduct == null) {
                            return;
                        }
                        String q = reader.readLine();
                        if (q.equals("Removed")) {
                            JOptionPane.showMessageDialog(null, "Product successfully removed!",
                                    "Marketplace", JOptionPane.INFORMATION_MESSAGE);
                        } else {
                            JOptionPane.showMessageDialog(null, "Sorry this product does not exist",
                                    "Marketplace", JOptionPane.ERROR_MESSAGE);
                        }
                        break;

                    case "View Sales":
                        // NOT SURE IF THIS METHOD IS PROPERLY WORKING... CAN WE DOUBLE CHECK?
                        String sales = JOptionPane.showInputDialog(null, "What store would you like to view the sales for?",
                                "Marketplace", JOptionPane.QUESTION_MESSAGE);
                        writer.write(sales);
                        writer.println();
                        writer.flush();
                        if (sales == null) {
                            return;
                        }
                        String revenue = reader.readLine();
                        int rev = Integer.parseInt(revenue);
                        JOptionPane.showMessageDialog(null, "The total revenue for the store is " + revenue,
                                "Marketplace", JOptionPane.INFORMATION_MESSAGE);
                        break;

                    case "View number of products currently in customer shopping carts":
                        // allows the seller to view the shopping cart of a specific user
                        String userWanted = JOptionPane.showInputDialog(null, "Please enter the EXACT username of the user whose shopping cart you would like to view",
                                "Marketplace", JOptionPane.QUESTION_MESSAGE);
                        writer.write(userWanted);
                        writer.println();
                        writer.flush();
                        if (userWanted == null) {
                            return;
                        }
                        String s = reader.readLine();
                        if (s.equals("Error")) {
                            JOptionPane.showMessageDialog(null, "Error this customer does not exist",
                                    "Marketplace", JOptionPane.INFORMATION_MESSAGE);
                        }
                        break;

                    case "Import products to store(csv file)":
                        String imports = JOptionPane.showInputDialog(null, "Enter the path to the CSV file for importing products: (MUST BE IN FORMAT AS FOLLOWS: PRODUCT NAME,STORE NAME,DESCRIPTION,PRICE,QUANTITY",
                                "Marketplace", JOptionPane.QUESTION_MESSAGE);
                        writer.write(imports);
                        writer.println();
                        writer.flush();
                        if (imports == null) {
                            return;
                        }
                        break;

                    case "Export products to store(csv file)":
                        String exportFilePath = JOptionPane.showInputDialog(null, "Enter the path to the CSV file for exporting products: (MUST BE IN .CSV FORMAT, THIS WILL EXPORT ALL PRODUCTS TO CSV FILE",
                                "Marketplace", JOptionPane.QUESTION_MESSAGE);
                        writer.write(exportFilePath);
                        writer.println();
                        writer.flush();
                        if (exportFilePath == null) {
                            return;
                        }
                        break;

                    case "View products currently in each store":
                        String storeWanted = JOptionPane.showInputDialog(null, "What store would you like to view the products for?",
                                "Marketplace", JOptionPane.QUESTION_MESSAGE);
                        writer.write(storeWanted);
                        writer.println();
                        writer.flush();
                        if (storeWanted == null) {
                            return;
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
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
