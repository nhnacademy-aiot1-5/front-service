package live.ioteatime.frontservice.service;

import feign.FeignException;
import live.ioteatime.frontservice.adaptor.ElectricityAdaptor;
import live.ioteatime.frontservice.adaptor.ModbusSensorAdaptor;
import live.ioteatime.frontservice.adaptor.PlaceAdaptor;
import live.ioteatime.frontservice.adaptor.UserAdaptor;
import live.ioteatime.frontservice.dto.*;
import live.ioteatime.frontservice.dto.response.OrganizationResponse;
import live.ioteatime.frontservice.dto.response.ElectricityResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import java.time.LocalDateTime;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * 월 별 레포트 관련 서비스를 제공하는 클래스 입니다.
 */
@Service
@RequiredArgsConstructor
public class MonthlyReportService {
    private final UserAdaptor userAdaptor;
    private final PlaceAdaptor placeAdaptor;
    private final ModbusSensorAdaptor modbusSensorAdaptor;
    private final ElectricityAdaptor electricityAdaptor;

    /**
     * 월 별 레포트의 초기 설정을 담당 하는 메소드 입니다.
     * 최근 12개월 간 채널별 월간 전기 사용량(kwh)을 계산 하고 모델에 저장 합니다.
     *
     * @param model 월 별 전력 레포트를 표시하기 위한 정보가 들어간 모델 입니다.
     *              recent12monthElectricities - 최근 12개월 간 채널별 월간 전기 사용량(kwh)
     *              MonthlyElectricityDto - (time, kwh, bill)
     *              budget - 사용자가 소속된 조직의 설정 요금
     */
    public void initMonthlyReport(Model model) {
        OrganizationResponse organization = getOrganization();
        List<ChannelDto> channelDtos = getAllChannelListByPlaceIds();
        List<ChannelDto> mainChannelIds = channelDtos.stream()
                .filter(channelDto -> channelDto.getId() == -1)
                .collect(Collectors.toList());

        Map<LocalDateTime, Double> summedKwhByTime = mainChannelIds.stream()
                .flatMap(c ->
                        Objects.requireNonNull(
                                userAdaptor.getMonthlyElectricities(LocalDateTime.now(), c.getId()).getBody()
                        ).stream()
                )
                .collect(Collectors.groupingBy(
                        MonthlyElectricityDto::getTime,
                        Collectors.summingDouble(MonthlyElectricityDto::getKwh)
                ));

        List<MonthlyElectricityDto> monthlyElectricityDtos =
                summedKwhByTime.entrySet().stream()
                        .map(entry -> {
                            MonthlyElectricityDto monthlyElectricityDto = new MonthlyElectricityDto();
                            monthlyElectricityDto.setTime(entry.getKey());
                            monthlyElectricityDto.setKwh(entry.getValue());
                            return monthlyElectricityDto;
                        })
                        .sorted(Comparator.comparing(MonthlyElectricityDto::getTime))
                        .collect(Collectors.toList());
        List<ElectricityResponse> nextMonthElectricityResponses = electricityAdaptor
                .getNextMonthlyPredictedValues(LocalDateTime.now(), organization.getId()).getBody();
        List<ElectricityResponse> thisMonthElectricityResponses = electricityAdaptor
                .getThisMonthlyPredictedValues(LocalDateTime.now(), organization.getId()).getBody();

        double nextMonthKwh = Optional.ofNullable(nextMonthElectricityResponses)
                .orElseGet(Collections::emptyList)
                .stream()
                .mapToDouble(ElectricityResponse::getKwh)
                .sum();

        double thisMonthKwh = Optional.ofNullable(thisMonthElectricityResponses)
                .orElseGet(Collections::emptyList)
                .stream()
                .mapToDouble(ElectricityResponse::getKwh)
                .sum();

        model.addAttribute("nextMonthCharge", electricityAdaptor.getChargeByKwh(nextMonthKwh).getBody());
        model.addAttribute("thisMonthCharge", electricityAdaptor.getChargeByKwh(thisMonthKwh).getBody());
        model.addAttribute("recent12monthElectricities", monthlyElectricityDtos);
        model.addAttribute("budget", organization.getElectricityBudget());
    }

