import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;

public class Marketplace extends JComponent implements Runnable{
    // STILL NEED TO MAKE SURE THE USER CAN PRESS CANCEL OR RED X BUTTON AT ANY TIME DURING THE PROGRAM
    private JFrame frame;
    private String userType;
    public static File f = new File("users.txt");

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
                        System.out.println("Success! You are now logged in!\n");
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
                System.out.println("Please create an account first!");
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
            String userType = (String) JOptionPane.showInputDialog(null,"Are you a customer or seller?", "Marketplace",
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
            user = new Customer(username, password);
            openCustomerFrame((Customer) user);
            // customer implementation
        } else {
            user = new Seller(username, password);

            openSellerFrame(username);

        }

    }
    private void openSellerFrame(String username) {
        SwingUtilities.invokeLater(() -> {
            SellerFrame sellerFrame = new SellerFrame(username);
            sellerFrame.setVisible(true);
            sellerFrame.setLocationRelativeTo(null);
        });
    }

    private void openCustomerFrame(Customer user) {
        SwingUtilities.invokeLater(() -> {
            CustomerFrame customerFrame = new CustomerFrame(user);
            customerFrame.setVisible(true);
            customerFrame.setLocationRelativeTo(null);
        });
    }

    public static ArrayList<Product> readFromFile() {
        ArrayList<Product> availableProducts = new ArrayList<>();
        try (BufferedReader bfr = new BufferedReader(new FileReader(new File("products.txt")))) {
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



}
