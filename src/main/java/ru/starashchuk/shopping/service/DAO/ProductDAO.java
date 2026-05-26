package ru.starashchuk.shopping.service.DAO;

import org.springframework.stereotype.Component;
import ru.starashchuk.shopping.service.db.DBConnection;
import ru.starashchuk.shopping.service.exceptions.DatabaseException;
import ru.starashchuk.shopping.service.exceptions.ProductNotFoundException;
import ru.starashchuk.shopping.service.models.Category;
import ru.starashchuk.shopping.service.models.Product;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@Component
public class ProductDAO {

    private final DBConnection dbConnection;

    public ProductDAO(DBConnection dbConnection) {
        this.dbConnection = dbConnection;
    }

    public List<Product> findAll() {
        List<Product> products = new ArrayList<>();
        String sql = "SELECT p.id, p.name, p.purchase_price, c.id as category_id, c.name as category_name, p.price, p.stock " +
                "FROM products p JOIN categories c ON c.id = p.category_id";
        try (Connection conn = dbConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                products.add(mapProduct(rs));
            }
        } catch (SQLException e) {
            throw new DatabaseException("Ошибка базы данных", e);
        }
        return products;
    }

    public Product findById(int id) {
        String sql = "SELECT p.id, p.name, p.purchase_price, c.id as category_id, c.name as category_name, p.price, p.stock " +
                "FROM products p JOIN categories c ON c.id = p.category_id WHERE p.id = ?";
        try (Connection conn = dbConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapProduct(rs);
                } else {
                    throw new ProductNotFoundException(id);
                }
            }
        } catch (SQLException e) {
            throw new DatabaseException("Ошибка базы данных", e);
        }
    }

    public List<Product> findByCategoryId(int categoryId) {
        List<Product> products = new ArrayList<>();
        String sql = "SELECT p.id, p.name, p.purchase_price, c.id as category_id, c.name as category_name, p.price, p.stock " +
                "FROM products p JOIN categories c ON c.id = p.category_id WHERE c.id = ?";
        try (Connection conn = dbConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, categoryId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    products.add(mapProduct(rs));
                }
            }
        } catch (SQLException e) {
            throw new DatabaseException("Ошибка базы данных", e);
        }
        return products;
    }

    public void updateStock(Connection conn, int productId, int quantity) {
        String sql = "UPDATE products SET stock = stock - ? WHERE id = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, quantity);
            ps.setInt(2, productId);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new DatabaseException("Ошибка базы данных", e);
        }
    }

    private Product mapProduct(ResultSet rs) throws SQLException {
        Product product = new Product();
        product.setId(rs.getInt("id"));
        product.setName(rs.getString("name"));
        product.setPrice(rs.getBigDecimal("price"));
        product.setPurchasePrice(rs.getBigDecimal("purchase_price"));
        product.setStock(rs.getInt("stock"));
        Category category = new Category();
        category.setId(rs.getInt("category_id"));
        category.setName(rs.getString("category_name"));
        product.setCategory(category);
        return product;
    }
}