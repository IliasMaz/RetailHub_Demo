package Services;

import Entities.Product;
import DAO.ProductDAO;



public class ProductService {
    private final ProductDAO productDAO ;

    public ProductService(ProductDAO productDAO) {
        if (productDAO == null) {
            throw new IllegalArgumentException("ProductDAO cannot be null");
        }
        this.productDAO = productDAO;
    }

    public Product createProduct(String name, double sellPrice, double purchasePrice, int stock, String category) {
        Product p = new Product(name, sellPrice, purchasePrice, stock, category);

        boolean success = this.productDAO.createProduct(p);
        if (success) {
            System.out.println("ProductService: Product  " + p.getName() + " created with ID: " + p.getId());
            return p;
        } else {
            throw new RuntimeException("Product creation failed on level DAO.");
        }
    }




}
