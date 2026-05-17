package ru.starashchuk.shopping.service.db;

import org.springframework.stereotype.Component;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
@Component
public class DBConnection {
    private static final String URL = "jdbc:postgresql://localhost:5432/shopping_db";
    private static final String username = "postgres";
    private static final String password = "Nikitosik2007";
    static {
        try {
            Class.forName("org.postgresql.Driver");  // ← явная загрузка
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("PostgreSQL драйвер не найден", e);
        }
    }
    public Connection getConnection() throws SQLException{
        return DriverManager.getConnection(URL, username, password);
    }
}
