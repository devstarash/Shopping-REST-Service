package ru.starashchuk.shopping.service.DTO;

import java.util.List;

public class PurchaseRequestDTO {
    List<PurchaseItemDTO> items;

    public List<PurchaseItemDTO> getItems() {
        return items;
    }

    public void setItems(List<PurchaseItemDTO> items) {
        this.items = items;
    }
}
