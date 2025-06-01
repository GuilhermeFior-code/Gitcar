package com.gitcar.app.controllers;

import com.gitcar.app.models.Vehicle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.util.StringConverter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;

public class ScheduleTestDriveController {

    @FXML
    private ComboBox<Vehicle> availableVehiclesComboBox;

    @FXML
    private TextField customerNameField;

    @FXML
    private TextField customerContactField;

    @FXML
    private DatePicker testDriveDatePicker;

    @FXML
    private ComboBox<String> testDriveTimePicker;

    @FXML
    private Button cancelButton;

    @FXML
    private Button scheduleButton;

    @FXML
    private Label statusLabel;

    // Placeholder data - replace with Service/DAO calls
    private ObservableList<Vehicle> availableVehicles = FXCollections.observableArrayList();
    private int currentEmployeeId = 1; // Placeholder - Get from logged-in user session

    @FXML
    public void initialize() {
        statusLabel.setText("");
        setupComboBoxes();
        loadAvailableVehiclesPlaceholder();
        testDriveDatePicker.setValue(LocalDate.now()); // Default to today
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

        // Time ComboBox (e.g., 30-minute intervals during business hours)
        List<String> times = new ArrayList<>();
        LocalTime time = LocalTime.of(9, 0); // Start at 9:00 AM
        LocalTime endTime = LocalTime.of(17, 30); // End at 5:30 PM
        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm");
        while (!time.isAfter(endTime)) {
            times.add(time.format(timeFormatter));
            time = time.plusMinutes(30);
        }
        testDriveTimePicker.setItems(FXCollections.observableArrayList(times));
    }

    @FXML
    void handleSchedule(ActionEvent event) {
        statusLabel.setText("");
        Vehicle selectedVehicle = availableVehiclesComboBox.getSelectionModel().getSelectedItem();
        String customerName = customerNameField.getText().trim();
        String customerContact = customerContactField.getText().trim();
        LocalDate testDriveDate = testDriveDatePicker.getValue();
        String selectedTime = testDriveTimePicker.getSelectionModel().getSelectedItem();

        // Validation
        if (selectedVehicle == null) {
            statusLabel.setText("Erro: Selecione um veículo.");
            return;
        }
        if (customerName.isEmpty()) {
            statusLabel.setText("Erro: Nome do cliente é obrigatório.");
            return;
        }
        if (customerContact.isEmpty()) {
            statusLabel.setText("Erro: Contato do cliente é obrigatório.");
            return;
        }
        if (testDriveDate == null) {
            statusLabel.setText("Erro: Selecione a data.");
            return;
        }
        if (selectedTime == null || selectedTime.isEmpty()) {
            statusLabel.setText("Erro: Selecione a hora.");
            return;
        }

        LocalTime testDriveTime;
        try {
            testDriveTime = LocalTime.parse(selectedTime, DateTimeFormatter.ofPattern("HH:mm"));
        } catch (DateTimeParseException e) {
            statusLabel.setText("Erro: Formato de hora inválido.");
            return;
        }

        LocalDateTime testDriveDateTime = LocalDateTime.of(testDriveDate, testDriveTime);
        String dateTimeString = testDriveDateTime.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME);

        // --- Placeholder Scheduling Logic ---
        // Replace with actual call to TestDriveService.scheduleTestDrive(...)
        System.out.println("Scheduling Test Drive:");
        System.out.println("  Vehicle ID: " + selectedVehicle.getVehicleId());
        System.out.println("  Employee ID: " + currentEmployeeId);
        System.out.println("  Customer Name: " + customerName);
        System.out.println("  Customer Contact: " + customerContact);
        System.out.println("  DateTime: " + dateTimeString);

        statusLabel.setText("Test-drive agendado com sucesso para " + customerName + " (placeholder)!");
        clearForm();
        // --- End Placeholder ---
    }

    @FXML
    void handleCancel(ActionEvent event) {
        clearForm();
        statusLabel.setText("Agendamento cancelado.");
    }

    private void clearForm() {
        availableVehiclesComboBox.getSelectionModel().clearSelection();
        customerNameField.clear();
        customerContactField.clear();
        testDriveDatePicker.setValue(LocalDate.now());
        testDriveTimePicker.getSelectionModel().clearSelection();
        statusLabel.setText("");
    }

    // --- Placeholder Data Loading ---
    private void loadAvailableVehiclesPlaceholder() {
        availableVehicles.clear();
        // Replace with actual call to VehicleService.getAvailableVehicles()
        availableVehicles.add(new Vehicle(1, "Civic", "Honda", 2022, 15000, "Sedan", 120000, "Preto", "CHASSI123", "Available", "2024-01-10"));
        availableVehicles.add(new Vehicle(3, "Corolla", "Toyota", 2021, 25000, "Sedan", 115000, "Prata", "CHASSI456", "Available", "2024-02-15"));
        availableVehicles.add(new Vehicle(5, "HB20", "Hyundai", 2023, 5000, "Hatch", 85000, "Branco", "CHASSI789", "Available", "2024-03-20"));
    }
    // --- End Placeholder ---
}

