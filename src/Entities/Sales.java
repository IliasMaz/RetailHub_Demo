/*
package Entities;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;

public class Sales {

    private final LocalDate date = LocalDate.now(); // Date of sale
    private final LocalTime time = LocalTime.now(); // Time of sale
    private double totalamount; // Total amount of sale
    private ArrayList<SaleItem> items; //the list of sold products per sale
    //private PaymentMethod paymentMethod; // Method of payment used
    private Customer customer; // customer who made the purchase



    public enum PaymentMethod {
        CASH,
        CREDIT_CARD,
        DEBIT_CARD,
        TRANSFER,
        MOBILE_PAY
    }

    public Sales(double totalamount, PaymentMethod paymentMethod, Customer customer) {
        this.totalamount = totalamount;
        this.paymentMethod = paymentMethod;
        this.customer = customer;
        this.items = new ArrayList<SaleItem>();
    }

    public LocalDate getDate() {
        return date;
    }

    public LocalTime getTime() {
        return time;
    }

    public double getTotalamount() {
        return totalamount;
    }

    public void setTotalamount(double totalamount) {
        this.totalamount = totalamount;
    }

    public ArrayList<SaleItem> getItems() {
        return items;
    }

    public void setItems(ArrayList<SaleItem> items) {
        this.items = items;
    }

    public PaymentMethod getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(PaymentMethod paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public void sumTotal() {
        double sum = 0;
        for(SaleItem i:items) {
            sum = sum + i.getLineTotal();
        }
        this.totalamount = sum;
    }

    public void addItem(SaleItem i) {

        Product p =i.getProduct();
        int qty = i.getQuantity();
        if(p.getStock() < qty){
            System.out.println("Insufficient stock!");
        }
        items.add(i);
        sumTotal();
        p.decreaseStock(qty); //decrease stock when we make a sale
    }


}
**/