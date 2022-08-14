package controller;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
/** This is the main class. it contains the methods to launch the application.
 <p><b>
 The Javadoc folder can be found inside the C482Project folder.
 </b></p>
 <p><b>
 FUTURE ENHANCEMENT: Currently, products cannot be deleted that have associated parts. A good update would be to <br>
 add this function to parts. Parts should not be able to be deleted if there are still products that use them.
 </b></p>
 <p><b>
 LOGICAL ERROR: Please see the MainScreenController class deleteProduct method for <br>
 a runtime error that I encountered and how it was resolved.
 </b></p>
 */
public class Main extends Application {


    /** This is the start method. It stets the first stage and scene.
     @param stage holds the GUI. */
    @Override
    public void start(Stage stage) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/view/MainScreen.fxml"));
        stage.setTitle("Main Screen");
        stage.setScene(new Scene(root, 1000, 400));
        stage.show();
    }
    /** This is the main method. it launches the application.
     @param args is for arguments in the constructor. */
    public static void main(String[] args) {
        launch();
    }
}