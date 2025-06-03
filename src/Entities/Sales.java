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
        NUll,
        CASH,
        CREDIT_CARD,
        DEBIT_CARD,
        TRANSFER,
        MOBILE_PAY
    }

    public Sales(Customer customer, PaymentMethod paymentMethod) {
        this.customer = customer;
        this.items = new ArrayList<>();
        this.totalAmount = 0;
        this.date = LocalDate.now();
        this.time = LocalTime.now();
        this.paymentMethod = paymentMethod;
    }

    public Sales() {
        this.customer = null;
        this.items = new ArrayList<>();
        this.totalAmount = 0.0;
        this.date = null;
        this.time = null;
        this.paymentMethod = null;
    }

    public void sumTotal() {
        double sum = 0;
        for(SaleItem i:items) {
            sum = sum + i.getLineTotal();
        }
        this.totalAmount = sum;
    }

    public void addItem(Product product, int quantity) {
        if (product == null || quantity <= 0) {
            throw new IllegalArgumentException("Invalid product or quantity");
        }
        SaleItem newItem = new SaleItem(product, quantity);
        if(product.getStock() < quantity){
            throw new IllegalStateException("Insufficient stock for product.");
        }
        this.items.add(newItem);
        sumTotal();
        product.decreaseStock(quantity);

    }

    public void removeItem(SaleItem itemToRemove) {
        if (this.items.remove(itemToRemove)) {
            sumTotal();
            Product p = itemToRemove.getProduct();
            int qnty = itemToRemove.getQuantity();
            p.increaseStock(qnty);
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

    public void setItems(List<SaleItem> items) {
        this.items = items;
    }

    public double getTotalAmount() {
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

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public LocalTime getTime() {
        return time;
    }

    public void setTime(LocalTime time) {
        this.time = time;
    }

    public void setPaymentMethod(PaymentMethod paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public PaymentMethod getPaymentMethod() {
        return paymentMethod;
    }

    public void receipt() {

        // At first we have the header

        String receipt =
                "=== RECEIPT ===\n"
                        + "Sale ID: " + id + "\n"
                        + "Date   : " + date + "\n"
                        + "Time   : " + time + "\n"
                        + "Items  : " + items;

        // Add each product to the receipt

        for (SaleItem i : items) {
            receipt +=  "  - " + i.getName() + " x" + i.getQuantity() + " @ " + i.getPrice() + "\n";
        }

        // Payment method and total amount

        receipt +=
                "Payment: " + paymentMethod + "\n"
                        + "-----------------\n"
                        + "TOTAL  : " + totalAmount + "\n";

        System.out.println(receipt);

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

