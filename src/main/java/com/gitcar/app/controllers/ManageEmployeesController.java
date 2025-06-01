package com.gitcar.app.controllers;

import com.gitcar.app.models.Employee;
// import com.gitcar.app.services.EmployeeService; // Placeholder
// import com.gitcar.app.utils.PasswordUtils; // Placeholder for hashing
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.util.Optional;

public class ManageEmployeesController {

    //<editor-fold desc="FXML Fields">
    @FXML private TextField employeeSearchField;
    @FXML private ComboBox<String> statusFilterComboBox;
    @FXML private Button searchButton;
    @FXML private TableView<Employee> employeesTableView;
    @FXML private TableColumn<Employee, Integer> employeeIdCol;
    @FXML private TableColumn<Employee, String> nameCol;
    @FXML private TableColumn<Employee, String> emailCol;
    @FXML private TableColumn<Employee, String> roleCol;
    @FXML private TableColumn<Employee, String> statusCol;
    @FXML private TextField employeeIdField;
    @FXML private TextField nameField;
    @FXML private TextField emailField;
    @FXML private PasswordField passwordField;
    @FXML private ComboBox<String> roleComboBox;
    @FXML private ComboBox<String> statusComboBox;
    @FXML private Button addButton;
    @FXML private Button saveButton;
    @FXML private Button clearFormButton;
    @FXML private Button deactivateButton;
    @FXML private Label formStatusLabel;
    @FXML private Label statusLabel;
    //</editor-fold>

    // Placeholder data & service
    private ObservableList<Employee> allEmployees = FXCollections.observableArrayList();
    private FilteredList<Employee> filteredEmployees;
    // private EmployeeService employeeService = new EmployeeService(); // Placeholder
    private Employee selectedEmployee = null;

    @FXML
    public void initialize() {
        statusLabel.setText("");
        formStatusLabel.setText("");
        setupTableColumns();
        setupComboBoxes();
        loadEmployeesPlaceholder(); // Load initial data

        // Wrap the ObservableList in a FilteredList (initially display all data)
        filteredEmployees = new FilteredList<>(allEmployees, p -> true);

        // Bind the FilteredList to the TableView
        employeesTableView.setItems(filteredEmployees);

        // Add listener to TableView selection
        employeesTableView.getSelectionModel().selectedItemProperty().addListener(
                (observable, oldValue, newValue) -> populateForm(newValue));

        // Initial form state
        clearForm();
        disableForm(true);
    }

    private void setupTableColumns() {
        employeeIdCol.setCellValueFactory(new PropertyValueFactory<>("employeeId"));
        nameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        emailCol.setCellValueFactory(new PropertyValueFactory<>("email"));
        roleCol.setCellValueFactory(new PropertyValueFactory<>("role"));
        statusCol.setCellValueFactory(new PropertyValueFactory<>("status"));
    }

    private void setupComboBoxes() {
        statusFilterComboBox.setItems(FXCollections.observableArrayList("Todos", "Active", "Inactive"));
        statusFilterComboBox.getSelectionModel().select("Todos");

        roleComboBox.setItems(FXCollections.observableArrayList("Salesperson", "Manager"));
        statusComboBox.setItems(FXCollections.observableArrayList("Active", "Inactive"));
    }

    @FXML
    void handleSearch(ActionEvent event) {
        String searchTerm = employeeSearchField.getText().toLowerCase();
        String statusFilter = statusFilterComboBox.getValue();

        filteredEmployees.setPredicate(employee -> {
            // If filter text is empty, display all employees matching status
            if (searchTerm == null || searchTerm.isEmpty()) {
                return ("Todos".equals(statusFilter) || employee.getStatus().equalsIgnoreCase(statusFilter));
            }

            // Compare name and email
            boolean termMatch = employee.getName().toLowerCase().contains(searchTerm) ||
                                employee.getEmail().toLowerCase().contains(searchTerm);

            // Check status filter
            boolean statusMatch = ("Todos".equals(statusFilter) || employee.getStatus().equalsIgnoreCase(statusFilter));

            return termMatch && statusMatch;
        });
        statusLabel.setText("Exibindo " + filteredEmployees.size() + " funcionários.");
    }

