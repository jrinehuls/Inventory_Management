package controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;
import model.Inventory;
import model.Part;
import model.Product;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
/** This class is for modifying existing products.
 Implements the Initializable interface for use of the Initialize method to determine starting state of scene.*/
public class ModifyProductController implements Initializable{

    public TableView<Part> partTable;
    public TableColumn<Part, Integer> partPartId;
    public TableColumn<Part, String> partPartName;
    public TableColumn<Part, Integer> partInv;
    public TableColumn<Part, Number> partCost;
    public TableView<Part> assocPartTable;
    public TableColumn<Part, Integer> assocPartId;
    public TableColumn<Part, String> assocPartName;
    public TableColumn<Part, Integer> assocInv;
    public TableColumn<Part, Number> assocCost;
    public TextField searchTextField;
    public Button addButton;
    public Button removeButton;
    public Button saveButton;
    public Button cancelButton;
    public TextField idTextField;
    public TextField nameTextField;
    public TextField invTextField;
    public TextField priceTextField;
    public TextField maxTextField;
    public TextField minTextField;
    public Label nameExceptionLabel;
    public Label invExceptionLabel;
    public Label priceExceptionLabel;
    public Label maxExceptionLabel;
    public Label minExceptionLabel;
    public Label partErrorLabel;

    String name;
    int stock;
    double price;
    int min;
    int max;
    int id;
    int index;

    private static Product modifiableProduct = null;
    /** Setter method for the product being sent from the MainScreenController
     @param product The product being handed over from the MainScreenController class */
    public static void receiveProduct(Product product){
        modifiableProduct = product;
    }
    /** initialize determines the initial state of the scene when it gets called.
     Text fields are loaded with data from the selected product.<br>
     Tables are defined and loaded with the parts from the ObservableLists of the Inventory class.
     @param url points to a uniform resource locator (source: <a href="https://docs.oracle.com/javase/8/docs/api/java/net/URL.html"></a>)
     @param resourceBundle contains locale-specific objects (source: <a href="https://docs.oracle.com/javase/8/docs/api/java/util/ResourceBundle.html"></a>) */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        idTextField.setText(String.valueOf(modifiableProduct.getId()));
        nameTextField.setText(modifiableProduct.getName());
        invTextField.setText(String.valueOf(modifiableProduct.getStock()));
        priceTextField.setText(String.valueOf(modifiableProduct.getPrice()));
        minTextField.setText(String.valueOf(modifiableProduct.getMin()));
        maxTextField.setText(String.valueOf(modifiableProduct.getMax()));

        partTable.setItems(Inventory.getAllParts());

        partPartId.setCellValueFactory(new PropertyValueFactory<>("id"));
        partPartName.setCellValueFactory(new PropertyValueFactory<>("name"));
        partInv.setCellValueFactory(new PropertyValueFactory<>("stock"));
        partCost.setCellValueFactory(new PropertyValueFactory<>("price"));

        assocPartTable.setItems(modifiableProduct.getAllAssociatedParts());

