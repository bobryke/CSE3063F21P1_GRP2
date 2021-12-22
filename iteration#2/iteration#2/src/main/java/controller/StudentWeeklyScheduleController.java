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
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import system.RegistrationSystem;
import table.TranscriptTable;
import table.WeeklyScheduleTable;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class StudentWeeklyScheduleController implements Initializable {

    @FXML
    private RegistrationSystem registrationSystem;

    private Scene scene;
    private Parent root;
    private Stage stage;

    @FXML
    private Label studentName;
    @FXML
    private Label studentNumber;
    @FXML
    private Label semester;

    @FXML
    private TableView<WeeklyScheduleTable> weeklyScheduleTable;
    @FXML
    private TableColumn<WeeklyScheduleTable,String> hour;
    @FXML
    private TableColumn<WeeklyScheduleTable,String> monday;
    @FXML
    private TableColumn<WeeklyScheduleTable,String> tuesday;
    @FXML
    private TableColumn<WeeklyScheduleTable,String> wednesday;
    @FXML
    private TableColumn<WeeklyScheduleTable,String> thursday;
    @FXML
    private TableColumn<WeeklyScheduleTable,String> friday;

    private ObservableList<WeeklyScheduleTable> weeklyScheduleList;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        semester.setText("");
        studentNumber.setText("");
        studentName.setText("");
        weeklyScheduleList = null;
    }

    @FXML
    public void listOptions(RegistrationSystem registrationSystem){
        this.registrationSystem = registrationSystem;

        studentNumber.setText(registrationSystem.getLoggedStudentID());
        studentName.setText(registrationSystem.getLoggedStudentName());
        semester.setText(registrationSystem.getSemester());


        hour.setCellValueFactory(new PropertyValueFactory<WeeklyScheduleTable,String>("hour"));
        monday.setCellValueFactory(new PropertyValueFactory<WeeklyScheduleTable,String>("monday"));
        tuesday.setCellValueFactory(new PropertyValueFactory<WeeklyScheduleTable,String>("tuesday"));
        wednesday.setCellValueFactory(new PropertyValueFactory<WeeklyScheduleTable,String>("wednesday"));
        thursday.setCellValueFactory(new PropertyValueFactory<WeeklyScheduleTable,String>("thursday"));
        friday.setCellValueFactory(new PropertyValueFactory<WeeklyScheduleTable,String>("friday"));

        weeklyScheduleList = FXCollections.observableArrayList(
                WeeklyScheduleTable.convertToTable(registrationSystem.getLoggedStudentWeeklySchedule())
        );

        weeklyScheduleTable.setItems(weeklyScheduleList);
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
