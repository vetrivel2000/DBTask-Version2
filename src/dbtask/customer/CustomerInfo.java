/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author vetri
 */
package dbtask.customer;

public class CustomerInfo {
    //public static HashMap<Integer,CustomerInfo> map = new HashMap<>();
    //public HashMap<Integer,Integer> map1 = new HashMap<>();
    private int customerId;
    private String name;
    private long mobileNo;
    public void setCustomerId(int customerId)
    {
        this.customerId=customerId;
    }
    public int getCustomerId()
    {
        return this.customerId;
    }
    public void setName(String name)
    {
        this.name=name;
    }
    public String getName()
    {
        return this.name;
    }
    public void setMobileNo(long mobileNo)
    {
        this.mobileNo= mobileNo;
    }
    public long getMobileNo()
    {
        return this.mobileNo;
    }
    @Override
    public String toString(){
        String result="\nCustomerInfo\n"+"CustomerId  : "+this.customerId+"\n"+"Name        : "+this.name+"\n"+"MobileNo        :"+this.mobileNo+"\n";
        return result;
    }
}
