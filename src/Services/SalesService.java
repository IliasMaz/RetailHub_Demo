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

//TODO AFTI EINAI GIA TO OUMPI ADD WSTE NA PROSTHETEI SALEITEM
public Sales addItem(Sales currentSale, Product product, int quantity) {
    if (currentSale == null) {
        throw new IllegalArgumentException("Current sale object cannot be null.");
    }
    if (currentSale.getId() <= 0) {
        throw new IllegalStateException("Sale has not been initiated in the database yet (ID missing).");
    }
    if (product == null || product.getId() <= 0) {
        throw new IllegalArgumentException("Product is null or does not have a valid ID.");
    }
    if (quantity <= 0) {
        throw new IllegalArgumentException("Quantity must be positive.");
    }


    int stock = product.getStock();
    int itemCount = currentSale.getItems().size();
    SaleItem newItem = null;

    try {
        currentSale.addItem(product, quantity);

        // Εύρεση του item που μόλις προστέθηκε (υποθέτοντας ότι προστίθεται στο τέλος)
        if (currentSale.getItems().size() > itemCount) {
            newItem = currentSale.getItems().get(currentSale.getItems().size() - 1);
        } else {
            // Δεν θα έπρεπε να συμβεί αν η currentSale.addItem δούλεψε σωστά και δεν πέταξε exception
            throw new RuntimeException("Item was not added to in-memory sale list by currentSale.addItem().");
        }

        int saleItemIdFromDB = saleItemDAO.addSaleItem(newItem, currentSale.getId());

        //rollback
        if (saleItemIdFromDB == -1) {
            product.setStock(stock);
            currentSale.getItems().remove(newItem);
            currentSale.sumTotal();
            System.err.println("SalesService: Failed to save SaleItem to DB. In-memory changes rolled back.");
            throw new RuntimeException("Failed to save SaleItem to database for product " + product.getName());
        }


        if (!productDAO.updateProductStock(product.getId(), product.getStock())) {
            // Rollback: Διαγραφή του SaleItem από τη βάση και επαναφορά in-memory αλλαγών
            saleItemDAO.deleteSaleItem(saleItemIdFromDB);
            product.setStock(stock);
            currentSale.getItems().remove(newItem);
            currentSale.sumTotal();
            System.err.println("SalesService: Failed to update product stock in DB. SaleItem and in-memory changes rolled back.");
            throw new RuntimeException("Failed to update product stock in database for " + product.getName());
        }

        System.out.println("SalesService: Item " + product.getName() + " (SaleItem ID: " + saleItemIdFromDB + ") added to sale ID " + currentSale.getId() + " and DB updated. Current total: " + currentSale.getTotalAmount());
        return currentSale;

    } catch (IllegalStateException | IllegalArgumentException e) { // Από currentSale.addItem ή τους δικούς μας ελέγχους
        System.err.println("SalesService: Could not add item - " + e.getMessage());
        throw e;
    }
}

//TODO SYNDESE AFTIN ME TO REMOVE ITEM
    public boolean removeItem(Sales currentSale, SaleItem itemToRemove) {
        if (currentSale == null) {
            throw new IllegalArgumentException("Current sale object cannot be null.");
        }
        if (itemToRemove == null) {
            throw new IllegalArgumentException("Item to remove cannot be null.");
        }
        if (itemToRemove.getId() <= 0) {
            System.err.println("SalesService: Item to remove ("+ itemToRemove.getName() +") does not have a valid database ID. Cannot remove from DB.");
            return false;
        }

        Product productToRestore = itemToRemove.getProduct();
        int quantityRestored = itemToRemove.getQuantity();
        int orginalStock = productToRestore.getStock();

        int itemIndex = currentSale.getItems().indexOf(itemToRemove);
        if (itemIndex == -1) {
            System.err.println("SalesService: Item " + itemToRemove.getName() + " not found in the current in-memory sale list.");
            return false;
        }

        try {
            currentSale.removeItem(itemToRemove);

            if (!saleItemDAO.deleteSaleItem(itemToRemove.getId())) {
                currentSale.getItems().add(itemIndex, itemToRemove);
                productToRestore.setStock(orginalStock);
                currentSale.sumTotal();
                System.err.println("SalesService: Failed to delete SaleItem from DB. In-memory changes rolled back.");
                throw new RuntimeException("Failed to delete SaleItem " + itemToRemove.getName() + " from database.");
            }


            if (!productDAO.updateProductStock(productToRestore.getId(), productToRestore.getStock())) {
                saleItemDAO.addSaleItem(itemToRemove, currentSale.getId());
                currentSale.getItems().add(itemIndex, itemToRemove);
                productToRestore.setStock(orginalStock);
                currentSale.sumTotal();
                System.err.println("SalesService: Failed to update product stock in DB after item removal. SaleItem re-added to DB and in-memory changes rolled back.");
                throw new RuntimeException("Failed to update product stock in database for " + productToRestore.getName() + " after removal.");
            }

            System.out.println("SalesService: Item " + itemToRemove.getName() + " (SaleItem ID: " + itemToRemove.getId() + ") removed from sale ID " + currentSale.getId() + " and DB updated. Current total: " + currentSale.getTotalAmount());
            return true;

        } catch (Exception e) {
            System.err.println("SalesService: Error during item removal - " + e.getMessage());
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
        if (saleToSave == null || saleToSave.getId() <= 0) {
            throw new IllegalArgumentException("Sale to save is null or not initiated in DB (ID missing).");
        }
        if (saleToSave.getItems().isEmpty()) {
            System.out.println("SalesService: Sale has no items. Finalizing an empty sale. Consider if this is intended.");

        }

        saleToSave.setDate(LocalDate.now());
        saleToSave.setTime(LocalTime.now());
        saleToSave.sumTotal();

        if (salesDAO.updateSaleRecord(saleToSave)) {
            System.out.println("SalesService: Sale ID " + saleToSave.getId() + " finalized and saved successfully.");
            return saleToSave;
        } else {

             System.err.println("SalesService: CRITICAL - Failed to update final sale (ID: " + saleToSave.getId() + ") details in database. " +
                    "Individual items and stock changes WERE ALREADY COMMITTED TO DB. " +
                    "Database may be in a partially inconsistent state regarding this sale's header info.");
            throw new RuntimeException("Failed to save/update the final sale details. Previous item/stock changes persist.");
        }
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
        saleToCancel.setTotalamount(0.0);
        saleToCancel.setCustomer(null);
        saleToCancel.setPaymentMethod(null);
        // saleToCancel.setId(0); // Ίσως θέλεις να μηδενίσεις και το ID για να δείξεις ότι δεν είναι πλέον έγκυρη πώληση
        System.out.println("SalesService: Sale ID " + saleToCancel.getId() + " has been cancelled. In-memory object cleared.");
    }
}

