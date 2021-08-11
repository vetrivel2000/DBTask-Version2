package dbtask.logical;


import dbtask.account.AccountInfo;
import dbtask.customer.CustomerInfo;
import dbtask.databasemanagement.DataBase;
import dbtask.load.LoadToMemory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

public class LogicalLayer {
    private static LogicalLayer object=null;
    private final DataBase dbobject = new DataBase();
    private LogicalLayer(){
        loadMap();
    }
    public static LogicalLayer getInstance()
    {
        if(object==null)
            object = new LogicalLayer();
        return object;
    }
    public static CustomerInfo getCustomerObject(String name,long mobileNo)
    {
        CustomerInfo object = new CustomerInfo();
        object.setName(name);
        object.setMobileNo(mobileNo);
        return object;
    }
    public static AccountInfo getAccountObject(double balance)
    {
        return getAccountObject(-1,balance);
    }
    public static AccountInfo getAccountObject(int customerId,double balance)
    {
        AccountInfo object = new AccountInfo();
        if(customerId!=-1)
        {
            object.setCustomerId(customerId);
        }
        object.setBalance(balance);
        return object;
    }
    public AccountInfo setAccount(AccountInfo object) throws Exception
    {
        int accountNumber=dbobject.accountCreate(object);
        object.setAccountNumber(accountNumber);
        LoadToMemory.INSTANCE.dbToMap(object);
        return object;
    }
    public ArrayList setData(ArrayList<ArrayList> list) {
        ArrayList<ArrayList> correctDetails = new ArrayList<>();
        ArrayList<ArrayList> wrongDetails = new ArrayList<>();
        ArrayList<ArrayList> enteredDetails = new ArrayList<>();
        Iterator iterate = list.iterator();
        while (iterate.hasNext()) {
            ArrayList list1 = (ArrayList) iterate.next();
            CustomerInfo object=(CustomerInfo) list1.get(0);
            AccountInfo object1 =(AccountInfo)list1.get(1);
            ArrayList tempList = new ArrayList();
            ArrayList tempList1 = new ArrayList();
            try{
                int customerId=dbobject.customerCreate(object);
                object.setCustomerId(customerId);
                object1.setCustomerId(customerId);
                try{
                    int accountNumber=dbobject.accountCreate(object1);
                    object1.setAccountNumber(accountNumber);
                    LoadToMemory.INSTANCE.dbToMap(object1);
                    tempList.add(object);
                    tempList.add(object1);
                    correctDetails.add(tempList);
                }
                catch (Exception e)
                {
                    dbobject.deleteCustomer(object.getCustomerId());
                    tempList1.add(object);
                    tempList1.add(object1);
                    wrongDetails.add(tempList1);
                }
            }
            catch (Exception e)
            {
                tempList1.add(object);
                tempList1.add(object1);
                wrongDetails.add(tempList1);
            }
            enteredDetails.add(correctDetails);
            enteredDetails.add(wrongDetails);
        }
        return enteredDetails;
    }
    public void terminateConnection()
    {
        DataBase.closeConnection();
    }
    public void loadMap()
    {
        ArrayList<AccountInfo> list=dbobject.storeIntoList();
        LoadToMemory.INSTANCE.addIntoMap(list);
    }
    public boolean isAlreadyCustomer(int customerId)
    {
        boolean key =LoadToMemory.INSTANCE.isExistingCustomer(customerId);
        return key;
    }
    public HashMap getDetails(int customerId)
    {
        HashMap<Integer,HashMap> map =LoadToMemory.INSTANCE.getAccountInfo(customerId);
        return map;
    }
}
