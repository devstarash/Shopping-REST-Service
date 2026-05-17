package ru.starashchuk.shopping.service.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import ru.starashchuk.shopping.service.DTO.PurchaseRequestDTO;
import ru.starashchuk.shopping.service.DTO.PurchaseResponseDTO;

@RestController
public class PurchaseController {
    private PurchaseController purchaseController;
    @Autowired
    public PurchaseController(PurchaseController purchaseController) {
        this.purchaseController = purchaseController;
    }

    public PurchaseResponseDTO purchase(@RequestBody PurchaseRequestDTO requestDTO){
        PurchaseResponseDTO responseDTO = purchaseController.purchase(requestDTO);
        return responseDTO;
    }

}
