import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.io.*;
import java.util.Arrays;
import java.util.Scanner;

/**
 * A seller class with seller methods
 *
 * methods for sellers to add, modify, or delete products
 *
 * @author Neha Jain
 *
 * @version November 12th, 2023
 *
 */

public class Seller extends JComponent implements Serializable {
    private String username;
    private String password;
    private static ArrayList<String> stores;
    private String fileName;


    public Seller(String username, String password) {
        this.username = username;
        this.password = password;
        this.stores = new ArrayList<>();
        this.fileName = username + "_stores.txt";
    }

    public Seller(String username, String password, ArrayList<String> stores) {
        this.username = username;
        this.password = password;
        this.stores = stores;
    }

    public String getUsername() {
        return username;
    }

    /**
     * A method that retrieves the filename
     * @return - filename
     */

    public String getFileName() {
        return fileName;
    }

    public void setusername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * allows sellers to create stores, this will add a store to the arraylist,
     * and each store has their own arraylist of products
     * @param storeName - inputs store name as parameter so seller can
     * choose which store file they would like to create
     */
//    public static void createStore(String storeName) {
//        Store store = new Store(storeName);
//        stores.add(storeName);
//        //addStoreToSellerFile(storeName, this);
//        System.out.println("Store created successfully: " + storeName);
//    }
    public void createStore(String storeName) {
        Store store = new Store(storeName);
        stores.add(storeName);
        //addStoreToSellerFile(storeName, this);
        JOptionPane.showMessageDialog(null, "Store created successfully: " + storeName,
                "Marketplace", JOptionPane.INFORMATION_MESSAGE);
    }
    public void printStores() {
        System.out.println("List of Stores:");
        try (BufferedReader bfr = new BufferedReader(new FileReader(fileName))) {
            String line;
            while ((line = bfr.readLine()) != null) {
                System.out.println(line);
            }
        } catch (IOException e) {
            System.out.println("Error reading stores from the file: " + e.getMessage());
        }
    }

    /**
     * gets the stores
     * @return - returns list of stores
     */
    public ArrayList<String> getStores() {
        return stores;
    }


    /**
     * imports products to CSV file
     * @param filePath - this is the filepath the seller enters to read from
     */
    public void importProductsFromCSV(String filePath) {
        try {
            BufferedReader bfr = new BufferedReader(new FileReader(filePath));
            String line;
            while ((line = bfr.readLine()) != null) {
                String[] data = line.split(",");
                String productName = data[0];
                //String description = data[1];
                String storeName = data[1];
                double price = Double.parseDouble(data[3]);
                int quantity = Integer.parseInt(data[4]);


                File newFile = new File(storeName + ".txt");
                if (newFile.exists()) {
                    PrintWriter pw = new PrintWriter(new FileWriter(newFile, true));
                    PrintWriter writer = new PrintWriter(new FileWriter("products.txt", true));
                    pw.write(Arrays.toString(data).substring(1, Arrays.toString(data).length() - 1) + "\n");
                    writer.write(Arrays.toString(data).substring(1, Arrays.toString(data).length() - 1) + "\n");
                    pw.close();
                    writer.close();
                    JOptionPane.showMessageDialog(null, "Success! Your products have been imported!"
                            , "Marketplace", JOptionPane.INFORMATION_MESSAGE);
                } else {
                    JOptionPane.showMessageDialog(null, "Error this file does not exist!",
                            "Marketplace", JOptionPane.ERROR_MESSAGE);
                }
            }
            bfr.close();

        } catch (IOException e) {
            e.printStackTrace();
        }

    }


    /**
     * this method will write product to the CSV file as an arrayList
     * IN MAIN METHOD PROMPT USER FOR .CSV FILEPATH
     * @param filePath - this is the filepath the seller uses to save products to
     * @throws IOException
     */
    public void exportProductsToCSV(String filePath) throws IOException {
        BufferedReader bfr = new BufferedReader(new FileReader("products.txt"));
        PrintWriter pw = new PrintWriter(new FileWriter(filePath));
        try {
            String line;
            while ((line = bfr.readLine()) != null) {
                pw.println(line);
            }
            System.out.println("Success, all of your products have been saved to the .csv file!");
        } catch (IOException e) {
            System.out.println("There was an error writing to the file");
        }

        bfr.close();
        pw.close();

    }

    /**
     * method to write store names to each file
     * @param username - seller enters username to get cart of exact customer
     * @param storeNames - an arraylist to store the store names
     */
    public static void writeStoreNamesToFile(String username, ArrayList<String> storeNames) {
        try (PrintWriter writer = new PrintWriter(new FileWriter(username + "_stores.txt", false))) {
            for (String storeName : storeNames) {
                writer.println(storeName);
            }
        } catch (IOException e) {
            System.out.println("Error occurred while writing store names to file.");
        }
    }

    /**
     * method to read store names from the user file
     * @param username - seller passes in username to get exact cart of user
     * @return - returns list of store names
     */
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



    /**
     * this will read the products from the store file the user selects
     * @param storeName - seller passes in store name to read from
     * @return - returns list of product names
     */
    public static ArrayList<String> readProductsFromFile(String storeName) {
        ArrayList<String> productNames = new ArrayList<>();
        String fileName = storeName + ".txt";
        try {
            BufferedReader reader = new BufferedReader(new FileReader(fileName));
            String line = null;

            while ((line = reader.readLine()) != null) {
                String[] details = line.split(",");
                String name = details[0];
                productNames.add(name);
            }

        } catch (IOException e) {
            // Handle file reading errors, if any
            e.printStackTrace();
        }
        return productNames;
    }


