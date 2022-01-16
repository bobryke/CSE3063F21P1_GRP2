package controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import system.RegistrationSystem;

import java.io.IOException;
import java.net.URI;

public class AboutController {
    private Stage stage;
    private Scene scene;
    private Parent root;


    @FXML
    private RegistrationSystem registrationSystem;

    @FXML
    public void listOptions(RegistrationSystem registrationSystem){
        this.registrationSystem = registrationSystem;
    }

    @FXML
    public void goBackToLogin(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("studentLoginView.fxml"));

        root = loader.load();

        StudentLoginController studentLoginController = loader.getController();
        studentLoginController.listOptions(registrationSystem);

        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }
}
