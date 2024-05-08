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
        Map<LocalDateTime, Long> monthlyElectricitiesDtoMap = mainChannelIds.stream()
                .flatMap(channelDto -> {
                    try {
                        MonthlyElectricityDto monthlyElectricityDto = userAdaptor.getMonthlyElectricity(localDateTime, channelDto.getId()).getBody();
                        return Optional.ofNullable(monthlyElectricityDto).stream();
                    } catch (FeignException.NotFound e) {
                        return Stream.empty();
                    }
                })
                .collect(Collectors.groupingBy(
                        MonthlyElectricityDto::getTime,
                        Collectors.summingLong(MonthlyElectricityDto::getKwh)
                ));

        monthlyElectricitiesDto.setTime(localDateTime);
        monthlyElectricitiesDto.setKwh(monthlyElectricitiesDtoMap.get(localDateTime));

        // 시간을 기준으로 kwh 값을 합산하여 Map으로 수집
        Map<LocalDateTime, Long> dailyElectricitySummed = mainChannelIds.stream()
                .flatMap(channelDto -> {
                    try{
                        return Objects.requireNonNull(
                                userAdaptor.getDailyElectricities(localDateTime, channelDto.getId()).getBody()
                        ).stream();
                    } catch (FeignException.NotFound e) {
                        return Stream.empty();
                    }
                })
                .collect(Collectors.groupingBy(DailyElectricityDto::getTime, Collectors.summingLong(DailyElectricityDto::getKwh)));

        // Map에서 DailyElectricityDto 리스트로 변환
        List<DailyElectricityDto> dailyElectricityDtos = dailyElectricitySummed.entrySet().stream()
                .map(entry -> new DailyElectricityDto(entry.getKey(), entry.getValue()))
                .sorted(Comparator.comparing(DailyElectricityDto::getTime))
                .collect(Collectors.toList());

        monthlyElectricitiesDto.setDailyElectricityDtos(
                dailyElectricityDtos.stream()
                        .filter(dailyElectricityDto -> dailyElectricityDto.getTime().getMonth().equals(localDateTime.getMonth()))
                        .collect(Collectors.toList())
        );
        return monthlyElectricitiesDto;
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
