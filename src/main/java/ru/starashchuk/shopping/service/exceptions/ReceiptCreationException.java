package ru.starashchuk.shopping.service.exceptions;

public class ReceiptCreationException extends RuntimeException{
    public ReceiptCreationException(String msg){
        super(msg);
    }
}
