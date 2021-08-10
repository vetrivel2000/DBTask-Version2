package dbtask.load;
import dbtask.account.AccountInfo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

public class LoadToMemory {
    public HashMap<Integer, HashMap> map = new HashMap<>();
    public HashMap<Integer, AccountInfo> map1;
    private static LoadToMemory object = null;
    private LoadToMemory(){}
    public static LoadToMemory getInstance()
    {
        if(object==null)
            object = new LoadToMemory();
        return object;
    }
    public void addIntoMap(ArrayList<AccountInfo> list)
    {
        Iterator iterate = list.iterator();
        while(iterate.hasNext())
        {
            AccountInfo object=(AccountInfo) iterate.next();
            if(!(map.containsKey(object.getCustomerId())))
            {
                map1 = new HashMap<>();
                map1.put(object.getAccountNumber(),object);
                map.put(object.getCustomerId(),map1);
            }
            else
            {
                map1=map.get(object.getCustomerId());
                map1.put(object.getAccountNumber(),object);
            }
        }
    }
    public boolean isCustomerId(int customerId)
    {
        AccountInfo object = new AccountInfo();
        if(map.containsKey(customerId))
        {
            return true;
        }
        else
        {
            return  false;
        }
    }
    public HashMap getAccountInfo(int customerId)
    {
        return map.get(customerId);
    }
}
