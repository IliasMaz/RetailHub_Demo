package Entities;

import java.util.ArrayList;

public class Customer {

    private int customerId;
    private String name;
    private String email;
    private String phone;
    private String gender;
    private int age;
    //private int loyaltyPoints;
    //private ArrayList<Sales> salesHistory = new ArrayList<Sales>();


    public Customer(int customerId, String name, String email, String phone, String gender, int loyaltyPoints, int age) {
        this.customerId = customerId;
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.gender = gender;
        this.age = age;
        //this.loyaltyPoints = loyaltyPoints;
    }

    public int getCustomerId() {
        return customerId;
    }

    public void setCustomerId(int customerId) {
        this.customerId = customerId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    /**
    public int getLoyaltyPoints() {
        return loyaltyPoints;
    }

    public void setLoyaltyPoints(int loyaltyPoints) {
        this.loyaltyPoints = loyaltyPoints;
    }
     **/

    /**
    public ArrayList<Sales> getSalesHistory(){
        return salesHistory;
    }
     **/

    @Override
    public String toString() {
        return "Customer{" +
                "customerId = " + customerId +
                ", name = '" + name + '\'' +
                ", email = '" + email + '\'' +
                ", phone = '" + phone + '\'' +
                ", gender = '" + gender + '\'' +
                ", age = " + age +
                //", loyaltyPoints = " + loyaltyPoints +
                '}';
    }
}
