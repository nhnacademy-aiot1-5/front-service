package live.ioteatime.frontservice.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import live.ioteatime.frontservice.adaptor.UserAdaptor;
import live.ioteatime.frontservice.dto.*;
import live.ioteatime.frontservice.dto.response.OrganizationResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MonthlyReportService {
    private final UserAdaptor userAdaptor;
    private final ObjectMapper objectMapper;

    public void initMonthlyReport(Model model) {
        OrganizationResponse organization = getOrganization();
        List<ChannelDto> channelDtos = getAllChannelListByPlaceIds();
        List<ChannelDto> mainChannelIds = channelDtos.stream()
                .filter(channelDto -> channelDto.getChannelName().equalsIgnoreCase("main"))
                .collect(Collectors.toList());

        Map<LocalDateTime, Long> summedKwhByTime = mainChannelIds.stream()
                .flatMap(c ->
                        Objects.requireNonNull(
                                userAdaptor.getMonthlyElectricities(LocalDateTime.now(), c.getId()).getBody()
                        ).stream()
                )
                .collect(Collectors.groupingBy(
                        MonthlyElectricityDto::getTime,
                        Collectors.summingLong(MonthlyElectricityDto::getKwh)
                ));

        List<MonthlyElectricityDto> monthlyElectricityDtos = summedKwhByTime.entrySet().stream()
                .map(entry -> new MonthlyElectricityDto(entry.getKey(), entry.getValue()))
                .sorted(Comparator.comparing(MonthlyElectricityDto::getTime))
                .collect(Collectors.toList());

        model.addAttribute("recent12monthElectricities", monthlyElectricityDtos);
        model.addAttribute("budget", organization.getElectricityBudget());
    }

    public MonthlyElectricityPageDto getElectricityByMonth(LocalDateTime localDateTime) throws JsonProcessingException {
        OrganizationResponse organization = userAdaptor.getOrganization().getBody();
        if (Objects.isNull(organization)) {
            throw new NullPointerException();
        }
        MonthlyElectricityPageDto monthlyElectricityPageDto =
                objectMapper.readValue(
                        userAdaptor.getMonthlyElectricity(localDateTime, organization.getId()).getBody(),
                        MonthlyElectricityPageDto.class
                );
        ResponseEntity<List<DailyElectricityDto>> dailyResponse =
                userAdaptor.getDailyElectricities(localDateTime, organization.getId());
        List<DailyElectricityDto> dailyElectricityDtos = Optional.ofNullable(dailyResponse.getBody())
                .orElse(Collections.emptyList());

        monthlyElectricityPageDto.setDailyElectricityDtos(
                dailyElectricityDtos.stream()
                        .filter(dailyElectricityDto -> dailyElectricityDto.getTime().getMonth().equals(localDateTime.getMonth()))
                        .collect(Collectors.toList())
        );
        monthlyElectricityPageDto.setMonthlyElectricityDtos(
                userAdaptor.getMonthlyElectricities(localDateTime, organization.getId()).getBody()
        );
        return monthlyElectricityPageDto;
    }

    private OrganizationResponse getOrganization() {
        OrganizationResponse organization = userAdaptor.getOrganization().getBody();
        if (Objects.isNull(organization)) {
            throw new NullPointerException("couldn't find the Organization");
        }
        return organization;
    }

    private List<PlaceDto> getPlacesByOrganizationId() {
        OrganizationResponse organizationResponse = getOrganization();
        List<PlaceDto> placeDtos = userAdaptor.getPlacesByOrganizationId(organizationResponse.getId()).getBody();
        if (Objects.isNull(placeDtos)) {
            throw new NullPointerException("couldn't find places");
        }
        return placeDtos;
    }

    private List<ChannelDto> getAllChannelListByPlaceIds() {
        List<PlaceDto> placeDtos = getPlacesByOrganizationId();
        List<ChannelDto> channels = new ArrayList<>();
        placeDtos.stream()
                .map(PlaceDto::getId)
                .forEach(integer -> {
                    List<ChannelDto> channelDtos = userAdaptor.getChannelsByPlaceId(integer).getBody();
                    if (Objects.isNull(channelDtos)) {
                        throw new NullPointerException("couldn't find channels");
                    }
                    channels.addAll(channelDtos);
                });
        return channels;
    }
}
