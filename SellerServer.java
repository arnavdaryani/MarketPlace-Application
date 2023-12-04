import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Scanner;


public class SellerServer {
    public static ArrayList<Product> products = new ArrayList<>();
    private String userType;
    public static File f = new File("users.txt");
    public static File p = new File("products.txt");

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
                Product newProduct = new Product(name, storeNames, description, price, quantity);
                products.add(newProduct);
            }
        } catch (IOException e) {
            e.printStackTrace();
            writer.write("Error");
            writer.println();
            writer.flush();
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

    public static void main(String[] args) {
        Object user = null;
        String username = "";
        String password = "";
        while (true) {
            ServerSocket serverSocket = null;
            try {
                System.out.println("Waiting for the client to connect...");
                serverSocket = new ServerSocket(4242); // Port number = 4242
                Socket socket = serverSocket.accept();
                System.out.println("Client connected!");
                BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                PrintWriter writer = new PrintWriter(socket.getOutputStream());
                boolean valid = true;
                while (valid) {
                    String selection = reader.readLine();
                    switch (selection) {
                        case "Create a new store":
                            String storeName = reader.readLine();
                            ((Seller) user).createStore(storeName);
                            addStoreToSellerFile(storeName, (Seller) user);
                            break;
                        case "Delete an existing store":
                            String storeToRemove = reader.readLine();
                            String username1 = reader.readLine();
                            ArrayList<String> storeNames = Seller.readStoreNamesFromFile(username1);
                            if (storeNames.contains(storeToRemove)) {
                                storeNames.remove(storeToRemove);
                                Seller.writeStoreNamesToFile(username, storeNames);  // rewrite stores to the file
                            } else {
                                writer.write("DNE");
                                writer.println();
                                writer.flush();
                            }
                            break;

                        case "Edit a product":
                            String prodName = reader.readLine();
                            String store1 = reader.readLine();
                            Seller.editProduct(prodName, store1);
                            //saveProductsToFile();
                            break;

                        case "Add Product":
                            boolean isValid = true;
                            do {
                                String m = reader.readLine();
                                if (m.equals("False"))
                                    isValid = false;
                                String message = reader.readLine();
                                String product = reader.readLine();

                                if (message.equals("E")) {
                                    addProductToStore(product, (Seller) user, reader, writer);
                                    saveProductsToFile();
                                }
                            } while (!isValid);
                            break;

                        case "Delete Product":
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
                            break;

                        case "View Sales":
                            String sales = reader.readLine();
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
                            writer.write(String.valueOf(revenue));
                            writer.println();
                            writer.flush();
                            break;

                        case "View number of products currently in customer shopping carts":
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
                            } catch (IOException e) {
                                e.printStackTrace();
                                writer.write("Error");
                                writer.println();
                                writer.flush();
                            }
                            break;

                        case "Import products to store(csv file)":
                            String imports = reader.readLine();
                            ((Seller) user).importProductsFromCSV(imports);
                            break;

                        case "Export products to store(csv file)":
                            String exportFilePath = reader.readLine();
                            try {
                                ((Seller) user).exportProductsToCSV(exportFilePath);
                            } catch (IOException e) {
                                throw new RuntimeException(e);
                            }
                            break;

                        case "View products currently in each store":
                            String storeWanted = reader.readLine();
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
                            valid = false;
                    }
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }
}

