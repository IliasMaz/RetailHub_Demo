package Entities;

import Entities.Product;
import Entities.Customer;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Sales {

    private int id;
    private LocalDate date; // Date of sale
    private LocalTime time; // Time of sale
    private double totalAmount; // Total amount of sale
    private List<SaleItem> items;
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
        this.items = new ArrayList<>();
        this.totalAmount = 0;
        this.date = LocalDate.now();
        this.time = LocalTime.now();
    }

    public void addItem(Product product, int quantity) {
        if (product == null || quantity <= 0) {
            throw new IllegalArgumentException("Invalid product or quantity");
        }
            SaleItem newItem = new SaleItem(product, quantity);
            this.items.add(newItem);
            this.totalAmount += newItem.getLineTotal();

    }

    public void removeItem(SaleItem itemToRemove) {
        if (this.items.remove(itemToRemove)) {
            this.totalAmount -= itemToRemove.getLineTotal();
        }
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public List<SaleItem> getItems() {
        return items;
    }

    public double getTotalAmount() {
        return totalAmount;
    }

    public double getTotalamount() {
        return totalAmount;
    }

    public void setTotalamount(double totalamount) {
        this.totalAmount = totalamount;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public LocalTime getTime() {
        return time;
    }

    public void setTime(LocalTime time) {
        this.time = time;
    }

    //THA TO TESTAROUME LIGO MPOREI NA THELEI ALLH METHODO PRINT
    @Override
    public String toString() {
        return "Customer: " + customer.getName() + " ID: " + customer.getId() + " Products: "
                + Arrays.toString(items.toArray())
                + " Total amount: " + totalAmount
                + " Date: " + getDate().toString() + " Time: " + time;
    }
    /*   DEITE AFTIN TIN METHODO GIA PRINT
    @Override
    public String toString() {
        StringBuilder productDetails = new StringBuilder();
        for (SaleItem item : items) {
            productDetails.append("\n\t- ").append(item.getName()).append(" (Qty: ").append(item.getQuantity()).append(", Price: ").append(item.getPrice()).append(")");
        }

        return "Sale ID: " + id +
               "\nCustomer: " + (customer != null ? customer.getName() : "N/A") +
               "\nDate: " + date + " " + time +
               "\nItems: " + (items.isEmpty() ? "None" : productDetails.toString()) +
               "\nTotal Amount: " + String.format("%.2f", totalAmount) +
               "\nPayment Method: " + paymentMethod;
    }
     */
}

