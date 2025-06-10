package GUI.LogIn;

import DAO.*;
import GUI.Customer.CustomerGUI;
import GUI.MainMenu.MainMenu; // Η κλάση MainMenu
import Services.CustomerService;
import Services.ProductService;
import Services.SalesService;
import Services.UserService;
import Entities.User;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.URL;


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
    private JLabel welcomeMsg;
    private JLabel imageLabel;
    private JPanel panelForImage;
    private JLabel imageRetailHub;

    private UserService userService;
    private ProductService productService;
    private CustomerService customerService;
    private SalesService salesService;

    public LogIn(UserService userService, ProductService productService, CustomerService customerService, SalesService salesService) {
        // Αρχικοποίηση των πεδίων service
        if (userService == null || productService == null || customerService == null || salesService == null) {
            throw new IllegalArgumentException("Services cannot be null");
        }
        this.userService = userService;
        this.productService = productService;
        this.customerService = customerService;
        this.salesService = salesService;


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
                String username = usernameTextField.getText().trim();
                String password = new String(passwordTextField.getPassword());

                if (username.isEmpty() || password.isEmpty()) {
                    JOptionPane.showMessageDialog(LogIn.this,
                            "Please enter both username and password.",
                            "Login Error",
                            JOptionPane.ERROR_MESSAGE);
                    return;
                }

                User loggedInUser = userService.loginUser(username, password);

                if (loggedInUser != null) {
                    JOptionPane.showMessageDialog(LogIn.this, "Welcome " + loggedInUser.getName() + "!");
                    goToMainMenu(loggedInUser);
                } else {
                    JOptionPane.showMessageDialog(LogIn.this,
                            "Invalid username or password.",
                            "Login Failed",
                            JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        this.setVisible(true);
    }



    private void goToMainMenu(User loggedInUser) {

        MainMenu mainMenu = new MainMenu(productService, customerService, salesService, userService, loggedInUser);
        mainMenu.setVisible(true);

        this.dispose();
    }
    }

