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
import java.net.URL;
import java.util.ResourceBundle;
import java.util.Scanner;

public class StudentLoginController implements Initializable {

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
            return;
        }
        else{
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
}