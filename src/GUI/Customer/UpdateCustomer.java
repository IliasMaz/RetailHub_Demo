package GUI.Customer;

import Entities.Customer;

import javax.swing.*;

public class UpdateCustomer extends JDialog{
    private JTextField updatedName;
    private JTextField updatedPhone;
    private JTextField updatedEmail;
    private JButton saveButton;
    private JButton cancelButton;
    private JPanel panel1;

    private boolean saved = false;
    private Customer updatedCustomer = null;

    public UpdateCustomer(Customer customer){
        setModal(true);
        setContentPane(panel1);
        setTitle("Update Customer");
        setSize(350, 250);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        updatedName.setText(customer.getName());
        updatedPhone.setText(customer.getPhone());
        updatedEmail.setText(customer.getEmail());

        saveButton.addActionListener(e-> {
            String name = updatedName.getText();
            String phone = updatedPhone.getText();
            String email = updatedEmail.getText();

            if (name.isEmpty() || email.isEmpty() || phone.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please fill all the necessary fields!");
                return;
            }

            updatedCustomer = new Customer(name, email, phone, customer.getGender(), customer.getAge());
            updatedCustomer.setId(customer.getId());
            saved = true;
            dispose();
        });

        cancelButton.addActionListener(e->{
            updatedCustomer = null;
            saved = false;
            dispose();
        });
    }


    public Customer getCustomer() {
        return updatedCustomer;
    }

    public boolean isSaved() {
        return saved;
    }
}
