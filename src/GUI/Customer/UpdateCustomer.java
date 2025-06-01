package GUI.Customer;

import Entities.Customer;

import javax.swing.*;

public class UpdateCustomer extends JDialog{
    private JTextField updatedName;
    private JTextField updatedPhone;
    private JTextField updatedEmail;
    private JButton saveButton;
    private JButton cancelButton;

    private boolean saved = false;
    private Customer updatedCustomer = null;

    public UpdateCustomer(Customer customer){

    }


    public Customer getCustomer() {
        return updatedCustomer;
    }

    public boolean isSaved() {
        return saved;
    }
}
