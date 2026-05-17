package ru.starashchuk.shopping.service.DTO;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.math.BigDecimal;

public class ReceiptItemDTO {
    @JsonProperty("productName")
    private String productName;

    @JsonProperty("priceEach")
    private BigDecimal price;

    @JsonProperty("quantity")
    private int quantity;

    @JsonProperty("itemTotal")
    private BigDecimal total;

    public ReceiptItemDTO(String productName, int quantity, BigDecimal price) {
        this.productName = productName;
        this.quantity = quantity;
        this.price = price;
        this.total = price.multiply(BigDecimal.valueOf(quantity));
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public BigDecimal getTotal() {
        return total;
    }

    public void setTotal(BigDecimal total) {
        this.total = total;
    }
}
