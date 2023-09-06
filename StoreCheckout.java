//TO DO: create GUI


import java.io.File;
import java.io.FileNotFoundException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner; //imports Scanner class
//import java.sql.*;  //import sql for database storage of store items (MAY NOT USE)


public class StoreCheckout {

    //Method to check if input is integer
    public static boolean isInteger(String input){
        //try catch is used so there is no error when input is not an integer
        try{
            Integer.parseInt(input);
            return true;
        }
        //NumberFormatException occurs when an attempt is made to convert a string with an incorrect format to a numeric value
        catch(NumberFormatException e) {
            return false;
        }
    }

    //method to get customer name from user
    public static String customerName(){
        Scanner userInput = new Scanner(System.in);
        System.out.println("Please enter your first and last name: ");
        //takes in entire customer name, may cause issues with extremely long names but otherwise fine
        return userInput.nextLine();
    }

    //method to check input for double
    public static boolean isDouble(String input){
        //try catch is used so there is no error when input is not a double
        try{
            Double.parseDouble(input);
            return true;
        }
        //NumberFormatException occurs when an attempt is made to convert a string with an incorrect format to a numeric value
        catch(NumberFormatException e) {
            return false;
        }
    }

    //Method to retrieve only integer values from input
    public static int getInt(){
        Scanner scan = new Scanner(System.in);
        int input;
        //loop until user inputs valid integer
        do {
            while (!scan.hasNextInt()) {     //!scan.hasNextInt() returns false unless the input is an integer
                //asks the user to input a valid integer
                scan.next();
                System.out.print("Please enter valid amount: ");
            }
            //if integer is negative it goes back to the start of the loop and asks the user to input valid integers
            input = scan.nextInt();
            if (input < 0){
                System.out.print("Negative values are not accepted!\nPlease enter valid amount: ");
                continue;
            }
            //returns value if all the checks are passed and value is positive integer
            else return input;
        } while (true);
        //return input;
    }

    //method asking user if they are paying in full or installments
    public static boolean checkFullPayment(){
        Scanner input = new Scanner(System.in);
        System.out.println("\n*Paying in full provides a 5% discount*");
        System.out.println("Will you be paying in full or in installments? (Please type 'full' or 'installments')");
        //loop to validate input
        do {
            String choice = input.nextLine();
            if (choice.toLowerCase().equals("full")){
                return true;
            }
            else if (choice.toLowerCase().equals("installments")){
                return false;
            }
            else {
                System.out.println("Invalid input! Please enter 'full' or 'installments' to select your choice.");
                continue;
            }
        } while(true);
    }

    //method to display store catalog
    public static void catalogDisplay(ArrayList<StoreItem> itemList, double salesTaxMult){
        System.out.printf("|%-15s|%-15s|%-100s|%s|\n", "Code","Item","Description","Cost");
        System.out.println("-------------------------------------------------------------------------------------------" +
                "-------------------------------------------------");
        //loops through itemList array which contains all the StoreItems, prints out string format of each
        for (StoreItem x: itemList){
            System.out.print(x.toString());
        }
        System.out.println("-------------------------------------------------------------------------------------------" +
                "-------------------------------------------------");
        //Display sales tax, current items in basket, total cost for basket
        System.out.printf("Current sales tax multiplier is: %.2f\n", salesTaxMult);
        System.out.println("Current items in basket: ");
        for (StoreItem x : itemList){
            System.out.println(x.toStringCheckout());
        }
        System.out.printf("Current total in checkout basket (before tax): $%.2f\n\n", StoreItem.getTotal(itemList));

    }

    //method for displaying the checkout window
    public static void checkoutDisplay(ArrayList<StoreItem> itemList, double salesTaxMult, boolean isFullPayment, String customerName){
        for (StoreItem x: itemList){
            System.out.println(x.toStringCheckout());
        }
        //applies 5% discount if user pays in full
        if (isFullPayment){
            System.out.println("\n5% discount applied for paying in full!");
            StoreItem.total = StoreItem.total * 0.95;
        }
        System.out.println("-------------------------------------------------------------------------------------------" +
                "-------------------------------------------------");
        System.out.printf("Total before tax: $%.2f", StoreItem.total);
        System.out.printf("\nCurrent sales tax multiplier: %.2f", salesTaxMult);
        System.out.printf("\n\nThe grand total for %s is: $%.2f\n", customerName, StoreItem.total*salesTaxMult);
    }
    //method to check command line arguments for valid password

