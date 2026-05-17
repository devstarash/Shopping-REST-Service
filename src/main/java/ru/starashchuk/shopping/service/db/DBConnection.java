package ru.starashchuk.shopping.service.db;

import org.springframework.stereotype.Component;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
@Component
public class DBConnection {
    private static final String URL = "";
    private static final String username = "";
    private static final String password = "";
    public Connection getConnection() throws SQLException{
        return DriverManager.getConnection(URL, username, password);
    }
}
