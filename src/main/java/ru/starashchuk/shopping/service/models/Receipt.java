package ru.starashchuk.shopping.service.models;

import java.time.LocalDateTime;

public class Receipt {
    private int id;
    private LocalDateTime date;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }
}
