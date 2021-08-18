package dbtask.load;
import dbtask.account.AccountInfo;

import java.util.ArrayList;
import java.util.HashMap;

public enum LoadToMemory {
    INSTANCE;
    private HashMap<Integer, HashMap> map = new HashMap<>();
    private HashMap<Integer, AccountInfo> infoHashMap;
    public void addToMap(AccountInfo accountInfo)
    {
        int customerId=accountInfo.getCustomerId();
        infoHashMap=map.get(customerId);
        if(infoHashMap==null)
        {
            infoHashMap = new HashMap<>();
            map.put(customerId,infoHashMap);
        }
        infoHashMap.put(accountInfo.getAccountNumber(),accountInfo);
    }
    public void addToMap(ArrayList<AccountInfo> list)
    {
        for(AccountInfo accountInfo:list){
            addToMap(accountInfo);
        }
    }
    public boolean isExistingCustomer(int customerId)
    {
        return map.containsKey(customerId);
    }
    public boolean isExistingAccountNumber(int accountNumber,int customerId)
    {
        infoHashMap = map.get(customerId);
        return infoHashMap.containsKey(accountNumber);
    }
    public HashMap getAccountInfo(int customerId)
    {
        return map.get(customerId);
    }
    public HashMap getHashMap()
    {
        return map;
    }
}
