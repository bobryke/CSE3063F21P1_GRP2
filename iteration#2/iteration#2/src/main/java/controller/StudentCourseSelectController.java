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
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import system.RegistrationSystem;
import table.SelectCourseTable;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

import static javafx.scene.control.Alert.AlertType.INFORMATION;

public class StudentCourseSelectController implements Initializable {
    @FXML
    private RegistrationSystem registrationSystem;

    private Scene scene;
    private Parent root;
    private Stage stage;

    @FXML
    private Label studentNumber;

    @FXML
    private Label advisorName;

    @FXML
    private Label completedCredits;

    @FXML
    private TableColumn<SelectCourseTable, String> courseCode;

    @FXML
    private TableColumn<SelectCourseTable, String> courseName;

    @FXML
    private TableView<SelectCourseTable> courseSelectTable;

    @FXML
    private TableColumn<SelectCourseTable, Integer> credit;

    @FXML
    private TableColumn<SelectCourseTable, String> instructor;

    @FXML
    private TableColumn<SelectCourseTable, CheckBox> selection;

    @FXML
    private Label semester;

    @FXML
    private Label studentGPA;

    @FXML
    private Label studentName;

    private ObservableList<SelectCourseTable> selectCourseList;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        semester.setText("");
        studentGPA.setText("");
        studentName.setText("");
        studentNumber.setText("");
        completedCredits.setText("");
        advisorName.setText("");
        selectCourseList = null;
    }

    @FXML
    public void listOptions(RegistrationSystem registrationSystem){
        this.registrationSystem = registrationSystem;
        studentGPA.setText(String.format("%3.2f",registrationSystem.getLoggedStudentGPA()));
        studentNumber.setText(registrationSystem.getLoggedStudentID().toString());
        studentName.setText(registrationSystem.getLoggedStudentName().toString());
        completedCredits.setText(registrationSystem.getLoggedStudentCompletedCredit().toString());
        semester.setText(registrationSystem.getSemester());
        advisorName.setText(registrationSystem.getLoggedStudentAdvisor());

        courseCode.setCellValueFactory(new PropertyValueFactory<SelectCourseTable,String>("courseCode"));
        courseName.setCellValueFactory(new PropertyValueFactory<SelectCourseTable,String>("courseName"));
        credit.setCellValueFactory(new PropertyValueFactory<SelectCourseTable,Integer>("credit"));
        instructor.setCellValueFactory(new PropertyValueFactory<SelectCourseTable,String>("instructor"));
        selection.setCellValueFactory(new PropertyValueFactory<SelectCourseTable,CheckBox>("selection"));

        selectCourseList = FXCollections.observableArrayList(
            SelectCourseTable.convertToTable(registrationSystem.getAvailableCourses(registrationSystem.getLoggedStudent()))
        );
        courseSelectTable.setItems(selectCourseList);
    }

    @FXML
    public void sendAdvisorVerification(){
        ObservableList<SelectCourseTable> removedRows =
                FXCollections.observableArrayList();

        ArrayList<String> selectedCourses = new ArrayList<String>();
        for(SelectCourseTable currRow:selectCourseList){
            if(currRow.getSelection().isSelected()){
                selectedCourses.add(currRow.getCourseCode());
                removedRows.add(currRow);
            }
        }
        if(selectedCourses.size()==0){
            return;
        }
        String check = registrationSystem.makeSelection(selectedCourses);
        if(check.equals("success")){
            selectCourseList.removeAll(removedRows);
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Successfully registered courses");
            alert.setHeaderText("Registration was successful.");
            alert.showAndWait();
        }
        else{
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Failed to register courses");
            alert.setHeaderText("Failed to register courses.");
            alert.setContentText(check);
            alert.showAndWait();
        }
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