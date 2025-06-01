package com.gitcar.app.controllers;

import com.gitcar.app.models.Sale;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.text.NumberFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

public class SalesHistoryController {

    @FXML
    private DatePicker startDatePicker;

    @FXML
    private DatePicker endDatePicker;

    @FXML
    private Button filterButton;

    @FXML
    private Button clearFilterButton;

    @FXML
    private TableView<Sale> salesTableView;

    @FXML
    private TableColumn<Sale, Integer> saleIdCol;

    @FXML
    private TableColumn<Sale, Integer> vehicleIdCol;

    @FXML
    private TableColumn<Sale, String> vehicleModelCol;

    @FXML
    private TableColumn<Sale, String> vehicleBrandCol;

    @FXML
    private TableColumn<Sale, Double> saleValueCol;

    @FXML
    private TableColumn<Sale, String> saleDateCol;

    @FXML
    private TableColumn<Sale, String> customerNameCol;

    @FXML
    private TableColumn<Sale, String> paymentMethodCol;

    @FXML
    private Label statusLabel;

    // Placeholder data - replace with Service/DAO calls
    private ObservableList<Sale> salesHistory = FXCollections.observableArrayList();
    private int currentSalespersonId = 1; // Placeholder - Get this from logged-in user session

    @FXML
    public void initialize() {
        statusLabel.setText("");
        setupTableColumns();
        loadSalesHistoryPlaceholder(null, null); // Load all initially
        salesTableView.setItems(salesHistory);
    }

    private void setupTableColumns() {
        saleIdCol.setCellValueFactory(new PropertyValueFactory<>("saleId"));
        vehicleIdCol.setCellValueFactory(new PropertyValueFactory<>("vehicleId"));
        vehicleModelCol.setCellValueFactory(new PropertyValueFactory<>("vehicleModel")); // Assumes Sale object has this joined data
        vehicleBrandCol.setCellValueFactory(new PropertyValueFactory<>("vehicleBrand")); // Assumes Sale object has this joined data
        saleDateCol.setCellValueFactory(new PropertyValueFactory<>("saleDate"));
        customerNameCol.setCellValueFactory(new PropertyValueFactory<>("customerName")); // Assumes Sale object has this joined data
        paymentMethodCol.setCellValueFactory(new PropertyValueFactory<>("paymentMethod"));

        // Custom formatting for currency
        NumberFormat currencyFormat = NumberFormat.getCurrencyInstance(new Locale("pt", "BR"));
        saleValueCol.setCellValueFactory(cellData -> new SimpleDoubleProperty(cellData.getValue().getSaleValue()).asObject());
        saleValueCol.setCellFactory(column -> new TableCell<Sale, Double>() {
            @Override
            protected void updateItem(Double item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                } else {
                    setText(currencyFormat.format(item));
                }
            }
        });
    }

    @FXML
    void handleFilter(ActionEvent event) {
        LocalDate startDate = startDatePicker.getValue();
        LocalDate endDate = endDatePicker.getValue();

        if (startDate != null && endDate != null && startDate.isAfter(endDate)) {
            statusLabel.setText("Erro: Data de início não pode ser posterior à data de fim.");
            return;
        }

        statusLabel.setText("");
        loadSalesHistoryPlaceholder(startDate, endDate);
    }

    @FXML
    void handleClearFilter(ActionEvent event) {
        startDatePicker.setValue(null);
        endDatePicker.setValue(null);
        statusLabel.setText("");
        loadSalesHistoryPlaceholder(null, null);
    }

    // --- Placeholder Data Loading ---
    private void loadSalesHistoryPlaceholder(LocalDate startDate, LocalDate endDate) {
        salesHistory.clear();
        // Replace with actual call to SaleService.getSalesBySalesperson(currentSalespersonId, startDate, endDate)
        System.out.println("Loading sales history for salesperson ID: " + currentSalespersonId +
                           " between " + (startDate != null ? startDate : "start") +
                           " and " + (endDate != null ? endDate : "end"));

        // Dummy data matching the filter logic (simplified)
        Sale sale1 = new Sale(1, 1, currentSalespersonId, 1, "2025-05-10 10:00:00", 120000.00, "Financiamento");
        sale1.setVehicleModel("Civic"); sale1.setVehicleBrand("Honda"); sale1.setCustomerName("João Silva");

        Sale sale2 = new Sale(2, 3, currentSalespersonId, 2, "2025-05-25 15:30:00", 115000.00, "Cartão Crédito");
        sale2.setVehicleModel("Corolla"); sale2.setVehicleBrand("Toyota"); sale2.setCustomerName("Maria Oliveira");

        LocalDate sale1Date = LocalDate.parse("2025-05-10");
        LocalDate sale2Date = LocalDate.parse("2025-05-25");

        boolean addSale1 = true;
        boolean addSale2 = true;

        if (startDate != null && sale1Date.isBefore(startDate)) addSale1 = false;
        if (endDate != null && sale1Date.isAfter(endDate)) addSale1 = false;
        if (startDate != null && sale2Date.isBefore(startDate)) addSale2 = false;
        if (endDate != null && sale2Date.isAfter(endDate)) addSale2 = false;

        if (addSale1) salesHistory.add(sale1);
        if (addSale2) salesHistory.add(sale2);

        if (salesHistory.isEmpty()) {
            statusLabel.setText("Nenhuma venda encontrada para o período selecionado.");
        } else {
            statusLabel.setText("Exibindo " + salesHistory.size() + " vendas.");
        }
    }
    // --- End Placeholder ---
}

