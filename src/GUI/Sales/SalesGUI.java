package GUI.Sales;

import Entities.SaleItem;
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

        setTitle("RetailHub - GUI.Sales Menu");
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
            Sales newSale = new Sales();
            CreateSale dialog = new CreateSale(newSale, custService, prodService,salesService);
            dialog.setVisible(true);


            Sales completedSale = dialog.getSale();
            if (completedSale != null && completedSale.getCustomer() != null
                    && completedSale.getPaymentMethod() != null
                    && !completedSale.getItems().isEmpty()) {
                try {
                    salesService.finalizeAndSaveSale(completedSale);
                    refreshTable();

                    try {
                        String filePath = "receipts/sale_" + completedSale.getId() + ".txt";
                        Sales.saveReceiptToFile(completedSale, filePath);
                        JOptionPane.showMessageDialog(this, "Receipt created and saved in " + filePath,
                                "Receipt Saved", JOptionPane.INFORMATION_MESSAGE);
                    } catch (Exception ex) {
                        JOptionPane.showMessageDialog(this, "Error:\n" + ex.getMessage());
                    }

                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(this, "Error saving sale: " + ex.getMessage());
                }
            }
        });

        updateButton.addActionListener(e -> {
            int row = table1.getSelectedRow();
            if (row != -1) {
                int id = (int) table1.getValueAt(row, 0);
                Sales originalSale = salesService.getSaleById(id);
                if (originalSale == null) return;


                Sales editableSale = new Sales();
                editableSale.setId(originalSale.getId());
                editableSale.setDate(originalSale.getDate());
                editableSale.setTime(originalSale.getTime());
                editableSale.setTotalAmount(originalSale.getTotalAmount());
                editableSale.setCustomer(originalSale.getCustomer());
                editableSale.setPaymentMethod(originalSale.getPaymentMethod());

                for (SaleItem si : originalSale.getItems()) {
                    SaleItem copy = new SaleItem(si.getProduct(), si.getQuantity());
                    copy.setId(si.getId());
                    editableSale.getItems().add(copy);
                }

                UpdateSale dialog = new UpdateSale(editableSale, custService, prodService, salesService);
                dialog.setVisible(true);

                Sales result = dialog.getSale();
                if (result != null) {

                    originalSale.setCustomer(result.getCustomer());
                    originalSale.setPaymentMethod(result.getPaymentMethod());
                    originalSale.setDate(result.getDate());
                    originalSale.setTime(result.getTime());
                    originalSale.setTotalAmount(result.getTotalAmount());

                    originalSale.getItems().clear();
                    for (SaleItem si : result.getItems()) {
                        originalSale.getItems().add(si);
                    }

                    salesService.finalizeAndSaveSale(originalSale);
                    refreshTable();
                }
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
                Sales sale = salesService.getSaleById(id);
                salesService.cancelSale(sale);
                refreshTable();
            }
        });

        showAllButton.addActionListener(e -> refreshTable());

        searchButton.addActionListener(e -> {
            DefaultTableModel model = (DefaultTableModel) table1.getModel();
            model.setRowCount(0);
            String text = searchField.getText().trim();
            if (!text.isEmpty()) {
                List<Sales> salesList = salesService.findSalesByCustomerName(text);
                for (Sales sale : salesList) {
                    model.addRow(new Object[]{
                            sale.getId(),
                            sale.getDate(),
                            sale.getTime(),
                            sale.getTotalAmount(),
                            sale.getPaymentMethod(),
                            sale.getCustomer() != null ? sale.getCustomer().getName() : ""
                    });
                }
            }
        });

        showDetailsButton.addActionListener(e -> {
            int row = table1.getSelectedRow();
            if (row == -1) {
                JOptionPane.showMessageDialog(this, "Please select a sale to view its details.", "No Sale Selected", JOptionPane.INFORMATION_MESSAGE);
                return;
            }
            int saleId = (int) table1.getValueAt(row, 0);
            Sales saleDetails = salesService.getSaleById(saleId);

            if (saleDetails == null) {
                JOptionPane.showMessageDialog(this, "Could not find details for Sale ID: " + saleId, "Sale Not Found", JOptionPane.ERROR_MESSAGE);
                return;
            }
            StringBuilder details = new StringBuilder();
            details.append("Sale ID: " + saleDetails.getId());
            details.append("\nCustomer Name: " + saleDetails.getCustomer().getName());
            details.append("\nSale Date: " + saleDetails.getDate());
            details.append("\nSale Time: " + saleDetails.getTime());
            details.append("\nPayment Method: " + saleDetails.getPaymentMethod());

            details.append("\nTotal: ").append(saleDetails.getTotalAmount()).append(" €\n");
            details.append("Items:\n");
            for (SaleItem item : saleDetails.getItems()) {
                details.append("- ")
                        .append(item.getName())
                        .append(" x")
                        .append(item.getQuantity())
                        .append(" @ ")
                        .append(item.getPrice())
                        .append(" = ")
                        .append(item.getLineTotal())
                        .append("\n");
            }

            JOptionPane.showMessageDialog(this, details.toString(), "Sale Details", JOptionPane.INFORMATION_MESSAGE);

        });

        receiptButton.addActionListener(e -> {
            int row = table1.getSelectedRow();
            if (row == -1) return;
            int saleId = (int) table1.getValueAt(row, 0);

            Sales sale =  salesService.getSaleById(saleId);
            JOptionPane.showMessageDialog(this, sale.receipt(), "Receipt", JOptionPane.INFORMATION_MESSAGE);
        });


        setVisible(true);
    }

    private void refreshTable() {
        DefaultTableModel model = (DefaultTableModel) table1.getModel();
        model.setRowCount(0);
        List<Sales> sales = salesService.getAllSales();
        for (Sales sale : sales) {
            model.addRow(new Object[]{
                    sale.getId(),
                    sale.getDate(),
                    sale.getTime(),
                    sale.getTotalAmount(),
                    sale.getPaymentMethod(),
                    sale.getCustomer() != null ? sale.getCustomer().getName() : ""
            });
        }
    }




}