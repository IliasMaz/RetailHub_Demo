package Entities;

public class Product {

    //FIELDS
    private int id;
    private String name;
    private double sellPrice;
    private double purchasePrice;
    private int stock;
    private String category;

    //TODO ΘΑ ΗΤΑΝ ΚΑΛΟ ΑΝ ΕΧΟΥΜΕ ΧΡΟΝΟ Η CATEGORY NA ΓΙΝΕΙ ENUM (Electronics, Beauty, Clothing)

    public Product(String name, double sellPrice, double purchasePrice,int stock, String category) {
        this.name = name;
        this.sellPrice = sellPrice;
        this.purchasePrice = purchasePrice;
        this.stock = stock;
        this.category = category;
    }

    public Product() {

    }

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

    public double getSellPrice() {
        return sellPrice;
    }

    public void setSellPrice(double sellPrice) {
        this.sellPrice = sellPrice;
    }

    public double getPurchasePrice() {
        return purchasePrice;
    }

    public void setPurchasePrice(double purchasePrice) {
        this.purchasePrice = purchasePrice;
    }

    public int getStock() {
        return stock;
    }

    public void setStock(int stock) {
        this.stock = stock;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String toFullString() {
        return "Product{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", sellPrice=" + sellPrice +
                ", purchasePrice=" + purchasePrice +
                ", stock=" + stock +
                ", category='" + category + '\'' +
                '}';
    }

    @Override
    public String toString() { return name; }

    //increase the stock of product
    public void increaseStock(int quantity) {
        this.stock += quantity;
    }

    // Decrease the stock of the product
    public void decreaseStock(int quantity) {
        this.stock -= quantity;
    }
}
