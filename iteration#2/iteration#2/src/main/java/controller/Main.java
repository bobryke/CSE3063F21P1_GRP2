package controller;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.util.logging.Logger;
import java.util.logging.FileHandler;
import java.util.logging.SimpleFormatter;

import java.io.IOException;

public class Main extends Application {
    private static final Logger logger = Logger.getLogger("");
    FileHandler fh;

    @Override
    public void start(Stage  stage) throws IOException {
        // initializing logger
        try {

            // This block configure the logger with handler and formatter
            fh = new FileHandler("log.txt");
            logger.addHandler(fh);
            SimpleFormatter formatter = new SimpleFormatter();
            fh.setFormatter(formatter);

        } catch (SecurityException | IOException e) {
            e.printStackTrace();
        }

        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("welcomeControllerView.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        stage.setTitle("Offline Course Registration System");
        //stage.getIcons().add(new Image("iteration#1/src/resources/controller/images/group-1.png"));
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}