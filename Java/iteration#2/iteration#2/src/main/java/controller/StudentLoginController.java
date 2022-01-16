package controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import model.Student;
import system.RegistrationSystem;

import java.io.IOException;
import java.net.URI;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.Scanner;
import java.util.logging.Logger;


public class StudentLoginController implements Initializable {
    private static final Logger logger = Logger.getLogger("");

    @FXML
    private RegistrationSystem registrationSystem;

    private Stage stage;
    private Scene scene;
    private Parent root;

    @FXML
    private TextField studentLogin;
    @FXML
    private Label loginFailedWarning;


    public StudentLoginController(){

    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle){
        loginFailedWarning.setText("");
    }


    @FXML
    public void listOptions(RegistrationSystem registrationSystem){
        this.registrationSystem = registrationSystem;
    }


    @FXML
    private void goToMainMenu(ActionEvent event) throws IOException {

        String id;
        id = studentLogin.getText();
        Student loggedStudent = registrationSystem.getStudentByID(id);
        if (loggedStudent == null) {
            loginFailedWarning.setText("Login failed. Please enter your school ID correctly.");
            studentLogin.clear();
            logger.warning("Login failed. Provided student number is: "+ id);
            return;
        }
        else{
            logger.info("Successful login. Provided student number is: "+ id);
            loginFailedWarning.setText("");
            registrationSystem.loginStudent(loggedStudent);
        }

        FXMLLoader loader = new FXMLLoader(getClass().getResource("mainMenuView.fxml"));

        root = loader.load();

        MainMenuController mainMenuController = loader.getController();
        mainMenuController.listOptions(registrationSystem);

        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    @FXML
    private void goToAboutPage(ActionEvent event) throws IOException {
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