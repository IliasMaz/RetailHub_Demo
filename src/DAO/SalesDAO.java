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

    private CustomerDAO customerDAO;
    private SaleItemDAO saleItemDAO;
    private ProductDAO productDAO;

    public SalesDAO(CustomerDAO customerDAO, SaleItemDAO saleItemDAO, ProductDAO productDAO) {
        if (customerDAO == null || saleItemDAO == null || productDAO == null) {
            throw new IllegalArgumentException("DAOs cannot be null for SalesDAO");
        }
        this.customerDAO = customerDAO;
        this.saleItemDAO = saleItemDAO;
        this.productDAO = productDAO;
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

    public boolean updateSale(Sales sale) {
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
            return affectedRows > 0;
        } catch (SQLException e) {
            System.err.println("SalesDAO.deleteSale: Error - " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    public boolean finalizeSaleWithItems(Sales saleToFinalize) {

        String sqlUpdateSale = "UPDATE Sales SET customerId = ?, date = ?, time = ?, totalAmount = ?, paymentMethod = ? WHERE id = ?";
        String sqlInsertSaleItem = "INSERT INTO SaleItems (saleId, productId, quantity) VALUES (?, ?, ?)";

        boolean overallSuccess = false;

        if (saleToFinalize.getId() <= 0) {
            System.err.println("Cannot finalize sale: Sale ID is not valid (must be > 0). Was initiateSale called?");
            return false;
        }

        try (Connection conn = SQLiteConnector.getConnection()) {
            conn.setAutoCommit(false);

            try {
                try (PreparedStatement stmtUpdateSale = conn.prepareStatement(sqlUpdateSale)) {
                    if (saleToFinalize.getCustomer() != null) {
                        stmtUpdateSale.setInt(1, saleToFinalize.getCustomer().getId());
                    } else {
                        stmtUpdateSale.setNull(1, Types.INTEGER);
                    }
                    stmtUpdateSale.setString(2, saleToFinalize.getDate().toString());
                    stmtUpdateSale.setString(3, saleToFinalize.getTime().toString());
                    stmtUpdateSale.setDouble(4, saleToFinalize.getTotalAmount());
                    if (saleToFinalize.getPaymentMethod() != null) {
                        stmtUpdateSale.setString(5, saleToFinalize.getPaymentMethod().name());
                    } else {
                        stmtUpdateSale.setNull(5, Types.VARCHAR);
                    }
                    stmtUpdateSale.setInt(6, saleToFinalize.getId());

                    int rowsUpdatedSale = stmtUpdateSale.executeUpdate();
                    if (rowsUpdatedSale == 0) {

                        throw new SQLException("Updating sale failed, no sale found with ID: " + saleToFinalize.getId());
                    }
                }


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
                        }
                    }
                }

                // Βήμα 3: Οριστική Ενημέρωση Αποθέματος στον πίνακα Products
                if (items != null && !items.isEmpty()) {
                    for (SaleItem item : items) {
                        Product product = item.getProduct();
                        int quantitySold = item.getQuantity();
                        String sqlUpdateStock = "UPDATE Products SET stock = stock - ? WHERE id = ?";
                        try (PreparedStatement stmtStock = conn.prepareStatement(sqlUpdateStock)) {
                            stmtStock.setInt(1, quantitySold);
                            stmtStock.setInt(2, product.getId());
                            int stockRowsAffected = stmtStock.executeUpdate();
                            if (stockRowsAffected == 0) {
                                throw new SQLException("Failed to update stock for product: " + product.getName());
                            }
                        }
                    }
                }

                conn.commit();
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
        String sql = "SELECT id, customerId, date, time, totalAmount, paymentMethod FROM Sales";

        try (Connection conn = SQLiteConnector.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {


                int saleId = rs.getInt("id");
                int customerId = rs.getInt("customerId");
                String dateString = rs.getString("date");
                String timeString = rs.getString("time");
                double totalAmount = rs.getDouble("totalAmount");
                String paymentMethodString = rs.getString("PaymentMethod");

                Customer customer = null;
                if (!rs.wasNull()) {
                    customer = customerDAO.getCustomerById(customerId);
                }

                Sales.PaymentMethod paymentMethod = null;
                if (paymentMethodString != null) {
                    try {
                        paymentMethod = Sales.PaymentMethod.valueOf(paymentMethodString);
                    } catch (IllegalArgumentException e) {
                        System.err.println("Invalid payment method in DB for sale ID " + saleId + ": " + paymentMethodString);
                    }
                }


                Sales sale = new Sales(customer, paymentMethod);
                sale.setId(saleId);


                if (dateString != null) sale.setDate(LocalDate.parse(dateString));
                if (timeString != null) sale.setTime(LocalTime.parse(timeString));
                sale.setTotalAmount(totalAmount);

                List<SaleItem> itemsForThisSale = saleItemDAO.getSaleItemsBySaleId(saleId);

                if (sale.getItems() != null) {
                    sale.getItems().clear();
                    sale.getItems().addAll(itemsForThisSale);
                }
                salesList.add(sale);
            }
        } catch (SQLException e) {
            System.err.println("Error getting all sales: " + e.getMessage());
            e.printStackTrace();
        }
        return salesList;
    }

    public Sales getSaleById(int saleId) {
        Sales sale = null;
        String sql = "SELECT id, customerId, date, time, totalAmount, PaymentMethod FROM Sales WHERE id = ?";

        try (Connection conn = SQLiteConnector.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, saleId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    int customerId = rs.getInt("customerId");
                    String dateString = rs.getString("date");
                    String timeString = rs.getString("time");
                    double totalAmount = rs.getDouble("totalAmount");
                    String paymentMethodString = rs.getString("PaymentMethod");

                    Customer customer = null;
                    if (!rs.wasNull()) {
                        customer = customerDAO.getCustomerById(customerId);
                    }

                    Sales.PaymentMethod paymentMethod = null;
                    if (paymentMethodString != null) {
                        try {
                            paymentMethod = Sales.PaymentMethod.valueOf(paymentMethodString);
                        } catch (IllegalArgumentException e) {
                            System.err.println("Invalid payment method in DB for sale ID " + saleId + ": " + paymentMethodString);
                        }
                    }

                    sale = new Sales(customer, paymentMethod);
                    sale.setId(rs.getInt("id"));

                    if (dateString != null) sale.setDate(LocalDate.parse(dateString));
                    if (timeString != null) sale.setTime(LocalTime.parse(timeString));
                    sale.setTotalAmount(totalAmount);

                    List<SaleItem> itemsForThisSale = saleItemDAO.getSaleItemsBySaleId(saleId);
                    if (sale.getItems() != null) {
                        sale.getItems().clear();
                        sale.getItems().addAll(itemsForThisSale);
                    }
                }
            }
        } catch (SQLException e) {
            System.err.println("Error getting sale by ID ("+ saleId +"): " + e.getMessage());
            e.printStackTrace();
        }
        return sale;
    }


    public boolean updateSaleHeader(Sales sale) {
        String sql = "UPDATE Sales SET customerId = ?, date = ?, time = ?, totalAmount = ?, paymentMethod = ? WHERE id = ?";
        try (Connection conn = SQLiteConnector.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            if (sale.getCustomer() != null) {
                stmt.setInt(1, sale.getCustomer().getId());
            } else {
                stmt.setNull(1, Types.INTEGER);
            }
            stmt.setString(2, sale.getDate().toString());
            stmt.setString(3, sale.getTime().toString());
            stmt.setDouble(4, sale.getTotalAmount());
            if (sale.getPaymentMethod() != null) {
                stmt.setString(5, sale.getPaymentMethod().name());
            } else {
                stmt.setNull(5, Types.VARCHAR);
            }
            stmt.setInt(6, sale.getId());
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error updating sale header for ID " + sale.getId() + ": " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    public List<Sales> getSalesByCustomerId(int customerId) {
        List<Sales> salesList = new ArrayList<>();
        String sql = "SELECT id, customerId, date, time, totalAmount, paymentMethod FROM Sales WHERE customerId = ?";

        try (Connection conn = SQLiteConnector.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, customerId);

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    int saleId = rs.getInt("id");
                    int currentCustomerId = rs.getInt("customerId");
                    String dateString = rs.getString("date");
                    String timeString = rs.getString("time");
                    double totalAmount = rs.getDouble("totalAmount");
                    String paymentMethodString = rs.getString("PaymentMethod");

                    Customer customer = null;
                    if (!rs.wasNull() && currentCustomerId > 0) {
                        customer = customerDAO.getCustomerById(currentCustomerId);
                    }

                    Sales.PaymentMethod paymentMethod = null;
                    if (paymentMethodString != null) {
                        try {
                            paymentMethod = Sales.PaymentMethod.valueOf(paymentMethodString);
                        } catch (IllegalArgumentException e) {
                            System.err.println("Invalid payment method in DB for sale ID " + saleId + ": " + paymentMethodString);
                        }
                    }

                    Sales sale = new Sales(customer, paymentMethod);
                    sale.setId(saleId);
                    if (dateString != null) sale.setDate(LocalDate.parse(dateString));
                    if (timeString != null) sale.setTime(LocalTime.parse(timeString));
                    sale.setTotalAmount(totalAmount);

                    List<SaleItem> itemsForThisSale = saleItemDAO.getSaleItemsBySaleId(saleId);
                    if (sale.getItems() != null) {
                        sale.getItems().clear();
                        sale.getItems().addAll(itemsForThisSale);
                    }

                    salesList.add(sale);
                }
            }
        } catch (SQLException e) {
            System.err.println("Error getting sales by customer ID (" + customerId + "): " + e.getMessage());
            e.printStackTrace();
        }
        return salesList;
    }

    public List<Sales> getSalesByCustomerName(String customerName) {
        List<Sales> salesList = new ArrayList<>();
        String sql = "SELECT s.id, s.customerId, s.date, s.time, s.totalAmount, s.paymentMethod, " +
                "c.id as customer_id, c.name as customer_name " +
                "FROM Sales s " +
                "LEFT JOIN Customers c ON s.customerId = c.id " +
                "WHERE LOWER(c.name) LIKE ?";
        try (Connection conn = SQLiteConnector.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, "%" + customerName.toLowerCase() + "%");
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                int saleId = rs.getInt("id");
                int customerId = rs.getInt("customer_id");
                String custName = rs.getString("customer_name");
                String dateString = rs.getString("date");
                String timeString = rs.getString("time");
                double totalAmount = rs.getDouble("totalAmount");
                String paymentMethodString = rs.getString("paymentMethod");

                Customer customer = new Customer();
                customer.setId(customerId);
                customer.setName(custName);

                Sales.PaymentMethod paymentMethod = null;
                if (paymentMethodString != null) {
                    try {
                        paymentMethod = Sales.PaymentMethod.valueOf(paymentMethodString);
                    } catch (IllegalArgumentException e) {
                        System.err.println("Invalid payment method in DB for sale ID " + saleId + ": " + paymentMethodString);
                    }
                }

                Sales sale = new Sales(customer, paymentMethod);
                sale.setId(saleId);
                if (dateString != null) sale.setDate(LocalDate.parse(dateString));
                if (timeString != null) sale.setTime(LocalTime.parse(timeString));
                sale.setTotalAmount(totalAmount);

                // Προαιρετικά: φόρτωσε και sale items
                List<SaleItem> itemsForThisSale = saleItemDAO.getSaleItemsBySaleId(saleId);
                if (sale.getItems() != null) {
                    sale.getItems().clear();
                    sale.getItems().addAll(itemsForThisSale);
                }

                salesList.add(sale);
            }
        } catch (SQLException e) {
            System.err.println("Error getting sales by customer name: " + e.getMessage());
            e.printStackTrace();
        }
        return salesList;
    }




}




