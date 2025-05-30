package GUI;

import Entities.Product;
import Services.ProductService;

import javax.swing.*;
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


        });
        deleteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

            }
        });
        showAllButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

            }
        });

        setVisible(true);
    }



}
