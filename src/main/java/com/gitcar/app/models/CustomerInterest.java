package com.gitcar.app.models;

// POJO for the Collections-Only Screen (Manage Customer Interests)
// Data in this object is NOT persisted to the database.
public class CustomerInterest {
    private int id; // Simple in-memory ID
    private String customerName;
    private String contactInfo;
    private String vehicleInterest; // e.g., "Honda Civic 2023", "SUV Médio"
    private String notes;

    // Constructor for new interests
    public CustomerInterest(int id, String customerName, String contactInfo, String vehicleInterest, String notes) {
        this.id = id;
        this.customerName = customerName;
        this.contactInfo = contactInfo;
        this.vehicleInterest = vehicleInterest;
        this.notes = notes;
    }

    // Getters and Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getContactInfo() {
        return contactInfo;
    }

    public void setContactInfo(String contactInfo) {
        this.contactInfo = contactInfo;
    }

    public String getVehicleInterest() {
        return vehicleInterest;
    }

    public void setVehicleInterest(String vehicleInterest) {
        this.vehicleInterest = vehicleInterest;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    @Override
    public String toString() {
        return "Interest [id=" + id + ", customerName=" + customerName + ", vehicleInterest=" + vehicleInterest + "]";
    }
}

