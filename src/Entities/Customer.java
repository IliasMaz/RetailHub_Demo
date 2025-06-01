package Entities;

import java.util.ArrayList;

public class Customer {

    private int id;
    private String name;
    private String email;
    private String phone;
    private String gender;
    private int age;
    private int loyaltyPoints;
    //private ArrayList<Sales> salesHistory = new ArrayList<Sales>();



    public Customer( String name, String email, String phone, String gender,  int age) {
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.gender = gender;
        this.age = age;
        this.loyaltyPoints = 0;
    }

    public Customer() {this.loyaltyPoints = 0;}

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    public int getLoyaltyPoints() {
        return loyaltyPoints;
    }

    public void setLoyaltyPoints(int loyaltyPoints) {
        this.loyaltyPoints = loyaltyPoints;
    }

    /**
    public ArrayList<Sales> getSalesHistory(){
        return salesHistory;
    }
     **/

    @Override
    public String toString() {
        return "Customer{" +
                "Id = " + id +
                ", name = '" + name + '\'' +
                ", email = '" + email + '\'' +
                ", phone = '" + phone + '\'' +
                ", gender = '" + gender + '\'' +
                ", age = " + age +
                ", loyaltyPoints = " + loyaltyPoints +
                '}';
    }
}
