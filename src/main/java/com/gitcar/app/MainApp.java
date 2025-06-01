package com.gitcar.app;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;


public class MainApp extends Application {

    private static Stage primaryStage;

    @Override
    public void start(Stage stage) throws IOException {
        primaryStage = stage;
        primaryStage.setTitle("GIT CAR");
        showLoginScreen();
        primaryStage.show();
    }

    public static void showLoginScreen() throws IOException {
        loadScene("/com/gitcar/view/LoginView.fxml");
    }

    public static void showSalespersonMenu() throws IOException {
        loadScene("/com/gitcar/view/SalespersonMenuView.fxml");
    }

    public static void showManagerMenu() throws IOException {
        loadScene("/com/gitcar/view/ManagerMenuView.fxml");
    }

    private static void loadScene(String fxmlPath) throws IOException {
        URL fxmlUrl = MainApp.class.getResource(fxmlPath);
        if (fxmlUrl == null) {
            System.err.println("Cannot load FXML file: " + fxmlPath);
            // Consider throwing a custom exception or showing an error dialog
            throw new IOException("Cannot find FXML resource: " + fxmlPath);
        }
        FXMLLoader loader = new FXMLLoader(fxmlUrl);
        Parent root = loader.load();
        Scene scene = primaryStage.getScene();
        if (scene == null) {
            scene = new Scene(root);
            primaryStage.setScene(scene);
        } else {
            primaryStage.getScene().setRoot(root);
        }
        primaryStage.sizeToScene(); // Adjust stage size to fit the new scene
        primaryStage.centerOnScreen();
    }

    public static Stage getPrimaryStage() {
        return primaryStage;
    }

    public static void main(String[] args) {
        launch(args);
    }
}

