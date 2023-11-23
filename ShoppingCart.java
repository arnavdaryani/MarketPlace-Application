import java.io.Serializable;
import java.lang.reflect.Array;
import java.util.ArrayList;

public class ShoppingCart  {
    private ArrayList<Product> listOfProducts;

    public ShoppingCart() {
        this.listOfProducts = new ArrayList<>();
    }
    public ShoppingCart(ArrayList<Product> listOfProducts) {
        this.listOfProducts = listOfProducts;
    }
    public int getSize() {
        return listOfProducts.size();
    }

    public ArrayList<Product> getListOfProducts() {
        return listOfProducts;
    }

    public void addProduct(Product product, int quantity) {
        Product productToAdd;
        if (this.listOfProducts.contains(product)) {
            Product productToModify = this.listOfProducts.get(listOfProducts.indexOf(product));
            productToModify.setQuantity(productToModify.getQuantity() + quantity);
            product.setQuantity(product.getQuantity() - quantity);
            product.setQuantityInCart(product.getQuantityInCart() + quantity);
        } else {
            productToAdd = new Product(product.getProductName(), product.getStoreName(),
                    product.getDescription(), product.getPrice(), quantity);
            product.setQuantity(product.getQuantity() - quantity);
            product.setInCart(true);
            listOfProducts.add(productToAdd);
        }
    }

    public void deleteProduct(Product product) {
        listOfProducts.remove(product);
    }

    public void displayShoppingCart() {
        if (listOfProducts.isEmpty()) {
            System.out.println("Your cart is empty!");
        }
        int i = 1;
        for (Product product : listOfProducts) {
            System.out.println("[" + i + "] " + product.getProductName() + " -- " + product.getQuantity() + " x " + product.getPrice()
                    + " = " + product.getPrice()* product.getQuantity());
        }
    }

    public String toString() {
        String shoppingCart = "";
        for (Product product : listOfProducts) {
            shoppingCart += product.listInShoppingCart() + ";";
        }
        return shoppingCart;
    }


}

