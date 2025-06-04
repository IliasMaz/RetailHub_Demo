package Entities;

import Entities.Product;
import Entities.Customer;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
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


    public static void saveReceiptToFile(Sales sale, String filePath) throws IOException {
        String receipt = sale.receipt();
        java.nio.file.Path parentDir = java.nio.file.Paths.get(filePath).getParent();
        if (parentDir != null){
            java.nio.file.Files.createDirectories(parentDir);
        }
        try (FileWriter writer = new FileWriter(filePath)) {
            writer.write(receipt);
        }
    }



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
        this.date = LocalDate.now();
        this.time = LocalTime.now();
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
        //SaleItem newItem = new SaleItem(product, quantity);

        int alreadyInSale = 0;
        for (SaleItem item : items) {
            if (item.getProduct().getId() == product.getId()) {
                alreadyInSale = item.getQuantity();
                break;
            }
        }
        if (product.getStock() < (alreadyInSale + quantity)) {
            throw new IllegalStateException("Insufficient stock for product.");
        }

        if(product.getStock() < quantity){
            throw new IllegalStateException("Insufficient stock for product.");
        }

        for (SaleItem item : items) {
            if (item.getProduct().getId() == product.getId()) {
                item.setQuantity(item.getQuantity() + quantity);
                sumTotal();
                return;
            }
        }
        SaleItem newItem = new SaleItem(product, quantity);
        this.items.add(newItem);
        sumTotal();
        //product.decreaseStock(quantity);

    }

    public void removeItem(SaleItem itemToRemove) {
        if (this.items.remove(itemToRemove)) {
            sumTotal();
            Product p = itemToRemove.getProduct();
            int qnty = itemToRemove.getQuantity();
            //p.increaseStock(qnty);
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

    public void setTotalAmount(double totalAmount) {
        this.totalAmount = totalAmount;
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

    public String receipt() {
        StringBuilder receipt = new StringBuilder();
        receipt.append("=== RECEIPT ===\n")
                .append("Sale ID: ").append(id).append("\n")
                .append("Date   : ").append(date).append("\n")
                .append("Time   : ").append(time).append("\n")
                .append("Customer: ").append(customer != null ? customer.getName() : "-").append("\n")
                .append("Payment: ").append(paymentMethod != null ? paymentMethod : "-").append("\n")
                .append("----------------------\n")
                .append("Items:\n");

        for (SaleItem i : items) {
            receipt.append(String.format("  - %-18s x%-3d € %-8.2f = %8.2f\n",
                    i.getName(), i.getQuantity(), i.getPrice(), i.getLineTotal()));
        }

        receipt.append("----------------------\n")
                .append(String.format("TOTAL  : %.2f €\n", totalAmount));

        return receipt.toString();
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

