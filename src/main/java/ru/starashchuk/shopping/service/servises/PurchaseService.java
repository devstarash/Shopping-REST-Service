package ru.starashchuk.shopping.service.servises;

import jdk.vm.ci.meta.Local;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.starashchuk.shopping.service.DAO.ProductDAO;
import ru.starashchuk.shopping.service.DAO.ReceiptDAO;
import ru.starashchuk.shopping.service.DAO.ReceiptItemDAO;
import ru.starashchuk.shopping.service.DTO.PurchaseItemDTO;
import ru.starashchuk.shopping.service.DTO.PurchaseRequestDTO;
import ru.starashchuk.shopping.service.DTO.PurchaseResponseDTO;
import ru.starashchuk.shopping.service.DTO.ReceiptItemDTO;
import ru.starashchuk.shopping.service.exceptions.InsufficientStockException;
import ru.starashchuk.shopping.service.exceptions.ProductNotFoundException;
import ru.starashchuk.shopping.service.models.Product;
import ru.starashchuk.shopping.service.models.Receipt;
import ru.starashchuk.shopping.service.models.ReceiptItem;

import java.math.BigDecimal;
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
    @Autowired
    public PurchaseService(ProductDAO productDAO, ReceiptDAO receiptDAO, ReceiptItemDAO receiptItemDAO) {
        this.productDAO = productDAO;
        this.receiptDAO = receiptDAO;
        this.receiptItemDAO = receiptItemDAO;
    }

    public PurchaseResponseDTO purchase(PurchaseRequestDTO request){
        List<PurchaseItemDTO> purchaseItems = request.getPurchaseItems();
        Map<Integer, Product> products = new HashMap<>();
        for (PurchaseItemDTO item : purchaseItems){
            Product product = productDAO.findById(item.getProductId());
            if (product == null){
                throw new ProductNotFoundException(item.getProductId());
            }
            if (product.getStock() < item.getQuantity()){
                throw new InsufficientStockException(
                        product.getName(),
                        item.getQuantity(),
                        product.getStock()
                        );
            }
            products.put(product.getId(), product);
        }
        Receipt receipt = new Receipt();
        receipt.setDate(LocalDateTime.now());
        int receiptId = receiptDAO.save(receipt);
        List<ReceiptItemDTO> receiptItems = new ArrayList<>();
        BigDecimal total = BigDecimal.ZERO;
        for (PurchaseItemDTO item : purchaseItems){
            Product product = products.get(item.getProductId());
            ReceiptItem receiptItem = new ReceiptItem();
            receiptItem.setProductId(product.getId());
            receiptItem.setPrice(product.getPrice());
            receiptItem.setQuantity(item.getQuantity());
            receiptItem.setReceiptId(receiptId);
            receiptItemDAO.save(receiptItem);
            productDAO.updateStock(
                    product.getId(),
                    product.getStock() - item.getQuantity()
            );
            BigDecimal itemTotal = receiptItem.getPrice().multiply(BigDecimal.valueOf(receiptItem.getQuantity()));
            total = total.add(itemTotal);
            receiptItems.add(new ReceiptItemDTO(
                    product.getName(),
                    receiptItem.getQuantity(),
                    receiptItem.getPrice()
            ));
        }
        return new PurchaseResponseDTO(receiptId, LocalDateTime.now(), receiptItems, total);





    }
}
