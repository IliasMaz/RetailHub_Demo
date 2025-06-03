package GUI.Sales;

import Entities.Sales;
import Services.CustomerService;
import Services.ProductService;
import Services.SalesService;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.util.List;

public class SalesGUI extends JFrame {
    private JButton updateButton;
    private JButton showAllButton;
    private JButton createButton;
    private JButton deleteButton;
    private JTable table1;
    private JTextField searchField;
    private JButton searchButton;
    private JButton showDetailsButton;
    private JButton receiptButton;
    private JPanel panel1;

    private SalesService salesService;
    private CustomerService custService;
    private ProductService prodService;

    public SalesGUI(SalesService salesService, CustomerService custService, ProductService prodService) {
        this.salesService = salesService;
        this.custService = custService;
        this.prodService = prodService;

        setTitle("RetailHub - Sales Menu");
        setContentPane(panel1);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        pack();
        setLocationRelativeTo(null);

        // Table
        table1.setModel(new DefaultTableModel(
                new Object[]{"ID", "Date", "Time", "Total Amount", "Payment Method", "Customer"}, 0
        ));

        refreshTable();

        createButton.addActionListener(e -> {
            CreateSale dialog = new CreateSale(custService, prodService);
            dialog.setVisible(true);
            Sales saleCompleted = dialog.getSale();
            if (saleCompleted != null) {
                salesService.finalizeAndSaveSale(saleCompleted);
                refreshTable();
            }
        });

        updateButton.addActionListener(e -> {
            int row = table1.getSelectedRow();
            if (row == -1) {
                JOptionPane.showMessageDialog(this, "Choose sale first!");
                return;
            }
            int id = (int) table1.getValueAt(row, 0);
            Sales sale = salesService.getSaleById(id);
            UpdateSale dialog = new UpdateSale(sale, custService, prodService);
            dialog.setVisible(true);
            Sales updatedSale = dialog.getSale();
            if (updatedSale != null) {
                //salesService.updateSale(updatedSale);
                refreshTable();
            }
        });

        deleteButton.addActionListener(e -> {
            int row = table1.getSelectedRow();
            if (row == -1) {
                JOptionPane.showMessageDialog(this, "Choose sale first!");
                return;
            }
            int id = (int) table1.getValueAt(row, 0);
            int confirm = JOptionPane.showConfirmDialog(this, "Delete sale?", "Deletion", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                salesService.deleteSale(id);
                refreshTable();
            }
        });

        showAllButton.addActionListener(e -> refreshTable());

        searchButton.addActionListener(e -> {
            DefaultTableModel model = (DefaultTableModel) table1.getModel();
            model.setRowCount(0);
            String text = searchField.getText().trim();
            int customerId = Integer.parseInt(text);
            if (customerId <= 0) {
                JOptionPane.showMessageDialog(this, "Customer ID must be a positive number.", "Invalid Input", JOptionPane.ERROR_MESSAGE);
                return;
            }
            List<Sales> salesFound = salesService.findSalesByCustomer(customerId);
            for (Sales sale : salesFound) {
                model.addRow(new Object[]{
                        sale.getId(),
                        (sale.getCustomer() != null ? sale.getCustomer().getName() : "N/A"),
                        sale.getDate(),
                        sale.getTime(),
                        sale.getTotalAmount(),
                        sale.getPaymentMethod()
                });
                }
            });





        setVisible(true);
    }

    private void refreshTable() {
        DefaultTableModel model = (DefaultTableModel) table1.getModel();
        model.setRowCount(0);
        List<Sales> sales = salesService.getAllSales();
        for (Sales sale : sales) {
            model.addRow(new Object[]{
                    sale.getId(), sale.getCustomer().getName(), sale.getDate(), sale.getTime(), sale.getTotalAmount(), sale.getPaymentMethod()
            });
        }
    }




}