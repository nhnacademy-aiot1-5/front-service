package live.ioteatime.frontservice.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import live.ioteatime.frontservice.adaptor.UserAdaptor;
import live.ioteatime.frontservice.dto.GetUserResponse;
import live.ioteatime.frontservice.dto.MonthlyElectricityDto;
import live.ioteatime.frontservice.dto.OrganizationResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Objects;

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
            @PathVariable(name = "localdate")
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
            LocalDateTime localDateTime
    ) throws JsonProcessingException {
        OrganizationResponse organization = userAdaptor.getOrganization().getBody();
        if (Objects.isNull(organization)) {
            throw new NullPointerException();
        }
        MonthlyElectricityDto monthlyElectricityDto =
                objectMapper.readValue(userAdaptor.getMonthlyElectricity(localDateTime, organization.getId()).getBody(), MonthlyElectricityDto.class);
        monthlyElectricityDto.setDailyElectricityDtos(userAdaptor.getDailyElectricities(localDateTime, organization.getId()).getBody());

        return ResponseEntity.ok().body(monthlyElectricityDto);
    }

}
