import Connector.SQLiteConnector;
import DAO.*;
import GUI.Customer.CustomerGUI;
import GUI.MainMenu.MainMenu;
import GUI.Product.ProductGUI;
import GUI.LogIn.LogIn;
import Services.CustomerService;
import Services.ProductService;
import Services.SalesService;
import Services.UserService;


public class Main {
    public static void main(String[] args) {
        SQLiteConnector.initializeDatabase();

        ProductDAO productDAO = new ProductDAO();
        ProductService productService = new ProductService(productDAO);

        CustomerDAO customerDAO = new CustomerDAO();
        CustomerService customerService = new CustomerService(customerDAO);

        SaleItemDAO saleItemDAO = new SaleItemDAO(productDAO);
        SalesDAO salesDAO = new SalesDAO(customerDAO, saleItemDAO, productDAO);
        SalesService salesService = new SalesService(salesDAO, saleItemDAO, productDAO);
        UserDAO userDAO = new UserDAO();
        UserService userService = new UserService(userDAO);


        //MainMenu menu = new MainMenu(productService,customerService, salesService);

        LogIn login = new LogIn();



    }
}
