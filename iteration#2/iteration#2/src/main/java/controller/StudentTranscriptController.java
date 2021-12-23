package controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import system.RegistrationSystem;
import table.TranscriptTable;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class StudentTranscriptController implements Initializable {

    @FXML
    private RegistrationSystem registrationSystem;
    @FXML
    private Label studentNumber;
    @FXML
    private Label studentName;
    @FXML
    private Label completedCredit;
    @FXML
    private Label studentGPA;
    @FXML
    private TableView<TranscriptTable> transcriptTable;
    @FXML
    private TableColumn<TranscriptTable, String> courseCode;
    @FXML
    private TableColumn<TranscriptTable, String> courseName;
    @FXML
    private TableColumn<TranscriptTable, Integer> credit;
    @FXML
    private TableColumn<TranscriptTable, String> finalGrade;

    private ObservableList<TranscriptTable> transcriptList;

    private Scene scene;
    private Parent root;
    private Stage stage;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        studentGPA.setText("");
        studentNumber.setText("");
        studentName.setText("");
        completedCredit.setText("");
        transcriptList = null;
    }

    @FXML
    public void listOptions(RegistrationSystem registrationSystem){
        this.registrationSystem = registrationSystem;
        studentGPA.setText(String.format("%3.2f",registrationSystem.getLoggedStudentGPA()));
        studentNumber.setText(registrationSystem.getLoggedStudentID().toString());
        studentName.setText(registrationSystem.getLoggedStudentName().toString());
        completedCredit.setText(registrationSystem.getLoggedStudentCompletedCredit().toString());

        courseCode.setCellValueFactory(new PropertyValueFactory<TranscriptTable,String>("courseCode"));
        courseName.setCellValueFactory(new PropertyValueFactory<TranscriptTable,String>("courseName"));
        credit.setCellValueFactory(new PropertyValueFactory<TranscriptTable,Integer>("credit"));
        finalGrade.setCellValueFactory(new PropertyValueFactory<TranscriptTable,String>("finalGrade"));

        transcriptList = FXCollections.observableArrayList((
                TranscriptTable.convertToTable(registrationSystem.getLoggedStudentTranscript())));
        transcriptTable.setItems(transcriptList);
    }

    @FXML
    private void goBackToMainMenu(ActionEvent event) throws IOException {
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
