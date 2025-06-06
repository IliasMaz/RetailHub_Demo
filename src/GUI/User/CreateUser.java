package GUI.User;

import Entities.User;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class CreateUser extends JDialog {
    private JTextField nameField;
    private JTextField usernameField;
    private JLabel nameLabel;
    private JPasswordField passwordField;
    private JComboBox roleComboBox;
    private JTextField emailField;
    private JLabel usernameLabel;
    private JLabel passwordLabel;
    private JLabel roleLabel;
    private JLabel emailLabel;
    private JButton cancelButton;
    private JButton saveButton;
    private JLabel createUserLabel;
    private JPanel panel1;

    private User createdUser = null;
    private boolean saved = false;

    public CreateUser() {

        setModal(true);
        setContentPane(panel1);
        setTitle("Create Customer");
        setSize(350, 250);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        saveButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String name = nameField.getText().trim();
                String username = usernameField.getText().trim().toLowerCase();
                String password = new String(passwordField.getPassword());
                String role = (String) roleComboBox.getSelectedItem();
                String email = emailField.getText().trim().toLowerCase();

                if (name.isEmpty() || username.isEmpty() || password.isEmpty() || role.isEmpty() || email.isEmpty()) {
                    JOptionPane.showMessageDialog(CreateUser.this, "Please fill all necessary fields!");
                    return;
                }
                try {


                    if (password.length() < 6) {
                        JOptionPane.showMessageDialog(CreateUser.this, "Password must be at least 6 characters long");
                    }

                    if (!email.matches("^[A-Za-z0-9+_.-]+@(.+)$")) {
                        JOptionPane.showMessageDialog(CreateUser.this, "Invalid email");
                    }
                    User user = new User(name, username, password, role, email);
                    saved = true;
                    createdUser = user;
                    JOptionPane.showMessageDialog(CreateUser.this, "User created successfully!");
                    dispose();
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(CreateUser.this, "Invalid input");
                }

            }
        });
        cancelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                createdUser = null;
                saved = false;
                dispose();

            }
        });
    }

    public User getCreatedUser() {
        return createdUser;

    }

    public boolean isSaved() {
        return saved;
    }
}
