package DAO;

import Connector.SQLiteConnector;
import Entities.Sales;
import Entities.Customer;
import Entities.SaleItem;
import Entities.Product;

import java.sql.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

public class SalesDAO {

    private SaleItemDAO saleItemDAO;

    public SalesDAO(SaleItemDAO saleItemDAO) {
        if (saleItemDAO == null) {
            throw new IllegalArgumentException("SaleItemDAO cannot be null for SalesDAO");
        }
        this.saleItemDAO = saleItemDAO;
    }


    public boolean initiateSale(Sales initialSale) {

        String sql = "INSERT INTO Sales (customerId, date, time, totalAmount, paymentMethod) VALUES (?, ?, ?, ?, ?)";

        try (Connection conn = SQLiteConnector.getConnection();
             PreparedStatement statement = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            if (initialSale.getCustomer() != null) {
                statement.setInt(1, initialSale.getCustomer().getId());
            } else {
                statement.setNull(1, Types.INTEGER);
            }

            if (initialSale.getDate() != null) {
                statement.setString(2, initialSale.getDate().toString());
            } else {
                statement.setNull(2, Types.VARCHAR); // Ή Types.NULL
            }

            if (initialSale.getTime() != null) {
                statement.setString(3, initialSale.getTime().toString());
            } else {
                statement.setNull(3, Types.VARCHAR); // Ή Types.NULL
            }

            statement.setDouble(4, initialSale.getTotalAmount());

            if (initialSale.getPaymentMethod() != null) {
                statement.setString(5, initialSale.getPaymentMethod().name());
            } else {
                statement.setNull(5, Types.VARCHAR); // Ή Types.NULL
            }

            int rowsInserted = statement.executeUpdate();

            if (rowsInserted > 0) {
                try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        initialSale.setId(generatedKeys.getInt(1)); // Θέσε το ID στο αντικείμενο Sales
                        return true;
                    }
                }
            }
        } catch (SQLException e) {
            System.err.println("Error initiating sale: " + e.getMessage());
            e.printStackTrace();
        }
        return false;
    }

    public boolean updateSaleRecord(Sales sale) {
        if (sale == null || sale.getId() <= 0) {
            System.err.println("SalesDAO.updateSaleRecord: Invalid sale object or ID.");
            return false;
        }
        String sql = "UPDATE Sales SET customerId = ?, date = ?, time = ?, totalAmount = ?, paymentMethod = ? WHERE id = ?";
        try (Connection conn = SQLiteConnector.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            if (sale.getCustomer() != null) {
                pstmt.setInt(1, sale.getCustomer().getId());
            } else {
                pstmt.setNull(1, Types.INTEGER);
            }
            // Βεβαιωθείτε ότι η ημερομηνία και η ώρα έχουν οριστεί στο αντικείμενο 'sale'
            pstmt.setString(2, (sale.getDate() != null ? sale.getDate().toString() : null));
            pstmt.setString(3, (sale.getTime() != null ? sale.getTime().toString() : null));
            pstmt.setDouble(4, sale.getTotalAmount());
            if (sale.getPaymentMethod() != null) {
                pstmt.setString(5, sale.getPaymentMethod().name());
            } else {
                pstmt.setNull(5, Types.VARCHAR);
            }
            pstmt.setInt(6, sale.getId());

            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            System.err.println("SalesDAO.updateSaleRecord: Error - " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    public boolean deleteSale(int saleId) {
        String sql = "DELETE FROM Sales WHERE id = ?";
        try (Connection conn = SQLiteConnector.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, saleId);
            int affectedRows = pstmt.executeUpdate();
            // Αν το ON DELETE CASCADE είναι ενεργό για τα SaleItems, θα διαγραφούν αυτόματα.
            return affectedRows > 0;
        } catch (SQLException e) {
            System.err.println("SalesDAO.deleteSale: Error - " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    public boolean finalizeSaleWithItems(Sales saleToFinalize) {
        // SQL για την ενημέρωση της κύριας εγγραφής Sales
        String sqlUpdateSale = "UPDATE Sales SET customerId = ?, date = ?, time = ?, totalAmount = ?, paymentMethod = ? WHERE id = ?";
        String sqlInsertSaleItem = "INSERT INTO SaleItems (saleId, productId, quantity, unitPrice) VALUES (?, ?, ?, ?)";
        // Υποθέτουμε ότι ο πίνακας SaleItems έχει στήλη unitPrice.

        boolean overallSuccess = false;

        if (saleToFinalize.getId() <= 0) {
            System.err.println("Cannot finalize sale: Sale ID is not valid (must be > 0). Was initiateSale called?");
            return false;
        }

        try (Connection conn = SQLiteConnector.getConnection()) {
            conn.setAutoCommit(false); // Έναρξη transaction

            try {
                // Βήμα 1: Ενημέρωση της υπάρχουσας εγγραφής Sales
                try (PreparedStatement stmtUpdateSale = conn.prepareStatement(sqlUpdateSale)) {
                    if (saleToFinalize.getCustomer() != null) {
                        stmtUpdateSale.setInt(1, saleToFinalize.getCustomer().getId());
                    } else {
                        stmtUpdateSale.setNull(1, Types.INTEGER);
                    }
                    stmtUpdateSale.setString(2, saleToFinalize.getDate().toString());
                    stmtUpdateSale.setString(3, saleToFinalize.getTime().toString());
                    stmtUpdateSale.setDouble(4, saleToFinalize.getTotalAmount()); // Το τελικό totalAmount
                    if (saleToFinalize.getPaymentMethod() != null) {
                        stmtUpdateSale.setString(5, saleToFinalize.getPaymentMethod().name());
                    } else {
                        stmtUpdateSale.setNull(5, Types.VARCHAR); // Αν το PaymentMethod μπορεί να είναι NULL
                    }
                    stmtUpdateSale.setInt(6, saleToFinalize.getId()); // Το ID της πώλησης που ενημερώνουμε

                    int rowsUpdatedSale = stmtUpdateSale.executeUpdate();
                    if (rowsUpdatedSale == 0) {
                        // Δεν βρέθηκε πώληση με αυτό το ID για ενημέρωση, πιθανόν σφάλμα.
                        throw new SQLException("Updating sale failed, no sale found with ID: " + saleToFinalize.getId());
                    }
                } // Το stmtUpdateSale κλείνει

                // Βήμα 2: Εισαγωγή των SaleItems
                // (Υποθέτουμε ότι τυχόν παλιά SaleItems για αυτή την πώληση έχουν ήδη διαγραφεί
                //  ή ότι η λογική του GUI/Service διασφαλίζει ότι προσθέτουμε μόνο νέα.
                //  Μια πιο πλήρης update θα μπορούσε να διαγράφει τα παλιά items πρώτα.)
                List<SaleItem> items = saleToFinalize.getItems();
                if (items != null && !items.isEmpty()) {
                    for (SaleItem item : items) {
                        try (PreparedStatement stmtItem = conn.prepareStatement(sqlInsertSaleItem, Statement.RETURN_GENERATED_KEYS)) {
                            stmtItem.setInt(1, saleToFinalize.getId()); // Το ID της πώλησης
                            if (item.getProduct() != null) {
                                stmtItem.setInt(2, item.getProduct().getId());
                            } else {
                                throw new SQLException("Product in SaleItem cannot be null.");
                            }
                            stmtItem.setInt(3, item.getQuantity());
                            stmtItem.setDouble(4, item.getPrice()); // Η "παγωμένη" unitPrice

                            int itemInserted = stmtItem.executeUpdate();
                            if (itemInserted > 0) {
                                try (ResultSet itemGeneratedKeys = stmtItem.getGeneratedKeys()) {
                                    if (itemGeneratedKeys.next()) {
                                        item.setId(itemGeneratedKeys.getInt(1));
                                    }
                                }
                            } else {
                                throw new SQLException("Failed to insert sale item for product: " + item.getName());
                            }
                        } // Το stmtItem κλείνει
                    }
                }

                // Βήμα 3: Οριστική Ενημέρωση Αποθέματος στον πίνακα Products
                if (items != null && !items.isEmpty()) {
                    for (SaleItem item : items) {
                        Product product = item.getProduct();
                        int quantitySold = item.getQuantity();
                        // Ιδανικά, αυτή η λογική θα ήταν σε μια μέθοδο productDAO.decreaseStock(product.getId(), quantitySold, conn)
                        String sqlUpdateStock = "UPDATE Products SET stock = stock - ? WHERE id = ?";
                        try (PreparedStatement stmtStock = conn.prepareStatement(sqlUpdateStock)) {
                            stmtStock.setInt(1, quantitySold);
                            stmtStock.setInt(2, product.getId());
                            int stockRowsAffected = stmtStock.executeUpdate();
                            if (stockRowsAffected == 0) {
                                throw new SQLException("Failed to update stock for product: " + product.getName());
                            }
                        } // Το stmtStock κλείνει
                    }
                }

                conn.commit(); // Όλα πήγαν καλά, κάνε commit
                overallSuccess = true;

            } catch (SQLException e) {
                System.err.println("Error during sale finalization, rolling back: " + e.getMessage());
                e.printStackTrace();
                if (conn != null) {
                    try {
                        conn.rollback();
                    } catch (SQLException excep) {
                        System.err.println("Error during rollback: " + excep.getMessage());
                        excep.printStackTrace();
                    }
                }
            }
        } catch (SQLException e) {
            System.err.println("Error obtaining database connection for finalizing sale: " + e.getMessage());
            e.printStackTrace();
        }
        return overallSuccess;
    }


    public List<Sales> getAllSales() {

        List<Sales> salesList = new ArrayList<>();
        String sql = "SELECT * FROM Sales";

        try (Connection conn = SQLiteConnector.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Sales sale = new Sales();
                sale.setId(rs.getInt(id));

            }



        }catch (SQLException e) {
            System.err.println("Error getting all sales: " + e.getMessage());
            e.printStackTrace();
        }
        return salesList;
    }
}




