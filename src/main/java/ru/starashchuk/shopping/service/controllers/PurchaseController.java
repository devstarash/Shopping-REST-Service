package ru.starashchuk.shopping.service.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.starashchuk.shopping.service.DTO.PurchaseRequestDTO;
import ru.starashchuk.shopping.service.DTO.PurchaseResponseDTO;
import ru.starashchuk.shopping.service.servises.PurchaseService;

@RestController()
@RequestMapping("purchases")
public class PurchaseController {
    private PurchaseService purchaseService;
    @Autowired
    public PurchaseController(PurchaseService purchaseService) {
        this.purchaseService = purchaseService;
    }
    @PostMapping()
    public PurchaseResponseDTO purchase(@RequestBody PurchaseRequestDTO requestDTO){
        PurchaseResponseDTO responseDTO = purchaseService.purchase(requestDTO);
        return responseDTO;
    }

}
