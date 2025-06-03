package GUI.LogIn;

import javax.swing.*;


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
        setSize(600,300);

        ButtonGroup radioMode = new ButtonGroup();
        radioMode.add(darkRadioButton);
        radioMode.add(lightRadioButton);


        setVisible(true);

    }
    /*
    private boolean isValidLogin(String username,String password){

    }
*/



}
