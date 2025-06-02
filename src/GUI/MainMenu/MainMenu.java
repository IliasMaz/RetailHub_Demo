package GUI.MainMenu;

import GUI.Customer.CustomerGUI;
import GUI.Product.ProductGUI;
import Services.CustomerService;
import Services.ProductService;

import javax.swing.*;

public class MainMenu extends JFrame {
    private JButton productsButton1;
    private JButton customersButton1;
    private JPanel mainMenuPanel;
    private JLabel titleMenu;
    private JButton homeButton;
    private JButton helpButton;
    private JButton settingsButton;
    private JButton profileButton;
    private JButton employeesButton1;
    private JButton salesButton1;
    private JLabel choose;
    private ProductService productService;
    private CustomerService customerService;
    //private SalesService salesService;

    public MainMenu(ProductService productService, CustomerService customerService){


        setContentPane(mainMenuPanel);
        setResizable(false);
        setSize(760,560);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);


        this.productService = productService;
        this.customerService = customerService;

        productsButton1.addActionListener(e -> {
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
