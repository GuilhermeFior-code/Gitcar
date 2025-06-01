package com.gitcar.app.controllers;

import com.gitcar.app.MainApp;
// import com.gitcar.app.models.Employee; // Placeholder
// import com.gitcar.app.services.AuthService; // Placeholder
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

import java.io.IOException;

public class LoginController {

    @FXML
    private TextField emailField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private TextField passwordTextField; // Used for showing password

    @FXML
    private CheckBox showPasswordCheckbox;

    @FXML
    private Button loginButton;

    @FXML
    private Label statusLabel;

    // Placeholder for authentication service
    // private AuthService authService = new AuthService();

    @FXML
    public void initialize() {
        // Bind the visibility/managed properties for the password fields
        passwordTextField.managedProperty().bind(showPasswordCheckbox.selectedProperty());
        passwordTextField.visibleProperty().bind(showPasswordCheckbox.selectedProperty());
        passwordField.managedProperty().bind(showPasswordCheckbox.selectedProperty().not());
        passwordField.visibleProperty().bind(showPasswordCheckbox.selectedProperty().not());

        // Bind the text content between the two fields
        passwordTextField.textProperty().bindBidirectional(passwordField.textProperty());

        statusLabel.setText(""); // Clear status on init
    }

    @FXML
    void handleLoginButtonAction(ActionEvent event) {
        String email = emailField.getText();
        String password = passwordField.getText();

        statusLabel.setText(""); // Clear previous messages

        if (email.isEmpty() || password.isEmpty()) {
            statusLabel.setText("Email e senha são obrigatórios.");
            return;
        }

        // --- Authentication Logic (Placeholder) ---
        // In a real app, call authService.authenticate(email, password)
        // which would return an Employee object or null/throw exception.

        // Placeholder logic:
        String role = authenticatePlaceholder(email, password);

        if (role != null) {
            try {
                if ("Manager".equals(role)) {
                    MainApp.showManagerMenu();
                } else if ("Salesperson".equals(role)) {
                    MainApp.showSalespersonMenu();
                } else {
                    statusLabel.setText("Função de usuário desconhecida.");
                }
            } catch (IOException e) {
                statusLabel.setText("Erro ao carregar o menu: " + e.getMessage());
                e.printStackTrace(); // Log the error
            }
        } else {
            statusLabel.setText("Email ou senha inválidos.");
        }
        // --- End Placeholder ---
    }

    @FXML
    void togglePasswordVisibility(ActionEvent event) {
        // The visibility is handled by the bindings in initialize()
        // This method is kept if additional logic is needed on toggle
    }

    // --- Placeholder Authentication Method ---
    private String authenticatePlaceholder(String email, String password) {
        // This is a temporary stub. Replace with actual DB check via AuthService/DAO.
        // Use BCrypt.checkpw(password, employee.getPasswordHash()) for real check.
        if ("manager@gitcar.com".equals(email) && "admin".equals(password)) {
            return "Manager";
        } else if ("sales@gitcar.com".equals(email) && "sales".equals(password)) {
            return "Salesperson";
        } else {
            return null;
        }
    }
}

