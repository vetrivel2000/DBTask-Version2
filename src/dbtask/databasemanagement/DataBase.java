package dbtask.databasemanagement;
import dbtask.account.AccountInfo;
import dbtask.customer.CustomerInfo;
import dbtask.persistence.PersistenceLayer;

import java.sql.*;
import java.util.ArrayList;

public class DataBase implements PersistenceLayer {
    private static Connection connection = null;
    public ArrayList<AccountInfo> list = new ArrayList<>();
    public DataBase() {
        try {
            connection = DriverManager.getConnection("jdbc:mysql://localhost/db", "root", "Vetri@50");
            System.out.println("DataBase Connected");
        } catch (Exception e) {
            System.out.println(e);
        }
    }
    public void closeConnection() {
        if (connection != null) {
            try {
                connection.close();
                System.out.println("Connection closed");
            } catch (Exception e) {
                System.out.println(e);
            }
        }
    }
    public int createAccount(AccountInfo object) throws SQLException
    {
        ResultSet resultSet = null;
        PreparedStatement statement = null;
        int accountNumber;
        try{
            statement = connection.prepareStatement("insert into AccountInfo(CustomerId,Balance) values(?,?)",Statement.RETURN_GENERATED_KEYS);
            statement.setInt(1,object.getCustomerId());
            statement.setDouble(2,object.getBalance());
            statement.executeUpdate();
            resultSet=statement.getGeneratedKeys();
            resultSet.next();
            accountNumber=resultSet.getInt(1);
        }
        finally {
            try {
                statement.close();
                resultSet.close();
            }catch(Exception e){}
        }
        return accountNumber;
    }
    public ArrayList createAccount(ArrayList<ArrayList>list) throws SQLException
    {
        ResultSet resultSet=null;
        PreparedStatement statement=null;
        ArrayList<Integer> accountNumbers = new ArrayList<>();
        ArrayList<Integer> successRate=null;
        ArrayList<ArrayList> details = new ArrayList<>();
        try {
            statement = connection.prepareStatement("insert into AccountInfo(CustomerId,Balance) values(?,?)",Statement.RETURN_GENERATED_KEYS);
            for (ArrayList accountInfoList : list) {
                AccountInfo accountObject = (AccountInfo) accountInfoList.get(1);
                statement.setInt(1,accountObject.getCustomerId());
                statement.setDouble(2,accountObject.getBalance());;
                statement.addBatch();
            }
            int counts[] = statement.executeBatch();
            successRate =new ArrayList<>();
            for(int i=0;i<counts.length;i++)
            {
                successRate.add(counts[i]);
            }
            resultSet = statement.getGeneratedKeys();
            while(resultSet.next()) {
                int id=resultSet.getInt(1);
                accountNumbers.add(id);
            }
        }
        catch(BatchUpdateException batchUpdateException){
            try {
                int counts[]=batchUpdateException.getUpdateCounts();
                successRate =new ArrayList<>();
                for(int i=0;i<counts.length;i++)
                {
                    successRate.add(counts[i]);
                }
                resultSet = statement.getGeneratedKeys();
                while(resultSet.next()) {
                    int id=resultSet.getInt(1);
                    accountNumbers.add(id);
                }
            }
            catch (SQLException e)
            {
                System.out.println(e);
            }
        }
        catch (SQLException e)
        {
            e.addSuppressed(new SQLException("Your account details are wrong"));
            throw e;
        }
        finally {
            try {
                statement.close();
                resultSet.close();
            }catch(Exception e){}
        }
//        for(int i:accountNumbers)
//        {
//            System.out.println(i);
//        }
        details.add(successRate);
        details.add(accountNumbers);
        return details;
    }
    public ArrayList<ArrayList> createCustomer(ArrayList<ArrayList>list) throws SQLException {
        ResultSet resultSet=null;
        PreparedStatement statement=null;
        ArrayList<Integer> customerId = new ArrayList<>();
        ArrayList<Integer> successRate=null;
        ArrayList<ArrayList> details = new ArrayList<>();
        try {
            statement = connection.prepareStatement("insert into CustomerInfo(CustomerName,MobileNo) values(?,?)", Statement.RETURN_GENERATED_KEYS);
            for (ArrayList customerInfoList : list) {
                CustomerInfo customerObject = (CustomerInfo) customerInfoList.get(0);
                statement.setString(1, customerObject.getName());
                statement.setLong(2, customerObject.getMobileNo());
                statement.addBatch();
            }
            int counts[] = statement.executeBatch();
            successRate =new ArrayList<>();
            for(int i=0;i<counts.length;i++)
            {
                successRate.add(counts[i]);
            }
            resultSet = statement.getGeneratedKeys();
            while(resultSet.next()) {
                int id=resultSet.getInt(1);
                customerId.add(id);
            }
        }
        catch(BatchUpdateException batchUpdateException){
            try {
                int counts[]=batchUpdateException.getUpdateCounts();
                successRate =new ArrayList<>();
                for(int i=0;i<counts.length;i++)
                {
                    successRate.add(counts[i]);
                }
                resultSet = statement.getGeneratedKeys();
                while(resultSet.next()) {
                    int id=resultSet.getInt(1);
                    customerId.add(id);
                }
            }
            catch (SQLException e)
            {
                System.out.println(e);
            }
        }
        catch (SQLException e)
        {
            e.addSuppressed(new SQLException("Your customer details are wrong"));
            throw e;
        }
        finally {
            try {
                statement.close();
                resultSet.close();
            }catch(Exception e){}
        }
        details.add(successRate);
        details.add(customerId);
        return details;
    }
    public void deleteCustomer(int customerId) throws SQLException
    {
        try(Statement statement = connection.createStatement())
        {
            statement.executeUpdate("update CustomerInfo set ActiveStatus='InActive' where CustomerId="+customerId);
            deleteAccount(customerId);
        }
        catch (Exception e)
        {
            e.addSuppressed(new SQLException("Your customerId is wrong"));
            throw e;
        }
    }
    public void deActivateAccount(int accountNumber) throws SQLException
    {
        try(Statement statement = connection.createStatement())
        {
            statement.executeUpdate("update AccountInfo set ActiveStatus='InActive' where AccountNo="+accountNumber);
        }
        catch (Exception e)
        {
            e.addSuppressed(new SQLException("Your accountNumber is Wrong"));
            throw e;
        }
    }
    public void insertNewCash(double newBalance,int accountNumber) throws SQLException
    {
        try(Statement statement = connection.createStatement())
        {
            statement.executeUpdate("update AccountInfo set Balance="+newBalance+"where AccountNo="+accountNumber);
        }
        catch (Exception e)
        {
            e.addSuppressed(new SQLException("Can't find your accountNumber!!"));
            throw e;
        }
    }
    public void deleteAccount(int customerId) throws SQLException
    {
        try(Statement statement = connection.createStatement())
        {
            statement.executeUpdate("update AccountInfo set ActiveStatus='InActive' where CustomerId="+customerId);
        }
        catch (Exception e)
        {
            e.addSuppressed(new SQLException("Can't find your customerId!!"));
            throw e;
        }
    }

    public ArrayList storeAccountInfoToList() throws SQLException {
        try (
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("select * from AccountInfo where ActiveStatus='Active'")) {
        while (resultSet.next()) {
            AccountInfo object = new AccountInfo();
            object.setCustomerId(resultSet.getInt("CustomerId"));
            object.setBalance(resultSet.getDouble("Balance"));
            object.setAccountNumber(resultSet.getInt("AccountNo"));
            list.add(object);
        }
    }
        catch (Exception e)
        {
            e.addSuppressed(new SQLException("You have an Inactive account"));
            throw e;
        }
        return  list;
    }
}