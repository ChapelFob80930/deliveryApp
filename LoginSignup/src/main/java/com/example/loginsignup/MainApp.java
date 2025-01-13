package com.example.loginsignup;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.io.IOException;

public class MainApp extends Application {

    private static Stage primaryStage;

    @Override
    public void start(Stage stage) throws IOException {
        primaryStage = stage;
        FXMLLoader fxmlLoader = new FXMLLoader(MainApp.class.getResource("login-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        primaryStage.setTitle("Login");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    /**
     * Opens a new stage with the specified FXML file and title.
     * @param fxmlFile The FXML file to load.
     * @param title The title of the new stage.
     */
    public static void openNewStage(String fxmlFile, String title) {
        try {
            // Load the FXML file relative to the classpath
            FXMLLoader fxmlLoader = new FXMLLoader(MainApp.class.getResource(fxmlFile));

            // Create a new scene and set it to a new stage
            Scene scene = new Scene(fxmlLoader.load());
            Stage stage = new Stage();
            stage.setTitle(title);
            stage.setScene(scene);

            // Show the new stage
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Error loading FXML file: " + fxmlFile);
        }
    }

    public static void main(String[] args) {
        launch();
    }
}
