package DAO;

import Connector.SQLiteConnector;
import Entities.Customer;
import Entities.Product;
import org.sqlite.SQLiteException;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CustomerDAO {

    public boolean createCustomer(Customer customer) {

        String sql = "INSERT INTO Customers (name, email, phone, gender, age, loyaltyPoints) VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection conn = SQLiteConnector.getConnection();
             PreparedStatement statement = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)){
            statement.setString(1, customer.getName());
            statement.setString(2, customer.getEmail());
            statement.setString(3, customer.getPhone());
            statement.setString(4, customer.getGender());
            statement.setInt(5, customer.getAge());
            statement.setInt(6, customer.getLoyaltyPoints());

            int rowsInserted = statement.executeUpdate();

            if (rowsInserted > 0) {
                try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        customer.setId(generatedKeys.getInt(1));
                        return true;
                    }
                }
            }

        } catch (SQLException e){
            System.err.println("Error inserting customer: " + e.getMessage());
            e.printStackTrace();
        }

        return false;
    }

    public boolean updateCustomer(Customer customer) {

        String sql = "UPDATE Customers SET name = ?, email = ?, phone = ?, gender = ?, age = ?, loyaltyPoints = ? WHERE id = ?";

        try (Connection conn = SQLiteConnector.getConnection();
             PreparedStatement statement = conn.prepareStatement(sql)) {

            statement.setString(1, customer.getName());
            statement.setString(2, customer.getEmail());
            statement.setString(3, customer.getPhone());
            statement.setString(4, customer.getGender());
            statement.setInt(5, customer.getAge());
            statement.setInt(6, customer.getLoyaltyPoints());
            statement.setInt(7, customer.getId());
            int rowsUpdated = statement.executeUpdate();

            if (rowsUpdated > 0) {
                return true;
            }

        }
        catch (SQLException e){
            System.err.println("Error updating customer: " + e.getMessage());
            e.printStackTrace();
        }
        return false;
    }

    public boolean deleteCustomer(int customerId) {

        String sql = "DELETE FROM Customers WHERE id = ?";

        try (Connection conn = SQLiteConnector.getConnection();
             PreparedStatement statement = conn.prepareStatement(sql)) {

            statement.setInt(1, customerId);
            int rowsDeleted = statement.executeUpdate();

            if (rowsDeleted > 0) {
                return true;
            }


        } catch (SQLException e) {
            System.err.println("Error deleting customer: " + e.getMessage());
            e.printStackTrace();
        }
        return false;
    }

    public Customer getCustomerByPhone(String phone){
        String sql = "SELECT * FROM Customers WHERE phone = ?";

        try (Connection conn = SQLiteConnector.getConnection();
             PreparedStatement statement = conn.prepareStatement(sql)) {

            statement.setString(1, phone);
            ResultSet rs = statement.executeQuery();

            if (rs.next()) {
                Customer customer = new Customer();
                customer.setId(rs.getInt("id"));
                customer.setName(rs.getString("name"));
                customer.setEmail(rs.getString("email"));
                customer.setPhone(rs.getString("phone"));
                customer.setGender(rs.getString("gender"));
                customer.setAge(rs.getInt("age"));
                customer.setLoyaltyPoints(rs.getInt("loyaltyPoints"));

                return customer;
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public Customer findByName(String name) {
        String sql = "SELECT * FROM Customers WHERE name = ?";

        try (Connection conn = SQLiteConnector.getConnection();
            PreparedStatement statement = conn.prepareStatement(sql)) {

            statement.setString(1, name);
            ResultSet rs = statement.executeQuery();

            if (rs.next()) {
                Customer customer = new Customer();
                customer.setId(rs.getInt("id"));
                customer.setName(rs.getString("name"));
                customer.setEmail(rs.getString("email"));
                customer.setPhone(rs.getString("phone"));
                customer.setGender(rs.getString("gender"));
                customer.setAge(rs.getInt("age"));
                customer.setLoyaltyPoints(rs.getInt("loyaltyPoints"));

                return customer;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<Customer> getAllCustomers(){
        List<Customer> customers = new ArrayList<>();
        String sql = "SELECT * FROM Customers";

        try (Connection conn = SQLiteConnector.getConnection();
            PreparedStatement statement = conn.prepareStatement(sql)) {

            ResultSet rs = statement.executeQuery();
            while (rs.next()) {
                Customer customer = new Customer();
                customer.setId(rs.getInt("id"));
                customer.setName(rs.getString("name"));
                customer.setEmail(rs.getString("email"));
                customer.setPhone(rs.getString("phone"));
                customer.setGender(rs.getString("gender"));
                customer.setAge(rs.getInt("age"));
                customer.setLoyaltyPoints(rs.getInt("loyaltyPoints"));
                customers.add(customer);
            }

        } catch (SQLException e) {
            System.err.println("Error fetching all customers: " + e.getMessage());
            e.printStackTrace();
        }
        return customers;
    }

    public List<Customer> advancedSearch(String name){
        List<Customer> customers = new ArrayList<>();
        String sql = "SELECT * FROM Customers WHERE name LIKE ?";

        try (Connection conn = SQLiteConnector.getConnection();
             PreparedStatement statement = conn.prepareStatement(sql)) {

            statement.setString(1,name + "%");

            ResultSet rs = statement.executeQuery();

            while (rs.next()) {

                Customer customer = new Customer();
                customer.setId(rs.getInt("id"));
                customer.setName(rs.getString("name"));
                customer.setEmail(rs.getString("email"));
                customer.setPhone(rs.getString("phone"));
                customer.setGender(rs.getString("gender"));
                customer.setAge(rs.getInt("age"));
                customer.setLoyaltyPoints(rs.getInt("loyaltyPoints"));

                customers.add(customer);

        }

        } catch (SQLException e) {
            System.err.println("Error finding customers by name prefix: " + e.getMessage());
            e.printStackTrace();
        }
        return customers;

    }

    public boolean addPoints(int customerId, int points) {
        String sql = "UPDATE Customers SET loyaltyPoints = loyaltyPoints + ? WHERE id = ?";
        try (Connection conn = SQLiteConnector.getConnection();
             PreparedStatement statement = conn.prepareStatement(sql)) {
            statement.setInt(1, points);
            statement.setInt(2, customerId);
            return statement.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean subtractPoints(int customerId, int points) {
        String sql = "UPDATE Customers SET loyaltyPoints = loyaltyPoints - ? WHERE id = ?";
        try (Connection conn = SQLiteConnector.getConnection();
             PreparedStatement statement = conn.prepareStatement(sql)) {
            statement.setInt(1, points);
            statement.setInt(2, customerId);
            return statement.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public Customer getCustomerById(int id) {
        String sql = "SELECT * FROM Customers WHERE id = ?";

        try (Connection conn = SQLiteConnector.getConnection();
             PreparedStatement statement = conn.prepareStatement(sql)) {

            statement.setInt(1, id);
            ResultSet rs = statement.executeQuery();

            if (rs.next()) {
                Customer customer = new Customer();
                customer.setId(rs.getInt("id"));
                customer.setName(rs.getString("name"));
                customer.setEmail(rs.getString("email"));
                customer.setPhone(rs.getString("phone"));
                customer.setGender(rs.getString("gender"));
                customer.setAge(rs.getInt("age"));
                customer.setLoyaltyPoints(rs.getInt("loyaltyPoints"));

                return customer;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}




