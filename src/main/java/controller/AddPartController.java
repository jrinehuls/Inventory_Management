package controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import model.InHouse;
import model.Inventory;
import model.OutSourced;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
/** This class is for adding new parts.
 Implements the Initializable interface for use of the Initialize method to determine starting state of scene.*/
public class AddPartController implements Initializable {

    public Label machineLabel;
    public ToggleGroup choices;
    public RadioButton inHouseRadio;
    public RadioButton outsourcedRadio;
    public TextField idTextField;
    public TextField nameTextField;
    public TextField invTextField;
    public TextField costTextField;
    public TextField minTextField;
    public TextField maxTextField;
    public TextField machineTextField;
    public Button saveButton;
    public Button cancelButton;
    public Label nameExceptionLabel;
    public Label invExceptionLabel;
    public Label costExceptionLabel;
    public Label maxExceptionLabel;
    public Label minExceptionLabel;
    public Label machineExceptionLabel;

    static int id = Inventory.getAllParts().size() + 1;
    double cost ;
    int stock;
    int min;
    int max;
    int machineID;
    String companyName;
    String partName;

    /** initialize determines the initial state of the scene when it gets called.
     The ID text field is initialized with the next available ID.
     Note: The InHouse radio button is selected, but that is done via the associated FXML resource file.
     @param url points to a uniform resource locator (source: <a href="https://docs.oracle.com/javase/8/docs/api/java/net/URL.html"></a>)
     @param resourceBundle contains locale-specific objects (source: <a href="https://docs.oracle.com/javase/8/docs/api/java/util/ResourceBundle.html"></a>) */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        idTextField.setText(String.valueOf(id));
    }
    /** Changes text label for machine or company based on selected radio button.
     @param actionEvent Calls method when toggle group selection is changed. */
    @FXML
    public void getChoice(ActionEvent actionEvent) {
        if(inHouseRadio.isSelected()){
            machineLabel.setText("Machine ID");
            machineExceptionLabel.setText(null);
        }
        else {
            machineLabel.setText("Company Name");
            machineExceptionLabel.setText(null);
        }
    }
    /** Adds part to allParts ObservableList in the Inventory class.
     Default, yet invalid values are set to the part constructor to prevent any text fields from being null.<br>
     Exceptions are handled and logical checks are performed on each variable to ensure only valid input from user.<br>
     When all data is valid, the part is added, the ID is incremented for the next addition, and the MainScreenController scene is called.
     @param actionEvent Calls method when "Save" button is clicked. */
    @FXML
    public void savePart(ActionEvent actionEvent) throws IOException{

        cost = -1.0;
        stock = -1;
        min = -1;
        max = -1;
        machineID = -1;
        partName = nameTextField.getText();
        companyName = machineTextField.getText();
        idTextField.setText(String.valueOf(id));

        nameExceptionLabel.setText(null);
        costExceptionLabel.setText(null);
        invExceptionLabel.setText(null);
        minExceptionLabel.setText(null);
        maxExceptionLabel.setText(null);
        machineExceptionLabel.setText(null);

        if(partName.isEmpty()){
            nameExceptionLabel.setText("A name must be entered.");
        }
//------------------Try Catch to handle exceptions----------------------------------------------
        try {
            cost = Double.parseDouble(costTextField.getText());
            if(cost < 0) {
                costExceptionLabel.setText("No negative numbers.");
            }
        } catch (NumberFormatException | NullPointerException e) {
            costExceptionLabel.setText("Cost must be a number.");
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
//-------------------------------For InHouse being selected----------------------------------------------
        if(inHouseRadio.isSelected()) {
            try {
                machineID = Integer.parseInt(machineTextField.getText());
                if(machineID < 0) {
                    machineExceptionLabel.setText("No negative numbers.");
                }
            } catch (NumberFormatException e) {
                machineExceptionLabel.setText("Machine ID must be an integer.");
            }
//----------------------------Only adds parts if these conditions are met------------------------------
            if(!partName.isEmpty() && cost >= 0 && min >= 0 && stock >= min && max >= stock && machineID >= 0) {
                InHouse inHousePart = new InHouse(id, partName, cost, stock, min, max, machineID);
                Inventory.addPart(inHousePart);
                id++;
                Parent root = FXMLLoader.load(getClass().getResource("/view/MainScreen.fxml"));
                Stage stage = (Stage) ((Button) actionEvent.getSource()).getScene().getWindow();
                Scene scene = new Scene(root, 1000, 400);
                stage.setTitle("Main Screen");
                stage.setScene(scene);
                stage.show();
            }
        }
//---------------------------For OutSourced being selected----------------------------------------------
        else {
            if (companyName.isEmpty()) {
                machineExceptionLabel.setText("Machine name is required.");
            }
//----------------------------Only adds parts if these conditions are met------------------------------
            if (!partName.isEmpty() && cost >= 0 && min >= 0 && stock >= min && max >= stock && !companyName.isEmpty()) {
                OutSourced outSourcedPart = new OutSourced(id, partName, cost, stock, min, max, companyName);
                Inventory.addPart(outSourcedPart);
                id++;
                Parent root = FXMLLoader.load(getClass().getResource("/view/MainScreen.fxml"));
                Stage stage = (Stage) ((Button) actionEvent.getSource()).getScene().getWindow();
                Scene scene = new Scene(root, 1000, 400);
                stage.setTitle("Main Screen");
                stage.setScene(scene);
                stage.show();
            }
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