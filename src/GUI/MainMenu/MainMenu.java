package GUI.MainMenu;

import GUI.Customer.CustomerGUI;
import GUI.Product.ProductGUI;
import GUI.Sales.SalesGUI;
import Services.CustomerService;
import Services.ProductService;
import Services.SalesService;

import javax.swing.*;
import java.awt.*;
import java.net.URL;

public class MainMenu extends JFrame {
    private JButton productsButton1;
    private JButton customersButton1;
    private JPanel mainMenuPanel;
    private JLabel titleMainMenu;
    private JButton homeButton;
    private JButton helpButton;
    private JButton settingsButton;
    private JButton profileButton;
    private JButton employeesButton1;
    private JButton salesButton1;
    private JPanel mainMenu2ndPanel;
    private JPanel mainMenuFirstPanel;
    private JPanel topMainMenuPanel;
    private ProductService productService;
    private CustomerService customerService;
    private SalesService salesService;
    private JLabel imageRetailHub;
    //private SalesService salesService;

    public MainMenu(ProductService productService, CustomerService customerService, SalesService salesService){



        setContentPane(mainMenuPanel);
        setResizable(false);
        setSize(760,560);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);


        this.productService = productService;
        this.customerService = customerService;
        this.salesService = salesService;

        URL imgUrl = getClass().getResource("/img.png");

        ImageIcon icon = new ImageIcon(imgUrl);
        imageRetailHub = new JLabel(icon);

        topMainMenuPanel.add(imageRetailHub,FlowLayout.LEFT);

        productsButton1.addActionListener(e -> {
            new ProductGUI(productService);
        });

        customersButton1.addActionListener(e ->{
            new CustomerGUI(customerService);
        });


        salesButton1.addActionListener(e->{
            new SalesGUI(salesService, customerService, productService);
        });

        this.setVisible(true);
    }



}
