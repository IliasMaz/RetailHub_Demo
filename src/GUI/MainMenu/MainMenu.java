package GUI.MainMenu;

import GUI.Customer.CustomerGUI;
import GUI.Product.ProductGUI;
import GUI.Sales.SalesGUI;
import GUI.User.UserGUI;
import Services.*;
import Entities.User;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.URL;

public class MainMenu extends JFrame {
    private JButton productsButton1;
    private JButton customersButton1;
    private JPanel mainMenuPanel;
    private JButton homeButton;
    private JButton helpButton;
    private JButton settingsButton;
    private JButton profileButton;
    private JButton usersButton1;
    private JButton salesButton1;
    private JPanel mainMenu2ndPanel;
    private JPanel mainMenuFirstPanel;
    private JPanel topMainMenuPanel;
    private JLabel welcomeLabel;
    private ProductService productService;
    private CustomerService customerService;
    private SalesService salesService;
    private UserService userService;
    private User loggedInUser;
    private JLabel imageRetailHub;
    //private SalesService salesService;

    public MainMenu(ProductService productService, CustomerService customerService, SalesService salesService, UserService userService, User loggedInUser){



        setContentPane(mainMenuPanel);
        setResizable(false);
        setSize(760,560);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);


        this.productService = productService;
        this.customerService = customerService;
        this.salesService = salesService;
        this.loggedInUser = loggedInUser;

        URL imgUrl = getClass().getResource("/img.png");

        ImageIcon icon = new ImageIcon(imgUrl);
        imageRetailHub = new JLabel(icon);

        topMainMenuPanel.add(imageRetailHub,FlowLayout.LEFT);

        setupUIBasedOnUserRole();

        productsButton1.addActionListener(e -> {
            new ProductGUI(productService);
        });

        customersButton1.addActionListener(e ->{
            new CustomerGUI(customerService);
        });


        salesButton1.addActionListener(e->{
            new SalesGUI(salesService, customerService, productService);
        });

        usersButton1.addActionListener(e ->  {
            new UserGUI(userService);
        });

        this.setVisible(true);

    }

    private void setupUIBasedOnUserRole() {

         welcomeLabel.setText("Welcome,    " + this.loggedInUser.getName() + " !");



        boolean isAdmin = this.loggedInUser.getRole().equalsIgnoreCase("ADMIN");
        boolean isManager = this.loggedInUser.getRole().equalsIgnoreCase("MANAGER");


        usersButton1.setVisible(isAdmin);


        salesButton1.setVisible(true);


        customersButton1.setVisible(true);
        productsButton1.setVisible(true);


    }



}
