package controller;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.IOException;

public class Main extends Application {

    @Override
    public void start(Stage  stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("welcomeControllerView.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        stage.setTitle("Online Course Registration System");
        //stage.getIcons().add(new Image("iteration#1/src/resources/controller/images/group-1.png"));
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}