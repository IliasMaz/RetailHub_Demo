package GUI;

import Entities.Product;
import Services.ProductService;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

public class ProductGUI  extends JFrame{
    private JPanel panel1;
    private JButton createButton;
    private JButton deleteButton;
    private JButton updateButton;
    private JTextField searchTextField;
    private JTable table1;
    private JButton showAllButton;
    private JButton button1;

    private ProductService productService;


    public ProductGUI() {

        setTitle("RetailHub - Product Menu");
        setContentPane(panel1);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        pack();
        setLocationRelativeTo(null);


        createButton.addActionListener(e-> {
            CreateProduct dialog = new CreateProduct();
            dialog.setVisible(true);
            Product newProduct = dialog.getProduct();
            if (newProduct != null) {
                productService.createProduct(
                        newProduct.getName(),
                        newProduct.getSellPrice(),
                        newProduct.getPurchasePrice(),
                        newProduct.getStock(),
                        newProduct.getCategory()
                );
                //refreshTable(); TODO PREPEI NA GINEI TO JTABLE PRWTA
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
