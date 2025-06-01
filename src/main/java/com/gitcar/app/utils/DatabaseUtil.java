package com.gitcar.app.utils;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseUtil {

    private static final String DB_FILENAME = "gitcar.db";
    private static final String DB_URL = "jdbc:sqlite:" + DB_FILENAME;

    private static Connection connection = null;

    public static Connection getConnection() {
        if (connection == null) {
            try {
                // Ensure the database directory exists (optional, SQLite creates it)
                // Path dbPath = Paths.get(DB_FILENAME).getParent();
                // if (dbPath != null && !Files.exists(dbPath)) {
                //     Files.createDirectories(dbPath);
                // }

                // Load the SQLite JDBC driver
                Class.forName("org.sqlite.JDBC");
                // Create a connection to the database (will create the file if it doesn't exist)
                connection = DriverManager.getConnection(DB_URL);
                System.out.println("Conexão com SQLite estabelecida. Arquivo: " + DB_FILENAME);

                // REMOVED: Automatic schema initialization logic.
                // The user will create the schema manually using database_schema.sql

            } catch (SQLException e) {
                System.err.println("Erro ao conectar ao banco de dados SQLite: " + e.getMessage());
                e.printStackTrace();
                connection = null; // Ensure connection is null on error
                return null;
            } catch (ClassNotFoundException e) {
                System.err.println("Driver JDBC do SQLite não encontrado: " + e.getMessage());
                e.printStackTrace();
                return null;
            } /*catch (IOException e) { // If directory creation was used
                System.err.println("Erro ao criar diretório para o banco de dados: " + e.getMessage());
                e.printStackTrace();
                return null;
            }*/
        }
        return connection;
    }

    public static void closeConnection() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
                connection = null;
                System.out.println("Conexão com SQLite foi fechada.");
            }
        } catch (SQLException e) {
            System.err.println("Erro ao fechar conexão SQLite: " + e.getMessage());
            e.printStackTrace();
        }
    }

    // Add a main method for simple connection testing
    public static void main(String[] args) {
        System.out.println("Testando conexão com o banco de dados...");
        Connection conn = DatabaseUtil.getConnection();
        if (conn != null) {
            System.out.println("Teste de conexão bem-sucedido! O arquivo '" + DB_FILENAME + "' deve existir.");
            // Note: Tables will only exist if created manually.
            DatabaseUtil.closeConnection();
        } else {
            System.out.println("Teste de conexão falhou.");
        }
    }
}

