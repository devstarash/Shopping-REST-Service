package ru.starashchuk.shopping.service.DAO;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.starashchuk.shopping.service.DTO.HourlySalesDTO;
import ru.starashchuk.shopping.service.DTO.SoldItemDTO;
import ru.starashchuk.shopping.service.DTO.CategorySalesDTO;
import ru.starashchuk.shopping.service.DTO.BusinessSummaryDTO;
import ru.starashchuk.shopping.service.db.DBConnection;
import ru.starashchuk.shopping.service.exceptions.DatabaseException;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Component
public class ReportDAO {
    private final DBConnection dbConnection;

    @Autowired
    public ReportDAO(DBConnection dbConnection) {
        this.dbConnection = dbConnection;
    }

    public BigDecimal getRevenue(LocalDate date) {
        String SQL = "SELECT SUM(ri.quantity * ri.price_each) as revenue " +
                "FROM receipt_items ri JOIN receipts r ON ri.receipt_id = r.id " +
                "WHERE r.sale_date::date = ?";
        try (Connection connection = dbConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(SQL)) {
            statement.setObject(1, date);
            try (ResultSet result = statement.executeQuery()) {
                if (result.next() && result.getBigDecimal("revenue") != null) {
                    return result.getBigDecimal("revenue");
                }
            }
        } catch (SQLException e) {
            throw new DatabaseException("Ошибка базы данных при расчете выручки за день", e);
        }
        return BigDecimal.ZERO;
    }

    public List<SoldItemDTO> getSoldByDate(LocalDate date) {
        String SQL = "SELECT p.name, SUM(ri.quantity) as total_sold FROM receipt_items ri " +
                "JOIN receipts r ON r.id = ri.receipt_id " +
                "JOIN products p ON p.id = ri.product_id " +
                "WHERE r.sale_date::date = ? " +
                "GROUP BY p.id, p.name " +
                "ORDER BY total_sold DESC";
        return executeSoldItemsQuery(SQL, date, date, Integer.MAX_VALUE);
    }

    public BigDecimal getRevenueByPeriod(LocalDate from, LocalDate to) {
        String SQL = "SELECT SUM(ri.quantity * ri.price_each) as revenue " +
                "FROM receipt_items ri JOIN receipts r ON ri.receipt_id = r.id " +
                "WHERE r.sale_date::date BETWEEN ? AND ?";
        try (Connection connection = dbConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(SQL)) {
            statement.setObject(1, from);
            statement.setObject(2, to);
            try (ResultSet result = statement.executeQuery()) {
                if (result.next() && result.getBigDecimal("revenue") != null) {
                    return result.getBigDecimal("revenue");
                }
            }
        } catch (SQLException e) {
            throw new DatabaseException("Ошибка базы данных при расчете выручки за период", e);
        }
        return BigDecimal.ZERO;
    }

    public long getOrdersCountByPeriod(LocalDate from, LocalDate to) {
        String SQL = "SELECT COUNT(id) as orders_count FROM receipts WHERE sale_date::date BETWEEN ? AND ?";
        try (Connection connection = dbConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(SQL)) {
            statement.setObject(1, from);
            statement.setObject(2, to);
            try (ResultSet result = statement.executeQuery()) {
                if (result.next()) {
                    return result.getLong("orders_count");
                }
            }
        } catch (SQLException e) {
            throw new DatabaseException("Ошибка базы данных при подсчете количества заказов", e);
        }
        return 0;
    }

    public List<SoldItemDTO> getTopProducts(LocalDate from, LocalDate to, int limit) {
        String SQL = "SELECT p.name, SUM(ri.quantity) as total_sold FROM receipt_items ri " +
                "JOIN receipts r ON r.id = ri.receipt_id " +
                "JOIN products p ON p.id = ri.product_id " +
                "WHERE r.sale_date::date BETWEEN ? AND ? " +
                "GROUP BY p.id, p.name " +
                "ORDER BY total_sold DESC " +
                "LIMIT ?";
        return executeSoldItemsQuery(SQL, from, to, limit);
    }

