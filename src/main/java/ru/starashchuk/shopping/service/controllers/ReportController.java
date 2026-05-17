package ru.starashchuk.shopping.service.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.starashchuk.shopping.service.DTO.SoldItemDTO;
import ru.starashchuk.shopping.service.servises.ReportService;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/admin/reports")
public class ReportController {
    private ReportService reportService;
    @Autowired
    public ReportController(ReportService reportService) {
        this.reportService = reportService;
    }
    @GetMapping("/revenue")
    public BigDecimal getRevenueByDate(@RequestParam("date") LocalDate date){
        return reportService.getRevenueByDate(date);
    }
    @GetMapping("/sold")
    public List<SoldItemDTO> getSoldByDate(@RequestParam("date") LocalDate date){
        return reportService.getSoldByDate(date);
    }
}
