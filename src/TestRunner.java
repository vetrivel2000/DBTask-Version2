/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/*
 *
 * @author vetri
 */
import java.util.Scanner;
import java.util.ArrayList;


import dbtask.customer.CustomerInfo;
import dbtask.account.AccountInfo;
import dbtask.logical.LogicalLayer;

public class TestRunner {
    static Scanner scan = new Scanner(System.in);
    public static void main(String[] args) throws Exception
    {
        int choice;
        do{
            System.out.println("Enter your choice:");
            System.out.println("1.Add an account\n2.Show AccountInfo\n");
            choice = scan.nextInt();
            switch(choice)
            {
                case 1:
                {
                    addNewAccount();
                    break;
                }
                case 2:
                {
                    showAccountInfo();
                    break;
                }
                default: {
                    LogicalLayer.getInstance().terminateConnection();
                    System.out.println("No such choice!");
                }
            }
        }while(choice<3);
    }
    public static void addNewAccount() throws Exception
    {
        int choice1;
        do{
            System.out.println("Enter your choice:");
            System.out.println("1.Already a customer\n2.New customer\n");
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
        boolean check = true;
        do {

            System.out.println("Enter your CustomerId");
            int customerId=scan.nextInt();
            check=LogicalLayer.getInstance().isAlreadyCustomer(customerId);
            if(check)
            {
                System.out.println("Enter the amount you want to deposit into your new account:");
                double balance=scan.nextDouble();
                scan.nextLine();
                AccountInfo accountObject = LogicalLayer.getAccountObject(customerId,balance);
                LogicalLayer.getInstance().setAccount(accountObject);
                break;
            }
            else
            {
                System.out.println("Please enter the correct customerId...");
                check=true;
            }
        }while (check);
    }
    public static void newCustomer() throws Exception
    {
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
            //customerObject.setName(scan.nextLine());
            System.out.println("Enter the amount you want to deposit:");
            double balance = scan.nextDouble();
            //accountDetails.setBalance(scan.nextDouble());
            System.out.println("Enter your MobileNo:");
            long mobileNo = scan.nextLong();
            //customerObject.setMobileNo(scan.nextLong());
            scan.nextLine();
            CustomerInfo customerObject = LogicalLayer.getCustomerObject(name,mobileNo);
            AccountInfo accountDetails = LogicalLayer.getAccountObject(balance);
            tempList.add(customerObject);
            tempList.add(accountDetails);
            customerData.add(tempList);
        }
        LogicalLayer.getInstance().setData(customerData);
        //System.out.println(customerData);
    }
    public static void showAccountInfo()
    {
        boolean flag=true;
        do {
            LogicalLayer.getInstance().loadMap();
            System.out.println("Enter your CustomerId:");
            int customerId = scan.nextInt();
            flag=LogicalLayer.getInstance().isAlreadyCustomer(customerId);
            if(flag)
            {
                System.out.println(LogicalLayer.getInstance().getDetails(customerId));
                break;
            }
            else
            {
                System.out.println("Please enter the correct customerId...");
                flag=true;
            }
        }while(flag);
    }
}
