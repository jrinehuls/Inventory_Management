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
import model.*;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
/** This class gets called from the main class.
 From here, other scenes can be called to so that parts and products can be added, modified, or deleted.<br>
 Implements the Initializable interface for use of the Initialize method to determine starting state of scene.*/
public class MainScreenController implements Initializable {


    public Label titleLabel;
    public Label partsLabel;
    public TextField partTextField;
    public TableView<Part> partsTable;
    public TableColumn<Part, Integer> partIdCol;
    public TableColumn<Part, String> partNameCol;
    public TableColumn<Part, Integer> partInvCol;
    public TableColumn<Part, Number> partPriceCol;
    public Button partModButton;
    public Button partAddButton;
    public Button partDelButton;
    public Label productsLabel;
    public TextField productTextField;
    public TableView<Product> prodsTable;
    public TableColumn<Product, Integer> prodIdCol;
    public TableColumn<Product, String> prodNameCol;
    public TableColumn<Product, Integer> prodInvCol;
    public TableColumn<Product, Number> prodPriceCol;
    public Button prodAddButton;
    public Button prodModButton;
    public Button prodDelButton;
    public Label partErrorLabel;
    public Label productErrorLabel;

    private static boolean first = true;

    /** initialize determines the initial state of the scene when it gets called.
     Parts and products are added to ObservableLists in the Inventory class. Columns are defined.<br>
     List items are added to the tables with the getAllParts and getAllProducts methods from the Inventory class.
     @param url points to a uniform resource locator (source: <a href="https://docs.oracle.com/javase/8/docs/api/java/net/URL.html"></a>)
     @param resourceBundle contains locale-specific objects (source: <a href="https://docs.oracle.com/javase/8/docs/api/java/util/ResourceBundle.html"></a>)
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
//----boolean "first" in an if statement so parts and products don't get repeatedly added each time scene is visited.---
        if(first) {
            Inventory.addProduct(new Product(101, "Unicycle", 119.99, 4, 1, 5));
            Inventory.addProduct(new Product(102, "Bicycle", 199.99, 14, 5, 20));
            Inventory.addProduct(new Product(103, "Tricycle", 159.99, 9, 5, 15));
            Inventory.addPart(new InHouse(1, "Large Tire", 22.99, 12, 1, 20, 123));
            Inventory.addPart(new OutSourced(2, "Chain", 16.99, 8, 2, 15, "Chains N'at"));
            Inventory.addPart(new InHouse(3, "Seat", 42.99, 7, 2, 10, 806));
            Inventory.addPart(new OutSourced(4, "Small Tire", 17.99, 28, 10, 35, "Gears and That"));
            Inventory.addPart(new InHouse(5, "Pedal", 6.99, 11, 5, 20, 312));

            first = false;
        }

        prodsTable.setItems(Inventory.getAllProducts());

        prodIdCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        prodNameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        prodInvCol.setCellValueFactory(new PropertyValueFactory<>("stock"));
        prodPriceCol.setCellValueFactory(new PropertyValueFactory<>("price"));

        partsTable.setItems(Inventory.getAllParts());

        partIdCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        partNameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        partInvCol.setCellValueFactory(new PropertyValueFactory<>("stock"));
        partPriceCol.setCellValueFactory(new PropertyValueFactory<>("price"));

    }
    /** This method calls the AddPartController class scene.
     @param actionEvent Calls method when the add button in the Parts pane is clicked. */
    @FXML
    public void addPart(ActionEvent actionEvent) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/view/AddPart.fxml"));
        Stage stage = (Stage)((Button)actionEvent.getSource()).getScene().getWindow();
        Scene scene = new Scene(root, 600, 600);
        stage.setTitle("Add Parts");
        stage.setScene(scene);
        stage.show();
    }
    /** This method responds to the modify button for the Parts pane. The selected part gets further methods performed on it<br>
     in the ModifyPartController class via its receivePart method. The ModifyPart scene gets called.
     @param actionEvent calls method when the button is clicked.
    */
    @FXML
    public void modifyPart(ActionEvent actionEvent) throws IOException {
//----------------------<Handing off the part>---------------------------------------------------
        Part selectedPart = partsTable.getSelectionModel().getSelectedItem();
        ModifyPartController.receivePart(selectedPart);
//----------------------</Handing off the part>---------------------------------------------------
        if(selectedPart != null) {
            Parent root = FXMLLoader.load(getClass().getResource("/view/ModifyPart.fxml"));
            Stage stage = (Stage) ((Button) actionEvent.getSource()).getScene().getWindow();
            Scene scene = new Scene(root, 600, 600);
            stage.setTitle("Modify Parts");
            stage.setScene(scene);
            stage.show();
        }
    }
    /** This method deletes a part from the main screen.
     It checks if the deletePart method of the Inventory class is true for the selected part.<br>
     If so, the part is removed form the ObservableList, the search bar gets cleared and the table is updated.
     @param actionEvent checks for the event that action is taken on the button (click) */
    @FXML
    public void deletePart(ActionEvent actionEvent){
        Part selectedPart = partsTable.getSelectionModel().getSelectedItem();
        if(Inventory.deletePart(selectedPart)){
            Inventory.getAllParts().remove(selectedPart);
            partTextField.setText(null);
            partsTable.setItems(Inventory.getAllParts());
        }
    }
    /** getPartText uses a text field for input. It calls the overloaded method "lookUpPart" from the Inventory class. <br>
     If the text is an integer, it checks part ID and displays that part if a match is found.<br>
     If the text is not an integer, it searches if there is a substring in each part name<br>
     and displays those parts. If no matching parts are found, an error is displayed.
     @param keyEvent Method gets called each time a key is typed in this text field. */
    @FXML
    public void getPartText(KeyEvent keyEvent) {
        partErrorLabel.setText(null);
        String partText = partTextField.getText();
        try {
            int partNum = Integer.parseInt(partText);
            Part part = Inventory.lookUpPart(partNum);
            ObservableList<Part> partList = FXCollections.observableArrayList();
            if(part == null){
                partErrorLabel.setText("Part not found!");
            } else {
                partList.add(part);
            }
            partsTable.setItems(partList);
        } catch (NumberFormatException e) {
            ObservableList<Part> partList = Inventory.lookUpPart(partText);
            partsTable.setItems(partList);
            if(partList.isEmpty()){
                partErrorLabel.setText("Part not found!");
            }
        }
    }
    /** This method calls the AddProduct Controller class scene.
     @param actionEvent Calls method when the add button in the Products pane is clicked. */
    @FXML
    public void addProduct(ActionEvent actionEvent) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/view/AddProduct.fxml"));
        Stage stage = (Stage)((Button)actionEvent.getSource()).getScene().getWindow();
        Scene scene = new Scene(root, 1000, 700);
        stage.setTitle("Add Products");
        stage.setScene(scene);
        stage.show();
    }
    /** This method responds to the modify button for the Products pane. The selected product gets further methods performed on it<br>
     in the ModifyProductController class via its receiveProduct method. The ModifyProduct scene gets called.
     @param actionEvent calls method when the button is clicked.
     */
    @FXML
    public void modifyProduct(ActionEvent actionEvent) throws IOException {
//--------------------------Handing off the product-------------------------------------------------------
        Product selectedProduct = prodsTable.getSelectionModel().getSelectedItem();
        ModifyProductController.receiveProduct(selectedProduct);
        if(selectedProduct != null) {
            Parent root = FXMLLoader.load(getClass().getResource("/view/ModifyProduct.fxml"));
            Stage stage = (Stage) ((Button) actionEvent.getSource()).getScene().getWindow();
            Scene scene = new Scene(root, 1000, 700);
            stage.setTitle("Modify Products");
            stage.setScene(scene);
            stage.show();
        }
    }
    /** This method deletes a product from the main screen.
     It checks if the deleteProduct method of the Inventory class is true for the selected product.<br>
     It checks if the selected product has any parts associated before deleting<br>
     If it has parts associated, it doesn't delete and sets text to a label. If not,<br>
     If not, the product is removed form the ObservableList, the search bar gets cleared and the table is updated.<br>
     The label is cleared the next time the button is clicked.
     <p><b>
     LOGICAL ERROR: If the chosen had associated parts, the confirmation box came up. When OK was clicked, another confirmation box came up.<br>
     OK had to be clicked again for the confirmation to go away and set label text. This is because the first if statement had something like<br>
     if(Inventory.deleteProduct(selectedProduct) && selectedProduct.getAllAssociatedParts().size() == 0) and then there was an else if statement<br>
     else if(Inventory.deleteProduct(selectedProduct) && selectedProduct.getAllAssociatedParts().size() > 0). I charged my laptop and drew logic gates<br>
     in a notebook. I realized it was because the deleteProduct method from the Inventory class was getting called twice, once in each if statement.<br>
     I fixed the issue by nesting if statements as seen in the code.
     </b></p>
     @param actionEvent checks for the event that action is taken on the button (click) */
    @FXML
    public void deleteProduct(ActionEvent actionEvent) {
        productErrorLabel.setText(null);
        Product selectedProduct = prodsTable.getSelectionModel().getSelectedItem();
        if(Inventory.deleteProduct(selectedProduct)) {
            if(selectedProduct.getAllAssociatedParts().size() == 0) {
                Inventory.getAllProducts().remove(selectedProduct);
                productTextField.setText(null);
                prodsTable.setItems(Inventory.getAllProducts());
            }
            else {
                productErrorLabel.setText("This product still has associated parts, so it can't be deleted.");
            }
        }
    }
    /** getProductText uses a text field for input.  It calls the overloaded method "lookUpProduct" from the Inventory class.<br>
     If the text is an integer, it checks product ID and displays that product if a match is found.<br>
     If the text is not an integer, it searches if there is a substring in each product name<br>
     and displays those products. If no matching products are found, an error is displayed.
     @param keyEvent Method gets called each time a key is typed in this text field. */
    @FXML
    public void getProductText(KeyEvent keyEvent) {
        productErrorLabel.setText(null);
        String productText = productTextField.getText();
        try {
            int prodNum = Integer.parseInt(productText);
            Product product = Inventory.lookUpProduct(prodNum);
            ObservableList<Product> prodList = FXCollections.observableArrayList();
            if(product == null){
                productErrorLabel.setText("Product not found!");
            } else {
                prodList.add(product);
            }
            prodsTable.setItems(prodList);
        } catch (NumberFormatException e) {
            ObservableList<Product> prodList = Inventory.lookUpProduct(productText);
            prodsTable.setItems(prodList);
            if(prodList.isEmpty()){
                productErrorLabel.setText("Product not found!");
            }
        }
    }
    /** The GUI closes and the program ends when the exit button is clicked.
     @param actionEvent Calls method when button is clicked. */
    @FXML
    protected void onExit(ActionEvent actionEvent) {
        Stage stage = (Stage)((Button)actionEvent.getSource()).getScene().getWindow();
        stage.close();
    }

}