module c482.project.c482project {
    requires javafx.controls;
    requires javafx.fxml;


    opens controller to javafx.fxml;
    exports controller;
    exports model;
    opens model to javafx.fxml;
}