package ru.starashchuk.shopping.service.DAO;

import org.springframework.stereotype.Component;
import ru.starashchuk.shopping.service.models.ReceiptItem;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
@Component
public class ReceiptItemDAO {
    private static final String URL = "";
    private static final String password = "";
    private static final String username = "";
    private static Connection connection;

    static {
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

    public void save(ReceiptItem receiptItem) {
        try {
            PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO ReceiptItem(receipt, product_id, quantity, price) VALUES(?, ?, ?, ?)");
            preparedStatement.setInt(1, receiptItem.getReceiptId());
            preparedStatement.setInt(2, receiptItem.getProductId());
            preparedStatement.setInt(3, receiptItem.getQuantity());
            preparedStatement.setBigDecimal(4, receiptItem.getPrice());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }
}
