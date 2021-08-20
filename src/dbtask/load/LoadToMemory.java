package dbtask.load;
import dbtask.account.AccountInfo;

import java.util.ArrayList;
import java.util.HashMap;

public enum LoadToMemory {
    INSTANCE;
    private HashMap<Integer,HashMap> activeMap = new HashMap<>();
    private HashMap<Integer,HashMap> inActiveMap= new HashMap<>();
    private HashMap<Integer,AccountInfo> infoHashMap;
    private HashMap<Integer,AccountInfo> infoInActiveHashMap;
    public void addToActiveMap(AccountInfo accountInfo)
    {
        int customerId=accountInfo.getCustomerId();
        infoHashMap= activeMap.get(customerId);
        if(infoHashMap==null)
        {
            infoHashMap = new HashMap<>();
            activeMap.put(customerId,infoHashMap);
        }
        infoHashMap.put(accountInfo.getAccountNumber(),accountInfo);
    }
    public void addToActiveMap(ArrayList<AccountInfo> list)
    {
        for(AccountInfo accountInfo:list){
            addToActiveMap(accountInfo);
        }
    }
    public void addToInActiveMap(AccountInfo accountInfo)
    {
        int customerId=accountInfo.getCustomerId();
        infoInActiveHashMap= inActiveMap.get(customerId);
        if(infoInActiveHashMap==null)
        {
            infoInActiveHashMap = new HashMap<>();
            inActiveMap.put(customerId,infoInActiveHashMap);
        }
        infoInActiveHashMap.put(accountInfo.getAccountNumber(),accountInfo);
    }
    public void addToInActiveMap(ArrayList<AccountInfo> list)
    {
        for(AccountInfo accountInfo:list){
            addToInActiveMap(accountInfo);
        }
    }
    public boolean isActiveExistingCustomer(int customerId)
    {
//        System.out.println(activeMap.get(customerId));
        return activeMap.containsKey(customerId);
    }
    public boolean isInActiveExistingCustomer(int customerId)
    {
        return inActiveMap.containsKey(customerId);
    }
    public boolean isActiveExistingAccountNumber(int accountNumber, int customerId)
    {
        infoHashMap = activeMap.get(customerId);
        return infoHashMap.containsKey(accountNumber);
    }
    public boolean isInActiveExistingAccountNumber(int accountNumber, int customerId)
    {
        infoInActiveHashMap = inActiveMap.get(customerId);
        return infoInActiveHashMap.containsKey(accountNumber);
    }
    public HashMap getAccountInfo(int customerId)
    {
        return activeMap.get(customerId);
    }
    public HashMap getHashMap()
    {
        return activeMap;
    }
    public HashMap getInActiveHashMap()
    {
        return inActiveMap;
    }
}
