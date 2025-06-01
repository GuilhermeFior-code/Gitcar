package com.gitcar.app.dao;

import com.gitcar.app.models.Vehicle;
import com.gitcar.app.utils.DatabaseUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class VehicleDAO {

    // Method to map ResultSet to Vehicle object
    private Vehicle mapResultSetToVehicle(ResultSet rs) throws SQLException {
        return new Vehicle(
                rs.getInt("vehicle_id"),
                rs.getString("model"),
                rs.getString("brand"),
                rs.getInt("year"),
                rs.getInt("mileage"),
                rs.getString("category"),
                rs.getDouble("price"),
                rs.getString("color"),
                rs.getString("chassis_number"),
                rs.getString("status"),
                rs.getString("date_registered")
        );
    }

    public Vehicle findById(int id) {
        String sql = "SELECT * FROM vehicles WHERE vehicle_id = ?";
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, id);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                return mapResultSetToVehicle(rs);
            }
        } catch (SQLException e) {
            System.err.println("Database error finding vehicle by ID: " + e.getMessage());
            e.printStackTrace();
        }
        return null;
    }

    public List<Vehicle> findAll() {
        List<Vehicle> vehicles = new ArrayList<>();
        String sql = "SELECT * FROM vehicles ORDER BY brand, model";
        try (Connection conn = DatabaseUtil.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                vehicles.add(mapResultSetToVehicle(rs));
            }
        } catch (SQLException e) {
            System.err.println("Database error finding all vehicles: " + e.getMessage());
            e.printStackTrace();
        }
        return vehicles;
    }

    public List<Vehicle> findByStatus(String status) {
        List<Vehicle> vehicles = new ArrayList<>();
        String sql = "SELECT * FROM vehicles WHERE status = ? ORDER BY brand, model";
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, status);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                vehicles.add(mapResultSetToVehicle(rs));
            }
        } catch (SQLException e) {
            System.err.println("Database error finding vehicles by status: " + e.getMessage());
            e.printStackTrace();
        }
        return vehicles;
    }

     public List<Vehicle> findAvailableByTerm(String term) {
        List<Vehicle> vehicles = new ArrayList<>();
        // Search by ID (if numeric), model, brand, or chassis number
        String sql = "SELECT * FROM vehicles WHERE status = 'Available' AND (" +
                     " CAST(vehicle_id AS TEXT) LIKE ? OR " +
                     " model LIKE ? OR " +
                     " brand LIKE ? OR " +
                     " chassis_number LIKE ?)" +
                     " ORDER BY brand, model";

        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            String likeTerm = "%" + term + "%";
            pstmt.setString(1, likeTerm);
            pstmt.setString(2, likeTerm);
            pstmt.setString(3, likeTerm);
            pstmt.setString(4, likeTerm);

            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                vehicles.add(mapResultSetToVehicle(rs));
            }
        } catch (SQLException e) {
            System.err.println("Database error searching available vehicles: " + e.getMessage());
            e.printStackTrace();
        }
        return vehicles;
    }

    public Vehicle addVehicle(Vehicle vehicle) {
        String sql = "INSERT INTO vehicles(model, brand, year, mileage, category, price, color, chassis_number, status) VALUES(?,?,?,?,?,?,?,?,?)";
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            pstmt.setString(1, vehicle.getModel());
            pstmt.setString(2, vehicle.getBrand());
            pstmt.setInt(3, vehicle.getYear());
            pstmt.setInt(4, vehicle.getMileage());
            pstmt.setString(5, vehicle.getCategory());
            pstmt.setDouble(6, vehicle.getPrice());
            pstmt.setString(7, vehicle.getColor());
            pstmt.setString(8, vehicle.getChassisNumber());
            pstmt.setString(9, vehicle.getStatus() != null ? vehicle.getStatus() : "Available"); // Default if null

            int affectedRows = pstmt.executeUpdate();

            if (affectedRows > 0) {
                try (ResultSet generatedKeys = pstmt.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        vehicle.setVehicleId(generatedKeys.getInt(1));
                        // Optionally retrieve and set the date_registered if needed
                        return vehicle;
                    }
                }
            }
        } catch (SQLException e) {
            System.err.println("Database error adding vehicle: " + e.getMessage());
            // Consider throwing custom exception for duplicate chassis number
            e.printStackTrace();
        }
        return null; // Indicate failure
    }

    public boolean updateVehicle(Vehicle vehicle) {
        String sql = "UPDATE vehicles SET model = ?, brand = ?, year = ?, mileage = ?, category = ?, price = ?, color = ?, chassis_number = ?, status = ? WHERE vehicle_id = ?";
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, vehicle.getModel());
            pstmt.setString(2, vehicle.getBrand());
            pstmt.setInt(3, vehicle.getYear());
            pstmt.setInt(4, vehicle.getMileage());
            pstmt.setString(5, vehicle.getCategory());
            pstmt.setDouble(6, vehicle.getPrice());
            pstmt.setString(7, vehicle.getColor());
            pstmt.setString(8, vehicle.getChassisNumber());
            pstmt.setString(9, vehicle.getStatus());
            pstmt.setInt(10, vehicle.getVehicleId());

            int affectedRows = pstmt.executeUpdate();
            return affectedRows > 0;

        } catch (SQLException e) {
            System.err.println("Database error updating vehicle: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

     public boolean updateVehicleStatus(int vehicleId, String status) {
        String sql = "UPDATE vehicles SET status = ? WHERE vehicle_id = ?";
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, status);
            pstmt.setInt(2, vehicleId);
            int affectedRows = pstmt.executeUpdate();
            return affectedRows > 0;

        } catch (SQLException e) {
            System.err.println("Database error updating vehicle status: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    // Delete might be restricted by FK constraints if sales/test drives exist.
    // Consider changing status to 'Removed' or similar instead of hard delete.
    public boolean deleteVehicle(int id) {
        String sql = "DELETE FROM vehicles WHERE vehicle_id = ?";
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, id);
            int affectedRows = pstmt.executeUpdate();
            return affectedRows > 0;

        } catch (SQLException e) {
            System.err.println("Database error deleting vehicle (might be referenced in sales/test drives): " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
}

