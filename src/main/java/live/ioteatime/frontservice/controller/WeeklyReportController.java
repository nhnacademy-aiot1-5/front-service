package live.ioteatime.frontservice.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class WeeklyReportController {
    @GetMapping("/weekly-report")
    public String getElectricityCharge() {
        return "detail/weekly-report";
    }
}
