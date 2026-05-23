package ru.starashchuk.shopping.service.DAO;

import org.springframework.stereotype.Component;
import ru.starashchuk.shopping.service.db.DBConnection;
import ru.starashchuk.shopping.service.exceptions.DatabaseException;
import ru.starashchuk.shopping.service.exceptions.ReceiptCreationException;
import ru.starashchuk.shopping.service.models.Receipt;

import java.sql.*;

@Component
public class ReceiptDAO {

    private final DBConnection dbConnection;

    public ReceiptDAO(DBConnection dbConnection) {
        this.dbConnection = dbConnection;
    }

    public int save(Connection conn, Receipt receipt) {
        String sql = "INSERT INTO receipts (sale_date, user_id) VALUES (?, ?)";

        try (PreparedStatement ps = conn.prepareStatement(
                sql,
                Statement.RETURN_GENERATED_KEYS)) {

            ps.setTimestamp(1, Timestamp.valueOf(receipt.getDate()));
            ps.setInt(2, receipt.getUserId());
            ps.executeUpdate();

            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    return rs.getInt(1);
                } else {
                    throw new ReceiptCreationException("Не удалось создать чек, id не получен");
                }
            }

        } catch (SQLException e) {
            throw new DatabaseException("Ошибка базы данных", e);
        }
    }
}