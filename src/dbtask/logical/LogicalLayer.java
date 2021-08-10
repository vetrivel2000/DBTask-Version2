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
    private LogicalLayer(){}
    public static LogicalLayer getInstance()
    {
        if(object==null)
            object = new LogicalLayer();
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
            DataBase.getInstance().customerCreate((CustomerInfo) list1.get(0), (AccountInfo) list1.get(1));
        }
    }
    public void terminateConnection()
    {
        DataBase.closeConnection();
    }
    public void loadMap()
    {
        ArrayList<AccountInfo> list = DataBase.getInstance().storeIntoMap();
        LoadToMemory.getInstance().addOuterMap(list);
    }
    public HashMap getDetails(int customerId)
    {
        HashMap<Integer,AccountInfo> map = LoadToMemory.getInstance().getAccountInfo(customerId);
        return map;
    }
}
