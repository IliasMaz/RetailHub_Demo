package GUI.Customer;

import Entities.Customer;

import javax.swing.*;

public class CreateCustomer extends JDialog {
    private JPanel panel1;
    private JTextField nameField;
    private JTextField ageField;
    private JTextField emailField;
    private JTextField phoneField;
    private JComboBox genderComboBox;
    private JButton saveButton;
    private JButton cancelButton;

    private Customer customer = null;
    private boolean saved = false;

    public CreateCustomer() {
        setModal(true);
        setContentPane(panel1);
        setTitle("Create Customer");
        setSize(350, 250);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        saveButton.addActionListener(e -> {
           String name = nameField.getText().trim().toLowerCase();
           String email = emailField.getText().trim().toLowerCase();
           String phone = phoneField.getText().trim();
           String gender = (String) genderComboBox.getSelectedItem();
           String ageTxt = ageField.getText().trim();

           if (name.isEmpty() || email.isEmpty() || phone.isEmpty() ||  gender.isEmpty() || ageTxt.isEmpty()) {
               JOptionPane.showMessageDialog(this, "Please fill all necessary fields");
               return;
           }

           try {
               int ageInt = Integer.parseInt(ageTxt);
               if (ageInt < 0 || ageInt > 120) {
                   JOptionPane.showMessageDialog(this, "Enter a vaild age");
               }

               customer = new Customer(name, email, phone, gender, ageInt);
               saved = true;
               dispose();
           }
           catch (NumberFormatException ex) {
               JOptionPane.showMessageDialog(this, "Invalid input");
           }
        });

        cancelButton.addActionListener(e -> {
            customer = null;
            saved = false;
            dispose();
        });
    }

    public Customer getCustomer() {
        return customer;
    }

    public boolean isSaved() {
        return saved;
    }
}
