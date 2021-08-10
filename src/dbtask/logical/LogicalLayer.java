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
    public void setAccount(AccountInfo object)
    {
        DataBase.getInstance().accountCreate(object);
    }
    public void setData(ArrayList<ArrayList> list) throws Exception {

        Iterator iterate = list.iterator();
        while (iterate.hasNext()) {
            ArrayList list1 = (ArrayList) iterate.next();
            int customerId=DataBase.getInstance().customerCreate((CustomerInfo) list1.get(0));
            AccountInfo object=(AccountInfo)list1.get(1);
            object.setCustomerId(customerId);
            DataBase.getInstance().accountCreate(object);
        }
    }
    public void terminateConnection()
    {
        DataBase.closeConnection();
    }
    public void loadMap()
    {
        LoadToMemory.getInstance().addIntoMap(DataBase.getInstance().storeIntoList());
    }
    public boolean isAlreadyCustomer(int customerId)
    {
        return LoadToMemory.getInstance().isExistingCustomer(customerId);
    }
    public HashMap getDetails(int customerId)
    {
        return LoadToMemory.getInstance().getAccountInfo(customerId);
    }
}
