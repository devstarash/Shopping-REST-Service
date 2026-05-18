package ru.starashchuk.shopping.service.DAO;

import org.springframework.stereotype.Component;
import ru.starashchuk.shopping.service.db.DBConnection;
import ru.starashchuk.shopping.service.exceptions.DatabaseException;
import ru.starashchuk.shopping.service.models.Category;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@Component
public class CategoryDAO {

    private final DBConnection dbConnection;

    public CategoryDAO(DBConnection dbConnection) {
        this.dbConnection = dbConnection;
    }

    public List<Category> findAll() {
        List<Category> categories = new ArrayList<>();
        String sql = "SELECT * FROM categories";

        try (Connection conn = dbConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                categories.add(mapCategory(rs));
            }

        } catch (SQLException e) {
            throw new DatabaseException("Ошибка базы данных", e);
        }
        return categories;
    }

    private Category mapCategory(ResultSet rs) throws SQLException {
        Category category = new Category();
        category.setId(rs.getInt("id"));
        category.setName(rs.getString("name"));
        return category;
    }
}