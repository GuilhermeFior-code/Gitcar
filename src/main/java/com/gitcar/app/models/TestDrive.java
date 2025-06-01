package com.gitcar.app.models;

// Placeholder for TestDrive model (POJO)
public class TestDrive {
    private int testDriveId;
    private int vehicleId;
    private int employeeId;
    private String customerName;
    private String customerContact;
    private String testDriveDatetime; // Store as ISO 8601 String or use LocalDateTime
    private String status; // "Scheduled", "Completed", "Cancelled"

    // Optional: Include joined data for display
    private String vehicleInfo; // e.g., "Brand Model (Year)"
    private String employeeName;

    // Constructors, Getters, Setters

    // Constructor for new test drives
    public TestDrive(int vehicleId, int employeeId, String customerName, String customerContact, String testDriveDatetime) {
        this.vehicleId = vehicleId;
        this.employeeId = employeeId;
        this.customerName = customerName;
        this.customerContact = customerContact;
        this.testDriveDatetime = testDriveDatetime;
        this.status = "Scheduled"; // Default status
    }

    // Full constructor (e.g., when reading from DB)
    public TestDrive(int testDriveId, int vehicleId, int employeeId, String customerName, String customerContact, String testDriveDatetime, String status) {
        this.testDriveId = testDriveId;
        this.vehicleId = vehicleId;
        this.employeeId = employeeId;
        this.customerName = customerName;
        this.customerContact = customerContact;
        this.testDriveDatetime = testDriveDatetime;
        this.status = status;
    }

    // Getters and Setters for all fields...

    public int getTestDriveId() {
        return testDriveId;
    }

    public void setTestDriveId(int testDriveId) {
        this.testDriveId = testDriveId;
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

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getCustomerContact() {
        return customerContact;
    }

    public void setCustomerContact(String customerContact) {
        this.customerContact = customerContact;
    }

    public String getTestDriveDatetime() {
        return testDriveDatetime;
    }

    public void setTestDriveDatetime(String testDriveDatetime) {
        this.testDriveDatetime = testDriveDatetime;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    // Getters/Setters for optional joined data
    public String getVehicleInfo() { return vehicleInfo; }
    public void setVehicleInfo(String vehicleInfo) { this.vehicleInfo = vehicleInfo; }
    public String getEmployeeName() { return employeeName; }
    public void setEmployeeName(String employeeName) { this.employeeName = employeeName; }
}

