package GUI.User;

import Entities.Product;
import Entities.User;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class UpdateUser extends JDialog {
    private JTextField nameField;
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JComboBox<String> roleComboBox;
    private JTextField emailField;
    private JPanel panel1;
    private JButton saveButton;
    private JButton cancelButton;
    private JLabel passwordLabel;


    private User updatedUser = null;
    private boolean saved = false;


    public UpdateUser(User user) {
        setModal(true);
        setContentPane(panel1);
        setTitle("Update User");
        setSize(350, 250);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        nameField.setText(user.getName());
        usernameField.setText(user.getUsername());
        usernameField.setEditable(false);
        passwordField.setText("");
        roleComboBox.setSelectedItem(user.getRole());
        emailField.setText(user.getEmail());

        saveButton.addActionListener(e -> {
           String name = nameField.getText().trim();
           String username = usernameField.getText().trim().toLowerCase();
           String password = new String(passwordField.getPassword());
           String role = (String) roleComboBox.getSelectedItem();
           String email = emailField.getText().trim().toLowerCase();

           if (name.isEmpty() || username.isEmpty() || role == null|| role.isEmpty() || email.isEmpty()) {
               JOptionPane.showMessageDialog(this, "Please fill all the necessary fields");
               return;
           }

            try {

                if (!email.matches("^[A-Za-z0-9+_.-]+@(.+)$")) {
                    JOptionPane.showMessageDialog(this, "Invalid email");
                    return;
                }

                if (!password.isEmpty() && password.length() < 6) {
                    JOptionPane.showMessageDialog(UpdateUser.this, "Password must be at least 6 characters long");
                    return;
                }

                // Αν ο χρήστης άφησε το password κενό, κράτα το παλιό (αν θες)
                String finalPassword = password.isEmpty() ? user.getPassword() : password;

                updatedUser = new User(name, username, finalPassword, role, email);
                updatedUser.setId(user.getId());
                saved = true;
                JOptionPane.showMessageDialog(UpdateUser.this, "User updated successfully!");
                dispose();

            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Invalid input");
            }
        });

        cancelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                updatedUser = null;
                saved = false;
                dispose();
            }
        });
    }

    public User getUpdatedUser() {
        return updatedUser;
    }

    public boolean isSaved() {
        return saved;
    }
}

