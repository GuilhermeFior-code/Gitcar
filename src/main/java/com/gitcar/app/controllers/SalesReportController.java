package com.gitcar.app.controllers;

import com.gitcar.app.models.Employee;
import com.gitcar.app.models.Sale;
import com.gitcar.app.models.Vehicle;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.util.StringConverter; // Added missing import

import java.text.NumberFormat;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.stream.Collectors;

public class SalesReportController {

    //<editor-fold desc="FXML Fields">
    @FXML private DatePicker startDatePicker;
    @FXML private DatePicker endDatePicker;
    @FXML private ComboBox<Employee> salespersonFilterComboBox;
    @FXML private ComboBox<String> vehicleStatusFilterComboBox;
    @FXML private TextField modelBrandFilterField;
    @FXML private Button applyFiltersButton;
    @FXML private Button clearFiltersButton;
    @FXML private Button exportReportButton;

    @FXML private TableView<Sale> soldVehiclesTableView;
    @FXML private TableColumn<Sale, Integer> soldVehicleIdCol;
    @FXML private TableColumn<Sale, String> soldModelCol;
    @FXML private TableColumn<Sale, String> soldBrandCol;
    @FXML private TableColumn<Sale, Integer> soldYearCol;
    @FXML private TableColumn<Sale, Double> soldValueCol;
    @FXML private TableColumn<Sale, String> soldDateCol;
    @FXML private TableColumn<Sale, String> soldSalespersonCol;
    @FXML private TableColumn<Sale, String> soldCustomerCol;
    @FXML private TableColumn<Sale, String> soldPaymentCol;

    @FXML private TableView<Vehicle> availableVehiclesTableView;
    @FXML private TableColumn<Vehicle, Integer> availVehicleIdCol;
    @FXML private TableColumn<Vehicle, String> availModelCol;
    @FXML private TableColumn<Vehicle, String> availBrandCol;
    @FXML private TableColumn<Vehicle, Integer> availYearCol;
    @FXML private TableColumn<Vehicle, Double> availPriceCol;
    @FXML private TableColumn<Vehicle, String> availStatusCol;
    @FXML private TableColumn<Vehicle, String> availDateRegCol;
    @FXML private TableColumn<Vehicle, Integer> availMileageCol;
    @FXML private TableColumn<Vehicle, String> availCategoryCol;

    @FXML private Label totalSoldLabel;
    @FXML private Label totalSalesValueLabel;
    @FXML private Label totalAvailableLabel;
    @FXML private Label topSellersLabel;
    @FXML private Label topModelLabel;
    @FXML private Label statusLabel;
    //</editor-fold>

    // Placeholder data - replace with Service/DAO calls
    private ObservableList<Sale> allSales = FXCollections.observableArrayList();
    private ObservableList<Vehicle> allVehicles = FXCollections.observableArrayList();
    private ObservableList<Employee> allEmployees = FXCollections.observableArrayList();

    private final NumberFormat currencyFormat = NumberFormat.getCurrencyInstance(new Locale("pt", "BR"));

    @FXML
    public void initialize() {
        statusLabel.setText("");
        setupTableColumns();
        setupFilterComboBoxes();
        loadInitialDataPlaceholder(); // Load all data initially
        applyFilters(); // Apply default filters (all data)
    }

    private void setupTableColumns() {
        // Sold Vehicles Table
        soldVehicleIdCol.setCellValueFactory(cellData -> new SimpleIntegerProperty(cellData.getValue().getVehicleId()).asObject());
        soldModelCol.setCellValueFactory(new PropertyValueFactory<>("vehicleModel"));
        soldBrandCol.setCellValueFactory(new PropertyValueFactory<>("vehicleBrand"));
        soldYearCol.setCellValueFactory(cellData -> {
            // Need to fetch year from vehicle if not directly in Sale model
            Vehicle v = findVehicleById(cellData.getValue().getVehicleId());
            return new SimpleIntegerProperty(v != null ? v.getYear() : 0).asObject();
        });
        soldValueCol.setCellValueFactory(new PropertyValueFactory<>("saleValue"));
        soldDateCol.setCellValueFactory(new PropertyValueFactory<>("saleDate"));
        soldSalespersonCol.setCellValueFactory(new PropertyValueFactory<>("employeeName"));
        soldCustomerCol.setCellValueFactory(new PropertyValueFactory<>("customerName"));
        soldPaymentCol.setCellValueFactory(new PropertyValueFactory<>("paymentMethod"));
        configureCurrencyColumn(soldValueCol); // Corrected call

        // Available Vehicles Table
        availVehicleIdCol.setCellValueFactory(new PropertyValueFactory<>("vehicleId"));
        availModelCol.setCellValueFactory(new PropertyValueFactory<>("model"));
        availBrandCol.setCellValueFactory(new PropertyValueFactory<>("brand"));
        availYearCol.setCellValueFactory(new PropertyValueFactory<>("year"));
        availPriceCol.setCellValueFactory(new PropertyValueFactory<>("price"));
        availStatusCol.setCellValueFactory(new PropertyValueFactory<>("status"));
        availDateRegCol.setCellValueFactory(new PropertyValueFactory<>("dateRegistered"));
        availMileageCol.setCellValueFactory(new PropertyValueFactory<>("mileage"));
        availCategoryCol.setCellValueFactory(new PropertyValueFactory<>("category"));
        configureCurrencyColumn(availPriceCol); // Corrected call
    }

