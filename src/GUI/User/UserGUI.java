package GUI.User;

import javax.swing.*;

import Entities.User;
import Services.UserService;

public class UserGUI {
    private JButton createButton;
    private JButton updateButton;
    private JButton deleteButton;
    private JButton searchButton;
    private JTextField textField1;
    private JButton showAllButton;
    private UserService userService;

    public UserGUI( UserService userService) {
        this.userService = userService;

        setTitle("RetailHub - Customer Menu");
        setContentPane(panel1);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        pack();
        setLocationRelativeTo(null);

        createButton.addActionListener(e -> {


        });
    }
}
