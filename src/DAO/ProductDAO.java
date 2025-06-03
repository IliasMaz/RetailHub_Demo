package DAO;

import Connector.SQLiteConnector;
import Entities.Product;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ProductDAO {

    public boolean createProduct(Product product) {

        String sql = "INSERT INTO Products (name, sellPrice, purchasePrice, stock, category) VALUES (?, ?, ?, ?, ?)";

        try (Connection conn = SQLiteConnector.getConnection();
            PreparedStatement statement = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)){
            statement.setString(1, product.getName());
            statement.setDouble(2, product.getSellPrice());
            statement.setDouble(3, product.getPurchasePrice());
            statement.setInt(4, product.getStock());
            statement.setString(5, product.getCategory());


            int rowsInserted = statement.executeUpdate();

            if (rowsInserted > 0) {
                try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        product.setId(generatedKeys.getInt(1));
                        return true;
                    }
                }
            }

        }catch (SQLException e){
            System.err.println("Error inserting product: " + e.getMessage());
            e.printStackTrace();
        }
        return false;
    }

    public boolean updateProduct(Product product) {

        String sql = "UPDATE Products SET name = ?, sellPrice = ?, purchasePrice = ?, stock = ?, category = ? WHERE id = ?";

        try (Connection conn = SQLiteConnector.getConnection();
             PreparedStatement statement = conn.prepareStatement(sql)) {

            statement.setString(1, product.getName());
            statement.setDouble(2, product.getSellPrice());
            statement.setDouble(3, product.getPurchasePrice());
            statement.setInt(4, product.getStock());
            statement.setString(5, product.getCategory());
            statement.setInt(6, product.getId());
            int rowsUpdated = statement.executeUpdate();

            if (rowsUpdated > 0) {
                return true;
            }
        }
        catch (SQLException e){
            System.err.println("Error updating product: " + e.getMessage());
            e.printStackTrace();
        }
        return false;
    }

    public boolean deleteProduct(int productId) {

        String sql = "DELETE FROM Products WHERE id = ?";

        try (Connection conn = SQLiteConnector.getConnection();
            PreparedStatement statement = conn.prepareStatement(sql)){

            statement.setInt(1, productId);

            int rowsDeleted = statement.executeUpdate();

            if (rowsDeleted > 0) {
                return true;
            }

        }catch (SQLException e){
            System.err.println("Error deleting product: " + e.getMessage());
            e.printStackTrace();
        }
        return false;
    }
    // TODO ΘΑ ΜΠΡΟΥΣΑΜΕ ΝΑ ΚΑΝΟΥΜΕ ΤΟ QUERY ΝΑ ΕΜΦΑΝΙΖΕΙ ΛΙΣΤΑ ΠΡΟΙΟΝΤΩΝ ΠΟΥ ΜΟΙΖΟΥΝ ΜΕ ΤΟ TEXT ΣΤΟ TEXT FIELD! <3
    public Product findByName(String name) {
        String sql = "SELECT * FROM Products WHERE name = ?";

        try (Connection conn = SQLiteConnector.getConnection();
             PreparedStatement statement = conn.prepareStatement(sql)) {

            statement.setString(1, name);
            ResultSet rs = statement.executeQuery();

            if (rs.next()) {
                Product product = new Product();
                product.setId(rs.getInt("id"));
                product.setName(rs.getString("name"));
                product.setSellPrice(rs.getDouble("sellPrice"));
                product.setPurchasePrice(rs.getDouble("purchasePrice"));
                product.setStock(rs.getInt("stock"));
                product.setCategory(rs.getString("category"));


                return product;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<Product> getAllProducts(){
        List<Product> products = new ArrayList<>();
        String sql = "SELECT * FROM Products";

        try (Connection conn = SQLiteConnector.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Product product = new Product();
                product.setId(rs.getInt("id"));
                product.setName(rs.getString("name"));
                product.setSellPrice(rs.getDouble("sellPrice"));
                product.setPurchasePrice(rs.getDouble("purchasePrice"));
                product.setStock(rs.getInt("stock"));
                product.setCategory(rs.getString("category"));
                products.add(product);
            }
        } catch (SQLException e) {
            System.err.println("Error fetching all products: " + e.getMessage());
            e.printStackTrace();

        }
        return products; // Επιστροφή της λίστας (μπορεί να είναι κενή)
    }

    public Product getProductById(int id){
        String sql = "SELECT * FROM Products WHERE id = ?";

        try (Connection conn = SQLiteConnector.getConnection();
             PreparedStatement statement = conn.prepareStatement(sql)) {

            statement.setInt(1, id);
            ResultSet rs = statement.executeQuery();

            if (rs.next()) {
                Product product = new Product();
                product.setId(rs.getInt("id"));
                product.setName(rs.getString("name"));
                product.setSellPrice(rs.getDouble("sellPrice"));
                product.setPurchasePrice(rs.getDouble("purchasePrice"));
                product.setStock(rs.getInt("stock"));
                product.setCategory(rs.getString("category"));
                return product;
            }
        } catch (SQLException e) {
            System.err.println("Error fetching product by id: " + e.getMessage());
            e.printStackTrace();
        }
        return null;
    }

    public boolean updateProductStock(int productId, int newStock) {
        String sql = "UPDATE Products SET stock = ? WHERE id = ?";
        try (Connection conn = SQLiteConnector.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, newStock);
            pstmt.setInt(2, productId);
            int affectedRows = pstmt.executeUpdate();
            return affectedRows > 0;
        } catch (SQLException e) {
            System.err.println("ProductDAO.updateProductStock: Error - " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }


}
