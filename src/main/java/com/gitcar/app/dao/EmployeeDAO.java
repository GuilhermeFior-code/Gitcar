package com.gitcar.app.dao;

import com.gitcar.app.models.Employee;
import com.gitcar.app.utils.DatabaseUtil;
import com.gitcar.app.utils.PasswordUtils;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class EmployeeDAO {

    // Method to map ResultSet to Employee object
    private Employee mapResultSetToEmployee(ResultSet rs) throws SQLException {
        return new Employee(
                rs.getInt("employee_id"),
                rs.getString("name"),
                rs.getString("email"),
                rs.getString("password_hash"),
                rs.getString("role"),
                rs.getString("status")
        );
    }

    public Employee findByEmail(String email) {
        String sql = "SELECT * FROM employees WHERE email = ?";
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, email);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                return mapResultSetToEmployee(rs);
            }
        } catch (SQLException e) {
            System.err.println("Database error finding employee by email: " + e.getMessage());
            e.printStackTrace();
        }
        return null;
    }

    public Employee findById(int id) {
        String sql = "SELECT * FROM employees WHERE employee_id = ?";
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, id);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                return mapResultSetToEmployee(rs);
            }
        } catch (SQLException e) {
            System.err.println("Database error finding employee by ID: " + e.getMessage());
            e.printStackTrace();
        }
        return null;
    }

    public List<Employee> findAll() {
        List<Employee> employees = new ArrayList<>();
        String sql = "SELECT * FROM employees ORDER BY name";
        try (Connection conn = DatabaseUtil.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                employees.add(mapResultSetToEmployee(rs));
            }
        } catch (SQLException e) {
            System.err.println("Database error finding all employees: " + e.getMessage());
            e.printStackTrace();
        }
        return employees;
    }

    public Employee addEmployee(Employee employee) {
        String sql = "INSERT INTO employees(name, email, password_hash, role, status) VALUES(?,?,?,?,?)";
        // Hash the password before saving
        String hashedPassword = PasswordUtils.hashPassword(employee.getPasswordHash()); // Assuming getPasswordHash temporarily holds plain text

        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            pstmt.setString(1, employee.getName());
            pstmt.setString(2, employee.getEmail());
            pstmt.setString(3, hashedPassword);
            pstmt.setString(4, employee.getRole());
            pstmt.setString(5, employee.getStatus());

            int affectedRows = pstmt.executeUpdate();

            if (affectedRows > 0) {
                try (ResultSet generatedKeys = pstmt.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        employee.setEmployeeId(generatedKeys.getInt(1));
                        employee.setPasswordHash(hashedPassword); // Update model with the actual hash
                        return employee;
                    }
                }
            }
        } catch (SQLException e) {
            System.err.println("Database error adding employee: " + e.getMessage());
            // Consider throwing custom exception for duplicate email
            e.printStackTrace();
        }
        return null; // Indicate failure
    }

    public boolean updateEmployee(Employee employee) {
        // Decide if password needs update
        boolean updatePassword = employee.getPasswordHash() != null && !employee.getPasswordHash().startsWith("$2a$"); // Simple check if it looks like plain text

        String sqlBase = "UPDATE employees SET name = ?, email = ?, role = ?, status = ?";
        String sqlPassword = ", password_hash = ?";
        String sqlEnd = " WHERE employee_id = ?";
        String sql = sqlBase + (updatePassword ? sqlPassword : "") + sqlEnd;

        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, employee.getName());
            pstmt.setString(2, employee.getEmail());
            pstmt.setString(3, employee.getRole());
            pstmt.setString(4, employee.getStatus());

            int parameterIndex = 5;
            if (updatePassword) {
                String hashedPassword = PasswordUtils.hashPassword(employee.getPasswordHash());
                pstmt.setString(parameterIndex++, hashedPassword);
            }
            pstmt.setInt(parameterIndex, employee.getEmployeeId());

            int affectedRows = pstmt.executeUpdate();
            return affectedRows > 0;

        } catch (SQLException e) {
            System.err.println("Database error updating employee: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    public boolean updateEmployeeStatus(int employeeId, String status) {
        String sql = "UPDATE employees SET status = ? WHERE employee_id = ?";
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, status);
            pstmt.setInt(2, employeeId);
            int affectedRows = pstmt.executeUpdate();
            return affectedRows > 0;

        } catch (SQLException e) {
            System.err.println("Database error updating employee status: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    // Delete is usually discouraged for employees due to FK constraints. Deactivate instead.
    // public boolean deleteEmployee(int id) { ... }
}

