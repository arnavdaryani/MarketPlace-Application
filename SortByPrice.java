import java.util.Comparator;

/**
 * Compares products by price
 *
 * Compares the products in the arraylist products (in marketplace) by price
 *
 * @author Sathvik Swamy - Lab Sec 26
 *
 * @version November 12, 2023
 */
public class SortByPrice implements Comparator<Product> {  // implements comparator interface

    /**
     * Compares the price of two products
     * @param p1 the first object to be compared.
     * @param p2 the second object to be compared.
     * @return an integer storing the difference in price of the two products
     */
    public int compare(Product p1, Product p2) {
        return (int) (p1.getPrice() - p2.getPrice());
    }
}
