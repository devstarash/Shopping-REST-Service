package ru.starashchuk.shopping.service.db;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

@Component
public class DBConnection {

    @Value(value = "${db.url}")
    private String URL;
    @Value(value = "${db.username}")
    private String username;
    @Value(value = "${db.password}")
    private String password;

    public Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, username, password);
    }
}