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
import model.Part;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
/** This class is for modifying existing parts.
 Implements the Initializable interface for use of the Initialize method to determine starting state of scene.*/
public class ModifyPartController implements Initializable {

    public TextField idTextField;
    public TextField nameTextField;
    public TextField invTextField;
    public TextField costTextField;
    public TextField minTextField;
    public TextField maxTextField;
    public TextField machineTextField;
    public ToggleGroup buttonGroup;
    public RadioButton inHouseRadio;
    public RadioButton outSourcedRadio;
    public Label machineLabel;
    public Label invExceptionLabel;
    public Label costExceptionLabel;
    public Label maxExceptionLabel;
    public Label minExceptionLabel;
    public Label machineExceptionLabel;
    public Label nameExceptionLabel;
    public Button saveButton;

    private static Part modifiablePart = null;

    int id;
    double cost;
    int stock;
    int min;
    int max;
    int machineID;
    String companyName;
    String partName;
    int index;

    /** Setter method for the part being sent from the MainScreenController
     @param part The part being handed over from the MainScreenController class */
    public static void receivePart(Part part){
        modifiablePart = part;
    }


    /** initialize determines the initial state of the scene when it gets called.
     Text fields are loaded with data from the selected part.<br>
     Toggle group radio button is selected based on child class of Part class.
     @param url points to a uniform resource locator (source: <a href="https://docs.oracle.com/javase/8/docs/api/java/net/URL.html"></a>)
     @param resourceBundle contains locale-specific objects (source: <a href="https://docs.oracle.com/javase/8/docs/api/java/util/ResourceBundle.html"></a>) */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        idTextField.setText(String.valueOf(modifiablePart.getId()));
        nameTextField.setText(modifiablePart.getName());
        invTextField.setText(String.valueOf(modifiablePart.getStock()));
        costTextField.setText(String.valueOf(modifiablePart.getPrice()));
        minTextField.setText(String.valueOf(modifiablePart.getMin()));
        maxTextField.setText(String.valueOf(modifiablePart.getMax()));

        if(modifiablePart.getClass() == InHouse.class){
            InHouse part = (InHouse)modifiablePart;
            buttonGroup.selectToggle(inHouseRadio);
            machineLabel.setText("Machine ID");
            machineTextField.setText(String.valueOf(part.getMachineID()));
        }
        if(modifiablePart.getClass() == OutSourced.class){
            OutSourced part = (OutSourced)modifiablePart;
            buttonGroup.selectToggle(outSourcedRadio);
            machineLabel.setText("Company Name");
            machineTextField.setText(String.valueOf(part.getCompanyName()));
        }

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
    /** Modifies part in allParts ObservableList in the Inventory class.
     Default, yet invalid values are set to the part constructor to prevent any text fields from being null.<br>
     Exceptions are handled and logical checks are performed on each variable to ensure only valid input from user.<br>
     When all data is valid, the part replaces the part at the index of the original part using the updatePart<br>
     method of the Inventory class. Then, the MainScreenController scene is called.
     @param actionEvent Calls method when "Save" button is clicked. */
    @FXML
    public void modifyPart(ActionEvent actionEvent) throws IOException {

        id = Integer.parseInt(idTextField.getText());
        cost = -1.0;
        stock = -1;
        min = -1;
        max = -1;
        machineID = -1;
        partName = nameTextField.getText();
        companyName = machineTextField.getText();
        index = Inventory.getAllParts().indexOf(modifiablePart);

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
//---------------------------For InHouse being selected----------------------------------------------
        if(inHouseRadio.isSelected()) {
            try {
                machineID = Integer.parseInt(machineTextField.getText());
                if(machineID < 0) {
                    machineExceptionLabel.setText("No negative numbers.");
                }
            } catch (NumberFormatException e) {
                machineExceptionLabel.setText("Machine ID must be an integer.");
            }

//----------------------------Only modifies if these conditions are met------------------------------
            if(!partName.isEmpty() && cost >= 0 && min >= 0 && stock >= min && max >= stock && machineID >= 0) {
                InHouse part = new InHouse(id, partName, cost, stock, min, max, machineID);
                Inventory.updatePart(index, part);
//----------------------------------Go back to Main Screen------------------------------------------------
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
//----------------------------Only modify parts if these conditions are met------------------------------
            if (!partName.isEmpty() && cost >= 0 && min >= 0 && stock >= min && max >= stock && !companyName.isEmpty()) {
                OutSourced part = new OutSourced(id, partName, cost, stock, min, max, companyName);
                Inventory.updatePart(index, part);
//----------------------------------Go back to Main Screen------------------------------------------------
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