    /**
     * 요청 일에 해당 하는 달 동안 사용한 총 전력량/요금과 일 단위 기록을 검색 합니다.
     *
     * @param localDateTime 요청일 (보통은 해당 달의 마지막 날짜)
     * @return MonthlyElectricitiesDto - (time, kwh, bill, List<DailyElectricityDto>)
     * time - 요청일
     * kwh - 요청일 까지 사용한 전력량
     * bill - 요청일 까지 사용한 전기 요금
     * DailyElectricityDto - 일 단위 기록 (time, kwh, bill)
     */
    public MonthlyElectricitiesDto getElectricityByMonth(LocalDateTime localDateTime) {
        List<ChannelDto> channelDtos = getAllChannelListByPlaceIds();
        List<ChannelDto> mainChannelIds = channelDtos.stream()
                .filter(channelDto -> channelDto.getId() == -1)
                .collect(Collectors.toList());

        MonthlyElectricitiesDto monthlyElectricitiesDto = new MonthlyElectricitiesDto();
        monthlyElectricitiesDto.setTime(localDateTime);
        monthlyElectricitiesDto.setKwh(getMonthlyKwh(mainChannelIds, localDateTime));

        List<DailyElectricityDto> dailyElectricityDtos = getDailyElectricities(mainChannelIds, localDateTime);
        monthlyElectricitiesDto.setDailyElectricityDtos(filterCurrentMonth(dailyElectricityDtos, localDateTime));

        return monthlyElectricitiesDto;
    }

    /**
     * 요청일로 검색한 한달간 채널별 전기 사용량(kwh)을 전부 더합니다. default 값은 0L 입니다.
     *
     * @param mainChannelIds 채널 명이 "main"인 채널들의 id가 담긴 리스트 입니다.
     * @param dateTime       요청일 입니다.
     * @return 계산 결과(kwh)를 반환 합니다.
     */
    private Double getMonthlyKwh(List<ChannelDto> mainChannelIds, LocalDateTime dateTime) {
        return mainChannelIds.stream()
                .flatMap(channelDto -> fetchMonthlyElectricity(dateTime, channelDto.getId()))
                .collect(Collectors.groupingBy(MonthlyElectricityDto::getTime, Collectors.summingDouble(MonthlyElectricityDto::getKwh)))
                .getOrDefault(dateTime, 0.0);
    }

    /**
     * 해당 일과, 채널에 대한 전력 사용량을 월별 테이블에서 검색 하는 메소드 입니다.
     *
     * @param dateTime  검색 하고 싶은 날짜 입니다
     * @param channelId 검색 하고 싶은 채널의 id 입니다.
     * @return 검색 결과를 Stream으로 반환 합니다. 없다면 빈 값을 반환 합니다.
     */
    private Stream<MonthlyElectricityDto> fetchMonthlyElectricity(LocalDateTime dateTime, int channelId) {
        try {
            MonthlyElectricityDto result = userAdaptor.getMonthlyElectricity(dateTime, channelId).getBody();
            return Optional.ofNullable(result).stream();
        } catch (FeignException.NotFound e) {
            return Stream.empty();
        }
    }


    /**
     * 요청 일의 첫 날 부터 요청일 까지 일 단위로 하루 동안 사용한 총 전기 사용 정보를 계산 하는 메소드 입니다.
     *
     * @param mainChannelIds 채널 명이 "main"인 채널 리스트
     * @param dateTime       요청일
     * @return 해당 달동안 사용한 전체 전기 사용 정보를 일 단위 리스트로 반환 합니다.
     */
    private List<DailyElectricityDto> getDailyElectricities(List<ChannelDto> mainChannelIds, LocalDateTime dateTime) {
        Map<LocalDateTime, DailyElectricityDto> dailyElectricitySummed = mainChannelIds.stream()
                .flatMap(channelDto -> fetchDailyElectricities(dateTime, channelDto.getId()))
                .collect(Collectors.toMap(
                        DailyElectricityDto::getTime,
                        Function.identity(),
                        (dto1, dto2) -> new DailyElectricityDto(
                                dto1.getTime(),
                                dto1.getKwh() + dto2.getKwh(),
                                dto1.getBill() + dto2.getBill()
                        )
                ));

        return dailyElectricitySummed.values().stream()
                .sorted(Comparator.comparing(DailyElectricityDto::getTime))
                .collect(Collectors.toList());
    }

