package GUI.Customer;

import Entities.Customer;

import javax.swing.*;

public class CreateCustomer extends JDialog {
    private JTextField nameField;
    private JTextField ageField;
    private JTextField emailField;
    private JTextField phoneField;
    private JComboBox genderComboBox;
    private JButton saveButton;
    private JButton cancelButton;

    private Customer customer = null;
    private boolean saved = false;

    public Customer getCustomer() {
        return customer;
    }

    public boolean isSaved() {
        return saved;
    }
}
