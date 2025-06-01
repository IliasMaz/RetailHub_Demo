package GUI.Customer;

import Entities.Customer;
import Services.CustomerService;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.util.List;

public class CustomerGUI extends JFrame {
    private JButton updateButton;
    private JButton createButton;
    private JButton deleteButton;
    private JButton showAllButton;
    private JTextField searchField;
    private JButton searchButton;
    private JTable table1;
    private JPanel panel1;
    private CustomerService customerService;

    public CustomerGUI(CustomerService customerService) {
        this.customerService = customerService;

        setTitle("RetailHub - Customer Menu");
        setContentPane(panel1);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        pack();
        setLocationRelativeTo(null);

        table1.setModel(new DefaultTableModel(
                new Object[]{"ID", "Name", "Email", "Phone", "Gender", "Age", "Loyalty Points"}, 0
        ));

        refreshTable();

        createButton.addActionListener(e -> {
            CreateCustomer dialog = new CreateCustomer();
            dialog.setVisible(true);
            Customer newCustomer = dialog.getCustomer();
            if (newCustomer != null) {
                customerService.createCustomer(
                        newCustomer.getName(),
                        newCustomer.getEmail(),
                        newCustomer.getPhone(),
                        newCustomer.getGender(),
                        newCustomer.getAge()
                );
                refreshTable();
            }
            else {
                System.out.println("newCustomer is null. User probably cancelled or save failed in dialog.");
            }
        });

        updateButton.addActionListener(e -> {
            int row = table1.getSelectedRow();
            if (row == -1) {
                JOptionPane.showMessageDialog(this, "Choose customer first!");
                return;
            }
            int id = (int) table1.getValueAt(row, 0);
            Customer customer = customerService.getAllCustomers().stream()
                    .filter(c -> c.getId() == id).findFirst().orElse(null);

            if (customer != null) {
                UpdateCustomer dialog = new UpdateCustomer(customer);
                dialog.setVisible(true);

                Customer updatedCustomer = dialog.getCustomer();
                if (updatedCustomer != null) {
                    customerService.updateCustomer(updatedCustomer);
                    refreshTable();
                }
            }
        });

        deleteButton.addActionListener(e -> {
            int row = table1.getSelectedRow();
            if (row == -1) {
                JOptionPane.showMessageDialog(this, "Choose customer first!");
                return;
            }
            int id = (int) table1.getValueAt(row, 0);
            int confirm = JOptionPane.showConfirmDialog(this, "Delete Customer?", "Deletion", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                customerService.deleteCustomer(id);
                refreshTable();
            }
        });

        showAllButton.addActionListener(e -> refreshTable());

        searchButton.addActionListener(e -> {
            DefaultTableModel model = (DefaultTableModel) table1.getModel();
            model.setRowCount(0);
            String text = searchField.getText().trim();
            if (!text.isEmpty()) {
                Customer c = customerService.findByName(text);
                if (c != null) {
                    model.addRow(new Object[]{
                            c.getId(), c.getName(), c.getEmail(), c.getPhone(), c.getGender(),
                            c.getAge(), c.getLoyaltyPoints()
                    });
                }
            }
        });

        setVisible(true);
    }

    private void refreshTable() {
        DefaultTableModel model = (DefaultTableModel) table1.getModel();
        model.setRowCount(0);
        List<Customer> customers = customerService.getAllCustomers();
        for (Customer c : customers) {
            model.addRow(new Object[]{
                    c.getId(), c.getName(), c.getEmail(), c.getPhone(), c.getGender(),
                    c.getAge(), c.getLoyaltyPoints()
            });
        }
    }




}
