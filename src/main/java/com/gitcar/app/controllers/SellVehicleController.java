package com.gitcar.app.controllers;

import com.gitcar.app.models.Customer;
import com.gitcar.app.models.Vehicle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.util.StringConverter;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

public class SellVehicleController {

    @FXML
    private TextField vehicleSearchField;
    @FXML
    private Button searchVehicleButton;
    @FXML
    private ComboBox<Vehicle> availableVehiclesComboBox;
    @FXML
    private TextField modelField;
    @FXML
    private TextField brandField;
    @FXML
    private TextField yearField;
    @FXML
    private TextField priceField;
    @FXML
    private TextField chassisNumberField;
    @FXML
    private ComboBox<Customer> customerComboBox;
    @FXML
    private Button addNewCustomerButton;
    @FXML
    private TextField saleValueField;
    @FXML
    private ComboBox<String> paymentMethodComboBox;
    @FXML
    private DatePicker saleDatePicker;
    @FXML
    private Button cancelButton;
    @FXML
    private Button confirmSaleButton;
    @FXML
    private Label statusLabel;

    // Placeholder data - replace with Service/DAO calls
    private ObservableList<Vehicle> availableVehicles = FXCollections.observableArrayList();
    private ObservableList<Customer> customers = FXCollections.observableArrayList();
    private Vehicle selectedVehicle = null;

    @FXML
    public void initialize() {
        statusLabel.setText("");
        setupComboBoxes();
        saleDatePicker.setValue(LocalDate.now());

        // Load initial data (placeholders)
        loadCustomersPlaceholder();
        // Initially disable sale details until a vehicle is selected
        disableSaleDetails(true);
    }

    private void setupComboBoxes() {
        // Vehicle ComboBox
        availableVehiclesComboBox.setConverter(new StringConverter<Vehicle>() {
            @Override
            public String toString(Vehicle vehicle) {
                return vehicle == null ? null : vehicle.getVehicleId() + " - " + vehicle.getBrand() + " " + vehicle.getModel() + " (" + vehicle.getYear() + ")";
            }

            @Override
            public Vehicle fromString(String string) {
                return null; // Not needed for selection only
            }
        });
        availableVehiclesComboBox.setItems(availableVehicles);

        // Customer ComboBox
        customerComboBox.setConverter(new StringConverter<Customer>() {
            @Override
            public String toString(Customer customer) {
                return customer == null ? null : customer.getCustomerId() + " - " + customer.getName();
            }

            @Override
            public Customer fromString(String string) {
                return null;
            }
        });
        customerComboBox.setItems(customers);

        // Payment Method ComboBox
        paymentMethodComboBox.setItems(FXCollections.observableArrayList("Dinheiro", "Cartão Débito", "Cartão Crédito", "Financiamento"));
    }

    @FXML
    void handleSearchVehicle(ActionEvent event) {
        String searchTerm = vehicleSearchField.getText().trim();
        statusLabel.setText("");
        availableVehicles.clear(); // Clear previous results

        if (searchTerm.isEmpty()) {
            statusLabel.setText("Digite um termo para buscar.");
            return;
        }

        // --- Placeholder Search Logic ---
        // Replace with actual call to VehicleService.findAvailableByTerm(searchTerm)
        System.out.println("Searching for available vehicles matching: " + searchTerm);
        // Add dummy results
        availableVehicles.add(new Vehicle(1, "Civic", "Honda", 2022, 15000, "Sedan", 120000, "Preto", "CHASSI123", "Available", "2024-01-10"));
        availableVehicles.add(new Vehicle(3, "Corolla", "Toyota", 2021, 25000, "Sedan", 115000, "Prata", "CHASSI456", "Available", "2024-02-15"));
        if (availableVehicles.isEmpty()) {
            statusLabel.setText("Nenhum veículo disponível encontrado.");
        }
        // --- End Placeholder ---
    }

    @FXML
    void handleVehicleSelection(ActionEvent event) {
        selectedVehicle = availableVehiclesComboBox.getSelectionModel().getSelectedItem();
        if (selectedVehicle != null) {
            modelField.setText(selectedVehicle.getModel());
            brandField.setText(selectedVehicle.getBrand());
            yearField.setText(String.valueOf(selectedVehicle.getYear()));
            priceField.setText(String.format("%.2f", selectedVehicle.getPrice()));
            chassisNumberField.setText(selectedVehicle.getChassisNumber());
            saleValueField.setText(String.format("%.2f", selectedVehicle.getPrice())); // Pre-fill sale value
            disableSaleDetails(false);
            statusLabel.setText("");
        } else {
            clearVehicleDetails();
            disableSaleDetails(true);
        }
    }

