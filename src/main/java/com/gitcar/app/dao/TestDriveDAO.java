package com.gitcar.app.dao;

import com.gitcar.app.models.TestDrive;
import com.gitcar.app.utils.DatabaseUtil;

import java.sql.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class TestDriveDAO {

    private static final DateTimeFormatter ISO_FORMATTER = DateTimeFormatter.ISO_LOCAL_DATE_TIME;

    // Method to map ResultSet to TestDrive object
    private TestDrive mapResultSetToTestDrive(ResultSet rs) throws SQLException {
        return new TestDrive(
                rs.getInt("test_drive_id"),
                rs.getInt("vehicle_id"),
                rs.getInt("employee_id"),
                rs.getString("customer_name"),
                rs.getString("customer_contact"),
                rs.getString("test_drive_datetime"),
                rs.getString("status")
        );
    }

    public TestDrive addTestDrive(TestDrive testDrive) {
        String sql = "INSERT INTO test_drives(vehicle_id, employee_id, customer_name, customer_contact, test_drive_datetime, status) VALUES(?,?,?,?,?,?)";
        String dateTimeString = testDrive.getTestDriveDatetime(); // Corrected method name

        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            pstmt.setInt(1, testDrive.getVehicleId());
            pstmt.setInt(2, testDrive.getEmployeeId());
            pstmt.setString(3, testDrive.getCustomerName());
            pstmt.setString(4, testDrive.getCustomerContact());
            pstmt.setString(5, dateTimeString);
            pstmt.setString(6, testDrive.getStatus() != null ? testDrive.getStatus() : "Scheduled"); // Default

            int affectedRows = pstmt.executeUpdate();

            if (affectedRows > 0) {
                try (ResultSet generatedKeys = pstmt.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        testDrive.setTestDriveId(generatedKeys.getInt(1));
                        return testDrive;
                    }
                }
            }
        } catch (SQLException e) {
            System.err.println("Database error adding test drive: " + e.getMessage());
            e.printStackTrace();
        }
        return null; // Indicate failure
    }

    public List<TestDrive> findAllScheduled() {
        List<TestDrive> testDrives = new ArrayList<>();
        String sql = "SELECT * FROM test_drives WHERE status = ? ORDER BY test_drive_datetime";
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, "Scheduled");
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                testDrives.add(mapResultSetToTestDrive(rs));
            }
        } catch (SQLException e) {
            System.err.println("Database error finding scheduled test drives: " + e.getMessage());
            e.printStackTrace();
        }
        return testDrives;
    }

    public boolean updateTestDriveStatus(int testDriveId, String status) {
        String sql = "UPDATE test_drives SET status = ? WHERE test_drive_id = ?";
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, status);
            pstmt.setInt(2, testDriveId);
            int affectedRows = pstmt.executeUpdate();
            return affectedRows > 0;

        } catch (SQLException e) {
            System.err.println("Database error updating test drive status: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    // Find by ID, Find All, etc. can be added if needed

}

