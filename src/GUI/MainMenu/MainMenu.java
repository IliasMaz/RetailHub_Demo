package GUI.MainMenu;

import DAO.CustomerDAO;
import DAO.ProductDAO;
import GUI.Customer.CustomerGUI;
import GUI.Product.ProductGUI;
import Services.CustomerService;
import Services.ProductService;

import javax.swing.*;

public class MainMenu extends JFrame {
    private JButton productsButton;
    private JButton customersButton1;
    private JButton salesButton;
    private JPanel panel1;
    private ProductService productService;
    private CustomerService customerService;
    //private SalesService salesService;

    public MainMenu(ProductService productService, CustomerService customerService){


        setContentPane(panel1);
        setSize(350, 250);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);


        this.productService = productService;
        this.customerService = customerService;

        productsButton.addActionListener(e -> {
            new ProductGUI(productService);
        });

        customersButton1.addActionListener(e ->{
            new CustomerGUI(customerService);
        });

        /**
        salesButton.addActionListener(e->{
            new SalesGUI(salesService);
        });
         **/
        this.setVisible(true);
    }



}
