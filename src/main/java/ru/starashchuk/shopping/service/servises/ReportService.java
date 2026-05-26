package ru.starashchuk.shopping.service.servises;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.starashchuk.shopping.service.DAO.ReportDAO;
import ru.starashchuk.shopping.service.DTO.HourlySalesDTO;
import ru.starashchuk.shopping.service.DTO.SoldItemDTO;
import ru.starashchuk.shopping.service.DTO.CategorySalesDTO;
import ru.starashchuk.shopping.service.DTO.BusinessSummaryDTO;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Component
public class ReportService {

    private final ReportDAO reportDAO;

    @Autowired
    public ReportService(ReportDAO reportDAO) {
        this.reportDAO = reportDAO;
    }

    public BigDecimal getRevenueByDate(LocalDate date) {
        return reportDAO.getRevenue(date);
    }

    public List<SoldItemDTO> getSoldByDate(LocalDate date) {
        return reportDAO.getSoldByDate(date);
    }

    public BigDecimal getRevenueByPeriod(LocalDate from, LocalDate to) {
        BigDecimal revenue = reportDAO.getRevenueByPeriod(from, to);
        return revenue != null ? revenue : BigDecimal.ZERO;
    }

    public List<SoldItemDTO> getTopProducts(LocalDate from, LocalDate to, int limit) {
        return reportDAO.getTopProducts(from, to, limit);
    }

    public List<CategorySalesDTO> getSalesByCategories(LocalDate from, LocalDate to) {
        return reportDAO.getSalesByCategories(from, to);
    }


    public BusinessSummaryDTO getBusinessSummary(LocalDate from, LocalDate to) {

        return reportDAO.getBusinessSummary(from, to);
    }

    public List<HourlySalesDTO> getSalesByHours(LocalDate from, LocalDate to) {
        return reportDAO.getSalesByHours(from, to);
    }
}