        assocPartId.setCellValueFactory(new PropertyValueFactory<>("id"));
        assocPartName.setCellValueFactory(new PropertyValueFactory<>("name"));
        assocInv.setCellValueFactory(new PropertyValueFactory<>("stock"));
        assocCost.setCellValueFactory(new PropertyValueFactory<>("price"));

    }
    /** searchPart uses a text field for input. It calls the overloaded method "lookUpPart" from the Inventory class.<br>
     If the text is an integer, it checks product ID and displays that part if a match is found. <br>
     If the text is not an integer, it searches if there is a substring in each part name and displays those parts.<br>
     If no matching products are found, an error is displayed.
     @param keyEvent Method gets called each time a key is typed in this text field. */
    @FXML
    public void searchPart(KeyEvent keyEvent) {
        partErrorLabel.setText(null);
        String partText = searchTextField.getText();
        try {
            int partNum = Integer.parseInt(partText);
            Part part = Inventory.lookUpPart(partNum);
            ObservableList<Part> partList = FXCollections.observableArrayList();
            if(part == null){
                partErrorLabel.setText("Part not found!");
            } else {
                partList.add(part);
            }
            partTable.setItems(partList);
        } catch (NumberFormatException e) {
            ObservableList<Part> partList = Inventory.lookUpPart(partText);
            partTable.setItems(partList);
            if(partList.isEmpty()){
                partErrorLabel.setText("Part not found!");
            }
        }
    }
    /** This method adds a part to the Associated Parts table.
     Loops through already associated parts to check if the part ID already exists in the table for the part the user<br>
     intends to add. If it is there, it cannot be added. If it is not, it is added to this product's ObservableList.<br>
     The table is updated with all parts in the ObservableList.
     @param actionEvent Calls method when the add button is pressed.*/
    @FXML
    public void addPart(ActionEvent actionEvent) {
        Part part = partTable.getSelectionModel().getSelectedItem();
        for(Part parts : modifiableProduct.getAllAssociatedParts()){
            if(part.getId() == parts.getId()){
                return;
            }
        }
        modifiableProduct.addAssociatedPart(part);
        assocPartTable.setItems(modifiableProduct.getAllAssociatedParts());
    }
    /** Removes a part from the Associated Parts table.
     A selected part is deleted if conditions are met from the Product class's deleteAssociatePart method<br>
     assocPartTable is updated with parts in the ObservableList associateParts for this Product object.
     @param actionEvent Calls method when the remove associated part button is pressed.*/
    @FXML
    public void removePart(ActionEvent actionEvent) {
        Part part = assocPartTable.getSelectionModel().getSelectedItem();
        if(modifiableProduct.deleteAssociatedPart(part)){
            assocPartTable.setItems(modifiableProduct.getAllAssociatedParts());
        }

    }
    /** Modifies product in allProducts ObservableList in the Inventory class.
     Default, yet invalid values are set to the product constructor to prevent any text fields from being null.<br>
     Exceptions are handled and logical checks are performed on each variable to ensure only valid input from user.<br>
     When all data is valid, the product replaces the product at the index of the original product using the updateProduct<br>
     method of the Inventory class. Then, the MainScreenController scene is called.
     @param actionEvent Calls method when "Save" button is clicked. */
    @FXML
    public void updateProduct(ActionEvent actionEvent) throws IOException{

        id = Integer.parseInt(idTextField.getText());
        price = -1.0;
        stock = -1;
        min = -1;
        max = -1;
        name = nameTextField.getText();
        index = Inventory.getAllProducts().indexOf(modifiableProduct);

        nameExceptionLabel.setText(null);
        priceExceptionLabel.setText(null);
        invExceptionLabel.setText(null);
        minExceptionLabel.setText(null);
        maxExceptionLabel.setText(null);

        if(name.isEmpty()){
            nameExceptionLabel.setText("A name must be entered.");
        }
//------------------Try Catch to handle exceptions----------------------------------------------
        try {
            price = Double.parseDouble(priceTextField.getText());
            if(price < 0) {
                priceExceptionLabel.setText("No negative numbers.");
            }
        } catch (NumberFormatException | NullPointerException e) {
            priceExceptionLabel.setText("Cost must be a number.");
        }
        try {
            min = Integer.parseInt(minTextField.getText());
            if(min < 0) {
                minExceptionLabel.setText("No negative numbers.");
            }
        } catch (NumberFormatException e) {
            minExceptionLabel.setText("Min must be an integer.");
        }
        try {
            max = Integer.parseInt(maxTextField.getText());
            if(max < 0) {
                maxExceptionLabel.setText("No negative numbers.");
            }
            else if(max <= min) {
                maxExceptionLabel.setText("Max must exceed min.");
            }
        } catch (NumberFormatException e) {
            maxExceptionLabel.setText("Max must be an integer.");
        }
        try {
            stock = Integer.parseInt(invTextField.getText());
            if(stock < 0) {
                invExceptionLabel.setText("No negative numbers.");
            }
            else if(stock > max) {
                invExceptionLabel.setText("Inventory must not exceed max.");
            }
            else if(stock < min) {
                invExceptionLabel.setText("Inventory must equal or exceed min.");
            }
        } catch (NumberFormatException e) {
            invExceptionLabel.setText("Inventory must be an integer.");
        }
//----------------------------Back to home---------------------------------------------------------
        if(!name.isEmpty() && price >= 0 && min >= 0 && stock >= min && max >= stock) {
            modifiableProduct.setId(id);
            modifiableProduct.setName(name);
            modifiableProduct.setPrice(price);
            modifiableProduct.setStock(stock);
            modifiableProduct.setMin(min);
            modifiableProduct.setMax(max);
            Inventory.updateProduct(index, modifiableProduct);
            Parent root = FXMLLoader.load(getClass().getResource("/view/MainScreen.fxml"));
            Stage stage = (Stage) ((Button) actionEvent.getSource()).getScene().getWindow();
            Scene scene = new Scene(root, 1000, 400);
            stage.setTitle("Main Screen");
            stage.setScene(scene);
            stage.show();
        }



    }
    /** Goes back to MainScreenController scene.
     @param actionEvent Calls method when cancel button is clicked. */
    @FXML
    public void toMain(ActionEvent actionEvent) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/view/MainScreen.fxml"));
        Stage stage = (Stage)((Button)actionEvent.getSource()).getScene().getWindow();
        Scene scene = new Scene(root, 1000, 400);
        stage.setTitle("Main Screen");
        stage.setScene(scene);
        stage.show();
    }

}
