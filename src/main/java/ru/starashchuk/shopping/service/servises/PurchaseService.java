package ru.starashchuk.shopping.service.servises;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.starashchuk.shopping.service.DAO.ProductDAO;
import ru.starashchuk.shopping.service.DAO.ReceiptDAO;
import ru.starashchuk.shopping.service.DAO.ReceiptItemDAO;
import ru.starashchuk.shopping.service.DTO.PurchaseItemDTO;
import ru.starashchuk.shopping.service.DTO.PurchaseRequestDTO;
import ru.starashchuk.shopping.service.DTO.PurchaseResponseDTO;
import ru.starashchuk.shopping.service.DTO.ReceiptItemDTO;
import ru.starashchuk.shopping.service.db.DBConnection;
import ru.starashchuk.shopping.service.exceptions.InsufficientStockException;
import ru.starashchuk.shopping.service.models.Product;
import ru.starashchuk.shopping.service.models.Receipt;
import ru.starashchuk.shopping.service.models.ReceiptItem;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class PurchaseService {
    private ProductDAO productDAO;
    private ReceiptDAO receiptDAO;
    private ReceiptItemDAO receiptItemDAO;
    private DBConnection dbConnection;
    @Autowired
    public PurchaseService(ProductDAO productDAO, ReceiptDAO receiptDAO, ReceiptItemDAO receiptItemDAO, DBConnection dbConnection) {
        this.productDAO = productDAO;
        this.receiptDAO = receiptDAO;
        this.receiptItemDAO = receiptItemDAO;
        this.dbConnection = dbConnection;
    }

    public PurchaseResponseDTO purchase(PurchaseRequestDTO request) {
        List<PurchaseItemDTO> purchaseItems = request.getItems();
        Map<Integer, Product> products = new HashMap<>();

        for (PurchaseItemDTO item : purchaseItems) {
            Product product = productDAO.findById(item.getProductId());  // сам бросит если null

            if (product.getStock() < item.getQuantity()) {
                throw new InsufficientStockException(
                        product.getName(),
                        item.getQuantity(),
                        product.getStock()
                );
            }
            products.put(product.getId(), product);
        }

        try (Connection connection = dbConnection.getConnection()) {
            connection.setAutoCommit(false);
            try {
                Receipt receipt = new Receipt();
                receipt.setDate(LocalDateTime.now());
                int receiptId = receiptDAO.save(connection, receipt);

                List<ReceiptItemDTO> receiptItems = new ArrayList<>();
                BigDecimal total = BigDecimal.ZERO;

                for (PurchaseItemDTO item : purchaseItems) {
                    Product product = products.get(item.getProductId());

                    ReceiptItem receiptItem = new ReceiptItem();
                    receiptItem.setProductId(product.getId());
                    receiptItem.setPrice(product.getPrice());
                    receiptItem.setQuantity(item.getQuantity());
                    receiptItem.setReceiptId(receiptId);
                    receiptItemDAO.save(connection, receiptItem);

                    productDAO.updateStock(connection, product.getId(), item.getQuantity());

                    BigDecimal itemTotal = product.getPrice()
                            .multiply(BigDecimal.valueOf(item.getQuantity()));
                    total = total.add(itemTotal);

                    receiptItems.add(new ReceiptItemDTO(
                            product.getName(),
                            item.getQuantity(),
                            product.getPrice()
                    ));
                }

                connection.commit();
                return new PurchaseResponseDTO(receiptId, LocalDateTime.now(), receiptItems, total);

            } catch (Exception e) {
                connection.rollback();
                throw new RuntimeException(e);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
