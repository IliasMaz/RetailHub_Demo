package GUI.Product;

import Entities.Product;
import Services.ProductService;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.util.List;

public class ProductGUI  extends JFrame{
    private JPanel panel1;
    private JButton createButton;
    private JButton deleteButton;
    private JButton updateButton;
    private JTextField searchTextField;
    private JTable table1;
    private JButton showAllButton;
    private JButton searchButton;
    private ProductService productService;

    public ProductGUI(ProductService productService) {
        this.productService = productService;

        setTitle("RetailHub - Product Menu");
        setContentPane(panel1);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        pack();
        setLocationRelativeTo(null);

        table1.setModel(new DefaultTableModel(
                new Object[]{"ID", "Name", "Sell Price", "Purchase Price", "Stock", "Category"}, 0
        ));

        refreshTable();

        createButton.addActionListener(e-> {
            CreateProduct dialog = new CreateProduct();
            dialog.setVisible(true);
            Product newProduct = dialog.getProduct();
            System.out.println("Product from dialog: " + newProduct);
            if (newProduct != null) {
                productService.createProduct(
                        newProduct.getName(),
                        newProduct.getSellPrice(),
                        newProduct.getPurchasePrice(),
                        newProduct.getStock(),
                        newProduct.getCategory()
                );
                refreshTable();
            }
            else {
                System.out.println("newProduct is null. User probably cancelled or save failed in dialog.");
            }
        });

        updateButton.addActionListener(e-> {
            int row = table1.getSelectedRow();
            if (row == -1) {
                JOptionPane.showMessageDialog(this, "Επίλεξε πρώτα προϊόν!");
                return;
            }
            int id = (int) table1.getValueAt(row, 0);
            Product product = productService.getProductById(id);

            UpdateProduct dialog = new UpdateProduct(product);
            dialog.setVisible(true);

            Product updatedProduct = dialog.getProduct();
            if (updatedProduct != null) {
                productService.updateProduct(updatedProduct);
                refreshTable();
            }

        });

        deleteButton.addActionListener(e-> {
            int row = table1.getSelectedRow();
            if (row == -1) {
                JOptionPane.showMessageDialog(this, "Επίλεξε πρώτα προϊόν!");
                return;
            }
            int id = (int) table1.getValueAt(row, 0);
            int confirm = JOptionPane.showConfirmDialog(this, "Να διαγραφεί το προϊόν;", "Διαγραφή", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                productService.deleteProduct(id);
                refreshTable();
            }

        });

        showAllButton.addActionListener(e-> {
            refreshTable();
        });



        setVisible(true);

        searchButton.addActionListener(e-> {
            DefaultTableModel model = (DefaultTableModel) table1.getModel();
            model.setRowCount(0);
            String text = searchTextField.getText().trim();
            if (!text.isEmpty()) {
                Product p = productService.findByName(text);
                model.addRow(new Object[]{
                        p.getId(), p.getName(), p.getSellPrice(), p.getPurchasePrice(), p.getStock(), p.getCategory()

                });

            }


        });
    }

    private void refreshTable() {
        DefaultTableModel model = (DefaultTableModel) table1.getModel();
        model.setRowCount(0);
        List<Product> products = productService.getAllProducts();
        for (Product p : products) {
            model.addRow(new Object[]{
                    p.getId(), p.getName(), p.getSellPrice(), p.getPurchasePrice(), p.getStock(), p.getCategory()
            });
        }
    }

}
