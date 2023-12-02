import java.util.Comparator;

/**
 * Compares products by quantity
 *
 * Compares the products in the arraylist products (in marketplace)
 * by quantity available
 *
 * @author Sathvik Swamy - Lab Sec 26
 *
 * @version November 12, 2023
 */
public class SortByQuantity implements Comparator<Product> {  // implements Comparator interface

    /**
     * Compares the quantity available of two products
     * @param p1 the first object to be compared.
     * @param p2 the second object to be compared.
     * @return the difference in quantity available of the two products
     */
    public int compare(Product p1, Product p2) {
        return p1.getQuantity() - p2.getQuantity();
    }
}