    // Changed to generic method to handle different TableColumn types
    private <T> void configureCurrencyColumn(TableColumn<T, Double> column) {
        column.setCellFactory(tc -> new TableCell<T, Double>() {
            @Override
            protected void updateItem(Double price, boolean empty) {
                super.updateItem(price, empty);
                if (empty || price == null) {
                    setText(null);
                } else {
                    setText(currencyFormat.format(price));
                }
            }
        });
    }

    private void setupFilterComboBoxes() {
        // Salesperson Filter
        salespersonFilterComboBox.setConverter(new StringConverter<Employee>() {
            @Override public String toString(Employee e) { return e == null ? "Todos" : e.getName(); }
            @Override public Employee fromString(String s) { return null; }
        });
        // Add "Todos" option represented by null
        ObservableList<Employee> employeeOptions = FXCollections.observableArrayList();
        employeeOptions.add(null); // Represents "Todos"
        employeeOptions.addAll(allEmployees);
        salespersonFilterComboBox.setItems(employeeOptions);
        salespersonFilterComboBox.getSelectionModel().selectFirst(); // Default to "Todos"

        // Vehicle Status Filter
        vehicleStatusFilterComboBox.setItems(FXCollections.observableArrayList("Todos", "Disponível", "Vendido", "Reservado"));
        vehicleStatusFilterComboBox.getSelectionModel().selectFirst(); // Default to "Todos"
    }

    @FXML
    void handleApplyFilters(ActionEvent event) {
        applyFilters();
    }

    @FXML
    void handleClearFilters(ActionEvent event) {
        startDatePicker.setValue(null);
        endDatePicker.setValue(null);
        salespersonFilterComboBox.getSelectionModel().selectFirst();
        vehicleStatusFilterComboBox.getSelectionModel().selectFirst();
        modelBrandFilterField.clear();
        applyFilters();
        statusLabel.setText("Filtros limpos.");
    }

    @FXML
    void handleExportReport(ActionEvent event) {
        // TODO: Implement report export functionality (e.g., to CSV or PDF)
        statusLabel.setText("Funcionalidade de exportação ainda não implementada.");
    }

    private void applyFilters() {
        LocalDate startDate = startDatePicker.getValue();
        LocalDate endDate = endDatePicker.getValue();
        Employee selectedSalesperson = salespersonFilterComboBox.getValue(); // Can be null for "Todos"
        String selectedStatus = vehicleStatusFilterComboBox.getValue();
        String modelBrandTerm = modelBrandFilterField.getText().trim().toLowerCase();

        // --- Placeholder Filtering Logic ---
        // Replace with efficient Service/DAO calls using criteria
        System.out.println("Applying filters...");

        // Filter Sales
        List<Sale> filteredSales = allSales.stream()
                .filter(sale -> startDate == null || !LocalDate.parse(sale.getSaleDate().substring(0, 10)).isBefore(startDate))
                .filter(sale -> endDate == null || !LocalDate.parse(sale.getSaleDate().substring(0, 10)).isAfter(endDate))
                .filter(sale -> selectedSalesperson == null || sale.getEmployeeId() == selectedSalesperson.getEmployeeId())
                .filter(sale -> {
                    Vehicle v = findVehicleById(sale.getVehicleId());
                    return v != null && (modelBrandTerm.isEmpty() ||
                           v.getModel().toLowerCase().contains(modelBrandTerm) ||
                           v.getBrand().toLowerCase().contains(modelBrandTerm));
                })
                .collect(Collectors.toList());
        soldVehiclesTableView.setItems(FXCollections.observableArrayList(filteredSales));

        // Filter Available/All Vehicles (based on status filter)
        List<Vehicle> filteredVehicles = allVehicles.stream()
                .filter(vehicle -> "Todos".equals(selectedStatus) || vehicle.getStatus().equalsIgnoreCase(selectedStatus))
                .filter(vehicle -> modelBrandTerm.isEmpty() ||
                       vehicle.getModel().toLowerCase().contains(modelBrandTerm) ||
                       vehicle.getBrand().toLowerCase().contains(modelBrandTerm))
                .collect(Collectors.toList());
        availableVehiclesTableView.setItems(FXCollections.observableArrayList(filteredVehicles));

        updateStatistics(filteredSales, filteredVehicles);
        statusLabel.setText("Relatório atualizado.");
        // --- End Placeholder ---
    }

