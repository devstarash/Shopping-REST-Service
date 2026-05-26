package ru.starashchuk.shopping.service.DTO;

public class HourlySalesDTO {
    private int hour;
    private long ordersCount;

    public HourlySalesDTO(int hour, long ordersCount) {
        this.hour = hour;
        this.ordersCount = ordersCount;
    }

    public int getHour() {
        return hour;
    }

    public void setHour(int hour) {
        this.hour = hour;
    }

    public long getOrdersCount() {
        return ordersCount;
    }

    public void setOrdersCount(long ordersCount) {
        this.ordersCount = ordersCount;
    }
}