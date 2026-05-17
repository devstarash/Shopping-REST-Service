package ru.starashchuk.shopping.service.DTO;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public class PurchaseResponseDTO {
    private int receiptId;
    private LocalDateTime date;
    private List<ReceiptItemDTO> receiptItems;
    private BigDecimal total;

    public PurchaseResponseDTO(int receiptId, LocalDateTime date, List<ReceiptItemDTO> receiptItems, BigDecimal total) {
        this.receiptId = receiptId;
        this.date = date;
        this.receiptItems = receiptItems;
        this.total = total;
    }

    public int getReceiptId() {
        return receiptId;
    }

    public void setReceiptId(int receiptId) {
        this.receiptId = receiptId;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }

    public List<ReceiptItemDTO> getReceiptItems() {
        return receiptItems;
    }

    public void setReceiptItems(List<ReceiptItemDTO> receiptItems) {
        this.receiptItems = receiptItems;
    }

    public BigDecimal getTotal() {
        return total;
    }

    public void setTotal(BigDecimal total) {
        this.total = total;
    }
}
