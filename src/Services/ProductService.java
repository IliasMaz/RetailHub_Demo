package Services;

import Entities.Product;
import DAO.ProductDAO;

public class ProductService {
    private ProductDAO dao = new ProductDAO();

    public void createProduct(String name, double sellPrice, double purchasePrice, int stock, String category){
        Product newP = new Product(name, sellPrice, purchasePrice, stock, category);
        dao.insertProduct(newP);
    }




}
