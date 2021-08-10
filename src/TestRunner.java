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
    public static void main(String[] args) throws Exception
    {
        Scanner scan = new Scanner(System.in);
        int choice;
        do{
            System.out.println("Enter your choice:");
            System.out.println("1.Add an account\n2.Show AccountInfo\n");
            choice = scan.nextInt();
            switch(choice)
            {
                case 1:
                {
                    int choice1;
                    do{
                        System.out.println("Enter your choice:");
                        System.out.println("1.Already a customer\n2.New customer\n");
                        choice1 = scan.nextInt();
                        scan.nextLine();
                        int no_Of_Accounts;
                        switch(choice1)
                        {
                            case 1:
                            {
                                AccountInfo accountObject = new AccountInfo();
                                System.out.println("Enter your CustomerId");
                                accountObject.setCustomerId(scan.nextInt());
                                System.out.println("Enter the amount you want to deposit into your new account:");
                                accountObject.setBalance(scan.nextDouble());
                                scan.nextLine();
                                LogicalLayer.getInstance().setAccount(accountObject);
                                break;
                            }
                            case 2:
                            {
                                System.out.println("Enter the no.of.accounts you want to add");
                                no_Of_Accounts=scan.nextInt();
                                scan.nextLine();
                                ArrayList<ArrayList> customerData =new ArrayList<>(no_Of_Accounts);
                                for(int i=0;i<no_Of_Accounts;i++)
                                {
                                    ArrayList tempList = new ArrayList();
                                    CustomerInfo customerObject = new CustomerInfo();
                                    AccountInfo accountDetails = new AccountInfo();
                                    System.out.println("Enter your name:");
                                    customerObject.setName(scan.nextLine());
                                    System.out.println("Enter the amount you want to deposit:");
                                    accountDetails.setBalance(scan.nextDouble());
                                    System.out.println("Enter your MobileNo:");
                                    customerObject.setMobileNo(scan.nextLong());
                                    scan.nextLine();
                                    tempList.add(customerObject);
                                    tempList.add(accountDetails);
                                    customerData.add(tempList);
                                }
                                    LogicalLayer.getInstance().setData(customerData);
                                break;
                            }
                            default:
                                System.out.println("No such choice!");
                        }
                    }while(choice1<3);
                    break;
                }
                case 2:
                {
                    boolean flag=true;
                    do {
                        LogicalLayer.getInstance().loadMap();
                        System.out.println("Enter your CustomerId:");
                        int customerId = scan.nextInt();
                        flag=LogicalLayer.getInstance().isCustomerId(customerId);
                        if(flag==true)
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
                    break;
                }
                default: {
                    LogicalLayer.getInstance().terminateConnection();
                    System.out.println("No such choice!");
                }
            }
        }while(choice<3);
    }
}
