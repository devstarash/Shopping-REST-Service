package ru.starashchuk.shopping.service.servises;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.starashchuk.shopping.service.DAO.ReportDAO;
import ru.starashchuk.shopping.service.DTO.SoldItemDTO;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Component
public class ReportService {
    private ReportDAO reportDAO;
    @Autowired
    public ReportService(ReportDAO reportDAO) {
        this.reportDAO = reportDAO;
    }

    public BigDecimal getRevenueByDate(LocalDate date){
        return reportDAO.getRevenue(date);
    }

    public List<SoldItemDTO> getSoldByDate(LocalDate date){
        return reportDAO.getSoldByDate(date);
    }
}
