package ru.starashchuk.shopping.service.DAO;

import org.springframework.stereotype.Component;
import ru.starashchuk.shopping.service.models.Category;
import ru.starashchuk.shopping.service.models.Product;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
@Component
public class ProductDAO {
    private static final String URl = "a";
    private static final String username = "a";
    private static final String password = "a";
    private static Connection connection;

    static {
        try {
            Class.forName("org.postgresql.Driver");
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
        try {
            connection = DriverManager.getConnection(URl, username, password);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public List<Product> findAll() {
        List<Product> products = new ArrayList<>();
        try {
            PreparedStatement preparedStatement = connection.prepareStatement("SELECT p.id, p.name, c.id as category_id, c.name as category, p.price, p.stock FROM Product JOIN Category c ON c.id = p.category");
            ResultSet result = preparedStatement.executeQuery();
            while (result.next()) {
                Product product = new Product();
                product.setId(result.getInt("id"));
                product.setName(result.getString("name"));
                Category category = new Category();
                category.setId(result.getInt("category_id"));
                category.setName(result.getString("category"));
                product.setCategory(category);
                product.setPrice(result.getBigDecimal("price"));
                product.setStock(result.getInt("stock"));
                products.add(product);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return products;

    }

    public Product findById(int id) {
        PreparedStatement statement = null;
        Product product = null;
        try {
            statement = connection.prepareStatement("SELECT p.id, p.name, c.id as category_id, c.name as category, p.price, p.stock FROM Product JOIN Category c ON c.id = p.category  WHERE p.id = ?");
            statement.setInt(1, id);
            ResultSet result = statement.executeQuery();
            result.next();
            product.setId(result.getInt("id"));
            product.setName(result.getString("name"));
            Category category = new Category();
            category.setId(result.getInt("category_id"));
            category.setName(result.getString("category"));
            product.setCategory(category);
            product.setPrice(result.getBigDecimal("price"));
            product.setStock(result.getInt("stock"));

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return product;
    }

    public List<Product> findByCategoryId(int categoryId) {
        List<Product> products = new ArrayList<>();
        PreparedStatement statement = null;
        try {
            statement = connection.prepareStatement("SELECT p.id, p.name, c.id as category_id, c.name as category, p.price, p.stock FROM Product JOIN Category c ON c.id = p.category  WHERE c.id = ?");
            statement.setInt(1, categoryId);
            ResultSet result = statement.executeQuery();
            while (result.next()) {
                Product product = new Product();
                product.setId(result.getInt("id"));
                product.setName(result.getString("name"));
                Category category = new Category();
                category.setId(result.getInt("category_id"));
                category.setName(result.getString("category"));
                product.setCategory(category);
                product.setPrice(result.getBigDecimal("price"));
                product.setStock(result.getInt("stock"));
                products.add(product);
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return products;

    }
    public void updateStock(int productId, int quantity){
        try {
            PreparedStatement preparedStatement = connection.prepareStatement("UPDATE Product SET stock = ? WHERE id = ?");
            preparedStatement.setInt(1, quantity);
            preparedStatement.setInt(2, productId);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }


}
