package live.ioteatime.frontservice.controller;

import live.ioteatime.frontservice.dto.DailyElectricitiesDto;
import live.ioteatime.frontservice.service.DailyReportService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Slf4j
@Controller
@RequiredArgsConstructor
@RequestMapping("/daily-report")
public class DailyReportController {
    private final DailyReportService dailyReportService;

    @GetMapping
    public String dailyReport(Model model) {
        dailyReportService.initDailyReport(model);
        return "detail/daily-report";
    }

    @GetMapping("/{channelId}")
    public ResponseEntity<DailyElectricitiesDto> getDailyElectricites(@PathVariable int channelId, Model model) {
        return ResponseEntity.ok(dailyReportService.getDailyElectricites(channelId, model));
    }

}
