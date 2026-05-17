package ru.starashchuk.shopping.service.DAO;

import ru.starashchuk.shopping.service.models.Category;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CategoryDAO {
    private static final String URL = "";
    private static final String username = "";
    private static final String password = "";
    private static Connection connection;

    static {
        try {
            Class.forName("org.postgresql.driver");
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
        try {
            DriverManager.getConnection(URL, username, password);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private List<Category> findAllCategories() {
        List<Category> categories = new ArrayList<>();
        try {
            PreparedStatement statement = connection.prepareStatement("SELECT * FROM Category");
            ResultSet result = statement.executeQuery();
            while (result.next()) {
                Category category = new Category();
                category.setId(result.getInt("id"));
                category.setName(result.getString("name"));
                categories.add(category);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return categories;

    }
}
