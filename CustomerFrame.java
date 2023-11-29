import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.util.ArrayList;
import java.util.Random;

public class CustomerFrame extends JFrame {
    public static File f = new File("users.txt");
    public static File p = new File("products.txt");
    JButton button1;
    JButton button2;
    JButton button3;
    JButton button4;
    JButton button5;
    JButton button6;
    JButton button7;
    JButton button8;

    Customer user;

    int customerChoice;
    ActionListener actionListener = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            if (e.getSource() == button1) {
            }
            if (e.getSource() == button2) {
                ArrayList<Product> market = Marketplace.readFromFile();
                ArrayList<Product> products = user.getShoppingCart().getListOfProducts();
                Product newOne = market.get(0);
                user.getShoppingCart().addProduct(newOne, 3);
                products.sort(new SortByPrice());
                String[] values1 = new String[products.size()];
                int ii = 0;
                String productString = "";
                for (Product i : products) {
                    productString = i.getProductName();
                    values1[ii] = productString;
                    ii++;
                }
                String item = (String) JOptionPane.showInputDialog(null,
                        "The Products Listed by Price", "",
                        JOptionPane.PLAIN_MESSAGE, null, values1, null);
            }
            if (e.getSource() == button3) {
                ArrayList<Product> market = Marketplace.readFromFile();
                ArrayList<Product> products = user.getShoppingCart().getListOfProducts();
                Product newOne = market.get(0);
                user.getShoppingCart().addProduct(newOne, 3);
                products.sort(new SortByQuantity());
                String[] values1 = new String[products.size()];
                int ii = 0;
                String productString = "";
                for (Product i : products) {
                    productString = i.getProductName();
                    values1[ii] = productString;
                    ii++;
                }
                String item = (String) JOptionPane.showInputDialog(null,
                        "The Products Listed by Quantity", "",
                        JOptionPane.PLAIN_MESSAGE, null, values1, null);
            }
            if (e.getSource() == button4) {
                ArrayList<Product> market = Marketplace.readFromFile();
                ArrayList<Product> products = user.getShoppingCart().getListOfProducts();
                Product newOne = market.get(0);
                user.getShoppingCart().addProduct(newOne, 3);
                String[] values1 = new String[products.size()];
                int ii = 0;
                String productString = "";
                for (Product i : products) {
                    productString = i.getProductName();
                    values1[ii] = productString;
                    ii++;
                }
                String[] values2 = {"Checkout $" + shoppingCartTotal((Customer) user), "Remove Item", "Go Back"};
                String item = (String) JOptionPane.showInputDialog(null,
                        "The Shopping Cart", "",
                        JOptionPane.PLAIN_MESSAGE, null, values1, null);
                String decision = (String) JOptionPane.showInputDialog(null,
                        "Options", "",
                        JOptionPane.PLAIN_MESSAGE, null, values2, null);
                if (decision == null) {
                    return;
                }
                user.saveShoppingCart();
                if (decision.equals("Checkout $" + shoppingCartTotal((Customer) user))) {
                    ((Customer) user).checkout();
                    ((Customer) user).previouslyPurchasedFile();
                    ((Customer) user).saveShoppingCart();
                }
                if (decision.equals("Remove Item")) {
                    Product productToRemove;
                    String pickIt = (String) JOptionPane.showInputDialog(null,
                            "Pick the item you want to remove", "",
                            JOptionPane.PLAIN_MESSAGE, null, values1, null);
                    Product productToRemove1 = new Product("", "", "", 0, 0);
                    for (Product product : ((Customer) user).getShoppingCart().getListOfProducts()) {
                        if (product.getProductName().equalsIgnoreCase(pickIt)) {
                            productToRemove1 = product;
                            //            ((Customer) user).getShoppingCart().deleteProduct(product);
                            //            break;
                        }
                    }
                    user.getShoppingCart().deleteProduct(productToRemove1);
                    productToRemove1.setQuantity(productToRemove1.getQuantity() + productToRemove1.getQuantityInCart());
                    saveProductsToFile();

                }
                if (decision.equals("Go Back")) {
                    user.saveShoppingCart();
                }
            }
            if (e.getSource() == button5) {
            }
            if (e.getSource() == button6) {
                String list = viewPreviousPurchases((Customer) user);
                String[] splitOnce = list.split(",");
                String item = (String) JOptionPane.showInputDialog(null,
                        "The Purchase History", "",
                        JOptionPane.PLAIN_MESSAGE, null, splitOnce, null);
            }
            if (e.getSource() == button7) {
                ArrayList<Product> market = Marketplace.readFromFile();
                ArrayList<Product> products = user.getShoppingCart().getListOfProducts();
                Product newOne = market.get(0);
                user.getShoppingCart().addProduct(newOne, 3);
                String[] values1 = new String[products.size()];
                int ii = 0;
                String productString = "";
                for (Product i : products) {
                    productString = i.getProductName();
                    values1[ii] = productString;
                    ii++;
                }
                previouslyPurchasedFile(values1);
                JOptionPane.showMessageDialog(null, "Exported!");
            }
            if (e.getSource() == button8) {
                dispose();
                SwingUtilities.invokeLater(new Marketplace());
            }
        }
    };

        //public static void main(String[] args) {
        //SwingUtilities.invokeLater(new CustomerFrame(args[0]));
        //}
        public void previouslyPurchasedFile(String[] products) {
            try {
                String newFile = getName() + " _purchases.txt";
                PrintWriter pw = new PrintWriter(new BufferedWriter(new FileWriter((newFile), true)));
                pw.println("Previously Purchased Items: ");
                for (String s : products) {
                    pw.println(s);
                }
                pw.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        public CustomerFrame(Customer user) {
            this.user = user;
            this.setTitle("The Customer Frame");
            this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            this.setSize(600, 400);
            this.setLocationRelativeTo(null);
            Container content = this.getContentPane();
            content.setLayout(new BorderLayout());
            this.setVisible(true);
            button1 = new JButton("Search for a product");
            button2 = new JButton("Sort products by price");
            button3 = new JButton("Sort products by quantity available");
            button4 = new JButton("View shopping cart");
            button5 = new JButton("View product page to purchase");
            button6 = new JButton("View purchase history");
            button7 = new JButton("Export purchase history");
            button8 = new JButton("Logout and Exit");
            JPanel lowPanel = new JPanel();
            button1.addActionListener(actionListener);
            button2.addActionListener(actionListener);
            button3.addActionListener(actionListener);
            button4.addActionListener(actionListener);
            button5.addActionListener(actionListener);
            button6.addActionListener(actionListener);
            button7.addActionListener(actionListener);
            button8.addActionListener(actionListener);
            JPanel panel = new JPanel();
            panel.add(button1);
            panel.add(button2);
            panel.add(button3);
            panel.add(button4);
            panel.add(button5);
            panel.add(button6);
            panel.add(button7);
            panel.add(button8);
            content.add(panel, BorderLayout.CENTER);
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
            // try (PrintWriter pw = new PrintWriter(new FileWriter(p))) {
            //products = readFromFile();

            //  for (Product prod : products) {
            //          pw.println(prod.listInShoppingCart());
            //           pw.flush();
            //        }
            //    } catch (IOException e) {
            //         e.printStackTrace();
            //     }
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
                System.out.println("No products are currently listed!\n");
            }
            return availableProducts;
        }

        public static String viewPreviousPurchases(Customer customer) {
            String previousProducts = "";
            File previousPurchases = new File(customer.getUsername() + " _purchases.txt");
            try (BufferedReader bfr = new BufferedReader(new FileReader(previousPurchases))) {
                bfr.readLine();
                String line = bfr.readLine();
                if (line == null || line.isEmpty()) {
                }
                while (line != null) {
                    if (!(line.contains("Previously Purchased Items:"))) {
                        previousProducts = previousProducts + line + ",";
                    }
                    line = bfr.readLine();
                }
            } catch (IOException e) {
                System.out.println("You have no previous purchases!");
            }
            return previousProducts;
        }
    }
