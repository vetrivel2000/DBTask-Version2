/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/*
 *
 * @author vetri
 */
package dbtask.account;
public class AccountInfo {
    private int accountNumber;
    private int customerId;
    private double balance;
    public void setCustomerId(int customerId)
    {
        this.customerId=customerId;
    }
    public int getCustomerId()
    {
        return this.customerId;
    }
    public void setAccountNumber(int accountNumber)
    {
        this.accountNumber=accountNumber;
    }
    public int getAccountNumber()
    {
        return this.accountNumber;
    }
    public void setBalance(double balance)
    {
        this.balance=balance;
    }
    public double getBalance()
    {
        return this.balance;
    }

    @Override
    public String toString()
    {
        String result="\nAccountInfo\n"+"AccountNumber    :"+this.accountNumber+"\n"+"CustomerId    :"+this.customerId+"\n"+"Balance       :"+this.balance+"\n";
        return result;
    }
}
