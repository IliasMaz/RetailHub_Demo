/**
package Entities;

public class SaleItem {

    private Product product; // The product being sold
    private int quantity; // Quantity of product sold

    /**
     * CONSTRUCTOR for SaleItem
     * @param product the product sold
     * @param quantity the quantity sold

    public SaleItem(Product product, int quantity) {

        this.product = product;
        this.quantity = quantity;
    }

    //RETURN name of the sold product

    public String getName() {
        return product.getName();
    }

    //RETURN selling price per unit

    public double getPrice() {
        return product.getSellPrice();
    }

    //RETURN Quantity sold

    public int getQuantity() {
        return quantity;
    }

    //calculate total amount of price * quantity

    public double getLineTotal() {
        return product.getSellPrice() * quantity;
    }

    //RETURN the product associated with the sale item

    public Product getProduct() {
        return this.product;
    }

    @Override
    public String toString() {
        return "SaleItem{" +
                "product=" + product +
                ", quantity=" + quantity +
                ' ';
    }


}

*/