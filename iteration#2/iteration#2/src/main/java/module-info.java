module CSE3063F21P1_GRP2.CSE3063F21P1_GRP2 {
    requires javafx.controls;
    requires javafx.fxml;
    requires com.google.gson;
    requires java.desktop;


    opens controller to javafx.fxml, com.google.gson;
    opens dto to javafx.fxml, com.google.gson;
    opens model to javafx.fxml, com.google.gson;
    opens system to javafx.fxml, com.google.gson;
    opens table to javafx.fxml, com.google.gson;

    exports table;
    exports model;
    exports system;
    exports controller;
    exports dto;
}