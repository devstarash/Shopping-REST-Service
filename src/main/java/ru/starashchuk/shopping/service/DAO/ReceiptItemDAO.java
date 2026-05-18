package ru.starashchuk.shopping.service.DAO;

import org.springframework.stereotype.Component;
import ru.starashchuk.shopping.service.db.DBConnection;
import ru.starashchuk.shopping.service.exceptions.DatabaseException;
import ru.starashchuk.shopping.service.models.ReceiptItem;

import java.sql.*;

@Component
public class ReceiptItemDAO {

    private final DBConnection dbConnection;

    public ReceiptItemDAO(DBConnection dbConnection) {
        this.dbConnection = dbConnection;
    }

    public void save(Connection conn, ReceiptItem receiptItem) {
        String sql = "INSERT INTO receipt_items (receipt_id, product_id, quantity, price_each) VALUES (?, ?, ?, ?)";

        try (PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, receiptItem.getReceiptId());
            ps.setInt(2, receiptItem.getProductId());
            ps.setInt(3, receiptItem.getQuantity());
            ps.setBigDecimal(4, receiptItem.getPrice());
            ps.executeUpdate();

        } catch (SQLException e) {
            throw new DatabaseException("Ошибка базы данных", e);
        }
    }
}