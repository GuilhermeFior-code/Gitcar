package com.gitcar.app.models;

// Placeholder for Employee model (POJO)
public class Employee {
    private int employeeId;
    private String name;
    private String email;
    private String passwordHash;
    private String role; // "Salesperson" or "Manager"
    private String status; // "Active" or "Inactive"

    // Constructors, Getters, Setters

    public Employee(int employeeId, String name, String email, String passwordHash, String role, String status) {
        this.employeeId = employeeId;
        this.name = name;
        this.email = email;
        this.passwordHash = passwordHash;
        this.role = role;
        this.status = status;
    }

    // Basic constructor for new employees
    public Employee(String name, String email, String passwordHash, String role) {
        this.name = name;
        this.email = email;
        this.passwordHash = passwordHash;
        this.role = role;
        this.status = "Active"; // Default status
    }

    public int getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(int employeeId) {
        this.employeeId = employeeId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPasswordHash() {
        return passwordHash;
    }

    public void setPasswordHash(String passwordHash) {
        this.passwordHash = passwordHash;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return name + " (" + email + ")"; // Useful for ComboBoxes if needed
    }
}

