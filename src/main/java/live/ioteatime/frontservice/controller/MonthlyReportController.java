package live.ioteatime.frontservice.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import live.ioteatime.frontservice.adaptor.UserAdaptor;
import live.ioteatime.frontservice.dto.MonthlyElectricityDto;
import live.ioteatime.frontservice.dto.response.GetUserResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.time.LocalDate;

@Slf4j
@Controller
@RequiredArgsConstructor
public class MonthlyReportController {
    private final UserAdaptor userAdaptor;
    private final ObjectMapper objectMapper;

    @GetMapping("/monthly-report")
    public String getElectricityCharge(Model model) {
        GetUserResponse userInfo = userAdaptor.getUser().getBody();
        model.addAttribute("userInfo", userInfo);
        return "detail/monthly-report";
    }

    @GetMapping("/monthly-electricity/{localdate}")
    public ResponseEntity<MonthlyElectricityDto> getElectricityByMonth(
            @PathVariable(name = "localdate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate localDate)
            throws JsonProcessingException {
        MonthlyElectricityDto monthlyElectricityDto =
                objectMapper.readValue(userAdaptor.getMonthlyElectricity(localDate).getBody(), MonthlyElectricityDto.class);
        monthlyElectricityDto.setDailyElectricityDtos(userAdaptor.getDailyElectricities(localDate).getBody());
        log.warn(monthlyElectricityDto.toString());
        return ResponseEntity.ok().body(monthlyElectricityDto);
    }

}
