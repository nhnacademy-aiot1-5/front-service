package live.ioteatime.frontservice.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import live.ioteatime.frontservice.adaptor.UserAdaptor;
import live.ioteatime.frontservice.dto.MonthlyElectricityDto;
import live.ioteatime.frontservice.dto.MonthlyElectricityPageDto;
import live.ioteatime.frontservice.dto.response.OrganizationResponse;
import live.ioteatime.frontservice.dto.response.GetUserResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.time.LocalDateTime;
import java.util.List;
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
        OrganizationResponse organization = userAdaptor.getOrganization().getBody();
        if (Objects.isNull(organization)) {
            throw new NullPointerException();
        }
        List<MonthlyElectricityDto> monthlyElectricityDtos =
                userAdaptor.getMonthlyElectricities(LocalDateTime.now(), organization.getId()).getBody();
        model.addAttribute("recent12monthElectricities", monthlyElectricityDtos);
        model.addAttribute("userInfo", userInfo);
        model.addAttribute("budget", organization.getElectricityBudget());
        return "detail/monthly-report";
    }

    @GetMapping("/monthly-electricity/{localdate}")
    public ResponseEntity<MonthlyElectricityPageDto> getElectricityByMonth(
            @PathVariable(name = "localdate")
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
            LocalDateTime localDateTime
    ) throws JsonProcessingException {
        OrganizationResponse organization = userAdaptor.getOrganization().getBody();
        if (Objects.isNull(organization)) {
            throw new NullPointerException();
        }
        MonthlyElectricityPageDto monthlyElectricityPageDto =
                objectMapper.readValue(
                        userAdaptor.getMonthlyElectricity(localDateTime, organization.getId()).getBody(),
                        MonthlyElectricityPageDto.class
                );
        monthlyElectricityPageDto.setDailyElectricityDtos(
                userAdaptor.getDailyElectricities(localDateTime, organization.getId()).getBody()
        );
        monthlyElectricityPageDto.setMonthlyElectricityDtos(
                userAdaptor.getMonthlyElectricities(localDateTime, organization.getId()).getBody()
        );
        return ResponseEntity.ok(monthlyElectricityPageDto);
    }

}
