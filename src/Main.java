import Connector.SQLiteConnector;
import DAO.CustomerDAO;
import DAO.ProductDAO;
import GUI.Customer.CustomerGUI;
import GUI.MainMenu.MainMenu;
import GUI.Product.ProductGUI;
import GUI.LogIn.LogIn;
import Services.CustomerService;
import Services.ProductService;


public class Main {
    public static void main(String[] args) {
        SQLiteConnector.initializeDatabase();

        ProductDAO productDAO = new ProductDAO();
        ProductService productService = new ProductService(productDAO);

        CustomerDAO customerDAO = new CustomerDAO();
        CustomerService customerService = new CustomerService(customerDAO);


        MainMenu menu = new MainMenu(productService,customerService);

        LogIn login = new LogIn();



    }
}
