package ru.starashchuk.shopping.service.DAO;

import org.springframework.stereotype.Component;
import ru.starashchuk.shopping.service.exceptions.ReceiptCreationException;
import ru.starashchuk.shopping.service.models.Receipt;

import java.sql.*;
@Component
public class ReceiptDAO {
    private static final String URL = "";
    private static final String username = "";
    private static final String password = "";
    private static Connection connection;

    static{
        try {
            Class.forName("org.postgresql.Driver");
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
        try {
            DriverManager.getConnection(URL, username, password);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    public int save(Receipt receipt){
        try {
            PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO Receipt(date) VALUES(?)");
            preparedStatement.setTimestamp(1, Timestamp.valueOf(receipt.getDate()));
            preparedStatement.executeUpdate();
            ResultSet result = preparedStatement.getGeneratedKeys();
            if (result.next()){
                return result.getInt(1);
            }
            else{
                throw new ReceiptCreationException("Не удалось создать чек");
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
