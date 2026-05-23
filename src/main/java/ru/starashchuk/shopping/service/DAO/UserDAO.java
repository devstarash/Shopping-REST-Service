package ru.starashchuk.shopping.service.DAO;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.starashchuk.shopping.service.db.DBConnection;
import ru.starashchuk.shopping.service.exceptions.DatabaseException;
import ru.starashchuk.shopping.service.exceptions.UserNotFoundException;
import ru.starashchuk.shopping.service.models.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

@Component
public class UserDAO {
    private DBConnection dbConnection;
    @Autowired
    public UserDAO(DBConnection dbConnection) {
        this.dbConnection = dbConnection;
    }

    public void save(User user) {
        String SQL = "INSERT INTO users(username, first_name, last_name, email, password) VALUES" +
                "(?, ?, ?, ?, ?)";
        try (Connection connection = dbConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(SQL)) {
            statement.setString(1, user.getUsername());
            statement.setString(2, user.getFirstName());
            statement.setString(3, user.getLastName());
            statement.setString(4, user.getEmail());
            statement.setString(5, user.getPassword());
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new DatabaseException("Ошибка базы данных", e);
        }
    }

    public User findByUsername(String username) {
        String SQL = "SELECT * FROM users WHERE username = ?";
        try (Connection connection = dbConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(SQL)) {
            statement.setString(1, username);
            ResultSet result = statement.executeQuery();
            if (result.next()) {
                User user = convertToUser(result);
                return user;

            } else {
                throw new UserNotFoundException("Пользователь не найден");
            }

        } catch (SQLException e) {
            throw new DatabaseException("Ошибка базы данных", e);
        }
    }

    public User findByEmail(String email) {
        String sql = "SELECT * FROM users WHERE email = ?";
        try (Connection connection = dbConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, email);
            ResultSet result = statement.executeQuery();
            if (result.next()) {
                return convertToUser(result);
            } else {
                throw new UserNotFoundException("Пользователь с таким email не найден");
            }
        } catch (SQLException e) {
            throw new DatabaseException("Ошибка базы данных", e);
        }
    }

    public boolean existsByUsername(String username) {
        String sql = "SELECT COUNT(*) FROM users WHERE username = ?";
        try (Connection connection = dbConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, username);
            ResultSet result = statement.executeQuery();
            result.next();
            return result.getInt(1) > 0;
        } catch (SQLException e) {
            throw new DatabaseException("Ошибка базы данных", e);
        }
    }

    public boolean existsByEmail(String email) {
        String sql = "SELECT COUNT(*) FROM users WHERE email = ?";
        try (Connection connection = dbConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, email);
            ResultSet result = statement.executeQuery();
            result.next();
            return result.getInt(1) > 0;
        } catch (SQLException e) {
            throw new DatabaseException("Ошибка базы данных", e);
        }
    }

    private User convertToUser(ResultSet result) throws SQLException {
        User user = new User();
        user.setId(result.getInt("id"));
        user.setUsername(result.getString("username"));
        user.setFirstName(result.getString("first_name"));
        user.setLastName(result.getString("last_name"));
        user.setEmail(result.getString("email"));
        user.setPassword(result.getString("password"));
        return user;
    }
}
