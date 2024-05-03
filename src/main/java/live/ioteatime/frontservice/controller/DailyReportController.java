package live.ioteatime.frontservice.controller;

import live.ioteatime.frontservice.adaptor.UserAdaptor;
import live.ioteatime.frontservice.dto.ChannelDto;
import live.ioteatime.frontservice.dto.DailyElectricitiesDto;
import live.ioteatime.frontservice.dto.DailyElectricityDto;
import live.ioteatime.frontservice.dto.PlaceDto;
import live.ioteatime.frontservice.dto.response.GetUserResponse;
import live.ioteatime.frontservice.dto.response.OrganizationResponse;
import live.ioteatime.frontservice.exception.UnauthorizedAccessException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Controller
@RequiredArgsConstructor
@RequestMapping("/daily-report")
public class DailyReportController {
    private final UserAdaptor userAdaptor;

    @GetMapping
    public String dailyReport(Model model) {
        OrganizationResponse organizationResponse = userAdaptor.getOrganization().getBody();
        if (Objects.isNull(organizationResponse)) {
            throw new UnauthorizedAccessException("organization not found");
        }
        model.addAttribute("budget", organizationResponse.getElectricityBudget());

        int organizationId = organizationResponse.getId();
        List<PlaceDto> placeDtos = userAdaptor.getPlacesByOrganizationId(organizationId).getBody();
        model.addAttribute("places", placeDtos);

        Map<Integer, List<ChannelDto>> placeDtoListMap = new HashMap<>();

        assert placeDtos != null;
        for (PlaceDto p : placeDtos) {
            List<ChannelDto> channelDtos = userAdaptor.getChannelsByPlaceId(p.getId()).getBody();
            placeDtoListMap.put(p.getId(), channelDtos);
        }
        model.addAttribute("placeMap", placeDtoListMap);

        this.getDailyElectricites(1, model);
        return "detail/daily-report";
    }

    @GetMapping("/{channelId}")
    public ResponseEntity<DailyElectricitiesDto> getDailyElectricites(@PathVariable int channelId, Model model) {
        List<DailyElectricityDto> dailyElectricityDtos = userAdaptor.getDailyElectricities(
                LocalDateTime.now().toLocalDate().atTime(LocalTime.MIDNIGHT),
                channelId
        ).getBody();
        List<DailyElectricityDto> hourlyElectricityDtos = userAdaptor.getDailyElectricities(
                LocalDateTime.now().withSecond(0).truncatedTo(ChronoUnit.SECONDS),
                channelId
        ).getBody();
        if (dailyElectricityDtos == null) {
            dailyElectricityDtos = Collections.emptyList();
        }
        dailyElectricityDtos = dailyElectricityDtos.stream()
                .filter(dailyElectricityDto -> dailyElectricityDto.getTime().isAfter(LocalDateTime.now().minusDays(8)))
                .collect(Collectors.toList());
        model.addAttribute("recent7DaysElectricities", dailyElectricityDtos);
        model.addAttribute("recent24HoursElectricites", hourlyElectricityDtos);
        return ResponseEntity.ok(new DailyElectricitiesDto(dailyElectricityDtos, hourlyElectricityDtos));
    }

}
