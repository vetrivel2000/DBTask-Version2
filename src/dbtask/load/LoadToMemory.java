package dbtask.load;
import dbtask.account.AccountInfo;

import java.util.HashMap;

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
    public void addOuterMap(AccountInfo object)
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
}
