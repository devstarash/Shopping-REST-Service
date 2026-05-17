package ru.starashchuk.shopping.service.DAO;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.starashchuk.shopping.service.DTO.SoldItemDTO;
import ru.starashchuk.shopping.service.db.DBConnection;

import java.math.BigDecimal;
import java.sql.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

@Component
public class ReportDAO {
    private DBConnection dbConnection;

    @Autowired
    public ReportDAO(DBConnection dbConnection) {
        this.dbConnection = dbConnection;
    }

    public BigDecimal getRevenue(LocalDate date) {
        try (Connection connection = dbConnection.getConnection()) {
            PreparedStatement statement = connection.prepareStatement("SELECT SUM(ri.quantity * ri.price_each) as revenue FROM receipt_items ri JOIN receipts r ON ri.receipt_id = r.id WHERE DATE(r.sale_date) = ?");
            statement.setObject(1, date);
            ResultSet result = statement.executeQuery();
            if (result.next() && result.getBigDecimal("revenue") != null) {
                return result.getBigDecimal("revenue");
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return BigDecimal.ZERO;

    }

    public List<SoldItemDTO> getSoldByDate(LocalDate date) {
        List<SoldItemDTO> soldItems = new ArrayList<>();
        String SQL = "SELECT p.name, SUM(ri.quantity) as total_sold FROM receipt_items ri " +
                "JOIN receipts r ON r.id = ri.receipt_id " +
                "JOIN products p ON p.id = ri.product_id " +
                "WHERE DATE(r.sale_date) = ? " +
                "GROUP BY p.id " +
                "ORDER BY total_sold DESC";
        try (Connection connection = dbConnection.getConnection()) {
            PreparedStatement statement = connection.prepareStatement(SQL);
            statement.setObject(1, date);
            ResultSet result = statement.executeQuery();
            while (result.next()) {
                SoldItemDTO soldItem = new SoldItemDTO();
                soldItem.setProductName(result.getString("name"));
                soldItem.setQuantity(result.getInt("total_sold"));
                soldItems.add(soldItem);
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return soldItems;
    }
}

