package com.gitcar.app.dao;

import com.gitcar.app.models.Sale;
import com.gitcar.app.utils.DatabaseUtil;

import java.sql.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class SaleDAO {

    // Method to map ResultSet to Sale object
    // Note: This basic mapper doesn't include joined data like names/models.
    // A more complex query/mapper would be needed for reports.
    private Sale mapResultSetToSale(ResultSet rs) throws SQLException {
        return new Sale(
                rs.getInt("sale_id"),
                rs.getInt("vehicle_id"),
                rs.getInt("employee_id"),
                rs.getInt("customer_id"),
                rs.getString("sale_date"),
                rs.getDouble("sale_value"),
                rs.getString("payment_method")
        );
    }

    public Sale addSale(Sale sale) {
        String sql = "INSERT INTO sales(vehicle_id, employee_id, customer_id, sale_value, payment_method, sale_date) VALUES(?,?,?,?,?,?)";
        // Use current timestamp if sale_date is not provided in the model
        String saleDate = (sale.getSaleDate() != null) ? sale.getSaleDate() :
                          java.time.LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME);

        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            pstmt.setInt(1, sale.getVehicleId());
            pstmt.setInt(2, sale.getEmployeeId());
            pstmt.setInt(3, sale.getCustomerId());
            pstmt.setDouble(4, sale.getSaleValue());
            pstmt.setString(5, sale.getPaymentMethod());
            pstmt.setString(6, saleDate);

            int affectedRows = pstmt.executeUpdate();

            if (affectedRows > 0) {
                try (ResultSet generatedKeys = pstmt.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        sale.setSaleId(generatedKeys.getInt(1));
                        sale.setSaleDate(saleDate); // Ensure model has the date used
                        return sale;
                    }
                }
            }
        } catch (SQLException e) {
            System.err.println("Database error adding sale: " + e.getMessage());
            e.printStackTrace();
        }
        return null; // Indicate failure
    }

    public List<Sale> findSalesBySalesperson(int employeeId, LocalDate startDate, LocalDate endDate) {
        List<Sale> sales = new ArrayList<>();
        StringBuilder sqlBuilder = new StringBuilder("SELECT s.*, v.model as vehicleModel, v.brand as vehicleBrand, c.name as customerName " +
                                                   "FROM sales s " +
                                                   "JOIN vehicles v ON s.vehicle_id = v.vehicle_id " +
                                                   "JOIN customers c ON s.customer_id = c.customer_id " +
                                                   "WHERE s.employee_id = ?");

        List<Object> params = new ArrayList<>();
        params.add(employeeId);

        if (startDate != null) {
            sqlBuilder.append(" AND date(s.sale_date) >= ?");
            params.add(startDate.format(DateTimeFormatter.ISO_DATE));
        }
        if (endDate != null) {
            sqlBuilder.append(" AND date(s.sale_date) <= ?");
            params.add(endDate.format(DateTimeFormatter.ISO_DATE));
        }
        sqlBuilder.append(" ORDER BY s.sale_date DESC");

        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sqlBuilder.toString())) {

            for (int i = 0; i < params.size(); i++) {
                pstmt.setObject(i + 1, params.get(i));
            }

            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                Sale sale = mapResultSetToSale(rs);
                // Add joined data
                sale.setVehicleModel(rs.getString("vehicleModel"));
                sale.setVehicleBrand(rs.getString("vehicleBrand"));
                sale.setCustomerName(rs.getString("customerName"));
                sales.add(sale);
            }
        } catch (SQLException e) {
            System.err.println("Database error finding sales by salesperson: " + e.getMessage());
            e.printStackTrace();
        }
        return sales;
    }

    // Method for complex report filtering (example structure)
    public List<Sale> findSalesByCriteria(LocalDate startDate, LocalDate endDate, Integer employeeId, String vehicleModelBrand) {
        List<Sale> sales = new ArrayList<>();
        StringBuilder sqlBuilder = new StringBuilder(
            "SELECT s.*, v.model as vehicleModel, v.brand as vehicleBrand, e.name as employeeName, c.name as customerName " +
            "FROM sales s " +
            "JOIN vehicles v ON s.vehicle_id = v.vehicle_id " +
            "JOIN employees e ON s.employee_id = e.employee_id " +
            "JOIN customers c ON s.customer_id = c.customer_id " +
            "WHERE 1=1"); // Start with a true condition

        List<Object> params = new ArrayList<>();

        if (startDate != null) {
            sqlBuilder.append(" AND date(s.sale_date) >= ?");
            params.add(startDate.format(DateTimeFormatter.ISO_DATE));
        }
        if (endDate != null) {
            sqlBuilder.append(" AND date(s.sale_date) <= ?");
            params.add(endDate.format(DateTimeFormatter.ISO_DATE));
        }
        if (employeeId != null) {
            sqlBuilder.append(" AND s.employee_id = ?");
            params.add(employeeId);
        }
        if (vehicleModelBrand != null && !vehicleModelBrand.isEmpty()) {
            sqlBuilder.append(" AND (v.model LIKE ? OR v.brand LIKE ?)");
            String likeTerm = "%" + vehicleModelBrand + "%";
            params.add(likeTerm);
            params.add(likeTerm);
        }

        sqlBuilder.append(" ORDER BY s.sale_date DESC");

        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sqlBuilder.toString())) {

            for (int i = 0; i < params.size(); i++) {
                pstmt.setObject(i + 1, params.get(i));
            }

            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                 Sale sale = mapResultSetToSale(rs);
                 // Add joined data
                 sale.setVehicleModel(rs.getString("vehicleModel"));
                 sale.setVehicleBrand(rs.getString("vehicleBrand"));
                 sale.setEmployeeName(rs.getString("employeeName"));
                 sale.setCustomerName(rs.getString("customerName"));
                 sales.add(sale);
            }
        } catch (SQLException e) {
            System.err.println("Database error finding sales by criteria: " + e.getMessage());
            e.printStackTrace();
        }
        return sales;
    }

}

