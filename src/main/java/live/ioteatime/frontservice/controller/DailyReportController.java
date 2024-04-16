package live.ioteatime.frontservice.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class DailyReportController {
    @GetMapping("/daily-report")
    public String getElectricityCharge() {
        return "detail/daily-report";
    }
}
