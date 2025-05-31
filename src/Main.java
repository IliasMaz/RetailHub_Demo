import Connector.SQLiteConnector;
import DAO.ProductDAO;
import GUI.Product.ProductGUI;
import Services.ProductService;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    public static void main(String[] args) {
        SQLiteConnector.initializeDatabase();

        ProductDAO productDAO = new ProductDAO();
        ProductService productService = new ProductService(productDAO);
        new ProductGUI(productService);


    }
}
