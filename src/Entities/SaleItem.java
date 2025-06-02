
package Entities;

public class SaleItem {



    private int id;
    private Product product; // The product being sold
    private int quantity; // Quantity of product sold

    /**
     * CONSTRUCTOR for SaleItem
     * @param product the product sold
     * @param quantity the quantity sold
     */

    public SaleItem(Product product, int quantity) {

        this.product = product;
        this.quantity = quantity;
    }

    public SaleItem() {}

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

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    //calculate total amount of price * quantity

    public double getLineTotal() {
        return product.getSellPrice() * quantity;
    }

    //RETURN the product associated with the sale item

    public Product getProduct() {
        return this.product;
    }

    public void setProduct(Product product) {
        this.product = product;}

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "SaleItem{" +
                "id=" + id +
                ", product=" + (product != null ? product.getName() : "N/A") +
                ", quantity=" + quantity +
                '}';
    }

}

