package GUI.Sales;

import Entities.Customer;
import Entities.Product;
import Entities.SaleItem;
import Entities.Sales;
import Services.CustomerService;
import Services.ProductService;
import Services.SalesService;

import javax.swing.*;

public class UpdateSale extends JDialog {
    private JComboBox<Customer> customerDrop;
    private JComboBox<Product> productDrop;
    private JTextField qtyField;
    private JComboBox<Sales.PaymentMethod> paymentDrop;
    private JButton addButton;
    private JButton removeButton;
    private JList<SaleItem> itemList;
    private JButton saveButton;
    private JButton cancelButton;
    private JPanel panel1;
    private DefaultListModel<SaleItem> itemListModel;
    private JLabel totalLabel;

    private final Sales sale;         // το υπάρχον sale!
    private final SalesService salesService;

    public UpdateSale(Sales sale, CustomerService customerService, ProductService productService, SalesService salesService) {
        this.sale = sale;
        this.salesService = salesService;

        setContentPane(panel1);
        setTitle("Edit Sale");
        setModal(true);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        pack();

        // (setup your panel & layout if needed)
        // setContentPane(panel1);

        // Fill customer dropdown
        customerDrop.removeAllItems();
        for (Customer c : customerService.getAllCustomers()) customerDrop.addItem(c);

        // Προεπιλογή ο πελάτης της τρέχουσας πώλησης:
        if (sale.getCustomer() != null) customerDrop.setSelectedItem(sale.getCustomer());

        // Product dropdown
        productDrop.removeAllItems();
        for (Product p : productService.getAllProducts()) productDrop.addItem(p);

        // Payment method
        paymentDrop.removeAllItems();
        for (Sales.PaymentMethod m : Sales.PaymentMethod.values()) paymentDrop.addItem(m);
        if (sale.getPaymentMethod() != null) paymentDrop.setSelectedItem(sale.getPaymentMethod());

        // Item list
        itemListModel = new DefaultListModel<>();
        itemList.setModel(itemListModel);
        refreshItemList();
        updateTotal();

        // Add Item
        addButton.addActionListener(e -> {
            Product prod = (Product) productDrop.getSelectedItem();
            int quantity;
            try {
                quantity = Integer.parseInt(qtyField.getText().trim());
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Enter a valid quantity!");
                return;
            }
            if (prod == null || quantity <= 0) {
                JOptionPane.showMessageDialog(this, "Select a product and enter positive quantity!");
                return;
            }
            try {
                salesService.addItem(sale, prod, quantity);
                refreshItemList();
                updateTotal();
                qtyField.setText("");
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
            }
        });

        // Remove Item
        removeButton.addActionListener(e -> {
            int idx = itemList.getSelectedIndex();
            if (idx != -1) {
                SaleItem item = sale.getItems().get(idx);
                try {
                    salesService.removeItem(sale, item);
                    refreshItemList();
                    updateTotal();
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
                }
            }
        });

        // Save
        saveButton.addActionListener(e -> {
            if (sale.getItems().isEmpty()) {
                JOptionPane.showMessageDialog(this, "Add at least one product!");
                return;
            }
            Customer customer = (Customer) customerDrop.getSelectedItem();
            Sales.PaymentMethod paymentMethod = (Sales.PaymentMethod) paymentDrop.getSelectedItem();
            if (customer == null || paymentMethod == null) {
                JOptionPane.showMessageDialog(this, "Choose customer and payment method!");
                return;
            }
            sale.setCustomer(customer);
            sale.setPaymentMethod(paymentMethod);
            dispose();
        });

        // Cancel
        cancelButton.addActionListener(e -> {
            // Τυχόν αλλαγές στα sale items έγιναν ήδη (μέσω salesService)
            // Αν δεν θες να σωθούν, πρέπει να ξαναφορτώσεις το αρχικό sale από DB στο parent!
            dispose();
        });
    }

    public Sales getSale() {
        return sale;
    }

    private void refreshItemList() {
        itemListModel.clear();
        for (SaleItem si : sale.getItems()) {
            itemListModel.addElement(si);
        }
    }

    private void updateTotal() {
        if (totalLabel != null)
            totalLabel.setText("Σύνολο: " + sale.getTotalAmount() + " €");
    }
}
