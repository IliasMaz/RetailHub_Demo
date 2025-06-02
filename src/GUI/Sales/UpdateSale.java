package GUI.Sales;

import Entities.Customer;
import Entities.Product;
import Entities.SaleItem;
import Entities.Sales;
import Services.CustomerService;
import Services.ProductService;

import javax.swing.*;

public class UpdateSale extends JDialog {
    private JComboBox<Customer> customerDrop;
    private JComboBox<Product> productDrop;
    private JComboBox<Sales.PaymentMethod> paymentDrop;
    private JTextField qtyField;
    private JList<SaleItem> itemList;
    private JButton addItemButton;
    private JButton removeItemButton;
    private JButton saveButton;
    private JButton cancelButton;

    private DefaultListModel<SaleItem> itemListModel;
    private Sales sale;

    public UpdateSale(Sales oldSale, CustomerService customerService, ProductService productService) {
        setTitle("Update Sale");
        setModal(true);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        // Φόρτωσε Customers
        customerDrop.removeAllItems();
        for (Customer c : customerService.getAllCustomers()) customerDrop.addItem(c);
        customerDrop.setSelectedItem(oldSale.getCustomer());

        // Φόρτωσε Products
        productDrop.removeAllItems();
        for (Product p : productService.getAllProducts()) productDrop.addItem(p);

        // Φόρτωσε Payment Methods
        paymentDrop.removeAllItems();
        for (Sales.PaymentMethod m : Sales.PaymentMethod.values()) paymentDrop.addItem(m);
        paymentDrop.setSelectedItem(oldSale.getPaymentMethod());

        // Φόρτωσε SaleItems στη λίστα
        itemListModel = new DefaultListModel<>();
        for (SaleItem item : oldSale.getItems()) itemListModel.addElement(item);
        itemList.setModel(itemListModel);

        // Add Item
        addItemButton.addActionListener(e -> {
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
            // Αν υπάρχει ήδη, προειδοποίηση (ή μπορείς να προσθέσεις κώδικα να ενημερώνει quantity)
            for(int i=0;i<itemListModel.size();i++){
                SaleItem si = itemListModel.get(i);
                if(si.getProduct().equals(prod)){
                    JOptionPane.showMessageDialog(this, "Product already in sale! Remove it first to change quantity.");
                    return;
                }
            }
            SaleItem item = new SaleItem(prod, quantity);
            itemListModel.addElement(item);
            qtyField.setText("");
        });

        // Remove Item
        removeItemButton.addActionListener(e -> {
            int idx = itemList.getSelectedIndex();
            if(idx != -1) itemListModel.remove(idx);
        });

        // Save
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
            // Δημιούργησε νέο Sale, κράτα το ίδιο id
            Sales updatedSale = new Sales(customer, paymentMethod);
            updatedSale.setId(oldSale.getId()); // Προσοχή: αν έχεις id στο Sales!
            for (int i = 0; i < itemListModel.size(); i++) {
                SaleItem item = itemListModel.get(i);
                updatedSale.addItem(item.getProduct(), item.getQuantity());
            }
            updatedSale.setPaymentMethod(paymentMethod);
            updatedSale.sumTotal();
            this.sale = updatedSale;
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
