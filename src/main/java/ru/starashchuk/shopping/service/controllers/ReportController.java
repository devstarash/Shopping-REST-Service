package ru.starashchuk.shopping.service.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;
import ru.starashchuk.shopping.service.DTO.HourlySalesDTO;
import ru.starashchuk.shopping.service.DTO.SoldItemDTO;
import ru.starashchuk.shopping.service.DTO.CategorySalesDTO; // Нужно будет создать
import ru.starashchuk.shopping.service.DTO.BusinessSummaryDTO; // Нужно будет создать
import ru.starashchuk.shopping.service.servises.ReportService;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/admin/reports")
public class ReportController {

    private final ReportService reportService;

    @Autowired
    public ReportController(ReportService reportService) {
        this.reportService = reportService;
    }


    @GetMapping("/revenue-period")
    public BigDecimal getRevenueByPeriod(
            @RequestParam("from") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate from,
            @RequestParam("to") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate to) {
        return reportService.getRevenueByPeriod(from, to);
    }


    @GetMapping("/top-products")
    public List<SoldItemDTO> getTopProducts(
            @RequestParam("from") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate from,
            @RequestParam("to") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate to,
            @RequestParam(value = "limit", defaultValue = "5") int limit) {
        return reportService.getTopProducts(from, to, limit);
    }


    @GetMapping("/categories")
    public List<CategorySalesDTO> getSalesByCategories(
            @RequestParam("from") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate from,
            @RequestParam("to") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate to) {
        return reportService.getSalesByCategories(from, to);
    }


    @GetMapping("/summary")
    public BusinessSummaryDTO getBusinessSummary(
            @RequestParam("from") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate from,
            @RequestParam("to") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate to) {
        return reportService.getBusinessSummary(from, to);
    }

    @GetMapping("/hourly")
    public List<HourlySalesDTO> getSalesByHours(
            @RequestParam("from") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate from,
            @RequestParam("to") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate to) {
        return reportService.getSalesByHours(from, to);
    }

}