    private void populateForm(Employee employee) {
        selectedEmployee = employee;
        if (employee != null) {
            disableForm(false);
            employeeIdField.setText(String.valueOf(employee.getEmployeeId()));
            nameField.setText(employee.getName());
            emailField.setText(employee.getEmail());
            passwordField.clear(); // Don't show existing hash
            passwordField.setPromptText("(Deixe em branco para não alterar)");
            roleComboBox.setValue(employee.getRole());
            statusComboBox.setValue(employee.getStatus());
            formStatusLabel.setText("");
            saveButton.setText("Salvar Alterações");
        } else {
            clearForm();
            disableForm(true);
        }
    }

    @FXML
    void handleAdd(ActionEvent event) {
        clearForm();
        disableForm(false);
        selectedEmployee = null; // Indicate new employee mode
        employeeIdField.setText("(Novo)");
        statusComboBox.setValue("Active"); // Default for new
        passwordField.setPromptText("(Senha obrigatória)");
        formStatusLabel.setText("Preencha os dados para adicionar novo funcionário.");
        saveButton.setText("Adicionar");
        nameField.requestFocus();
    }

    @FXML
    void handleSave(ActionEvent event) {
        formStatusLabel.setText("");
        String name = nameField.getText().trim();
        String email = emailField.getText().trim();
        String password = passwordField.getText(); // No trim for password
        String role = roleComboBox.getValue();
        String status = statusComboBox.getValue();

        // Validation
        if (name.isEmpty() || email.isEmpty() || role == null || status == null) {
            formStatusLabel.setText("Erro: Nome, Email, Cargo e Status são obrigatórios.");
            return;
        }
        // Basic email format check (can be improved)
        if (!email.contains("@") || !email.contains(".")) {
            formStatusLabel.setText("Erro: Formato de email inválido.");
            return;
        }

        // --- Placeholder Save/Update Logic ---
        try {
            if (selectedEmployee == null) { // Adding new employee
                if (password.isEmpty()) {
                    formStatusLabel.setText("Erro: Senha é obrigatória para novo funcionário.");
                    return;
                }
                // String hashedPassword = PasswordUtils.hashPassword(password); // Use BCrypt
                String hashedPassword = "hashed_" + password; // Placeholder hash
                Employee newEmployee = new Employee(name, email, hashedPassword, role);
                newEmployee.setStatus(status);
                // Employee savedEmployee = employeeService.addEmployee(newEmployee);
                // Simulate save
                int newId = allEmployees.stream().mapToInt(Employee::getEmployeeId).max().orElse(0) + 1;
                newEmployee.setEmployeeId(newId);
                allEmployees.add(newEmployee);
                formStatusLabel.setText("Funcionário adicionado com sucesso (ID: " + newId + ").");
                System.out.println("Adding Employee: " + name);

            } else { // Updating existing employee
                selectedEmployee.setName(name);
                selectedEmployee.setEmail(email);
                selectedEmployee.setRole(role);
                selectedEmployee.setStatus(status);
                if (!password.isEmpty()) {
                    // String hashedPassword = PasswordUtils.hashPassword(password);
                    String hashedPassword = "hashed_" + password; // Placeholder hash
                    selectedEmployee.setPasswordHash(hashedPassword);
                    System.out.println("Updating Employee ID: " + selectedEmployee.getEmployeeId() + " (Password Changed)");
                } else {
                    System.out.println("Updating Employee ID: " + selectedEmployee.getEmployeeId() + " (Password Unchanged)");
                }
                // employeeService.updateEmployee(selectedEmployee);
                formStatusLabel.setText("Funcionário atualizado com sucesso.");
            }
            refreshTable();
            clearForm();
            disableForm(true);
        } catch (Exception e) { // Catch specific exceptions in real app (e.g., DuplicateEmailException)
            formStatusLabel.setText("Erro ao salvar: " + e.getMessage());
            e.printStackTrace();
        }
        // --- End Placeholder ---
    }

