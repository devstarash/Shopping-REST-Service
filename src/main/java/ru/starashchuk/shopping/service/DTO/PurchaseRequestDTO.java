package ru.starashchuk.shopping.service.DTO;

import java.util.List;

public class PurchaseRequestDTO {
    List<PurchaseItemDTO> purchaseItems;

    public List<PurchaseItemDTO> getPurchaseItems() {
        return purchaseItems;
    }

    public void setPurchaseItems(List<PurchaseItemDTO> purchaseItems) {
        this.purchaseItems = purchaseItems;
    }
}
