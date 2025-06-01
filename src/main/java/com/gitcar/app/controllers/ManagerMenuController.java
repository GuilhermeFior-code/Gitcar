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

public class ManagerMenuController {

    @FXML
    private BorderPane mainPane;

    @FXML
    private MenuItem logoutMenuItem;

    @FXML
    private MenuItem aboutMenuItem;

    @FXML
    private Button salesReportButton;

    @FXML
    private Button manageEmployeesButton;

    @FXML
    private Button registerVehicleButton;

    @FXML
    private Button manageInterestsButton; // For Collections screen

    @FXML
    private Pane contentArea;

    @FXML
    private Label statusBarLabel;

    @FXML
    public void initialize() {
        // TODO: Get logged-in user info and set status bar
        statusBarLabel.setText("Usuário: manager@gitcar.com (Gerente)"); // Placeholder
        // Load a default view or welcome message into contentArea if desired
        loadView("/com/gitcar/view/WelcomeManagerView.fxml"); // Example placeholder view
    }

    @FXML
    void handleSalesReport(ActionEvent event) {
        loadView("/com/gitcar/view/SalesReportView.fxml");
    }

    @FXML
    void handleManageEmployees(ActionEvent event) {
        loadView("/com/gitcar/view/ManageEmployeesView.fxml");
    }

    @FXML
    void handleRegisterVehicle(ActionEvent event) {
        loadView("/com/gitcar/view/RegisterVehicleView.fxml");
    }

    @FXML
    void handleManageInterests(ActionEvent event) {
        loadView("/com/gitcar/view/ManageInterestsView.fxml");
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

