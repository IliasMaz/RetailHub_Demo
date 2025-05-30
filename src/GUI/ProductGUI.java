package GUI;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ProductGUI {
    private JPanel panel1;
    private JButton createButton;
    private JButton deleteButton;
    private JButton updateButton;
    private JTextField searchTextField;
    private JTable table1;
    private JButton showAllButton;
    private JButton button1;


    public ProductGUI() {
        createButton.addActionListener(e-> {
            new CreateProduct();
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
    }
}
