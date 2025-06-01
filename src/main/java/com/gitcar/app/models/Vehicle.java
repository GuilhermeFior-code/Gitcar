package com.gitcar.app.models;

// Placeholder for Vehicle model (POJO)
public class Vehicle {
    private int vehicleId;
    private String model;
    private String brand;
    private int year;
    private int mileage;
    private String category;
    private double price;
    private String color;
    private String chassisNumber;
    private String status; // "Available", "Sold", "Reserved"
    private String dateRegistered;

    // Constructors, Getters, Setters

    // Full constructor
    public Vehicle(int vehicleId, String model, String brand, int year, int mileage, String category, double price, String color, String chassisNumber, String status, String dateRegistered) {
        this.vehicleId = vehicleId;
        this.model = model;
        this.brand = brand;
        this.year = year;
        this.mileage = mileage;
        this.category = category;
        this.price = price;
        this.color = color;
        this.chassisNumber = chassisNumber;
        this.status = status;
        this.dateRegistered = dateRegistered;
    }

    // Constructor for new vehicles (ID and dateRegistered are often auto-generated)
    public Vehicle(String model, String brand, int year, int mileage, String category, double price, String color, String chassisNumber) {
        this.model = model;
        this.brand = brand;
        this.year = year;
        this.mileage = mileage;
        this.category = category;
        this.price = price;
        this.color = color;
        this.chassisNumber = chassisNumber;
        this.status = "Available"; // Default status
    }

    // Getters and Setters for all fields...

    public int getVehicleId() {
        return vehicleId;
    }

    public void setVehicleId(int vehicleId) {
        this.vehicleId = vehicleId;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public int getMileage() {
        return mileage;
    }

    public void setMileage(int mileage) {
        this.mileage = mileage;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getChassisNumber() {
        return chassisNumber;
    }

    public void setChassisNumber(String chassisNumber) {
        this.chassisNumber = chassisNumber;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getDateRegistered() {
        return dateRegistered;
    }

    public void setDateRegistered(String dateRegistered) {
        this.dateRegistered = dateRegistered;
    }

    @Override
    public String toString() {
        // Useful for ComboBox display
        return vehicleId + " - " + brand + " " + model + " (" + year + ")";
    }
}

