package Services;

import Entities.Product;
import DAO.ProductDAO;

import java.util.List;


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

    public boolean updateProduct(Product updatedProduct) {
        boolean success = productDAO.updateProduct(updatedProduct);
        if (!success) {
            throw new RuntimeException("Product update failed on level DAO.");
        }
        return true;
    }

    public boolean deleteProduct(int productId) {
        boolean success = productDAO.deleteProduct(productId);
        if (!success) {
            throw new RuntimeException("Product delete failed on level DAO.");
        }
        return true;
    }

    public List<Product> getAllProducts() {
        return productDAO.getAllProducts();
    }

    public Product getProductById(int id){
        return productDAO.getProductById(id);
    }

    public Product findByName(String name) {
        return productDAO.findByName(name);
    }
}
