package live.ioteatime.frontservice.controller;

import com.fasterxml.jackson.core.JsonProcessingException;

import live.ioteatime.frontservice.dto.MonthlyElectricityPageDto;
import live.ioteatime.frontservice.service.MonthlyReportService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.time.LocalDateTime;

@Slf4j
@Controller
@RequiredArgsConstructor
public class MonthlyReportController {
    private final MonthlyReportService monthlyReportService;

    @GetMapping("/monthly-report")
    public String getElectricityCharge(Model model) {
        monthlyReportService.initMonthlyReport(model);
        return "detail/monthly-report";
    }

    @GetMapping("/monthly-electricity/{localdate}")
    public ResponseEntity<MonthlyElectricityPageDto> getElectricityByMonth(
            @PathVariable(name = "localdate")
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
            LocalDateTime localDateTime
    ) throws JsonProcessingException {
        return ResponseEntity.ok(monthlyReportService.getElectricityByMonth(localDateTime));
    }

}
