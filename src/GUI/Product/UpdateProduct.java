package GUI.Product;

import Entities.Product;

import javax.swing.*;

public class UpdateProduct extends JDialog {
    private JTextField nameField;
    private JTextField sellPriceField;
    private JTextField purchasePriceField;
    private JTextField categoryField;
    private JTextField stockField;
    private JButton saveButton;
    private JButton cancelButton;
    private JPanel panel1;
    private Product updatedProduct = null;
    private boolean saved = false;

    public UpdateProduct(Product product) {
        setModal(true);
        setContentPane(panel1);
        setTitle("Update Product");
        setSize(350, 250);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        nameField.setText(product.getName());
        sellPriceField.setText(String.valueOf(product.getSellPrice()));
        purchasePriceField.setText(String.valueOf(product.getPurchasePrice()));
        stockField.setText(String.valueOf(product.getStock()));
        categoryField.setText(product.getCategory());

        saveButton.addActionListener(e -> {
            String name = nameField.getText().trim();
            String sellPriceTxt = sellPriceField.getText().trim();
            String purchasePriceTxt = purchasePriceField.getText().trim();
            String stockTxt = stockField.getText().trim();
            String category = categoryField.getText().trim();

            if (name.isEmpty() || purchasePriceTxt.isEmpty() || stockTxt.isEmpty() || sellPriceTxt.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please fill all the necessary fields!");
                return;
            }

            try {
                double sellPrice = Double.parseDouble(sellPriceTxt);
                double purchasePrice = Double.parseDouble(purchasePriceTxt);
                int stock = Integer.parseInt(stockTxt);

                if (sellPrice < 0 || purchasePrice < 0 || stock < 0) {
                    JOptionPane.showMessageDialog(this, "Prices and stock must be non-negative numbers!");
                    return;
                }


                updatedProduct = new Product(name, sellPrice, purchasePrice, stock, category);
                updatedProduct.setId(product.getId());
                saved = true;
                dispose();

            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Invalid input (in price or stock).");
            }
        });

        cancelButton.addActionListener(e -> {
            updatedProduct = null;
            saved = false;
            dispose();
        });
    }

    public Product getProduct() {
        return updatedProduct;
    }

    public boolean isSaved() {
        return saved;
    }
}
