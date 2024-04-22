package live.ioteatime.frontservice.controller;

import live.ioteatime.frontservice.adaptor.UserAdaptor;
import live.ioteatime.frontservice.dto.DailyElectricityDto;
import live.ioteatime.frontservice.dto.response.GetUserResponse;
import live.ioteatime.frontservice.dto.response.OrganizationResponse;
import live.ioteatime.frontservice.exception.UnauthorizedAccessException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Slf4j
@Controller
@RequiredArgsConstructor
public class DailyReportController {
    private final UserAdaptor userAdaptor;

    @GetMapping("/daily-report")
    public String getElectricityCharge(Model model) {
        GetUserResponse userInfo = userAdaptor.getUser().getBody();
        model.addAttribute("userInfo", userInfo);
        OrganizationResponse organizationResponse = userAdaptor.getOrganization().getBody();
        if (Objects.isNull(organizationResponse)) {
            throw new UnauthorizedAccessException("organization not found");
        }
        model.addAttribute("budget", organizationResponse.getElectricityBudget());

        List<DailyElectricityDto> dailyElectricityDtos = userAdaptor.getDailyElectricities(
                LocalDateTime.now().toLocalDate().atTime(LocalTime.MIDNIGHT),
                organizationResponse.getId()
        ).getBody();
        if (dailyElectricityDtos == null) {
            dailyElectricityDtos = Collections.emptyList();
        }
        model.addAttribute("recent7DaysElectricities",
                dailyElectricityDtos.stream()
                        .filter(dailyElectricityDto -> dailyElectricityDto.getTime().isAfter(LocalDateTime.now().minusDays(8)))
                        .collect(Collectors.toList())
        );

        model.addAttribute("recent24HoursElectricites",
                userAdaptor.getDailyElectricities(
                        LocalDateTime.now().withSecond(0).truncatedTo(ChronoUnit.SECONDS),
                        organizationResponse.getId()
                ).getBody());

        return "detail/daily-report";
    }
}
