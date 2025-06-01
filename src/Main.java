import Connector.SQLiteConnector;
import DAO.CustomerDAO;
import DAO.ProductDAO;
import GUI.Customer.CustomerGUI;
import GUI.Product.ProductGUI;
import Services.CustomerService;
import Services.ProductService;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    public static void main(String[] args) {
        SQLiteConnector.initializeDatabase();

        ProductDAO productDAO = new ProductDAO();
        ProductService productService = new ProductService(productDAO);
        new ProductGUI(productService);

        CustomerDAO customerDAO = new CustomerDAO();
        CustomerService customerService = new CustomerService(customerDAO);
        new CustomerGUI(customerService);




    }
}
