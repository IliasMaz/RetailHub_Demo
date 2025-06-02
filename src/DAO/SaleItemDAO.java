package DAO;

import Connector.SQLiteConnector;
import Entities.SaleItem;
import Entities.Product;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class SaleItemDAO {

    private ProductDAO productDAO;

    public SaleItemDAO(ProductDAO productDAO) {
        if (productDAO == null) {
            throw new IllegalArgumentException("ProductDAO cannot be null for SaleItemDAO");
        }
        this.productDAO = productDAO;
    }

    boolean createSaleItem(SaleItem saleItem, int saleId){

        String sql = "INSERT INTO SaleItems (saleId, productId, quantity) VALUES (?, ?, ?)";

        try (Connection conn = SQLiteConnector.getConnection();
             PreparedStatement statement = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)){

            statement.setInt(1, saleId);
            if (saleItem.getProduct() != null) {
                statement.setInt(2, saleItem.getProduct().getId());
            } else {
                throw new IllegalArgumentException("Product cannot be null");

            }
            statement.setInt(3, saleItem.getQuantity());

            int rowsInserted = statement.executeUpdate();

            if (rowsInserted > 0) {

                try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
                    if (generatedKeys.next()) {

                         saleItem.setId(generatedKeys.getInt(1));
                        return true;

                    }
                }
            }

        }catch (SQLException e){
            System.err.println("Error inserting sale item: " + e.getMessage());
            e.printStackTrace();
        }
        return false;
    }

    public boolean updateSaleItem(SaleItem saleItem){

        String sql = "UPDATE SaleItems SET quantity = ? WHERE id = ?";

        try (Connection conn = SQLiteConnector.getConnection();
            PreparedStatement statement = conn.prepareStatement(sql)) {

            statement.setInt(1, saleItem.getQuantity());
            statement.setInt(2, saleItem.getId());

            int rowsUpdated = statement.executeUpdate();

            if (rowsUpdated > 0) {
                return true;
            }
        } catch (SQLException e) {
            System.err.println("Error updating sale item: " + e.getMessage());
            e.printStackTrace();
        }
        return false;
    }

    public boolean deleteSaleItem(int SaleItemId){

        String sql = "DELETE FROM SaleItems WHERE id = ?";

        try (Connection conn = SQLiteConnector.getConnection();
             PreparedStatement statement = conn.prepareStatement(sql)){

            statement.setInt(1, SaleItemId);

            int rowsDeleted = statement.executeUpdate();

            if (rowsDeleted > 0) {
                return true;
            }

        }catch (SQLException e){
            System.err.println("Error deleting sale item: " + e.getMessage());
            e.printStackTrace();
        }
            return false;
    }

    public List<SaleItem> getSaleItemsBySaleId(int saleId){

        List<SaleItem> saleItemsList = new ArrayList<>();
        String sql = "SELECT * FROM SaleItems WHERE salesId = ?";

        try (Connection conn = SQLiteConnector.getConnection();
             PreparedStatement statement = conn.prepareStatement(sql)){

            statement.setInt(1, saleId);

            ResultSet rs = statement.executeQuery();

                while (rs.next()){
                    int saleItemIdFromDB = rs.getInt("id");
                    int productId = rs.getInt("productId");
                    int quantity = rs.getInt("quantity");
                    Product product = productDAO.getProductById(productId);

                    if (product != null) {
                        SaleItem saleItem = new SaleItem();
                        saleItem.setId(saleItemIdFromDB);
                        saleItem.setProduct(product);
                        saleItem.setQuantity(quantity);
                        saleItemsList.add(saleItem);
                    } else {
                        System.err.println("Warning: Product with ID " + productId + " not found for SaleItem ID " + saleItemIdFromDB);
                    }
                }


        }catch (SQLException e){
            System.err.println("Error fetching sale items by sale id: " + e.getMessage());
            e.printStackTrace();
        }

        return saleItemsList;
    }


}

