SELLER TESTING:


Test 1 - Create account 

User launches application

User selects the create an account button

User selects either customer or seller from the dropdown menu and press ok/hit enter

User enters desired username via textbox and press ok/hit enter

User enters desired password via textbox and press ok/hit enter

User re enters same password via textbox and press ok/hit enter

Expected result: A file will be created or it may already exist called users.txt which stores the user info in this order: username,password,Seller/Customer. Then the program will end. 

Test Status: Passed 

Test 2 - Login
User launches application 

User selects login button 

User enters username via textbox and press ok/hit enter

User enters password via textbox and press ok/hit enter

Either seller or customer menu will pop up depending on whether the user is a seller or customer

Expected Result:  Application verifies the user's username and password and loads their menu automatically. If the user enters the incorrect username or password an error message will pop up and they will repeat the process. 

Test Status: Passed


Test 3 - Seller menu - create Store option

Login as a seller - seller username and password

Select option called “create store” from dropdown menu

Enter name of store you would like to create and press ok/enter

Expected result: 2 files are created, one with store name.txt, which is empty for now and will contain product information, and one with the username_stores.txt which contains the name of the store you have just created.

Test status: passed

Test 3 - Seller menu - Delete Store Option

Login as a seller - seller username and password

Select option called “delete store” from dropdown menu

Enter the name of the store you would like to delete and press ok/enter

Enter the username associated with the account and press ok/enter

Expected result: The store file.txt will be deleted and the store name will be removed off of the username_stores.txt file, and a message saying the deletion was successful will pop up. 

Test status: passed


Test 4 - Seller menu - Edit Product Option

Login as a seller - seller username and password

Select option called “Edit a product” from dropdown menu

Enter the name of the product you would like to edit (there must already be an existing product) 

Enter the name of the store that contains the product you would like to edit

Current product information will display and press ok/enter

Enter the new details of the product in the exact format it asks for and press okay

Product updated successfully message will pop up (if the product does not exist an error message will pop up) 

Expected result: After the success message pops up the product should be edited in 2 different places, the store.txt file that contains the list of products specific to that store, and the mass list of products called products.txt

Test status: passed


Test 5 - Seller menu - Add product option

Login as a seller - seller username and password

Select option called “Add product” from dropdown menu

Enter the name of the store you would like to add the product to 

Enter the product details in the exact format the program asks

Success message will pop up 

Expected Result: After the success message pops up, the product will be added to 2 files, the store.txt file, and the products.txt file. 

Test status: passed


Test 6 - Seller Menu - Delete product Option 

Login as a seller - seller username and password

Select option called “delete product” from dropdown menu

Enter the exact name of the store that has the product you would like to delete

Enter the exact name of the product you would like to delete

A success message will pop up 

Expected Result: After the success message pops up the product will be deleted from 2 files, the store.txt file, and the products.txt file

Test status: passed


Test 7 - Seller Menu - View Sales option 

Login as a seller - seller username and password

Select option called “view sales”

Enter the name of the store you would like to view the sales for 

A message will display showing the total revenue for that store


Expected Result: The message will display showing the total amount of revenue for the selected store

Test status: Passed.


Test 8 - Seller Menu - view number of products currently in customer shopping carts option 

Login as a seller - seller username and password

Select option called “View number of products currently in customer shopping carts”

Enter the exact username of the customer whose cart you would like to view

A message will display showing the shopping cart information for that specific user 

Expected Result: A message will display showing the shopping cart information for the specified user. 

Test Status: Passed.



Test 9 - Seller Menu - Import products to store(csv file) option 

IMPORTANT NOTE: you MUST have a previously loaded CSV file containing products already in the database to import products from this file 

Login as a seller - seller username and password

Select option called “Import products to store(csv file)” 

Enter the exact csv file containing the products you would like to import

A success message will pop up 

Expected result: After the success message pops up the products will be read from the csv file given and wrote to both the store file, and the products.txt file, this will make sure the products are imported

Test Status: passed

Test 10 - Seller Menu - Export Products to csv file

Login as a seller - seller username and password

Select option called “Export Products to csv file”

Enter the desired file name to export the products to (MUST BE IN .CSV FORMAT)

A new file will be created with the desired file name and a success message will pop up

Expected Result: A new .csv file will be created that stores all the information from the products.txt file into the new file. 

Test status: Passed 


Test 11 - Seller Menu - View products currently in each store 

Login as a seller - seller username and password

Select option front the menu called “View products currently in each store”

Enter the name of the store wanted to view products for 

A message will display showing the products in the store

Expected result: Message will display stating the products currently in the specified store

Test status: Passed 

Test 12 - Seller Menu - Logout and exit option 

Login as a seller - seller username and password

Select the option from the menu called “logout and exit”

Message will display saying “Thank you for using the marketplace”

Expected Result: Thank you message will pop up and the program will end. 

Test status: Passed




CUSTOMER TESTING:








