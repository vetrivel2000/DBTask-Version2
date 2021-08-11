package dbtask.databasemanagement;
import dbtask.account.AccountInfo;
import dbtask.customer.CustomerInfo;

import java.sql.*;
import java.util.ArrayList;

public class DataBase{
    private static DataBase object = null;
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
    public static void closeConnection() {
        if (connection != null) {
            try {
                connection.close();
                System.out.println("Connection closed");
            } catch (Exception e) {
                System.out.println(e);
            }
        }
    }
    public int accountCreate(AccountInfo object) throws SQLException{
            PreparedStatement statement = connection.prepareStatement("insert into AccountInfo(CustomerId,Balance) values(?,?)",Statement.RETURN_GENERATED_KEYS);
            statement.setInt(1, object.getCustomerId());
            statement.setDouble(2, object.getBalance());
            statement.executeUpdate();
            ResultSet resultSet = statement.getGeneratedKeys();
            resultSet.next();
            int accountNumber = resultSet.getInt(1);
            statement.close();
            resultSet.close();
            return accountNumber;
    }
    public void deleteCustomer(int customerId)
    {
        try(Statement statement = connection.createStatement())
        {
            statement.executeQuery("delete from CustomerInfo where CustomerId="+customerId);
        }
        catch (Exception e)
        {
            System.out.println(e);
        }
    }
    public int customerCreate(CustomerInfo object) throws SQLException {
            PreparedStatement statement = connection.prepareStatement("insert into CustomerInfo(CustomerName,MobileNo) values(?,?)",Statement.RETURN_GENERATED_KEYS);
            statement.setString(1, object.getName());
            statement.setLong(2, object.getMobileNo());
            statement.executeUpdate();
            ResultSet resultSet = statement.getGeneratedKeys();
            resultSet.next();
            int customerId=resultSet.getInt(1);
            statement.close();
            resultSet.close();
            return customerId;
    }

    public ArrayList storeIntoList() {
        try (
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("select * from AccountInfo")) {
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
            System.out.println(e);
        }
        return  list;
    }
}