package live.ioteatime.frontservice.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Slf4j
@Controller
public class MonthlyReportController {
    @GetMapping("/monthly-report")
    public String getElectricityCharge() {
        return "detail/monthly-report";
    }
}
