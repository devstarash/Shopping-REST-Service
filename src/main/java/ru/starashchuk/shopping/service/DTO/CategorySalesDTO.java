package ru.starashchuk.shopping.service.DTO;

import java.math.BigDecimal;

public class CategorySalesDTO {
    private String categoryName;
    private long totalQuantitySold;
    private BigDecimal totalRevenue;

    public CategorySalesDTO(String categoryName, long totalQuantitySold, BigDecimal totalRevenue) {
        this.categoryName = categoryName;
        this.totalQuantitySold = totalQuantitySold;
        this.totalRevenue = totalRevenue;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public long getTotalQuantitySold() {
        return totalQuantitySold;
    }

    public void setTotalQuantitySold(long totalQuantitySold) {
        this.totalQuantitySold = totalQuantitySold;
    }

    public BigDecimal getTotalRevenue() {
        return totalRevenue;
    }

    public void setTotalRevenue(BigDecimal totalRevenue) {
        this.totalRevenue = totalRevenue;
    }
}