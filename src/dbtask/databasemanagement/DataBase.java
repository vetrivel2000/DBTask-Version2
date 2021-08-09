package dbtask.databasemanagement;
import dbtask.account.AccountInfo;
import dbtask.customer.CustomerInfo;
import dbtask.load.LoadToMemory;
import dbtask.logical.LogicalLayer;

import java.util.Iterator;
import java.util.ArrayList;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.DriverManager;

public class DataBase{
    private static DataBase object = null;
    private static Connection connection = null;

    private DataBase() {
        try {
            connection = DriverManager.getConnection("jdbc:mysql://localhost/db", "root", "Vetri@50");
            System.out.println("DataBase Connected");
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    public static DataBase getInstance() {
        if (object == null)
            object = new DataBase();
        return object;
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

    public void accountCreate(AccountInfo object) {
        try (PreparedStatement statement = connection.prepareStatement("insert into AccountInfo(CustomerId,Balance) values(?,?)"))
        {
            statement.setInt(1, object.getCustomerId());
            statement.setDouble(2, object.getBalance());
            statement.executeUpdate();
        }
        catch (Exception e)
        {
            System.out.println(e);
        }
    }

    public void customerCreate(CustomerInfo object, AccountInfo object1) throws Exception {
        ResultSet resultSet =null;
        try (PreparedStatement statement = connection.prepareStatement("insert into CustomerInfo(CustomerName,MobileNo) values(?,?)",Statement.RETURN_GENERATED_KEYS))
        {
            statement.setString(1, object.getName());
            statement.setLong(2, object.getMobileNo());
            statement.executeUpdate();
            resultSet = statement.getGeneratedKeys();
            resultSet.next();
            object1.setCustomerId(resultSet.getInt(1));
            //System.out.println("Your customer id is:"+resultSet.getInt(1));
            accountCreate(object1);
        } catch (Exception e) {
            System.out.println(e);
        }
        finally {
            resultSet.close();
        }
    }
    public void storeIntoMap() {
        try (
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("select * from AccountInfo")) {
        while (resultSet.next()) {
            AccountInfo object = new AccountInfo();
            object.setCustomerId(resultSet.getInt("CustomerId"));
            object.setBalance(resultSet.getDouble("Balance"));
            object.setAccountNumber(resultSet.getInt("AccountNo"));
            //LoadToMemory.getInstance().addAccount(object);
            LogicalLayer.getInstance().loadMap(object);
        }
    }
        catch (Exception e)
        {
            System.out.println(e);
        }
    }
}