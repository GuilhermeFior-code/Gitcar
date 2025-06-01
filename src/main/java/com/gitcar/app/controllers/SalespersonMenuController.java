package com.gitcar.app.controllers;

import com.gitcar.app.MainApp;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;

import java.io.IOException;
import java.net.URL;

public class SalespersonMenuController {

    @FXML
    private BorderPane mainPane;

    @FXML
    private MenuItem logoutMenuItem;

    @FXML
    private MenuItem aboutMenuItem;

    @FXML
    private Button sellVehicleButton;

    @FXML
    private Button salesHistoryButton;

    @FXML
    private Button scheduleTestDriveButton;

    @FXML
    private Pane contentArea;

    @FXML
    private Label statusBarLabel;

    @FXML
    public void initialize() {
        // TODO: Get logged-in user info and set status bar
        statusBarLabel.setText("Usuário: sales@gitcar.com (Vendedor)"); // Placeholder
        // Load a default view or welcome message into contentArea if desired
        loadView("/com/gitcar/view/WelcomeView.fxml"); // Example placeholder view
    }

    @FXML
    void handleSellVehicle(ActionEvent event) {
        loadView("/com/gitcar/view/SellVehicleView.fxml");
    }

    @FXML
    void handleSalesHistory(ActionEvent event) {
        loadView("/com/gitcar/view/SalesHistoryView.fxml");
    }

    @FXML
    void handleScheduleTestDrive(ActionEvent event) {
        loadView("/com/gitcar/view/ScheduleTestDriveView.fxml");
    }

    @FXML
    void handleLogout(ActionEvent event) {
        try {
            MainApp.showLoginScreen();
        } catch (IOException e) {
            showError("Erro ao fazer logout: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @FXML
    void handleAbout(ActionEvent event) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Sobre GIT CAR");
        alert.setHeaderText("GIT CAR - Sistema de Gerenciamento de Concessionária");
        alert.setContentText("Versão 1.0\nDesenvolvido como projeto.");
        alert.showAndWait();
    }

    private void loadView(String fxmlPath) {
        try {
            URL fxmlUrl = getClass().getResource(fxmlPath);
            if (fxmlUrl == null) {
                showError("Não foi possível encontrar o arquivo FXML: " + fxmlPath);
                return;
            }
            FXMLLoader loader = new FXMLLoader(fxmlUrl);
            Parent view = loader.load();
            contentArea.getChildren().setAll(view);
        } catch (IOException e) {
            showError("Erro ao carregar a tela: " + fxmlPath + "\n" + e.getMessage());
            e.printStackTrace();
        }
    }

    private void showError(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Erro");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}

