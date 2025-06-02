package GUI.Sales;

import Entities.Customer;
import Entities.Product;
import Entities.SaleItem;
import Entities.Sales;
import Services.CustomerService;
import Services.ProductService;

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
    private DefaultListModel<SaleItem> itemListModel;
    private Sales sale;

    public CreateSale(CustomerService customerService, ProductService productService) {
        setTitle("New Sale");
        setModal(true);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

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

        // Add Item Button
        addButton.addActionListener(e->{
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
            if (prod.getStock() < quantity) {
                JOptionPane.showMessageDialog(this, "Not enough stock for this product!");
                return;
            }
            SaleItem item = new SaleItem(prod,quantity);
            itemListModel.addElement(item);
            qtyField.setText("");

        });

        // Remove Item Button
        removeButton.addActionListener(e -> {
            int idx = itemList.getSelectedIndex();
            if(idx != -1) {
                itemListModel.remove(idx);
            }
        });


        // Save/Cancel
        saveButton.addActionListener(e -> {
            if (itemListModel.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Add at least one product!");
                return;
            }
            Customer customer = (Customer) customerDrop.getSelectedItem();
            Sales.PaymentMethod paymentMethod = (Sales.PaymentMethod) paymentDrop.getSelectedItem();
            if (customer == null || paymentMethod == null) {
                JOptionPane.showMessageDialog(this, "Choose customer and payment method!");
                return;
            }
            sale = new Sales(customer,paymentMethod);
            for (int i = 0; i < itemListModel.size(); i++) {
                SaleItem item = itemListModel.get(i);
                sale.addItem(item.getProduct(), item.getQuantity());
            }
            sale.setPaymentMethod(paymentMethod);
            dispose();
        });

        cancelButton.addActionListener(e -> {
            sale = null;
            dispose();
        });
    }

    public Sales getSale() {
        return sale;
    }


}