    /**
     * this will re write the products to the store file if the seller chooses to modify a product/delete
     * @param storeName - passes in store name to read from the store file
     * @param productNames - passes in the list of product names where everything is saved
     */
    public static void writeProductNamesToFile(String storeName, ArrayList<String> productNames) {
        try (PrintWriter writer = new PrintWriter(new FileWriter(storeName + ".txt", false))) {
            for (String productName : productNames) {
                writer.println(productName);
            }
        } catch (IOException e) {
            System.out.println("Error occurred while writing store names to file.");
        }
    }

    /**
     * reads products from mass list of products
     * @return - returns list of products
     */
    public static ArrayList<String> readProductsFromFiles() {
        ArrayList<String> productNames = new ArrayList<>();
        String fileName = "products.txt";
        try {
            BufferedReader reader = new BufferedReader(new FileReader(fileName));
            String line = null;

            while ((line = reader.readLine()) != null) {
                String[] details = line.split(",");
                String name = details[0];
                productNames.add(name);
            }

        } catch (IOException e) {
            // Handle file reading errors, if any
            e.printStackTrace();
        }
        return productNames;
    }


    /**
     * // re writes to the mass list of products
     * @param productNames - passes in list of products to write to the file
     */
    public static void writeProductNamesToFiles(ArrayList<String> productNames) {
        try (PrintWriter writer = new PrintWriter(new FileWriter( "products.txt", false))) {
            for (String productName : productNames) {
                writer.println(productName);
            }
        } catch (IOException e) {
            System.out.println("Error occurred while writing store names to file.");
        }
    }

    public static void editProduct(String productName, String storeName) {
        File storeFile = new File(storeName + ".txt");
        File tempFile = new File("temp.txt");

        try (BufferedReader reader = new BufferedReader(new FileReader(storeFile));
             PrintWriter writer = new PrintWriter(new FileWriter(tempFile))) {

            String line;
            while ((line = reader.readLine()) != null) {
                String[] productDetails = line.split(",");
                if (productDetails[0].equalsIgnoreCase(productName)) {
                    // Display current product information
                    JOptionPane.showMessageDialog(null, "Current product information: " + line,
                            "Marketplace", JOptionPane.INFORMATION_MESSAGE);

                    // Allow the user to modify specific attributes
                    String newProductDetails = JOptionPane.showInputDialog(null, "Enter new product details separated by commas (name,store name,description,price,quantity):",
                            "Marketplace", JOptionPane.QUESTION_MESSAGE);
                    String[] details = newProductDetails.split(",");
                    String name = details[0];

                    // Update the line with new product details in the store file
                    writer.println(newProductDetails);

                    // Update the products.txt file
                    try (BufferedReader bfr = new BufferedReader(new FileReader("products.txt"));
                         PrintWriter pw = new PrintWriter(new FileWriter("tempProducts.txt", true))) {

                        String lines;
                        while ((lines = bfr.readLine()) != null) {
                            if (lines.contains(name)) {
                                pw.println(newProductDetails);
                            } else {
                                pw.println(lines);
                            }
                        }

                        // Replace the original file with the temporary file
                        if (new File("tempProducts.txt").renameTo(new File("products.txt"))) {
                            System.out.println("Products file updated successfully");
                        } else {
                            System.out.println("Failed to update products file");
                        }

                    } catch (IOException e) {
                        JOptionPane.showMessageDialog(null, "Error occurred while updating the products file.",
                                "Marketplace", JOptionPane.ERROR_MESSAGE);
                    }

                    JOptionPane.showMessageDialog(null, "Product information updated successfully!",
                            "Marketplace", JOptionPane.INFORMATION_MESSAGE);
                } else {
                    writer.println(line);
                }
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "Error occurred while editing the product.",
                    "Marketplace", JOptionPane.ERROR_MESSAGE);
        }

        // Replace the original file with the temporary file for both store and products
        if (storeFile.delete() && tempFile.renameTo(storeFile)) {
            System.out.println("Store file updated successfully");
        } else {
            System.out.println("Failed to update store file");
        }
    }
        public static void deleteProd(String productName, String storeName) {
        File storeFile = new File(storeName + ".txt");
        File tempFile = new File("temp.txt");

        try (BufferedReader reader = new BufferedReader(new FileReader(storeFile));
             PrintWriter writer = new PrintWriter(new FileWriter(tempFile));
             BufferedReader bfr = new BufferedReader(new FileReader("products.txt"));
             PrintWriter pw = new PrintWriter(new FileWriter("tempProducts.txt"))) {

            String line;
            while ((line = reader.readLine()) != null) {
                String[] productDetails = line.split(",");
                if (productDetails[0].equalsIgnoreCase(productName)) {
                    // ... (existing code for updating the store file)

                    // Update the products.txt file
                    String newProductDetails = ""; // Your logic to obtain the new details
                    pw.println(newProductDetails);
                    JOptionPane.showMessageDialog(null, "Product information updated successfully!",
                            "Marketplace", JOptionPane.INFORMATION_MESSAGE);
                } else {
                    writer.println(line);
                }
            }

            // Close the writer for the store file
            writer.close();

            // Copy the remaining lines from the original products.txt file to tempProducts.txt
            String lines;
            while ((lines = bfr.readLine()) != null) {
                pw.println(lines);
            }

            // Replace the original file with the temporary file for both store and products
            if (storeFile.delete() && tempFile.renameTo(storeFile) &&
                    new File("tempProducts.txt").renameTo(new File("products.txt"))) {
                System.out.println("Files updated successfully");
            } else {
                System.out.println("Failed to update files");
            }

        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "Error occurred while editing the product.",
                    "Marketplace", JOptionPane.ERROR_MESSAGE);
        }
    }
}

