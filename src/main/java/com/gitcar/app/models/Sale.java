package com.gitcar.app.models;

// Placeholder for Sale model (POJO)
public class Sale {
    private int saleId;
    private int vehicleId;
    private int employeeId;
    private int customerId;
    private String saleDate; // Store as ISO 8601 String (e.g., "YYYY-MM-DD HH:MM:SS") or use LocalDateTime
    private double saleValue;
    private String paymentMethod;

    // Optional: Include joined data for display purposes (e.g., in reports)
    private String vehicleModel;
    private String vehicleBrand;
    private String employeeName;
    private String customerName;

    // Constructors, Getters, Setters

    // Constructor for creating new sales record
    public Sale(int vehicleId, int employeeId, int customerId, double saleValue, String paymentMethod) {
        this.vehicleId = vehicleId;
        this.employeeId = employeeId;
        this.customerId = customerId;
        this.saleValue = saleValue;
        this.paymentMethod = paymentMethod;
        // saleDate can be set by DB default or in service layer
    }

    // Full constructor (e.g., when reading from DB)
    public Sale(int saleId, int vehicleId, int employeeId, int customerId, String saleDate, double saleValue, String paymentMethod) {
        this.saleId = saleId;
        this.vehicleId = vehicleId;
        this.employeeId = employeeId;
        this.customerId = customerId;
        this.saleDate = saleDate;
        this.saleValue = saleValue;
        this.paymentMethod = paymentMethod;
    }

    // Getters and Setters for all fields...

    public int getSaleId() {
        return saleId;
    }

    public void setSaleId(int saleId) {
        this.saleId = saleId;
    }

    public int getVehicleId() {
        return vehicleId;
    }

    public void setVehicleId(int vehicleId) {
        this.vehicleId = vehicleId;
    }

    public int getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(int employeeId) {
        this.employeeId = employeeId;
    }

    public int getCustomerId() {
        return customerId;
    }

    public void setCustomerId(int customerId) {
        this.customerId = customerId;
    }

    public String getSaleDate() {
        return saleDate;
    }

    public void setSaleDate(String saleDate) {
        this.saleDate = saleDate;
    }

    public double getSaleValue() {
        return saleValue;
    }

    public void setSaleValue(double saleValue) {
        this.saleValue = saleValue;
    }

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    // Getters/Setters for optional joined data
    public String getVehicleModel() { return vehicleModel; }
    public void setVehicleModel(String vehicleModel) { this.vehicleModel = vehicleModel; }
    public String getVehicleBrand() { return vehicleBrand; }
    public void setVehicleBrand(String vehicleBrand) { this.vehicleBrand = vehicleBrand; }
    public String getEmployeeName() { return employeeName; }
    public void setEmployeeName(String employeeName) { this.employeeName = employeeName; }
    public String getCustomerName() { return customerName; }
    public void setCustomerName(String customerName) { this.customerName = customerName; }
}

