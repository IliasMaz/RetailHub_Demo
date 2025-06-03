import Connector.SQLiteConnector;
import DAO.CustomerDAO;
import DAO.ProductDAO;
import DAO.SaleItemDAO;
import DAO.SalesDAO;
import GUI.Customer.CustomerGUI;
import GUI.MainMenu.MainMenu;
import GUI.Product.ProductGUI;
import GUI.LogIn.LogIn;
import Services.CustomerService;
import Services.ProductService;
import Services.SalesService;


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



        MainMenu menu = new MainMenu(productService,customerService, salesService);

        LogIn login = new LogIn();



    }
}
