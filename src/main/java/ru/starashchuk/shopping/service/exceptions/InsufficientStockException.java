package ru.starashchuk.shopping.service.exceptions;

public class InsufficientStockException extends RuntimeException {
    public InsufficientStockException(String productName, int requested, int available) {
        super("Товар '" + productName + "': запрошено " + requested + ", доступно " + available);
    }
}
