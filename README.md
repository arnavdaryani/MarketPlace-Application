# MarketPlace Application

## Instructions on how to run and compile
<p> Begin by compiling all the classes. <br>
<b>The main method of the program is located in the Marketplace and MarketplaceServer classes. Run the MarketplaceServer class and then the Marketplace class to begin the program. If you wish to test the multithreading functionality, you can run the Marketplace class more than once to open as many different windows as you like.</b> <br>
You should create a new Customer account and a new Seller account to test the different functionalities. <br>
Interact with the program by selecting the apppropraite option from the dropdown or clicking the appropriate buttons. <br>
  
Eg., When you start the program, this is what is displayed in the GUI: <br>
  
![Screenshot 2023-12-09 163847](https://github.com/nkjain05/CS180-Project-5/assets/142682688/3c41147c-a1c0-4f13-ab93-5bddf2dda1b7) <br>
To select the option to login, press the login button on the GUI and proceed. <br>

<p>After logging in or creating an account, a window with a dropdown will appear.</p>

  ![image](https://github.com/nkjain05/CS180-Project-5/assets/142682688/603def61-59dd-43de-8582-ec3b3b970bb4) <br>
Select the appropriate option from the dropdwon to proceed by using the mouse. <br>

<p>The program will inform you when you need to enter text. Please follow the instructions displayed in the GUI. All typing input will be done through the keyboard. <br>
Eg., When you wish to search for a product by the name of the selling store, this is what will come up in the GUI. <br>
  
![image](https://github.com/nkjain05/CS180-Project-5/assets/142682688/f8f909d5-8692-4c92-a5cf-9554a7e35bca) <br>
Please enter the EXACT name of the store if you wish to obtain a result. <br>
Similarly, when removing a product from your cart. <br>
All interactions with the program will be handled in these ways.

Please select signup as a seller first. If you select login, it will just give you a message that you need to create an account first and end the program. Signup as a seller and follow the intsructions displayed. When you finish creating your account, the seller menu will print and you can interact by entering integers. Create as many products or stores as you would like before testing the customer functionality if you want to see the full results. If you wish to test the customer functionality first, to check that no errors are thrown, you may create a Customer account first.
After this, you may test anything as you wish and try every possible combination. 

## Submissions
Sathvik Swamy - Submitted report and presentation on Brightspace <br>
Arnav Daryani - Submitted project on Vocareum

## Description of each class
### <b>Marketplace</b> - 
This is where the main method of the program is located. This is where all the other classes join together <br>
When logged in as a Customer, a menu will display 8 choices described below: <br>
Option 1 - search for a product: the user will be allowed to search for a product with a general keyword or for the name of the selling store. <br>
Option 2 - sort products by price: this calls the compareTo method in the SortByPrice class, which sorts the arraylist of products by ascending price. <br>
Option 3 - sort products by quantity: this calls the compareTo method in the SortByQuantity class, which sorts the arraylist of prodycts by ascending quantity available. <br>
Option 4 - view shopping cart: the customer can view the items in their shopping cart, and the total cost. The customer can then choose to checkout or remove an item. If the customer chooses either of those options, the pertinents product(s) are updated in the products arraylist. <br>
Option 5 - view product page to purchase: the customer can view a products page by entering its name and the name of the selling store. From here, the customer can choose to add the amount they want to their cart. <br>
Option 6 - view purchase history: the customer can view the products they have purchased in the past. <br>
Option 7 - export purchase history: the info of the products purchased by the customer is stored in a file called <i>"(Customer username) _info.txt"</i>. The user can then access this file. <br>
Option 8 - Logout and exit: the customer can logout, all the information required is saved to files and the program terminates. <br>
Once logged in as a Seller, a menu will display with 11 choices described below:  <br>
Option 1 - creating a store: when selecting this option the seller will enter the name of the store they would like to create and 2 files should generate. One with storename.txt which contains the products of the store as they are added, and one with the seller's username _stores.txt which contains a list of stores the seller owns. <br>
Option 2 - deleting a store: when selecting this option the seller is prompted to enter the name of the store they would like to delete and the store file is deleted as well as removed from the list of stores they own. <br>
Option 3 - editing a product: the seller will enter the store name and the product name of the product they would like to edit and this product will be edited in the store file, and the products.txt file. <br>
Option 4 - adding a product: the seller will be prompted to enter the product details in a very specific format and the product will show up in the store file containing the products, and the products.txt file. <br> 
Option 5 - deleting a product: the seller will be prompted to enter the name and store that contains the product they would like to delete and the product will be removed from the store file and the mass list of products (products.txt) <br>
Option 6 - viewing total revenue: The seller will be prompted to enter the store whose revenue they would like to see, and the program will calculate the revenue as the customers buy their products. <br>
Option 7 - viewing the number of items in customer shopping carts: the seller will be prompted to enter the exact username of the user whose cart they would like to view, and the details will be displayed. <br>
Option 8 - Importing products to store via csv file: The seller will have a pre-existing file of products in a .csv file and the program will read the file and parse through and if the product contains a store name it will write each product to the store file (this is a way for the seller to import a mass list of products). <br>
Option 9 - Exporting products to csv file:  if the seller would like to save procuts to a .csv file the program will read through the mass list of products (products.txt) and save them to a file. (this is just a way for seller to save products to a different file, this will create a new file) <br>
Option 10 - view products currently in each store: the seller will be prompted to enter the store whose products they would like to view, and the program will display the list of products for that store.
Option 11 - logout and exit: the program will display a thank you message and save all information via files. <br>
<b>Methods</b> - <br>
login(Scanner sc) - applies the login logic by checking the user's inputted username and password <br>
createUser(Scanner sc) - creates a new user and stores their info to the file <i>users.txt</i> <br>

### MarketplaceServer
This class is responsible for creating the threads each time a new user starts the program then sends information back and forth to the client. This server is hosted on the local computer on a singular port. This allows mu;tiple users to connect to the server and run the program. It is related to the Marketplace and ThreadManager classes.

### ThreadManager
This class handles all of the processing for the client side, using the methods listed in this README, then sends the information back to the client. Thus, no data is accessed by the client-side, only the server. This allows the use of concurrency and multithreading, allowing ,multiple users to run the program at aonce. It is realted to the MarketplaceServer and Marketplace classes.<br>
<b>Methods</b> - <br>
ArrayList<Product> products - Stores all the available products <br>
saveProductsToFile() - saves the ArrayList of products to the file <i>products.txt</i> <br>
readFromFile() - reads the stored products ArrayList from the products.txt file <br>
displayProducts() - displays products from the arraylist products for the user to see <br>
searchForStore(String store) - searches products for a particular store name <br>
searchForItem(String itemSearch) - searches products for a particular keyword <br>
shoppingCartTotal(Customer customer) - finds the total cost of the items in the customer's shopping cart <br>
getUserShoppingCart(Customer customer) - gets the user's shopping cart, stored in the file <i>"(Customer username) _info.txt"</i> <br> 
viewItemPage(String itemName, String store) - Displays the item page with more information about the item <br>
viewPreviousPurchases(Customer customer) - allows the customer to view their previous purchases <br>
addStoreToSellerFile(String storeName, Seller seller) - add a list of stores to the seller's file <br>
addProductToStore(String storeName, Scanner sc, Seller seller) - for the seller to add products to a store file and mass list of products for the seller to view

### Customer
This class represents a customer and contains a number of methods to store and process info. These methods allow the customer to add products to their cart. modify their cart, checkout and view and export their purchase history. This is connected to the ShoppingCart class as each customer has their own shopping cart. Each customer also has a unqiue username, password, and filename <br>
<b>Methods</b> - <br>
Customer(String username, String password) - Constructor for a new customer<br>
Customer(String username, String password, ShoppingCart shoppingCart) - Constructor for a customer with existing info <br>
Getter and setter methods <br>
checkout() - Allows the customer to check out with the products in their shopping cart <br>
previouslyPurchasedFile() -  Stores the customer's previous purchases in a file <i>"(Customer username) purchases.txt"</i> <br>
saveShoppingCart() -  Saves the customer's shopping cart to a file <i>"(Customer username) _info.txt"</i>

### Seller
This class represents a seller and contains a number of methods to store and process info. These methods allow the seller to add, modify, and delete prodcts, create stores, view sales, import and export products to .csv files. This is connected to the Store class as each seller has an arraylist of stores. <br>
<b>Methods</b> - <br>
Seller(String username, String password) - Constructor for a new seller<br>
Seller(String username, String password, ArrayList<String> stores) - Constructor for a seller with existing info <br>
Getter and setter methods <br>
createStore(String storeName) -  allows sellers to create stores, this will add a store to the arraylist <br>
importProductsFromCSV(String filePath) - imports products from CSV file <br>
exportProductsToCSV(String filePath) - write product to the CSV file as an arrayList <br>
writeStoreNamesToFile(String username, ArrayList<String> storeNames) - write store names to each file <br>
readStoreNamesFromFile(String username) - read store names from the user file <br>
readProductsFromFile(String storeName) - read the products from the store file the user selects <br>
writeProductNamesToFile(String storeName, ArrayList<String> productNames) - re-write the products to the store file if the seller chooses to modify a product/delete <br>
readProductsFromFiles() - reads products from mass list of products <br>
riteProductNamesToFiles(ArrayList<String> productNames) - re-writes to the mass list of products <br>

### Store
This class represents a store. Each store contains an arraylist of products sold by it. This class is connected to the Seller and Product classes as each seller has an arraylist of the stores they have created to sell products and each store has an arraylist of products sold by the store. <br>
<b>Methods</b> - <br>
Store(String storeName) - Constructor for a new store <br>
Getter and setter methods <br>
saveProductsToFile() - save products to the store file

### ShoppingCart
This class represents a shopping cart for a customer. Each shopping cart contains an arraylist of products which the customer adds to the cart. This class is connected to the Customer and Product classes as each customer has a shopping cart and each shopping cart contains an arraylist of products in it.<br>
<b>Methods</b> - <br>
ShoppingCart() - Constructor for a new shopping cart <br>
ShoppingCart(ArrayList<Product> listOfProducts) - Constructor with an existing listOfProducts <br>
Getter and setter methods <br>
addProduct(Product product, int quantity) - Adds a product to the shopping cart <br>
deleteProduct(Product product) - Deletes the product from the shopping cart <br>
displayShoppingCart() - Displays the shopping cart to the customer <br>
toString() - he way in which the shopping cart is stored in a file

### Product
This class is the "base" class, it represents a product. Each product has a name, selling store, description, quantity, price and a list of customers who have purchased the product before. This class is connected to every other class as the program revolves around creating, storing, viewing and modifying products. <br>
<b>Methods</b> - <br>
Product(String productName, String storeName, String description, double price, int quantity) - Constructor for a new product <br>
Product(String productName, String storeName, String description, double price, int quantity, int quantitySold) - Constructor for a product with existing information <br>
equals(Product product) - checks if two products are the same <br>
Getter and setter methods <br>
listInShoppingCart() - Lists the product in a particular format to be stored <br>
listInFile() - Lists the product in a particular format to be stored <br>
displayProductPage() - Displays the product's page with all the information <br>
toString() - Displays the product to the customer in the main interface

### SortByQuantity
This class implements the Comparator interface to sort the arraylist of products. Compares the products in the arraylist products (in marketplace) by quantity available. This class is connected to the Product class and Marketplace class in order to sort the main arraylist of products. <br>
<b>Methods</b> - <br>
compare(Product p1, Product p2) - Compares the quantity available of two products

### SortByPrice
This class implements the Comparator interface to sort the arraylist of products. Compares the products in the arraylist products (in marketplace) by price.  This class is connected to the Product class and Marketplace class in order to sort the main arraylist of products. <br>
<b>Methods</b> - <br>
compare(Product p1, Product p2) - Compares the price of two products

## GUI implementation
This program uses an interactive JOptionPane to dislpay the customer and seller menus and all of the functionality of the program is done through soley the gui. <br>

## Network IO
The marketplace.java class is the client, while the marketplaceSever.java is the server. The user input will be conducted in the client throught the GUI and then sent to the server for processing. Then the server will send the info back to the client to display to the user. <br>

## Concurrency
Multiple users are able to login to the program at the same time and each user can do their own thing, and each user will be able to see the changes the other user has made at the same time <br> 

