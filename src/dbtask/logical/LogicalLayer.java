package dbtask.logical;


import dbtask.account.AccountInfo;
import dbtask.customer.CustomerInfo;
import dbtask.exception.ExceptionHandler;
import dbtask.load.LoadToMemory;
import dbtask.persistence.PersistenceLayer;

import java.io.FileReader;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Properties;

public class LogicalLayer {
    private static LogicalLayer object=null;
    private PersistenceLayer dbobject;
    private LogicalLayer(){
        try {
            FileReader fileReader = new FileReader("connection.properties");
            Properties p = new Properties();
            p.load(fileReader);
            String className=p.getProperty("dbname");
            dbobject= (PersistenceLayer) Class.forName(className).newInstance();
            loadActiveMap();
            loadInActiveMap();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
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
        int accountNumber=dbobject.createAccount(object);
        object.setAccountNumber(accountNumber);
        LoadToMemory.INSTANCE.addToActiveMap(object);
        return object;
    }
    public HashMap setData(ArrayList<ArrayList> list) throws Exception {
        for (int i=0;i<list.size();i++)
        {
            CustomerInfo customerObject = (CustomerInfo) list.get(i).get(0);
            AccountInfo accountObject = (AccountInfo) list.get(i).get(1);
            if(customerObject.getName()==null)
            {
                throw new ExceptionHandler("Some details missing");
            }
            if(customerObject.getMobileNo()==0)
            {
                throw new ExceptionHandler("Some details missing");
            }
            if(accountObject.getBalance()==0)
            {
                throw new ExceptionHandler("Some details missing");
            }
        }
        HashMap<Object,String> status = new HashMap<>();
          ArrayList<ArrayList> detailsList = null;
          ArrayList<ArrayList> detailsList1=null;
          ArrayList<Integer> indexes= new ArrayList<>();
          try{
              detailsList=dbobject.createCustomer(list);
              ArrayList<Integer> customerSuccessRate =detailsList.get(0);
              ArrayList<Integer> customerId=detailsList.get(1);

              for(int i=0;i<customerSuccessRate.size();i++) {
                  if (customerSuccessRate.get(i) < 0) {
                      CustomerInfo customerObject = (CustomerInfo) list.get(i).get(0);
                      status.put(customerObject, "Failure");
                      indexes.add(i);
                  }
              }
              for(int i:indexes)
              {
                  list.remove(i);
              }
              for(int i=0;i<customerId.size();i++){
                  AccountInfo accountObject = (AccountInfo) list.get(i).get(1);
                  CustomerInfo customerObject = (CustomerInfo) list.get(i).get(0);
                  accountObject.setCustomerId(customerId.get(i));
                  customerObject.setCustomerId(customerId.get(i));
                  status.put(customerObject, "Success");
              }
              try{
                  detailsList1 = dbobject.createAccount(list);
                  ArrayList<Integer> accountSuccessRate = detailsList1.get(0);
                  ArrayList<Integer> accountNumbers=detailsList1.get(1);
                  for(int j=0;j<accountSuccessRate.size();j++)
                  {
                      if(accountSuccessRate.get(j)<0)
                      {
                          AccountInfo accountObject = (AccountInfo) list.get(j).get(1);
                          status.put(accountObject,"Failure");
                      }
                  }
                  for(int i=0;i<accountNumbers.size();i++){
                      AccountInfo accountObject1 =(AccountInfo) list.get(i).get(1);
                      accountObject1.setAccountNumber(accountNumbers.get(i));
                      status.put(accountObject1,"Success");
                      LoadToMemory.INSTANCE.addToActiveMap(accountObject1);
                  }
              }
                      catch (Exception e)
                      {
                          e.printStackTrace();
                      }

          }
          finally {

          }
/*         for(Integer i:successRate)
            {
                System.out.println(i);
            }*/
//            for(ArrayList infoList:list)
//            {
//
//            }
//        catch (Exception e){
//            System.out.println(e);
//        }
//        for(ArrayList infoList:list)
//        {
//            CustomerInfo customerObject=(CustomerInfo) infoList.get(0);
//            AccountInfo accountObject =(AccountInfo)infoList.get(1);
//            ArrayList tempList = new ArrayList();
//            ArrayList tempList1 = new ArrayList();
//            try{
//                int customerId=dbobject.createCustomer(customerObject);
//                customerObject.setCustomerId(customerId);
//                accountObject.setCustomerId(customerId);
//                try{
//                    int accountNumber=dbobject.createAccount(accountObject);
//                    accountObject.setAccountNumber(accountNumber);
//                    LoadToMemory.INSTANCE.addToActiveMap(accountObject);
//                    tempList.add(customerObject);
//                    tempList.add(accountObject);
//                    correctDetails.add(tempList);
//                }
//                catch (Exception e)
//                {
//                    dbobject.deleteCustomer(customerObject.getCustomerId());
//                    tempList1.add(customerObject);
//                    tempList1.add(accountObject);
//                    wrongDetails.add(tempList1);
//                }
//            }
//            catch (Exception e)
//            {
//                tempList1.add(customerObject);
//                tempList1.add(accountObject);
//                wrongDetails.add(tempList1);
//            }
//            enteredDetails.add(correctDetails);
//            enteredDetails.add(wrongDetails);
//        }
        return status;
    }
    public void terminateConnection()
    {
        dbobject.closeConnection();
    }
    public void loadActiveMap() throws SQLException
    {
        ArrayList<AccountInfo> list=dbobject.storeActiveInfoToList();
        LoadToMemory.INSTANCE.addToActiveMap(list);
    }
    public void loadInActiveMap() throws SQLException{
        ArrayList<AccountInfo> list=dbobject.storeInActiveInfoToList();
        LoadToMemory.INSTANCE.addToInActiveMap(list);
    }
    public boolean isActiveAlreadyCustomer(int customerId)
    {
        boolean key =LoadToMemory.INSTANCE.isActiveExistingCustomer(customerId);
        return key;
    }
    public boolean isInActiveAlreadyCustomer(int customerId)
    {
        boolean key =LoadToMemory.INSTANCE.isInActiveExistingCustomer(customerId);
        return key;
    }
    public boolean isActiveExistingAccount(int accountNumber, int customerId)
    {
        boolean key = LoadToMemory.INSTANCE.isActiveExistingAccountNumber(accountNumber,customerId);
        return key;
    }
    public boolean isInActiveExistingAccount(int accountNumber, int customerId)
    {
        boolean key = LoadToMemory.INSTANCE.isInActiveExistingAccountNumber(accountNumber,customerId);
        return key;
    }
    public void deleteCustomer(int customerId) throws Exception
    {
        HashMap<Integer,HashMap> mainMap=LoadToMemory.INSTANCE.getHashMap();
        if(mainMap.get(customerId)==null)
        {
            throw new ExceptionHandler("Can't find customerId");
        }
        mainMap.remove(customerId);
        dbobject.deleteCustomer(customerId);
    }
    public void activateCustomer(int customerId) throws Exception
    {
        HashMap<Integer,HashMap> mainInActiveMap =LoadToMemory.INSTANCE.getInActiveHashMap();
        if(mainInActiveMap.get(customerId)==null)
        {
            throw new ExceptionHandler("Can't find customerId");
        }
        dbobject.activateCustomer(customerId);
    }
    public void activateAccount(int customerId,int accountNumber) throws Exception
    {
        HashMap<Integer,HashMap> inActiveHash=LoadToMemory.INSTANCE.getInActiveHashMap();
        HashMap<Integer,AccountInfo> infoMap= inActiveHash.get(customerId);
//        infoMap.remove(accountNumber);
        dbobject.activateAccount(accountNumber);
    }
    public void deleteAccount(int accountNumber,int customerId) throws Exception
    {
        HashMap<Integer,HashMap> map=LoadToMemory.INSTANCE.getHashMap();
        HashMap<Integer,AccountInfo> infoMap= map.get(customerId);
        if(infoMap==null)
        {
            throw new ExceptionHandler("Can't find customerId");
        }
        if(infoMap.get(accountNumber)==null) {
            throw new ExceptionHandler("Can't find your accountNumber!");
        }
        infoMap.remove(accountNumber);
        if(infoMap.isEmpty())
        {
            deleteCustomer(customerId);
        }
        dbobject.deActivateAccount(accountNumber);
    }
    public void addAmount(double amount,int customerId,int accountNumber) throws Exception
    {
        HashMap<Integer,AccountInfo> infoMap=LoadToMemory.INSTANCE.getAccountInfo(customerId);
        if(infoMap==null)
        {
            throw new ExceptionHandler("Can't find customerId");
        }
        if(infoMap.get(accountNumber)==null) {
            throw new ExceptionHandler("Can't find your accountNumber!");
        }
        double balance=infoMap.get(accountNumber).getBalance();
        double newBalance=balance+amount;
        infoMap.get(accountNumber).setBalance(newBalance);
        dbobject.insertNewCash(newBalance,accountNumber);
    }
    public boolean checkSufficientBalance(double amount,int customerId,int accountNumber)
    {
        HashMap<Integer,AccountInfo> infoMap=LoadToMemory.INSTANCE.getAccountInfo(customerId);
        double balance=infoMap.get(accountNumber).getBalance();
        if(balance<amount)
        {
            return false;
        }
        return true;
    }
    public void subtractAmount(double amount,int customerId,int accountNumber) throws Exception
    {
        HashMap<Integer,AccountInfo> infoMap= LoadToMemory.INSTANCE.getAccountInfo(customerId);
        if(infoMap == null){
            throw new ExceptionHandler("Can't find your customerId!");
        }
        if(infoMap.get(accountNumber)==null) {
            throw new ExceptionHandler("Can't find your accountNumber!");
        }
            double balance = infoMap.get(accountNumber).getBalance();
            double newBalance = balance - amount;
            infoMap.get(accountNumber).setBalance(newBalance);
            dbobject.insertNewCash(newBalance, accountNumber);
    }
    public HashMap getDetails(int customerId) throws Exception
    {
        HashMap<Integer,HashMap> map =LoadToMemory.INSTANCE.getAccountInfo(customerId);
        if(map==null)
        {
            throw new ExceptionHandler("Can't find customerId");
        }
        return map;
    }
}
