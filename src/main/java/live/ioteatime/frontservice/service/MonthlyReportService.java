package live.ioteatime.frontservice.service;

import feign.FeignException;
import live.ioteatime.frontservice.adaptor.UserAdaptor;
import live.ioteatime.frontservice.dto.*;
import live.ioteatime.frontservice.dto.response.OrganizationResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
public class MonthlyReportService {
    private final UserAdaptor userAdaptor;

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

    /**
     * localDateTime의 달의 월 단위 전력량을 반환
     *
     * @param localDateTime 요청 월 말 날짜
     * @return MonthlyElectricitiesDto
     */
    public MonthlyElectricitiesDto getElectricityByMonth(LocalDateTime localDateTime) {
        List<ChannelDto> channelDtos = getAllChannelListByPlaceIds();
        List<ChannelDto> mainChannelIds = channelDtos.stream()
                .filter(channelDto -> channelDto.getChannelName().equalsIgnoreCase("main"))
                .collect(Collectors.toList());

        MonthlyElectricitiesDto monthlyElectricitiesDto = new MonthlyElectricitiesDto();
        monthlyElectricitiesDto.setTime(localDateTime);
        monthlyElectricitiesDto.setKwh(getMonthlyKwh(mainChannelIds, localDateTime));

        List<DailyElectricityDto> dailyElectricityDtos = getDailyElectricities(mainChannelIds, localDateTime);
        monthlyElectricitiesDto.setDailyElectricityDtos(filterCurrentMonth(dailyElectricityDtos, localDateTime));

        return monthlyElectricitiesDto;
    }

    private Long getMonthlyKwh(List<ChannelDto> mainChannelIds, LocalDateTime dateTime) {
        return mainChannelIds.stream()
                .flatMap(channelDto -> fetchMonthlyElectricity(dateTime, channelDto.getId()))
                .collect(Collectors.groupingBy(MonthlyElectricityDto::getTime, Collectors.summingLong(MonthlyElectricityDto::getKwh)))
                .getOrDefault(dateTime, 0L);
    }

    private Stream<MonthlyElectricityDto> fetchMonthlyElectricity(LocalDateTime dateTime, Integer channelId) {
        try {
            MonthlyElectricityDto result = userAdaptor.getMonthlyElectricity(dateTime, channelId).getBody();
            return Optional.ofNullable(result).stream();
        } catch (FeignException.NotFound e) {
            return Stream.empty();
        }
    }

    private List<DailyElectricityDto> getDailyElectricities(List<ChannelDto> mainChannelIds, LocalDateTime dateTime) {
        Map<LocalDateTime, Long> dailyElectricitySummed = mainChannelIds.stream()
                .flatMap(channelDto -> fetchDailyElectricities(dateTime, channelDto.getId()))
                .collect(Collectors.groupingBy(DailyElectricityDto::getTime, Collectors.summingLong(DailyElectricityDto::getKwh)));

        return dailyElectricitySummed.entrySet().stream()
                .map(entry -> new DailyElectricityDto(entry.getKey(), entry.getValue()))
                .sorted(Comparator.comparing(DailyElectricityDto::getTime))
                .collect(Collectors.toList());
    }

    private Stream<DailyElectricityDto> fetchDailyElectricities(LocalDateTime dateTime, Integer channelId) {
        try {
            List<DailyElectricityDto> results = userAdaptor.getDailyElectricities(dateTime, channelId).getBody();
            return Optional.ofNullable(results).stream().flatMap(Collection::stream);
        } catch (FeignException.NotFound e) {
            return Stream.empty();
        }
    }

    private List<DailyElectricityDto> filterCurrentMonth(List<DailyElectricityDto> electricityDtos, LocalDateTime dateTime) {
        return electricityDtos.stream()
                .filter(d -> d.getTime().getMonth() == dateTime.getMonth())
                .collect(Collectors.toList());
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
