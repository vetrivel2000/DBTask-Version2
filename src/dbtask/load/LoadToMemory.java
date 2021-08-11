package dbtask.load;
import dbtask.account.AccountInfo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

public enum LoadToMemory {
    INSTANCE;
    private HashMap<Integer, HashMap> map = new HashMap<>();
    private HashMap<Integer, AccountInfo> map1;
    public void dbToMap(AccountInfo object)
    {
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
    public boolean isExistingCustomer(int customerId)
    {
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
