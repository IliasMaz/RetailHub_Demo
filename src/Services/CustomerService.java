package Services;

import DAO.CustomerDAO;
import Entities.Customer;
import java.util.List;


public class CustomerService {

    private final CustomerDAO customerDAO;

    public CustomerService(CustomerDAO customerDAO) {
        if (customerDAO == null) {
            throw new IllegalArgumentException("CustomerDAO cannot be null");
        }
        this.customerDAO = customerDAO;
    }

    public Customer createCustomer(String name, String email, String phone, String gender, int age ) {

        Customer c = new Customer (name, email, phone, gender, age);
        boolean success = this.customerDAO.createCustomer(c);
        if (success) {
            System.out.println("CustomerService: Customer  " + c.getName() + " created with ID: " + c.getId());
            return c;
        } else {
            throw new RuntimeException("Customer creation failed on level DAO.");
        }

    }

    public boolean updateCustomer(Customer updatedCustomer) {
        boolean success = customerDAO.updateCustomer(updatedCustomer);
        if (!success) {
            throw new RuntimeException("Customer update failed on level DAO.");
        }
        return true;
    }

    public boolean deleteCustomer(int customerId) {
        boolean success = customerDAO.deleteCustomer(customerId);
        if (!success) {
            throw new RuntimeException("Customer delete failed on level DAO.");
            }
        return true;
    }

    public Customer findByName(String name) {
        return customerDAO.findByName(name);
    }

    public List<Customer> getAllCustomers(){
        return customerDAO.getAllCustomers();
    }

    public Customer getCustomerByPhone(String phone){
        return customerDAO.getCustomerByPhone(phone);
    }

    public boolean addLoyaltyPoints(int customerId, int points) {
        return customerDAO.addPoints(customerId, points);
    }

    public boolean subtractLoyaltyPoints(int customerId, int points) {
        return customerDAO.subtractPoints(customerId, points);
    }


}