    public List<CategorySalesDTO> getSalesByCategories(LocalDate from, LocalDate to) {
        List<CategorySalesDTO> categorySales = new ArrayList<>();
        String SQL = "SELECT c.name as category_name, " +
                "SUM(ri.quantity) as total_quantity, " +
                "SUM(ri.quantity * ri.price_each) as total_revenue " +
                "FROM receipt_items ri " +
                "JOIN receipts r ON r.id = ri.receipt_id " +
                "JOIN products p ON p.id = ri.product_id " +
                "JOIN categories c ON c.id = p.category_id " +
                "WHERE r.sale_date::date BETWEEN ? AND ? " +
                "GROUP BY c.id, c.name " +
                "ORDER BY total_revenue DESC";

        try (Connection connection = dbConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(SQL)) {
            statement.setObject(1, from);
            statement.setObject(2, to);
            try (ResultSet result = statement.executeQuery()) {
                while (result.next()) {
                    CategorySalesDTO dto = new CategorySalesDTO(
                            result.getString("category_name"),
                            result.getLong("total_quantity"),
                            result.getBigDecimal("total_revenue")
                    );
                    categorySales.add(dto);
                }
            }
        } catch (SQLException e) {
            throw new DatabaseException("Ошибка базы данных при получении отчета по категориям", e);
        }
        return categorySales;
    }

    private List<SoldItemDTO> executeSoldItemsQuery(String sql, LocalDate from, LocalDate to, int limit) {
        List<SoldItemDTO> soldItems = new ArrayList<>();
        try (Connection connection = dbConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setObject(1, from);
            statement.setObject(2, to);
            if (sql.contains("LIMIT")) {
                statement.setInt(3, limit);
            }
            try (ResultSet result = statement.executeQuery()) {
                while (result.next()) {
                    SoldItemDTO soldItem = new SoldItemDTO();
                    soldItem.setProductName(result.getString("name"));
                    soldItem.setTotalSold(result.getInt("total_sold"));
                    soldItems.add(soldItem);
                }
            }
        } catch (SQLException e) {
            throw new DatabaseException("Ошибка при выгрузке проданных товаров", e);
        }
        return soldItems;
    }

    public List<HourlySalesDTO> getSalesByHours(LocalDate from, LocalDate to) {
        List<HourlySalesDTO> hourlySales = new ArrayList<>();
        String SQL = "SELECT EXTRACT(HOUR FROM sale_date)::int as sale_hour, COUNT(id) as orders_count " +
                "FROM receipts " +
                "WHERE sale_date::date BETWEEN ? AND ? " +
                "GROUP BY EXTRACT(HOUR FROM sale_date) " +
                "ORDER BY sale_hour ASC";

        try (Connection connection = dbConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(SQL)) {
            statement.setObject(1, from);
            statement.setObject(2, to);
            try (ResultSet result = statement.executeQuery()) {
                while (result.next()) {
                    hourlySales.add(new HourlySalesDTO(
                            result.getInt("sale_hour"),
                            result.getLong("orders_count")
                    ));
                }
            }
        } catch (SQLException e) {
            throw new DatabaseException("Ошибка при получении почасовой аналитики", e);
        }
        return hourlySales;
    }


    public BusinessSummaryDTO getBusinessSummary(LocalDate from, LocalDate to) {
        String SQL = "SELECT " +
                "  SUM(ri.quantity * ri.price_each) as total_revenue, " +
                "  SUM(ri.quantity * (ri.price_each - ri.purchase_price)) as total_profit, " +
                "  COUNT(DISTINCT r.id) as total_orders " +
                "FROM receipts r " +
                "JOIN receipt_items ri ON r.id = ri.receipt_id " +
                "WHERE r.sale_date::date BETWEEN ? AND ?";

        try (Connection connection = dbConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(SQL)) {
            statement.setObject(1, from);
            statement.setObject(2, to);

            try (ResultSet result = statement.executeQuery()) {
                if (result.next()) {
                    BigDecimal revenue = result.getBigDecimal("total_revenue");
                    BigDecimal profit = result.getBigDecimal("total_profit");
                    long orders = result.getLong("total_orders");

                    if (revenue == null) revenue = BigDecimal.ZERO;
                    if (profit == null) profit = BigDecimal.ZERO;

                    BigDecimal avgCheck = orders > 0
                            ? revenue.divide(BigDecimal.valueOf(orders), 2, RoundingMode.HALF_UP)
                            : BigDecimal.ZERO;

                    return new BusinessSummaryDTO(revenue, profit, orders, avgCheck);
                }
            }
        } catch (SQLException e) {
            throw new DatabaseException("Ошибка при расчете сквозной бизнес-аналитики", e);
        }
        return new BusinessSummaryDTO(BigDecimal.ZERO, BigDecimal.ZERO, 0, BigDecimal.ZERO);
    }
}