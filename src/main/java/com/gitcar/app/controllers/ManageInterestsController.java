package com.gitcar.app.controllers;

import com.gitcar.app.models.CustomerInterest;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.util.concurrent.atomic.AtomicInteger;

public class ManageInterestsController {

    //<editor-fold desc="FXML Fields">
    @FXML private TextField interestSearchField;
    @FXML private Button searchInterestButton;
    @FXML private TableView<CustomerInterest> interestsTableView;
    @FXML private TableColumn<CustomerInterest, Integer> interestIdCol;
    @FXML private TableColumn<CustomerInterest, String> customerNameCol;
    @FXML private TableColumn<CustomerInterest, String> contactInfoCol;
    @FXML private TableColumn<CustomerInterest, String> vehicleInterestCol;
    @FXML private TableColumn<CustomerInterest, String> notesCol;
    @FXML private TextField interestIdField;
    @FXML private TextField customerNameField;
    @FXML private TextField contactInfoField;
    @FXML private TextField vehicleInterestField;
    @FXML private TextArea notesArea;
    @FXML private Button addInterestButton;
    @FXML private Button saveInterestButton;
    @FXML private Button clearInterestFormButton;
    @FXML private Button deleteInterestButton;
    @FXML private Label formStatusLabel;
    @FXML private Label statusLabel;
    //</editor-fold>

    // In-memory data storage using Java Collections
    private ObservableList<CustomerInterest> interestsData = FXCollections.observableArrayList();
    private FilteredList<CustomerInterest> filteredInterests;
    private CustomerInterest selectedInterest = null;
    private static final AtomicInteger idCounter = new AtomicInteger(0); // Simple ID generator

    @FXML
    public void initialize() {
        statusLabel.setText("Dados gerenciados em memória (temporário).");
        formStatusLabel.setText("");
        setupTableColumns();
        loadInitialInterestsPlaceholder(); // Load some dummy data

        // Wrap the ObservableList in a FilteredList
        filteredInterests = new FilteredList<>(interestsData, p -> true);

        // Bind the FilteredList to the TableView
        interestsTableView.setItems(filteredInterests);

        // Add listener to TableView selection
        interestsTableView.getSelectionModel().selectedItemProperty().addListener(
                (observable, oldValue, newValue) -> populateForm(newValue));

        // Initial form state
        clearForm();
        disableForm(true);
    }

    private void setupTableColumns() {
        interestIdCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        customerNameCol.setCellValueFactory(new PropertyValueFactory<>("customerName"));
        contactInfoCol.setCellValueFactory(new PropertyValueFactory<>("contactInfo"));
        vehicleInterestCol.setCellValueFactory(new PropertyValueFactory<>("vehicleInterest"));
        notesCol.setCellValueFactory(new PropertyValueFactory<>("notes"));
    }

    @FXML
    void handleSearchInterest(ActionEvent event) {
        String searchTerm = interestSearchField.getText().toLowerCase();

        filteredInterests.setPredicate(interest -> {
            if (searchTerm == null || searchTerm.isEmpty()) {
                return true; // No filter
            }
            // Basic search in name or vehicle interest
            return interest.getCustomerName().toLowerCase().contains(searchTerm) ||
                   interest.getVehicleInterest().toLowerCase().contains(searchTerm);
        });
        statusLabel.setText("Exibindo " + filteredInterests.size() + " interesses.");
    }

    private void populateForm(CustomerInterest interest) {
        selectedInterest = interest;
        if (interest != null) {
            disableForm(false);
            interestIdField.setText(String.valueOf(interest.getId()));
            customerNameField.setText(interest.getCustomerName());
            contactInfoField.setText(interest.getContactInfo());
            vehicleInterestField.setText(interest.getVehicleInterest());
            notesArea.setText(interest.getNotes());
            formStatusLabel.setText("");
            saveInterestButton.setText("Salvar Alterações");
        } else {
            clearForm();
            disableForm(true);
        }
    }

