package Services;


import DAO.ProductDAO;
import DAO.SaleItemDAO;
import DAO.SalesDAO;
import Entities.Customer;
import Entities.Product;
import Entities.SaleItem;
import Entities.Sales;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

public class SalesService {

    private final SalesDAO salesDAO;
    private final SaleItemDAO saleItemDAO;
    private final ProductDAO productDAO;

    public SalesService(SalesDAO salesDAO, SaleItemDAO saleItemDAO, ProductDAO productDAO) {
        if (salesDAO == null || saleItemDAO == null || productDAO == null) {
            throw new IllegalArgumentException("DAOs cannot be null");
        }
        this.salesDAO = salesDAO;
        this.saleItemDAO = saleItemDAO;
        this.productDAO = productDAO;
    }

//TODO SYNDESTE AFTIN ME TO TO KOUMPI "CREATE" STO SALES MENU GIA NA ARXIKOPOIEI MIA KENI SALE

    public Sales initiateNewSale() {
        Sales newSale = new Sales();
        boolean success = salesDAO.initiateSale(newSale);
        if (success) {
            System.out.println("SalesService: Sale initiated with ID: " + newSale.getId() + " (no customer initially)");
            return newSale;
        } else {
            throw new RuntimeException("Failed to initiate new sale in the database.");
        }
    }

//TODO AFTI EINAI GIA TO KOUMPI ADD WSTE NA PROSTHETEI SALEITEM
public Sales addItem(Sales currentSale, Product product, int quantity) {
    if (currentSale == null) {
        throw new IllegalArgumentException("Current sale object cannot be null.");
    }
    if (product == null || product.getId() <= 0) {
        throw new IllegalArgumentException("Product is null or does not have a valid ID.");
    }
    if (quantity <= 0) {
        throw new IllegalArgumentException("Quantity must be positive.");
    }

    if (currentSale.getId() <= 0) {
        currentSale.addItem(product, quantity);
        return currentSale;
    }

    return currentSale;
}

//TODO SYNDESE AFTIN ME TO REMOVE ITEM
public boolean removeItem(Sales currentSale, SaleItem itemToRemove) {
    if (currentSale == null) {
        throw new IllegalArgumentException("Current sale object cannot be null.");
    }
    if (itemToRemove == null) {
        throw new IllegalArgumentException("Item to remove cannot be null.");
    }

    if (currentSale.getId() <= 0) {
        boolean removed = currentSale.getItems().remove(itemToRemove);
        if (removed) {
            Product p = itemToRemove.getProduct();
            int qnty = itemToRemove.getQuantity();
            if (p != null) p.increaseStock(qnty);
            currentSale.sumTotal();
        }
        return removed;
    }

    if (itemToRemove.getId() <= 0) {
        System.err.println("Item to remove does not have a valid database ID.");
        return false;
    }
    Product productToRestore = itemToRemove.getProduct();
    int orginalStock = productToRestore.getStock();
    int itemIndex = currentSale.getItems().indexOf(itemToRemove);
    if (itemIndex == -1) return false;
    try {
        currentSale.removeItem(itemToRemove);
        if (!saleItemDAO.deleteSaleItem(itemToRemove.getId())) {
            currentSale.getItems().add(itemIndex, itemToRemove);
            productToRestore.setStock(orginalStock);
            currentSale.sumTotal();
            throw new RuntimeException("Failed to delete SaleItem from DB.");
        }

        return true;
    } catch (Exception e) {

        if (!currentSale.getItems().contains(itemToRemove)) {
            currentSale.getItems().add(itemIndex != -1 ? itemIndex : 0, itemToRemove);
            productToRestore.setStock(orginalStock);
            currentSale.sumTotal();
        }
        throw e;
    }
}


    //TODO SYNDESE AFTIN ME TO SAVE
    public Sales finalizeAndSaveSale(Sales saleToSave) {
        if (saleToSave == null) {
            throw new IllegalArgumentException("Sale to save is null.");
        }

        if (saleToSave.getId() <= 0) {
            boolean success = salesDAO.initiateSale(saleToSave);
            if (!success) throw new RuntimeException("Could not create sale in DB!");
        }

        for (SaleItem item : saleToSave.getItems()) {
            if (item.getId() <= 0) {
                int dbId = saleItemDAO.addSaleItem(item, saleToSave.getId());
                if (dbId <= 0)
                    throw new RuntimeException("Could not add sale item to DB!");
                item.setId(dbId);

            }
            Product prod = item.getProduct();
            prod.decreaseStock(item.getQuantity());
            productDAO.updateProductStock(prod.getId(), prod.getStock());
        }
        salesDAO.updateSale(saleToSave);

        return saleToSave;
    }


