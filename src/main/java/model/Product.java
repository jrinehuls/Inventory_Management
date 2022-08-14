package model;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;

import java.util.Optional;
/** The product class handles product related methods and objects. */
public class Product {

    private ObservableList<Part> associatedParts = FXCollections.observableArrayList();

    private int id;
    private String name;
    private double price;
    private int stock;
    private int min;
    private int max;
    /** Constructor for building a product.
     * @param id The ID of the product
     * @param name The name of the product
     * @param price The price of the product
     * @param stock The amount of the product on hand
     * @param min The minimum amount of products in stock
     * @param max The maximum amount of products in stock
     * */
    public Product (int id, String name, double price, int stock, int min, int max) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.stock = stock;
        this.min = min;
        this.max = max;
    }
    /** Sets ID of the product
     * @param id The ID of the product */
    public void setId(int id) {
        this.id = id;
    }
    /** Sets name of the product
     * @param name The name of the product */
    public void setName(String name) {
        this.name = name;
    }
    /** Sets price of the product
     * @param price The price of the product */
    public void setPrice(double price) {
        this.price = price;
    }
    /** Sets stock of the product
     * @param stock The stock of the product */
    public void setStock(int stock) {
        this.stock = stock;
    }
    /** Sets min of the product
     * @param min The min of the product */
    public void setMin(int min) {
        this.min = min;
    }
    /** Sets max of the product
     * @param max The max of the product */
    public void setMax(int max) {
        this.max = max;
    }
    /** Gets ID of the product
     * @return The ID of the product */
    public int getId() {
        return id;
    }
    /** Gets name of the product
     * @return The name of the product */
    public String getName() {
        return name;
    }
    /** Gets price of the product
     * @return The price of the product */
    public double getPrice() {
        return price;
    }
    /** Gets stock of the product
     * @return The stock of the product */
    public int getStock() {
        return stock;
    }
    /** Gets min of the product
     * @return The min of the product */
    public int getMin() {
        return min;
    }
    /** Gets max of the product
     * @return The max of the product */
    public int getMax() {
        return max;
    }
    /** Adds a part to the associatedParts list.
     * @param part The part to be added to the list */
    public void addAssociatedPart(Part part){
        associatedParts.add(part);
    }
    /** Checks conditions to see if a part can be deleted.
     Checks if a part is selected and if OK is clicked.
     @param selectedAssociatedPart The part to see if it can be deleted.
     @return true or false if the criteria are met. */
    public boolean deleteAssociatedPart(Part selectedAssociatedPart){

        if(selectedAssociatedPart != null) {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Delete Part");
            alert.setHeaderText("DELETE?");
            alert.setContentText("Are you sure you want to delete this part?");
            Optional<ButtonType> result = alert.showAndWait();
            if (result.get() == ButtonType.OK) {
                associatedParts.remove(selectedAssociatedPart);
                return true;
            }
            else {
                return false;
            }
        }
        else{
            return false;
        }
    }
    /** Returns the list of associated parts
     * @return Returns the ObservableList associatedParts */
    public ObservableList<Part> getAllAssociatedParts() {
        return associatedParts;
    }

}

