package live.ioteatime.frontservice.service;

import live.ioteatime.frontservice.adaptor.ModbusSensorAdaptor;
import live.ioteatime.frontservice.adaptor.PlaceAdaptor;
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
    private final PlaceAdaptor placeAdaptor;
    private final ModbusSensorAdaptor modbusSensorAdaptor;

    public void initDailyReport(Model model) {
        OrganizationResponse organizationResponse = userAdaptor.getOrganization().getBody();
        if (Objects.isNull(organizationResponse)) {
            throw new UnauthorizedAccessException("organization not found");
        }
        List<PlaceDto> placeDtos = placeAdaptor.getPlacesByOrganizationId(organizationResponse.getId()).getBody();

        Map<Integer, List<ChannelDto>> placeDtoListMap = new HashMap<>();

        List<ChannelDto> mainChannelDtos = new ArrayList<>();

        assert placeDtos != null;
        for (PlaceDto p : placeDtos) {
            List<ChannelDto> channelDtos = modbusSensorAdaptor.getChannelsByPlaceId(p.getId()).getBody();

            for (ChannelDto channelDto : channelDtos) {
                if (channelDto.getChannelName().equals("main")) {
                    mainChannelDtos.add(channelDto);
                }
            }
            placeDtoListMap.put(p.getId(), channelDtos);
        }

        model.addAttribute("budget", organizationResponse.getElectricityBudget());
        model.addAttribute("places", placeDtos);
        model.addAttribute("placeMap", placeDtoListMap);


        List<DailyElectricityDto> mainDailyElectricityDtos = new ArrayList<>();
        List<DailyElectricityDto> mainHourlyElectricityDtos = new ArrayList<>();

        for (ChannelDto channelDto : mainChannelDtos) {
            List<DailyElectricityDto> dailyElectricityDtos = userAdaptor.getDailyElectricities(
                    LocalDateTime.now().toLocalDate().atTime(LocalTime.MIDNIGHT),
                    channelDto.getId()
            ).getBody();
            if (mainDailyElectricityDtos.isEmpty()) {
                mainDailyElectricityDtos.addAll(dailyElectricityDtos);
            } else {
                for (int i = 0; i < mainDailyElectricityDtos.size(); i++) {
                    mainDailyElectricityDtos.get(i).setKwh(mainDailyElectricityDtos.get(i).getKwh() + dailyElectricityDtos.get(i).getKwh());
                    mainDailyElectricityDtos.get(i).setBill(mainDailyElectricityDtos.get(i).getBill() + dailyElectricityDtos.get(i).getBill());
                }
            }

            List<DailyElectricityDto> hourlyElectricityDtos = userAdaptor.getDailyElectricities(
                    LocalDateTime.now().withSecond(0).truncatedTo(ChronoUnit.SECONDS),
                    channelDto.getId()).getBody();
            if (mainHourlyElectricityDtos.isEmpty()) {
                mainHourlyElectricityDtos.addAll(hourlyElectricityDtos);
            } else {
                for (int i = 0; i < hourlyElectricityDtos.size(); i++) {
                    mainHourlyElectricityDtos.get(i).setKwh(mainHourlyElectricityDtos.get(i).getKwh() + hourlyElectricityDtos.get(i).getKwh());
                    mainHourlyElectricityDtos.get(i).setBill(mainHourlyElectricityDtos.get(i).getBill() + hourlyElectricityDtos.get(i).getBill());
                }
            }
        }

        mainDailyElectricityDtos = mainDailyElectricityDtos.stream()
                .filter(dailyElectricityDto -> dailyElectricityDto.getTime().isAfter(LocalDateTime.now().minusDays(8)))
                .collect(Collectors.toList());

        model.addAttribute("recent7DaysElectricities", mainDailyElectricityDtos);
        model.addAttribute("recent24HoursElectricites", mainHourlyElectricityDtos);
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
