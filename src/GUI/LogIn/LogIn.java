package GUI.LogIn;

import DAO.*;
import GUI.Customer.CustomerGUI;
import GUI.MainMenu.MainMenu; // Η κλάση MainMenu
import Services.CustomerService;
import Services.ProductService;
import Services.SalesService;
import Services.UserService;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;


public class LogIn extends JFrame {
    private JPanel loginPanel;
    private JTextField usernameTextField;
    private JPasswordField passwordTextField;
    private JLabel passwordLabel;
    private JLabel usernameLabel;
    private JButton loginButton;
    private JPanel authorization;
    private JTabbedPane tabbedPane1;
    private JTabbedPane LoginMenu;
    private JRadioButton lightRadioButton;
    private JButton fillButton;
    private JButton a169Button;
    private JRadioButton darkRadioButton;
    private JTabbedPane apperanceButtonsLogIn;
    private JTabbedPane SettingsLogIn;
    private JTabbedPane windowResizeButtonLogIn;
    private JTabbedPane HelpLogIn;
    private JPanel contactHelpLogin;
    private JTabbedPane guideHelpLogIn;

    public LogIn(){


        setTitle("RetailHub - Login page");
        setContentPane(loginPanel);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        pack();
        setLocationRelativeTo(null);
        setResizable(false);
        setSize(760,560);

        ButtonGroup radioMode = new ButtonGroup();
        radioMode.add(darkRadioButton);
        radioMode.add(lightRadioButton);


        setVisible(true);

        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                goToNextFrame();
            }
        });

        this.setVisible(true);
    }



    private void goToNextFrame() {

        ProductDAO productDAO = new ProductDAO();
        ProductService productService = new ProductService(productDAO);

        CustomerDAO customerDAO = new CustomerDAO();
        CustomerService customerService = new CustomerService(customerDAO);

        SaleItemDAO saleItemDAO = new SaleItemDAO(productDAO);
        SalesDAO salesDAO = new SalesDAO(customerDAO, saleItemDAO, productDAO);
        SalesService salesService = new SalesService(salesDAO, saleItemDAO, productDAO);
        UserDAO userDAO = new UserDAO();
        UserService userService = new UserService(userDAO);

        MainMenu mainMenu = new MainMenu(productService, customerService, salesService, userService);


        this.dispose();
    }
    }

