package Services;

import Entities.Product;
import DAO.ProductDAO;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class ProductService {
    //private ProductDAO dao = new ProductDAO();

    /**
     public void createProduct(String name, double sellPrice, double purchasePrice, int stock, String category){
     Product newP = new Product(name, sellPrice, purchasePrice, stock, category);
     dao.insertProduct(newP);
     }
     **/

    private final List<Product> mockList = new ArrayList<>();

    public List<Product> getAllProducts() {
        return new ArrayList<>(mockList); // Επιστρέφει αντίγραφο
    }

    public void createProduct(String name, double sellPrice, double purchasePrice, int stock, String category) {
        int id = mockList.size() + 1;
        Product p = new Product(name, sellPrice, purchasePrice, stock, category);
        p.setId(id);
        mockList.add(p);
    }

    public void updateProduct(Product p) {
        for (int i = 0; i < mockList.size(); i++) {
            if (mockList.get(i).getId() == p.getId()) {
                mockList.set(i, p);
                break;
            }
        }
    }

    public void deleteProduct(int id) {
        mockList.removeIf(p -> p.getId() == id);
    }

    public Product getProductById(int id) {
        for (Product p : mockList) {
            if (p.getId() == id) return p;
        }
        return null;
    }


}
