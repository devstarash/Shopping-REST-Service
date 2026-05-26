package ru.starashchuk.shopping.service.DTO;

import java.math.BigDecimal;

public class BusinessSummaryDTO {
    private BigDecimal totalRevenue;
    private BigDecimal totalProfit;
    private long totalOrders;
    private BigDecimal averageCheck;

    public BusinessSummaryDTO() {}

    public BusinessSummaryDTO(BigDecimal totalRevenue, BigDecimal totalProfit, long totalOrders, BigDecimal averageCheck) {
        this.totalRevenue = totalRevenue;
        this.totalProfit = totalProfit;
        this.totalOrders = totalOrders;
        this.averageCheck = averageCheck;
    }


    public BigDecimal getTotalRevenue() { return totalRevenue; }
    public void setTotalRevenue(BigDecimal totalRevenue) { this.totalRevenue = totalRevenue; }

    public BigDecimal getTotalProfit() { return totalProfit; }
    public void setTotalProfit(BigDecimal totalProfit) { this.totalProfit = totalProfit; }

    public long getTotalOrders() { return totalOrders; }
    public void setTotalOrders(long totalOrders) { this.totalOrders = totalOrders; }

    public BigDecimal getAverageCheck() { return averageCheck; }
    public void setAverageCheck(BigDecimal averageCheck) { this.averageCheck = averageCheck; }
}