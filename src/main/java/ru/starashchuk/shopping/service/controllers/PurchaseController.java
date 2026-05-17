package ru.starashchuk.shopping.service.controllers;

import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import ru.starashchuk.shopping.service.DTO.PurchaseRequestDTO;
import ru.starashchuk.shopping.service.DTO.PurchaseResponseDTO;

@RestController
public class PurchaseController {
    public PurchaseResponseDTO purchase(@RequestBody PurchaseRequestDTO requestDTO){
        return null;

    }

}