    @FXML
    void handleAddInterest(ActionEvent event) {
        clearForm();
        disableForm(false);
        selectedInterest = null; // Indicate new interest mode
        interestIdField.setText("(Novo)");
        formStatusLabel.setText("Preencha os dados para adicionar novo interesse.");
        saveInterestButton.setText("Adicionar");
        customerNameField.requestFocus();
    }

    @FXML
    void handleSaveInterest(ActionEvent event) {
        formStatusLabel.setText("");
        String customerName = customerNameField.getText().trim();
        String contactInfo = contactInfoField.getText().trim();
        String vehicleInterest = vehicleInterestField.getText().trim();
        String notes = notesArea.getText().trim();

        // Validation
        if (customerName.isEmpty() || vehicleInterest.isEmpty()) {
            formStatusLabel.setText("Erro: Nome do Cliente e Veículo de Interesse são obrigatórios.");
            return;
        }

        if (selectedInterest == null) { // Adding new interest
            int newId = idCounter.incrementAndGet();
            CustomerInterest newInterest = new CustomerInterest(newId, customerName, contactInfo, vehicleInterest, notes);
            interestsData.add(newInterest);
            formStatusLabel.setText("Interesse adicionado com sucesso (ID: " + newId + ").");
            System.out.println("Adding Interest (in memory): " + customerName);

        } else { // Updating existing interest
            selectedInterest.setCustomerName(customerName);
            selectedInterest.setContactInfo(contactInfo);
            selectedInterest.setVehicleInterest(vehicleInterest);
            selectedInterest.setNotes(notes);
            formStatusLabel.setText("Interesse atualizado com sucesso.");
            System.out.println("Updating Interest (in memory) ID: " + selectedInterest.getId());
            // Refresh the table view to show changes if the item itself is modified
            interestsTableView.refresh();
        }
        // No need to call refreshTable() like in DB-backed screens as ObservableList handles updates
        clearForm();
        disableForm(true);
    }

    @FXML
    void handleClearInterestForm(ActionEvent event) {
        clearForm();
        disableForm(true);
        interestsTableView.getSelectionModel().clearSelection();
    }

    @FXML
    void handleDeleteInterest(ActionEvent event) {
        if (selectedInterest == null) {
            formStatusLabel.setText("Selecione um interesse na tabela para excluir.");
            return;
        }

        Alert confirmation = new Alert(Alert.AlertType.CONFIRMATION);
        confirmation.setTitle("Confirmar Exclusão");
        confirmation.setHeaderText("Excluir interesse de " + selectedInterest.getCustomerName() + "?");
        confirmation.setContentText("Esta ação não pode ser desfeita (dados em memória).");

        confirmation.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                interestsData.remove(selectedInterest);
                formStatusLabel.setText("Interesse excluído.");
                System.out.println("Deleting Interest (in memory) ID: " + selectedInterest.getId());
                clearForm();
                disableForm(true);
            }
        });
    }

    private void clearForm() {
        selectedInterest = null;
        interestIdField.clear();
        customerNameField.clear();
        contactInfoField.clear();
        vehicleInterestField.clear();
        notesArea.clear();
        formStatusLabel.setText("");
        saveInterestButton.setText("Salvar");
    }

    private void disableForm(boolean disable) {
        customerNameField.setDisable(disable);
        contactInfoField.setDisable(disable);
        vehicleInterestField.setDisable(disable);
        notesArea.setDisable(disable);
        saveInterestButton.setDisable(disable);
        clearInterestFormButton.setDisable(disable);
        deleteInterestButton.setDisable(disable || selectedInterest == null);
    }

    // --- Placeholder Data Loading ---
    private void loadInitialInterestsPlaceholder() {
        interestsData.clear();
        interestsData.addAll(
                new CustomerInterest(idCounter.incrementAndGet(), "Ana Beatriz", "ana.b@email.com", "SUV Compacto", "Prefere cor branca ou prata"),
                new CustomerInterest(idCounter.incrementAndGet(), "Marcos Paulo", "11 98888-7777", "Pickup Média", "Necessita para trabalho, diesel")
        );
        statusLabel.setText("Carregado " + interestsData.size() + " interesses (placeholder).");
    }
    // --- End Placeholder ---
}

