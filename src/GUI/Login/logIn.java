package GUI;

import javax.swing.*;


public class logIn extends JFrame {
    private JPanel panel1;
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
    private JTabbedPane ModeRadioButton;
    private JTabbedPane SettingsLogIn;
    private JTabbedPane WindowLogIn;
    private JTabbedPane HelpLogIn;
    private JPanel contactHelpLogin;

    public logIn(){

        setTitle("RetailHub - Login page");
        setContentPane(panel1);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        pack();
        setLocationRelativeTo(null);
        setResizable(false);
        setSize(600,300);

        ButtonGroup radioMode = new ButtonGroup();
        radioMode.add(darkRadioButton);
        radioMode.add(lightRadioButton);


        setVisible(true);

    }




}