    private void updateStatistics(List<Sale> currentSales, List<Vehicle> currentVehicles) {
        // Total Sold
        totalSoldLabel.setText("Total vendidos: " + currentSales.size());

        // Total Sales Value
        double totalValue = currentSales.stream().mapToDouble(Sale::getSaleValue).sum();
        totalSalesValueLabel.setText("Valor total vendas: " + currencyFormat.format(totalValue));

        // Total Available Vehicles (count from the filtered vehicle list where status is Available or Reserved)
        long availableCount = currentVehicles.stream()
                                .filter(v -> "Available".equalsIgnoreCase(v.getStatus()) || "Reserved".equalsIgnoreCase(v.getStatus()))
                                .count();
        totalAvailableLabel.setText("Veículos disponíveis/reservados: " + availableCount);

        // Top Sellers (Placeholder logic)
        Map<String, Long> salesByEmployee = currentSales.stream()
                .collect(Collectors.groupingBy(Sale::getEmployeeName, Collectors.counting()));
        String topSellersText = salesByEmployee.entrySet().stream()
                .sorted(Map.Entry.<String, Long>comparingByValue().reversed())
                .limit(3)
                .map(entry -> entry.getKey() + " (" + entry.getValue() + ")")
                .collect(Collectors.joining("\n"));
        topSellersLabel.setText(topSellersText.isEmpty() ? "N/A" : topSellersText);

        // Top Model (Placeholder logic)
        Map<String, Long> salesByModel = currentSales.stream()
                .collect(Collectors.groupingBy(s -> findVehicleById(s.getVehicleId()) != null ? findVehicleById(s.getVehicleId()).getModel() : "N/A", Collectors.counting()));
        String topModelText = salesByModel.entrySet().stream()
                .filter(entry -> !"N/A".equals(entry.getKey()))
                .sorted(Map.Entry.<String, Long>comparingByValue().reversed())
                .limit(1)
                .map(entry -> entry.getKey() + " (" + entry.getValue() + ")")
                .findFirst().orElse("N/A");
        topModelLabel.setText(topModelText);
    }

    // Helper to find vehicle (replace with efficient map or service call)
    private Vehicle findVehicleById(int id) {
        return allVehicles.stream().filter(v -> v.getVehicleId() == id).findFirst().orElse(null);
    }

    // --- Placeholder Data Loading ---
    private void loadInitialDataPlaceholder() {
        allEmployees.clear();
        allVehicles.clear();
        allSales.clear();

        // Employees
        Employee emp1 = new Employee(1, "Carlos Silva", "carlos@gitcar.com", "", "Salesperson", "Active");
        Employee emp2 = new Employee(2, "Ana Pereira", "ana@gitcar.com", "", "Salesperson", "Active");
        Employee emp3 = new Employee(10, "Gerente Master", "manager@gitcar.com", "", "Manager", "Active");
        allEmployees.addAll(emp1, emp2, emp3);

        // Vehicles
        Vehicle v1 = new Vehicle(1, "Civic", "Honda", 2022, 15000, "Sedan", 120000, "Preto", "CHASSI123", "Sold", "2024-01-10");
        Vehicle v2 = new Vehicle(2, "Onix", "Chevrolet", 2023, 5000, "Hatch", 88000, "Vermelho", "CHASSIABC", "Available", "2024-01-20");
        Vehicle v3 = new Vehicle(3, "Corolla", "Toyota", 2021, 25000, "Sedan", 115000, "Prata", "CHASSI456", "Sold", "2024-02-15");
        Vehicle v4 = new Vehicle(4, "Kwid", "Renault", 2024, 100, "Compact", 72000, "Azul", "CHASSIDEF", "Available", "2024-03-01");
        Vehicle v5 = new Vehicle(5, "HB20", "Hyundai", 2023, 5000, "Hatch", 85000, "Branco", "CHASSI789", "Reserved", "2024-03-20");
        allVehicles.addAll(v1, v2, v3, v4, v5);

        // Sales
        Sale s1 = new Sale(1, v1.getVehicleId(), emp1.getEmployeeId(), 1, "2025-05-10 10:00:00", 120000.00, "Financiamento");
        s1.setVehicleModel(v1.getModel()); s1.setVehicleBrand(v1.getBrand()); s1.setCustomerName("João Silva"); s1.setEmployeeName(emp1.getName());
        Sale s2 = new Sale(2, v3.getVehicleId(), emp2.getEmployeeId(), 2, "2025-05-25 15:30:00", 115000.00, "Cartão Crédito");
        s2.setVehicleModel(v3.getModel()); s2.setVehicleBrand(v3.getBrand()); s2.setCustomerName("Maria Oliveira"); s2.setEmployeeName(emp2.getName());
        allSales.addAll(s1, s2);

        // Refresh ComboBox items after loading employees
        ObservableList<Employee> employeeOptions = FXCollections.observableArrayList();
        employeeOptions.add(null); // "Todos"
        employeeOptions.addAll(allEmployees);
        salespersonFilterComboBox.setItems(employeeOptions);
        salespersonFilterComboBox.getSelectionModel().selectFirst();
    }
    // --- End Placeholder ---
}

