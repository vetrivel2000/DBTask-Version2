/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/*
 *
 * @author vetri
 */
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Scanner;
import java.util.ArrayList;


import dbtask.customer.CustomerInfo;
import dbtask.account.AccountInfo;
import dbtask.logical.LogicalLayer;

public class TestRunner {
    static Scanner scan = new Scanner(System.in);
    public static void main(String[] args) throws Exception {
        int choice;
        do{
            System.out.println("Enter your choice:");
            System.out.println("1.Add an account\n2.Show AccountInfo\n3.Delete a customer\n4.Delete an account\n5.Deposit amount\n6.Withdraw amount\nPress any other number to exit\n");
            choice = scan.nextInt();
            switch(choice) {
                case 1: {
                    addNewAccount();
                    break;
                }
                case 2: {
                    showAccountInfo();
                    break;
                }
                case 3: {
                    deleteUser();
                    break;
                }
                case 4: {
                    deleteAccount();
                    break;
                }
                case 5:
                {
                    depositAmount();
                    break;
                }
                case 6:
                {
                    withdrawAmount();
                    break;
                }
                default: {
                    LogicalLayer.getInstance().terminateConnection();
                    System.out.println("No such choice!");
                }
            }
        }while(choice<7);
    }
    public static void addNewAccount() throws Exception {

        int choice1;
        do{
            System.out.println("Enter your choice:");
            System.out.println("1.Already a customer\n2.New customer\nPress any other number to exit\n");
            choice1 = scan.nextInt();
            scan.nextLine();
            switch(choice1)
            {
                case 1:
                {
                    existingCustomer();
                    break;
                }
                case 2:
                {
                    newCustomer();
                    break;
                }
                default:
                    System.out.println("No such choice!");
            }
        }while(choice1<3);
    }
    public static void existingCustomer()
    {
            System.out.println("Enter your CustomerId");
            int customerId=scan.nextInt();
            boolean check= LogicalLayer.getInstance().isAlreadyCustomer(customerId);
            if(!check)
            {
                System.out.println("Please enter the correct customerId...");
                return;
            }
            System.out.println("Enter the amount you want to deposit into your new account:");
            double balance=scan.nextDouble();
            scan.nextLine();
            AccountInfo accountObject = LogicalLayer.getAccountObject(customerId,balance);
            try{
               AccountInfo object= LogicalLayer.getInstance().setAccount(accountObject);
               System.out.println(object);
            }
            catch (Exception e)
           {
               System.out.println(e);
           }
    }
    public static void newCustomer() throws Exception {
        int no_Of_Accounts;
        System.out.println("Enter the no.of.accounts you want to add");
        no_Of_Accounts=scan.nextInt();
        scan.nextLine();
        ArrayList<ArrayList> customerData =new ArrayList<>(no_Of_Accounts);
        for(int i=0;i<no_Of_Accounts;i++)
        {
            ArrayList tempList = new ArrayList();
            System.out.println("Enter your name:");
            String name = scan.nextLine();
            System.out.println("Enter the amount you want to deposit:");
            double balance = scan.nextDouble();
            System.out.println("Enter your MobileNo:");
            long mobileNo = scan.nextLong();
            String num = Long.toString(mobileNo);
            if(num.length()!=10)
            {
                System.out.println("Please enter the correct mobile number..!");
                return;
            }
            scan.nextLine();
            CustomerInfo customerObject = LogicalLayer.getCustomerObject(name,mobileNo);
            AccountInfo accountDetails = LogicalLayer.getAccountObject(balance);
            tempList.add(customerObject);
            tempList.add(accountDetails);
            customerData.add(tempList);
        }

        try{
            HashMap<Object,String> inputStatus=LogicalLayer.getInstance().setData(customerData);
            for (HashMap.Entry<Object,String> entry : inputStatus.entrySet())
            {
                System.out.println(entry.getKey()+"="+entry.getValue());
            }
        }
        catch (Exception e)
        {
            System.out.println(e.getMessage());
        }
//        System.out.println("The successfully added accounts are:");
//        System.out.println(list.get(0));
//        System.out.println("The accounts that are failed to add:");
//        System.out.println(list.get(1));
    }
    public static void showAccountInfo() throws Exception
    {
        System.out.println("Enter your CustomerId:");
        int customerId = scan.nextInt();
        boolean flag=LogicalLayer.getInstance().isAlreadyCustomer(customerId);
        if(! flag)
        {
            System.out.println("Please enter the correct customerId...");
            return;
        }
        try{
            HashMap<Integer,HashMap> map = LogicalLayer.getInstance().getDetails(customerId);
            System.out.println(map);
        }
        catch (Exception e)
        {
            System.out.println(e.getMessage());
        }
    }
    public static void deleteUser() throws Exception
    {
        System.out.println("Enter your CustomerId:");
        int customerId = scan.nextInt();
        boolean flag=LogicalLayer.getInstance().isAlreadyCustomer(customerId);
        if(! flag)
        {
            System.out.println("Please enter the correct customerId...");
            return;
        }
        try{
            LogicalLayer.getInstance().deleteCustomer(customerId);
            System.out.println("Your accounts are deleted successfully!");
        }
        catch (Exception e)
        {
            System.out.println(e.getMessage());
        }
    }
    public static void deleteAccount() throws Exception
    {
        System.out.println("Enter your CustomerId:");
        int customerId = scan.nextInt();
        boolean flag=LogicalLayer.getInstance().isAlreadyCustomer(customerId);
        if(! flag)
        {
            System.out.println("Please enter the correct customerId...");
            return;
        }
        System.out.println("Enter your account number:");
        int accountNumber = scan.nextInt();
        boolean check = LogicalLayer.getInstance().isExistingAccount(accountNumber,customerId);
        if(! check)
        {
            System.out.println("Please enter the correct account number");
            return;
        }
        try{
            LogicalLayer.getInstance().deleteAccount(accountNumber,customerId);
        }
        catch (Exception e)
        {
            System.out.println(e.getMessage());
        }
    }
    public static void depositAmount() throws Exception
    {
        System.out.println("Enter your CustomerId:");
        int customerId = scan.nextInt();
        boolean flag=LogicalLayer.getInstance().isAlreadyCustomer(customerId);
        if(! flag)
        {
            System.out.println("Please enter the correct customerId...");
            return;
        }
        System.out.println("Enter your account number:");
        int accountNumber = scan.nextInt();
        boolean check = LogicalLayer.getInstance().isExistingAccount(accountNumber,customerId);
        if(! check)
        {
            System.out.println("Please enter the correct account number");
            return;
        }
        System.out.println("Enter the amount you want to deposit");
        double amount= scan.nextDouble();
        try{
            LogicalLayer.getInstance().addAmount(amount,customerId,accountNumber);
        }
        catch (Exception e)
        {
            System.out.println(e.getMessage());
        }
    }
    public static void withdrawAmount() throws Exception
    {
        System.out.println("Enter your CustomerId:");
        int customerId = scan.nextInt();
        boolean flag=LogicalLayer.getInstance().isAlreadyCustomer(customerId);
        if(! flag)
        {
            System.out.println("Please enter the correct customerId...");
            return;
        }
        System.out.println("Enter your account number:");
        int accountNumber = scan.nextInt();
        boolean check = LogicalLayer.getInstance().isExistingAccount(accountNumber,customerId);
        if(! check)
        {
            System.out.println("Please enter the correct account number");
            return;
        }
        System.out.println("Enter the amount you want to withdraw:");
        double amount= scan.nextDouble();
        boolean checkBalance=LogicalLayer.getInstance().checkSufficientBalance(amount,customerId,accountNumber);
        if(!checkBalance)
        {
            System.out.println("Insufficient Balance!!!");
            return;
        }
        try {
            LogicalLayer.getInstance().subtractAmount(amount,customerId,accountNumber);
        }
        catch (Exception e)
        {
            System.out.println(e.getMessage());
        }
    }
}
