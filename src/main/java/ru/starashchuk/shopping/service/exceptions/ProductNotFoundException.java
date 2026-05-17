package ru.starashchuk.shopping.service.exceptions;

public class ProductNotFoundException extends RuntimeException {
    public ProductNotFoundException(int id) {
        super("Продукт по id - " + id + " не найден");
    }
}