    //todo afto syndese to me to cancel gia na mporei na epistrefei ta stock stn basi prin kaneis dispose
    public void cancelSale(Sales saleToCancel) {
        if (saleToCancel == null || saleToCancel.getId() <= 0) {
            System.out.println("SalesService: Sale to cancel is null or not initiated. No action taken.");
            return;
        }

        System.out.println("SalesService: Attempting to cancel sale ID: " + saleToCancel.getId());

        List<SaleItem> itemsFromDB = saleItemDAO.getSaleItemsBySaleId(saleToCancel.getId());

        for (SaleItem dbItem : itemsFromDB) {
            Product productInDbItem = dbItem.getProduct();
            int quantityInDbItem = dbItem.getQuantity();


            int stockToRestoreInDB = productInDbItem.getStock() + quantityInDbItem;

            if (!productDAO.updateProductStock(productInDbItem.getId(), stockToRestoreInDB)) {
                System.err.println("SalesService (Cancel Sale): Failed to restore stock for product " +
                        productInDbItem.getName() + " (ID: " + productInDbItem.getId() + ") in DB. Continuing cancellation process.");

            } else {
                System.out.println("SalesService (Cancel Sale): Stock for product " + productInDbItem.getName() +
                        " (ID: " + productInDbItem.getId() + ") restored in DB to " + stockToRestoreInDB);
            }
        }

        if (!saleItemDAO.deleteSaleItemsBySaleId(saleToCancel.getId())) {
            System.err.println("SalesService (Cancel Sale): Failed to delete all SaleItems for sale ID " +
                    saleToCancel.getId() + " from database. Continuing cancellation process.");
        } else {
            System.out.println("SalesService (Cancel Sale): All SaleItems for sale ID " + saleToCancel.getId() + " deleted from database.");
        }

        if (!salesDAO.deleteSale(saleToCancel.getId())) {
            System.err.println("SalesService (Cancel Sale): Failed to delete Sale record ID " +
                    saleToCancel.getId() + " from database.");
        } else {
            System.out.println("SalesService (Cancel Sale): Sale record ID " + saleToCancel.getId() + " deleted from database.");
        }

        saleToCancel.getItems().clear();
        saleToCancel.setTotalAmount(0.0);
        saleToCancel.setCustomer(null);
        saleToCancel.setPaymentMethod(null);
        // saleToCancel.setId(0); // Ίσως θέλεις να μηδενίσεις και το ID για να δείξεις ότι δεν είναι πλέον έγκυρη πώληση
        System.out.println("SalesService: Sale ID " + saleToCancel.getId() + " has been cancelled. In-memory object cleared.");
    }


    public List<Sales> findSalesByCustomer(int customerId) {
        if (customerId <= 0) {
            System.err.println("SalesService: Invalid Customer ID provided (" + customerId + ").");
            return new ArrayList<>();
        }
        List<Sales> sales = salesDAO.getSalesByCustomerId(customerId);
        if (sales.isEmpty()) {
            System.out.println("SalesService: No sales found for customer ID: " + customerId);
        }
        return sales;
    }

    public Sales getSaleById(int saleId) {
        if (saleId <= 0) {
            System.err.println("SalesService: Invalid Sale ID provided (" + saleId + ").");
            return null;
        }
        Sales sale = salesDAO.getSaleById(saleId);
        if (sale == null) {
            System.out.println("SalesService: No sale found with ID: " + saleId);
        }
        return sale;
    }

    public List<Sales> getAllSales() {
        List<Sales> sales = salesDAO.getAllSales();
        if (sales.isEmpty()) {
            System.out.println("SalesService: No sales found in the database.");
        }
        return sales;
    }

    public boolean deleteSale(int saleId) {
        if (saleId <= 0) {
            System.err.println("SalesService: Invalid Sale ID for deletion (" + saleId + ").");
            return false;
        }

        // Προαιρετικά: Αν θέλεις να επαναφέρεις το stock, πρέπει να φορτώσεις την πώληση
        // και τα είδη της ΠΡΙΝ τη διαγράψεις.
        /*
        Sales saleToDelete = salesDAO.getSaleById(saleId);
        if (saleToDelete != null && saleToDelete.getItems() != null) {
            for (SaleItem item : saleToDelete.getItems()) {
                if (item.getProduct() != null) {
                    // Εδώ θα καλούσες μια μέθοδο του ProductDAO για αύξηση του αποθέματος στη ΒΑΣΗ
                    // productDAO.increaseStockInDB(item.getProduct().getId(), item.getQuantity());
                    // Αυτό απαιτεί η productDAO.increaseStockInDB να είναι μέρος μιας transaction
                    // ή να εκτελεστεί πριν το salesDAO.deleteSale.
                    System.out.println("SalesService (Before Deletion): Stock for product " + item.getProduct().getName() + " would be restored by " + item.getQuantity());
                }
            }
        } else if (saleToDelete == null) {
             System.err.println("SalesService: Sale with ID " + saleId + " not found for deletion or stock restoration.");
            return false; // Η πώληση δεν βρέθηκε καν
        }
        */

        boolean success = salesDAO.deleteSale(saleId); // Υποθέτοντας ότι έχεις μια deleteSale(int id) στο SalesDAO
        if (success) {
            System.out.println("SalesService: Sale with ID " + saleId + " deleted successfully.");
            return true;
        } else {
            throw new RuntimeException("Sale deletion failed at DAO level for ID: " + saleId);
        }
    }

    public List<Sales> findSalesByCustomerName(String customerName) {
        return salesDAO.getSalesByCustomerName(customerName);
    }







}

