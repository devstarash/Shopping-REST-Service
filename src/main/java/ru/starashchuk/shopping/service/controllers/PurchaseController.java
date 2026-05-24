package ru.starashchuk.shopping.service.controllers;

import jakarta.mail.MessagingException;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.starashchuk.email.EmailService;
import ru.starashchuk.email.dto.ItemDTO;
import ru.starashchuk.email.dto.ReceiptEmailDTO;
import ru.starashchuk.shopping.service.DTO.PurchaseRequestDTO;
import ru.starashchuk.shopping.service.DTO.PurchaseResponseDTO;
import ru.starashchuk.shopping.service.exceptions.UnauthorizedException;
import ru.starashchuk.shopping.service.models.User;
import ru.starashchuk.shopping.service.servises.PurchaseService;

import java.util.List;

@RestController()
@RequestMapping("purchases")
public class PurchaseController {
    private PurchaseService purchaseService;
    private EmailService emailService;
    @Autowired
    public PurchaseController(PurchaseService purchaseService, EmailService emailService) {
        this.purchaseService = purchaseService;
        this.emailService = emailService;
    }
    @PostMapping()
    public PurchaseResponseDTO purchase(@RequestBody PurchaseRequestDTO requestDTO, HttpSession session){
        User user = (User) session.getAttribute("user");
        if(user == null){
            throw new UnauthorizedException("Необходима авторизация");
        }
        PurchaseResponseDTO responseDTO = purchaseService.purchase(requestDTO, user.getId());
        List<ItemDTO> items = responseDTO.getReceiptItems().stream()
                .map(item -> {
                    ItemDTO dto = new ItemDTO();
                    dto.setProductName(item.getProductName());
                    dto.setQuantity(item.getQuantity());
                    dto.setPriceEach(item.getPrice());
                    dto.setItemTotal(item.getTotal());
                    return dto;
                }).toList();
        ReceiptEmailDTO receipt = new ReceiptEmailDTO();
        receipt.setToEmail(user.getEmail());
        receipt.setReceiptId(responseDTO.getReceiptId());
        receipt.setSaleDate(responseDTO.getDate());
        receipt.setItems(items);
        receipt.setTotal(responseDTO.getTotal());
        try {
            emailService.sendReceipt(receipt);
        } catch (MessagingException e) {
            System.err.println("Ошибка отправки письма: " + e.getMessage());
        }
        return responseDTO;
    }

}
