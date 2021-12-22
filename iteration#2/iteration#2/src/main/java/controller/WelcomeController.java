package controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import system.RegistrationSystem;

import java.io.IOException;
import java.net.URI;
import java.net.URL;
import java.util.ResourceBundle;

public class WelcomeController implements Initializable {

    @FXML
    private RegistrationSystem registrationSystem;

    private Stage stage;
    private Scene scene;
    private Parent root;

    public WelcomeController(){
        this.registrationSystem = null;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }

    @FXML
    public void fallButton(ActionEvent event) throws IOException {
        registrationSystem = new RegistrationSystem("Fall");
        goToLoginPage(event);
    }

    @FXML
    public void springButton(ActionEvent event) throws IOException {
        registrationSystem = new RegistrationSystem("Spring");
        goToLoginPage(event);
    }

    @FXML
    public void goToLoginPage(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("studentLoginView.fxml"));

        root = loader.load();

        StudentLoginController studentLoginController = loader.getController();
        studentLoginController.listOptions(registrationSystem);

        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    @FXML
    private void goToAboutPage(ActionEvent event) throws IOException {
        registrationSystem = new RegistrationSystem("Fall");

        FXMLLoader loader = new FXMLLoader(getClass().getResource("aboutProjectView.fxml"));

        root = loader.load();

        AboutController aboutController = loader.getController();
        aboutController.listOptions(registrationSystem);

        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    @FXML
    public void goToWebsite(){
        try{
            URI uri = new URI("https://github.com/berkaymengunogul/CSE3063F21P1_GRP2.git");
            java.awt.Desktop.getDesktop().browse(uri);
        }catch(Exception e){
            System.out.println("[INFO]:Couldn't redirect to the website...");
        }
    }
}
