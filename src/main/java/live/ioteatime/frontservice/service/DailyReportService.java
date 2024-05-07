package live.ioteatime.frontservice.service;

import live.ioteatime.frontservice.adaptor.UserAdaptor;
import live.ioteatime.frontservice.dto.ChannelDto;
import live.ioteatime.frontservice.dto.DailyElectricitiesDto;
import live.ioteatime.frontservice.dto.DailyElectricityDto;
import live.ioteatime.frontservice.dto.PlaceDto;
import live.ioteatime.frontservice.dto.response.OrganizationResponse;
import live.ioteatime.frontservice.exception.UnauthorizedAccessException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DailyReportService {
    private final UserAdaptor userAdaptor;

    public void initDailyReport(Model model) {
        OrganizationResponse organizationResponse = userAdaptor.getOrganization().getBody();
        if (Objects.isNull(organizationResponse)) {
            throw new UnauthorizedAccessException("organization not found");
        }
        List<PlaceDto> placeDtos = userAdaptor.getPlacesByOrganizationId(organizationResponse.getId()).getBody();

        Map<Integer, List<ChannelDto>> placeDtoListMap = new HashMap<>();

        assert placeDtos != null;
        for (PlaceDto p : placeDtos) {
            List<ChannelDto> channelDtos = userAdaptor.getChannelsByPlaceId(p.getId()).getBody();
            placeDtoListMap.put(p.getId(), channelDtos);
        }

        model.addAttribute("budget", organizationResponse.getElectricityBudget());
        model.addAttribute("places", placeDtos);
        model.addAttribute("placeMap", placeDtoListMap);

        this.getDailyElectricites(1, model);
    }

    public DailyElectricitiesDto getDailyElectricites(int channelId, Model model) {
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
        return new DailyElectricitiesDto(dailyElectricityDtos, hourlyElectricityDtos);
    }

}