    @FXML
    void handleAddNewCustomer(ActionEvent event) {
        // TODO: Implement opening a dialog/window to add a new customer
        // For now, add a placeholder customer to the list
        Customer newCust = new Customer(99, "Novo Cliente", "novo@email.com", "99999-9999");
        customers.add(newCust);
        customerComboBox.getSelectionModel().select(newCust);
        statusLabel.setText("Funcionalidade Adicionar Novo Cliente (placeholder).");
    }

    @FXML
    void handleConfirmSale(ActionEvent event) {
        statusLabel.setText("");
        if (selectedVehicle == null) {
            statusLabel.setText("Erro: Nenhum veículo selecionado.");
            return;
        }
        Customer selectedCustomer = customerComboBox.getSelectionModel().getSelectedItem();
        if (selectedCustomer == null) {
            statusLabel.setText("Erro: Nenhum cliente selecionado.");
            return;
        }
        String paymentMethod = paymentMethodComboBox.getSelectionModel().getSelectedItem();
        if (paymentMethod == null || paymentMethod.isEmpty()) {
            statusLabel.setText("Erro: Selecione a forma de pagamento.");
            return;
        }
        LocalDate saleDate = saleDatePicker.getValue();
        if (saleDate == null) {
            statusLabel.setText("Erro: Selecione a data da venda.");
            return;
        }
        double saleValue;
        try {
            saleValue = Double.parseDouble(saleValueField.getText().replace(",", "."));
        } catch (NumberFormatException e) {
            statusLabel.setText("Erro: Valor da venda inválido.");
            return;
        }

        // --- Placeholder Sale Confirmation Logic ---
        // Replace with actual call to SaleService.recordSale(...)
        // This service method should create Sale record and update Vehicle status
        System.out.println("Confirming Sale:");
        System.out.println("  Vehicle: " + selectedVehicle.getVehicleId());
        System.out.println("  Customer: " + selectedCustomer.getCustomerId());
        System.out.println("  Value: " + saleValue);
        System.out.println("  Payment: " + paymentMethod);
        System.out.println("  Date: " + saleDate.toString());

        statusLabel.setText("Venda confirmada com sucesso (placeholder)! Veículo ID: " + selectedVehicle.getVehicleId());
        // Clear form or navigate away
        clearForm();
        availableVehicles.remove(selectedVehicle); // Remove from available list
        selectedVehicle = null;
        disableSaleDetails(true);
        // --- End Placeholder ---
    }

    @FXML
    void handleCancel(ActionEvent event) {
        clearForm();
        statusLabel.setText("Operação cancelada.");
    }

    private void clearForm() {
        vehicleSearchField.clear();
        availableVehiclesComboBox.getSelectionModel().clearSelection();
        availableVehicles.clear();
        clearVehicleDetails();
        customerComboBox.getSelectionModel().clearSelection();
        saleValueField.clear();
        paymentMethodComboBox.getSelectionModel().clearSelection();
        saleDatePicker.setValue(LocalDate.now());
        statusLabel.setText("");
        selectedVehicle = null;
        disableSaleDetails(true);
    }

    private void clearVehicleDetails() {
        modelField.clear();
        brandField.clear();
        yearField.clear();
        priceField.clear();
        chassisNumberField.clear();
    }

    private void disableSaleDetails(boolean disable) {
        customerComboBox.setDisable(disable);
        addNewCustomerButton.setDisable(disable);
        saleValueField.setDisable(disable);
        paymentMethodComboBox.setDisable(disable);
        saleDatePicker.setDisable(disable);
        confirmSaleButton.setDisable(disable);
    }

    // --- Placeholder Data Loading ---
    private void loadCustomersPlaceholder() {
        customers.addAll(
                new Customer(1, "João Silva", "joao@email.com", "1111-1111"),
                new Customer(2, "Maria Oliveira", "maria@email.com", "2222-2222")
        );
    }
    // --- End Placeholder ---
}

