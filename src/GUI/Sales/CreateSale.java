package GUI.Sales;

import Entities.Customer;
import Entities.Product;
import Entities.SaleItem;
import Entities.Sales;
import Services.CustomerService;
import Services.ProductService;
import Services.SalesService;

import javax.swing.*;

public class CreateSale extends JDialog {
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

    private final Sales sale;         // ΠΑΝΩ ΣΕ ΑΥΤΟ ΔΟΥΛΕΥΕΙΣ!
    private final SalesService salesService;

    public CreateSale(Sales sale, CustomerService customerService, ProductService productService, SalesService salesService) {
        this.sale = sale;
        this.salesService = salesService;

        setTitle("New Sale");
        setModal(true);
        setContentPane(panel1);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        pack();

        // Customer dropdown
        customerDrop.removeAllItems();
        for (Customer c : customerService.getAllCustomers()) customerDrop.addItem(c);

        // Product dropdown
        productDrop.removeAllItems();
        for (Product p : productService.getAllProducts()) productDrop.addItem(p);

        // Payment Method
        paymentDrop.removeAllItems();
        for (Sales.PaymentMethod m : Sales.PaymentMethod.values()) paymentDrop.addItem(m);

        itemListModel = new DefaultListModel<>();
        itemList.setModel(itemListModel);

        refreshItemList();

        // Add Item Button
        addButton.addActionListener(e -> {
            Product prod = (Product) productDrop.getSelectedItem();
            int quantity;
            try {
                quantity = Integer.parseInt(qtyField.getText().trim());
            } catch (NumberFormatException ex){
                JOptionPane.showMessageDialog(this,"Enter a valid quantity!");
                return;
            }

            if(prod == null || quantity <=0){
                JOptionPane.showMessageDialog(this,"Select a product and enter positive quantity!");
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

        // Remove Item Button
        removeButton.addActionListener(e -> {
            int idx = itemList.getSelectedIndex();
            if(idx != -1) {
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

        // Save/Cancel
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

        cancelButton.addActionListener(e -> {
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