    //method for reading store items from file ("itemlist.txt" file must be in the same root folder as program)
    //format for file must be "item name - item description - item cost", quotations aren't necessary but the '-' is how each element is parsed.
    public static List<String> parseFile(String data) {
        List<String> convertedFileList = Arrays.asList(data.split("-", -1));
        int i = 0;
        for (String temp : convertedFileList){
            String trimList = convertedFileList.get(i).trim();  //trim list to remove whitespace
            convertedFileList.set(i, trimList);
            i++;
        }
        return convertedFileList;
    }

    //CURRENTLY UNUSED, .txt FILE CONTAINS ALL ITEMS FOR CATALOG
    /*public static void passwordCheck(String[] args, ArrayList<StoreItem> itemList){
        //formats command line arguments
        //joins command line arguments to one string then splits according to '|' to parse them
        String temp = String.join(" ", args);
        String[] output = temp.split("\\|");    //
        int i = 0;
        for (String str: output){
            //trims unnecessary whitespace around the strings to help parse
            str = str.trim();
            output[i] = str;
            i += 1;
        }
        //verifies password and amount of items being added so there are enough arguments to fully add an item
        if (output[0].equals("MCS3603") && output.length > 3) {
            System.out.println("Password is correct!");
            System.out.println("Items successfully added: ");
            //creates StoreItem objects and adds them to ArrayList
            //for loop doesn't go through if there are not enough arguments to fully add an item
            for (i = 1; i + 2 < output.length; i++) {
                String tempName = output[i++];
                //following if and else if check to see what positions the cost and descriptions are in
                //both add a new StoreItem object to the ArrayList "itemList"
                if (isDouble(output[i])) {
                    double tempCost = Double.parseDouble(output[i++]);
                    String tempDesc = output[i];
                    itemList.add(new StoreItem(tempName, tempDesc, tempCost));
                    //creates temp variable to output added items to the user
                    StoreItem tempItem = itemList.get(itemList.size() - 1);
                    System.out.print(tempItem.toString());
                } else if (isDouble(output[i + 1])) {
                    String tempDesc = output[i++];
                    double tempCost = Double.parseDouble(output[i]);
                    itemList.add(new StoreItem(tempName, tempDesc, tempCost));
                    //creates temp variable to output added items to the user
                    StoreItem tempItem = itemList.get(itemList.size() - 1);
                    System.out.print(tempItem.toString());
                }
                else {
                    System.out.println("Invalid command line arguments! Exiting now." + output[i]);
                    System.exit(-1);
                }
            }
        }
    }*/

    public static void main(String[] args){
        //Scanner is used to detect user inputs
        Scanner userInput = new Scanner(System.in);
        String customerName = "";
        //declares sales tax multiplier
        double salesTaxMult = 1.06;
        //create new ArrayList "itemList" to hold all the objects of type StoreItem so they can be iterated through
        ArrayList<StoreItem> itemList = new ArrayList<StoreItem>();
        //read file to retrieve store item details
        try {
            File fileIn = new File("itemlist.txt");
            Scanner myReader = new Scanner(fileIn);
            while (myReader.hasNextLine()) {
                String data = myReader.nextLine();
                List<String> parsedData = parseFile(data);

                itemList.add(new StoreItem(parsedData.get(0), parsedData.get(1), Double.parseDouble(parsedData.get(2))));
            }
            catalogDisplay(itemList, 1.06);
        } catch (FileNotFoundException e) {
            System.out.println("Error occurred when attempting to read input file.");
            e.printStackTrace();
            System.exit(-1);
        }

        //run passwordCheck method to check if more items need to be added before interacting with the user
        //*CURRENTLY UNUSED, .txt FILE IS USED TO ADD ITEMS TO CATALOG NOW*
        //passwordCheck(args, itemList);

        //run customerName() function to get the name of the customer
        customerName = customerName();

        //main loop for catalog and user actions
        do {
            //catalog display method call
            catalogDisplay(itemList, salesTaxMult);
            //asks user for their initial action, either adding product to checkout basket or proceeding to checkout
            System.out.print("Enter product choice or proceed to checkout (for product choices, input product number only. To checkout, type 'checkout'): ");
            String itemChoice = userInput.nextLine();
            //if statement to check if input is a valid number
            if (isInteger(itemChoice) && Integer.parseInt(itemChoice) > 0) {
                int checkChoice = Integer.parseInt(itemChoice);

                //if statement makes sure input isn't greater than the amount of added items
                if (checkChoice < StoreItem.numItems + 1){
                    //calls the addItem method in the StoreItem class for the currently selected item
                    itemList.get(checkChoice - 1).addItem();
                    continue;
                }
                //if user input is invalid nothing happens and the loop is continued into the next iteration so the user can try again
                else{
                    System.out.println("\n\nInvalid user input! Please try again with valid inputs.\n");
                    continue;
                }
            }
            //exit is treated as the checkout, displays items bought and cost of them along with total and sales tax
            else if (itemChoice.toLowerCase().equals("checkout")){
                //calls method to ask user if they are paying in full or in payments
                boolean isFullPayment = checkFullPayment();
                //calls checkoutDisplay method to display the checkout box/options to the user
                checkoutDisplay(itemList, salesTaxMult, isFullPayment, customerName);

                break;
            }
            //if user input is invalid nothing happens and the loop is continued into the next iteration so the user can try again
            else{
                System.out.println("\n\n\nInvalid User Input! Please try again with valid inputs.\n");
                continue;
            }
            //loop continues forever, the only reason to exit loop would be when user wants to check out
            //exit is the only block with the break command, all others continue
        } while (true);

    }

}

