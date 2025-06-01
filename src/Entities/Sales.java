package Entities;

import Entities.Product;
import Entities.Customer;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Arrays;

public class Sales {

    private final LocalDate date = LocalDate.now(); // Date of sale
    private final LocalTime time = LocalTime.now(); // Time of sale
    private double totalamount; // Total amount of sale
    private ArrayList<Product> productsToSell; //the list of sold products per sale
    private PaymentMethod paymentMethod; // Method of payment used
    private Customer customer; // customer who made the purchase

    public enum PaymentMethod {
        CASH,
        CREDIT_CARD,
        DEBIT_CARD,
        TRANSFER,
        MOBILE_PAY
    }

    public Sales(Customer customer) {
        this.customer = customer;
        this.productsToSell = new ArrayList<>();
        this.totalamount = 0;
    }

    public void addProduct(Product product) {
        this.productsToSell.add(product);
        this.totalamount += product.getSellPrice();
    }

    public void removeProduct(Product product) {
        if (this.productsToSell.remove(product)) {
            this.totalamount -= product.getSellPrice();
        }
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public ArrayList<Product> getProductsToSell() {
        return productsToSell;
    }

    public double getTotalAmount() {
        return totalamount;
    }

    public double getTotalamount() {
        return totalamount;
    }

    public void setTotalamount(double totalamount) {
        this.totalamount = totalamount;
    }

    public LocalDate getDate() {
        return date;
    }

    public LocalTime getTime() {
        return time;
    }

    //THA TO TESTAROUME LIGO MPOREI NA THELEI ALLH METHODO PRINT
    @Override
    public String toString() {
        return "Customer: " + customer.getName() + " ID: " + customer.getId() + " Products: "
                + Arrays.toString(productsToSell.toArray())
                + " Total amount: " + totalamount
                + " Date: " + getDate().toString() + " Time: " + time;
    }

}
