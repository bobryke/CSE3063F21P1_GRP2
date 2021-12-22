package controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import system.RegistrationSystem;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class MainMenuController implements Initializable {

    @FXML
    private RegistrationSystem registrationSystem;

    @FXML
    private Label studentName;

    @FXML
    private Label dateontop;

    private Stage stage;
    private Scene scene;
    private Parent root;

    public MainMenuController(){

    }


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        dateontop.setText(String.valueOf(java.time.LocalDate.now()));
        studentName.setText("");
    }

    @FXML
    public void listOptions(RegistrationSystem registrationSystem){
        this.registrationSystem = registrationSystem;
        dateontop.setText(String.valueOf(java.time.LocalDate.now()));
        studentName.setText(registrationSystem.getLoggedStudentName());
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

    @FXML
    public void goStudentTranscript(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("studentTranscriptView.fxml"));

        root = loader.load();

        StudentTranscriptController studentTranscriptController = loader.getController();
        studentTranscriptController.listOptions(registrationSystem);

        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    @FXML
    public void goStudentWeeklySchedule(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("studentWeeklyScheduleView.fxml"));

        root = loader.load();

        StudentWeeklyScheduleController studentWeeklyScheduleController = loader.getController();
        studentWeeklyScheduleController.listOptions(registrationSystem);

        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    @FXML
    public void goStudentCourseSelection(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("studentCourseSelectView.fxml"));

        root = loader.load();

        StudentCourseSelectController studentCourseSelectController = loader.getController();
        studentCourseSelectController.listOptions(registrationSystem);

        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }
}