    /**
     * 해당 채널과 날짜에 대한 전기 사용 정보를 일별 테이블에서 검색 합니다.
     *
     * @param dateTime  검색할 날짜 입니다.
     * @param channelId 검색할 채널 id 입니다.
     * @return 검색 결과를 반환 합니다. 검색 결과가 없다면 빈 Stream을 반환 합니다.
     */
    private Stream<DailyElectricityDto> fetchDailyElectricities(LocalDateTime dateTime, int channelId) {
        try {
            List<DailyElectricityDto> results = userAdaptor.getDailyElectricities(dateTime, channelId).getBody();
            return Optional.ofNullable(results).stream().flatMap(Collection::stream);
        } catch (FeignException.NotFound e) {
            return Stream.empty();
        }
    }

    /**
     * electricityDtos 요소의 달이 dateTime의 달과 같은 요소만 필터링 합니다.
     *
     * @param electricityDtos 검색할 Dto 리스트 입니다.
     * @param dateTime        LocalDateTime 타입의 필터링 하고 싶은 달 입니다.
     * @return 필터링 결과를 반환 합니다.
     */
    private List<DailyElectricityDto> filterCurrentMonth(List<DailyElectricityDto> electricityDtos, LocalDateTime dateTime) {
        return electricityDtos.stream()
                .filter(d -> d.getTime().getMonth() == dateTime.getMonth())
                .collect(Collectors.toList());
    }

    /**
     * 유저의 조직 정보를 검색 하는 메소드 입니다.
     * 조직 정보를 찾지 못한 경우 NullPointerException을 던집니다.
     *
     * @return (id, 조직명, 설정 요금, 조직 코드)
     */
    private OrganizationResponse getOrganization() {
        OrganizationResponse organization = userAdaptor.getOrganization().getBody();
        if (Objects.isNull(organization)) {
            throw new NullPointerException("couldn't find the Organization");
        }
        return organization;
    }

    /**
     * 사용자의 조직에 등록된 장소 리스트를 검색하는 메소드 입니다.
     * 등록된 장소가 없는 경우 NullPointerException을 던집니다.
     *
     * @return 조직에 등록된 장소 리스트 입니다. (id, 장소명, 조직 id, OrganizationResponse)
     * OrganizationResponse - (id, 조직명, 설정 요금, 조직 코드)
     */
    private List<PlaceDto> getPlacesByOrganizationId() {
        OrganizationResponse organizationResponse = getOrganization();
        List<PlaceDto> placeDtos = placeAdaptor.getPlacesByOrganizationId(organizationResponse.getId()).getBody();
        if (Objects.isNull(placeDtos)) {
            throw new NullPointerException("couldn't find places");
        }
        return placeDtos;
    }

    /**
     * 장소에 등록된 modbus 센서들의 채널 리스트를 검색합니다.
     * 등록된 채널이 없다면 NullPointerException을 던집니다.
     *
     * @return 장소에 등록된 채널들에 대한 정보 리스트를 반환합니다.
     * ChannelDto - (id, 채널명, 주소, 타입, fc, 센서 id, 장소 id, 장소명, ModbusSensorResponse, PlaceDto)
     * ModbusSensorResponse - (id, 센서명, 모델명, ip, port, 채널 수, Alive)
     * Alive - (UP, DOWN)
     * PlaceDto - (id, 장소명, 조직 id, OrganizationResponse)
     * OrganizationResponse - (id, 조직명, 설정 요금, 조직 코드)
     */
    private List<ChannelDto> getAllChannelListByPlaceIds() {
        List<PlaceDto> placeDtos = getPlacesByOrganizationId();
        List<ChannelDto> channels = new ArrayList<>();
        placeDtos.stream()
                .map(PlaceDto::getId)
                .forEach(integer -> {
                    List<ChannelDto> channelDtos = modbusSensorAdaptor.getChannelsByPlaceId(integer).getBody();
                    if (Objects.isNull(channelDtos)) {
                        throw new NullPointerException("couldn't find channels");
                    }
                    channels.addAll(channelDtos);
                });
        return channels;
    }
}
