package model;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;

import java.util.Optional;
/** This class holds ObservableLists and various useful methods.
 These methods are used in almost all other classes for updating tables, lists, products, and parts. */
public class Inventory {

    private static ObservableList<Part> allParts = FXCollections.observableArrayList();
    private static ObservableList<Product> allProducts = FXCollections.observableArrayList();

    /**
     Adds a part to the allParts ObservableList.
     @param newPart The new part to be added.
     */
    public static void addPart(Part newPart) {
        allParts.add(newPart);
    }

    /**
     Adds a product to the allProducts ObservableList.
     @param newProduct The new product to be added.
     */
    public static void addProduct(Product newProduct) {
        allProducts.add(newProduct);
    }

    /**
     Finds a part based on its ID.
     Loops through allParts to find a match.
     @param partId The ID of the part being searched
     @return The part with the ID that was searched
     */
    public static Part lookUpPart(int partId) {
        ObservableList<Part> partList = getAllParts();
        for (Part part : partList) {
            if (partId == part.getId()) {
                return part;
            }
        }
        return null;
    }

    /**
     Finds a product based on its ID.
     Loops through allProducts to find a match.
     @param productId The ID of the part being searched
     @return The product with the ID that was searched
     */
    public static Product lookUpProduct(int productId) {
        ObservableList<Product> prodList = getAllProducts();
        for (Product product : prodList) {
            if (productId == product.getId()) {
                return product;
            }
        }
        return null;
    }

    /**
     Finds parts based on substrings in their names.
     Loops through allParts to find matches and adds them to new list.
     @param partName A substring of the name of the part being searched
     @return The parts that contain the substring that was searched
     */
    public static ObservableList<Part> lookUpPart(String partName) {
        ObservableList<Part> foundParts = FXCollections.observableArrayList();
        ObservableList<Part> partList = getAllParts();
        for (Part part : partList) {
            if (part.getName().toLowerCase().contains(partName.toLowerCase())) {
                foundParts.add(part);
            }
        }
        return foundParts;
    }

    /**
     Finds products based on substrings in their names.
     Loops through allProducts to find matches and adds them to new list.
     @param productName A substring of the name of the product being searched
     @return The products that contain the substring that was searched
     */
    public static ObservableList<Product> lookUpProduct(String productName) {
        ObservableList<Product> foundProducts = FXCollections.observableArrayList();
        ObservableList<Product> productList = getAllProducts();
        for (Product product : productList) {
            if (product.getName().toLowerCase().contains(productName.toLowerCase())) {
                foundProducts.add(product);
            }
        }
        return foundProducts;
    }

    /**
     Replaces part with another.
     This is modifying the part by replacing it with another at its index.
     @param index        the index of the part to be replaced
     @param selectedPart the part to replace the old one
     */
    public static void updatePart(int index, Part selectedPart) {
        allParts.set(index, selectedPart);
    }

    /**
     Replaces product with another.
     This is modifying the product by replacing it with another at its index.
     @param index      the index of the product to be replaced
     @param newProduct the product to replace the old one
     */
    public static void updateProduct(int index, Product newProduct) {
        allProducts.set(index, newProduct);
    }
    /** Checks conditions to see if a part can be deleted.
     Checks if a part is selected and if OK is clicked.
     @param selectedPart The part to see if it can be deleted.
     @return true or false if the criteria are met.
     */
    public static boolean deletePart(Part selectedPart) {
        if (selectedPart != null) {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Delete Part");
            alert.setHeaderText("DELETE?");
            alert.setContentText("This part will be deleted. \nAre you sure you want to delete this part?");
            Optional<ButtonType> result = alert.showAndWait();
            return result.isPresent() && result.get() == ButtonType.OK;
        }
         else {
             return false;
         }
    }
    /** Checks conditions to see if a product can be deleted.
     Checks if a product is selected and if OK is clicked.
     @param selectedProduct The product to see if it can be deleted.
     @return true or false if the criteria are met.
     */
    public static boolean deleteProduct(Product selectedProduct){
        if(selectedProduct != null) {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Delete Part");
            alert.setHeaderText("DELETE?");
            alert.setContentText("This product will be deleted. \nAre you sure you want to delete this product?");
            Optional<ButtonType> result = alert.showAndWait();
            return result.isPresent() && result.get() == ButtonType.OK;
        }
        else {
            return false;
        }
    }
    /** Gets all parts in the list.
     @return Returns all parts as an ObservableList
     */
    public static ObservableList<Part> getAllParts(){
        return allParts;
    }
    /** Gets all products in the list.
     @return Returns all products as an ObservableList
     */
    public static ObservableList<Product> getAllProducts(){
        return allProducts;
    }
}