class StoreItem {
    //numItems tracks the amount of items added the catalog, used to determine the item's Code
    static int numItems = 0;
    //static "total" variable tracks the total cost for the users entire basket
    static double total = 0;
    //product name and product description are declared as attributes
    String pName, pDescription;
    //product cost and product total are declared as attributes
    //product total tracks the total cost for each item, not the total
    double pCost, pTotal;
    //product code and the amounts of each product are declared and tracked
    int pCode, amount;
    //discount boolean is declared to track whether each item qualifies for the 10% discount
    boolean discount = false;

    //default constructor
    StoreItem(){
        numItems += 1;
        pName = "PLACEHOLDER NAME";
        pDescription = "PLACEHOLDER DESCRIPTION";
        pCost = 0.00;
        pCode = numItems;
    }

    //constructor with expected parameters
    StoreItem(String name, String description, double cost){
        numItems += 1;
        pName = name;
        pDescription = description;
        pCost = cost;
        pCode = numItems;
    }

    //method to set the name of the item (unused but available if needed later)
    public void setName(String name){
        pName = name;
    }

    //method to retrieve the name of the item
    public String getName(){
        return pName;
    }

    //method to set the description of the item (unused but available if needed later)
    public void setDescription(String description){
        pDescription = description;
    }

    //method to get the description of the item (unused but available if needed later)
    public String getDescription(){
        return pDescription;
    }
    //method to get the amount of the item (unused but available if needed later)
    public void setAmount(int num){
        amount = num;
    }
    //method to set the amount of the item (unused but available if needed later)
    public int getAmount(){
        return amount;
    }

    //method to calculate and retrieve the total for a specific product, also sets discount
    public double getPTotal(){
        //apply discount if the amount of the item is greater than 10
        if (amount >= 10){
            discount = true;
            pTotal = pCost * amount * 0.9;
        }
        //take off the discount if amount is less than ten
        else {
            discount = false;
            pTotal = pCost * amount;
        }
        return pTotal;
    }

    //method to display object as a string, used mainly in catalog
    public String toString() {
        return String.format("|%-15s|%-15s|%-100s|$%.2f|\n", pCode, pName, pDescription, pCost);

    }

    //alternate method to display object as string, used mainly during checkout
    public String toStringCheckout() {
        //different output if discount is applied
        if (discount) {
            return String.format("Total cost for %d %ss: $%.2f *10%% discount applied* ", amount, pName, pTotal);
        }
        else{
            return String.format("Total cost for %d %ss: $%.2f", amount, pName, pTotal);
        }
    }

    //method to add items to the basket
    public void addItem(){
        //ask user to specify amount of item being added to cart
        System.out.printf("What is the amount of %ss that you would like to add to your cart? ", getName());
        //variable "newAmount" calls getInt() method to only accept positive integer values
        int newAmount = StoreCheckout.getInt();
        //calculates amount and product total
        amount = amount + newAmount;
        pTotal = getPTotal();

        //notify user if discount is applied, then notify of items added regardless of discount
        if(discount) System.out.print("Discount applied!");
        System.out.printf("\nYou have added %d %ss to your cart which increases the total cost by: $%.2f\n\n", amount, pName, pCost * amount);

    }

    //method to get the total cost for each "StoreItem" object in the "itemList" ArrayList
    //method is static because it is the same for every object
    public static double getTotal(ArrayList<StoreItem> itemList){
        //reset total
        total = 0;
        //iterates through "itemList" ArrayList which contains StoreItem objects
        for (StoreItem x: itemList){
            //calls getPTotal() method to retrieve each product total then adds it to grand total
            total += x.getPTotal();
        }

        return total;
    }
}