    @FXML
    void handleClearForm(ActionEvent event) {
        clearForm();
        disableForm(true);
        employeesTableView.getSelectionModel().clearSelection();
    }

    @FXML
    void handleDeactivate(ActionEvent event) {
        if (selectedEmployee == null) {
            formStatusLabel.setText("Selecione um funcionário na tabela.");
            return;
        }

        String currentStatus = selectedEmployee.getStatus();
        String newStatus = "Active".equals(currentStatus) ? "Inactive" : "Active";

        Alert confirmation = new Alert(Alert.AlertType.CONFIRMATION);
        confirmation.setTitle("Confirmar Alteração de Status");
        confirmation.setHeaderText("Alterar status de " + selectedEmployee.getName() + " para " + newStatus + "?");
        confirmation.setContentText("Tem certeza que deseja alterar o status deste funcionário?");

        Optional<ButtonType> result = confirmation.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            // --- Placeholder Deactivate Logic ---
            try {
                selectedEmployee.setStatus(newStatus);
                // employeeService.updateEmployeeStatus(selectedEmployee.getEmployeeId(), newStatus);
                System.out.println("Changing status for Employee ID: " + selectedEmployee.getEmployeeId() + " to " + newStatus);
                formStatusLabel.setText("Status alterado para " + newStatus + ".");
                refreshTable();
                // Re-populate form to show updated status
                populateForm(selectedEmployee);
            } catch (Exception e) {
                formStatusLabel.setText("Erro ao alterar status: " + e.getMessage());
                e.printStackTrace();
            }
            // --- End Placeholder ---
        }
    }

    private void clearForm() {
        selectedEmployee = null;
        employeeIdField.clear();
        nameField.clear();
        emailField.clear();
        passwordField.clear();
        passwordField.setPromptText("(Nova ou em branco)");
        roleComboBox.getSelectionModel().clearSelection();
        statusComboBox.getSelectionModel().clearSelection();
        formStatusLabel.setText("");
        saveButton.setText("Salvar");
    }

    private void disableForm(boolean disable) {
        nameField.setDisable(disable);
        emailField.setDisable(disable);
        passwordField.setDisable(disable);
        roleComboBox.setDisable(disable);
        statusComboBox.setDisable(disable);
        saveButton.setDisable(disable);
        clearFormButton.setDisable(disable);
        deactivateButton.setDisable(disable || selectedEmployee == null); // Also disable if nothing selected
    }

    private void refreshTable() {
        // This forces the FilteredList to re-evaluate its predicate
        // A bit of a hack, might need a better way if performance is an issue
        filteredEmployees.setPredicate(null);
        filteredEmployees.setPredicate(p -> true);
        // Re-apply current filters
        handleSearch(null);
        employeesTableView.refresh();
    }

    // --- Placeholder Data Loading ---
    private void loadEmployeesPlaceholder() {
        allEmployees.clear();
        // Replace with actual call to EmployeeService.getAllEmployees()
        allEmployees.addAll(
                new Employee(1, "Carlos Silva", "carlos@gitcar.com", "hashed_sales", "Salesperson", "Active"),
                new Employee(2, "Ana Pereira", "ana@gitcar.com", "hashed_sales2", "Salesperson", "Active"),
                new Employee(3, "Pedro Costa", "pedro@gitcar.com", "hashed_sales3", "Salesperson", "Inactive"),
                new Employee(10, "Gerente Master", "manager@gitcar.com", "hashed_admin", "Manager", "Active")
        );
        statusLabel.setText("Carregado " + allEmployees.size() + " funcionários (placeholder).");
    }
    // --- End Placeholder ---
}

