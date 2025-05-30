package GUI;

import Entities.Product;

import javax.swing.*;

public class CreateProduct extends JDialog {
    private JPanel panel1;
    private JTextField nameField;
    private JTextField sellPriceField;
    private JTextField purchasePriceField;
    private JTextField categoryField;
    private JTextField stockField;
    private JButton saveButton;
    private JButton cancelButton;

    private Product product = null;
    private boolean saved = false;

    public CreateProduct() {
        setContentPane(panel1);
        setTitle("Create Product");
        setSize(350, 250);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

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
            //O PAVLOS PROTEINEI TIN XRISI WHILE EDW
            //TODO: MORE VALIDATION CHECKS

            try {

                double sellPrice = Double.parseDouble(sellPriceTxt);
                double purchasePrice = Double.parseDouble(purchasePriceTxt);
                int stock = Integer.parseInt(stockTxt);

                product = new Product(name, sellPrice, purchasePrice, stock, category);
                saved = true;
                dispose();

            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Invalid input (in price or stock).");
            }
        });

        cancelButton.addActionListener(e -> {
            product = null;
            saved = false;
            dispose();
        });
    }

    public Product getProduct() {
        return product;
    }

    public boolean isSaved() {
        return saved;
    }
}
