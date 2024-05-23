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

/**
 * 일별 레포트 관련 서비스를 제공하는 클래스 입니다.
 */
@Service
@RequiredArgsConstructor
public class DailyReportService {
    private final UserAdaptor userAdaptor;
    private final PlaceAdaptor placeAdaptor;
    private final ModbusSensorAdaptor modbusSensorAdaptor;

    /**
     * 일별 레포트의 초기 설정을 담당하는 메소드 입니다.
     * 조직의 설정 요금, 조직에 등록된 장소 리스트, 각 장소에 등록된 채널 정보들, 장소별 최근 7일/24시간의 총 전력 사용량(kwh)과 사용 요금 정보를 설정 합니다.
     * 유저가 속한 조직을 찾지 못한 경우, UnauthorizedAccessException을 던집니다.
     *
     * @param model 일 별 전력 레포트를 표시하기 위한 정보가 들어간 모델 입니다.
     *              budget - 조직의 설정 요금
     *              places - 조직에 등록된 장소 리스트
     *              placeMap - (장소 id, ChannelDto) 정보가 저장된 HashMap
     *                          ChannelDto - (id, 채널명, 주소, 타입, fc, 센서 id, 장소 id, 장소명, ModbusSensorResponse, PlaceDto)
     *                                        ModbusSensorResponse - (id, 센서명, 모델명, ip, port, 채널 수, Alive)
     *                                                                Alive - (UP, DOWN)
     *                                        PlaceDto - (id, 장소명, 조직 id, OrganizationResponse)
     *                                                    OrganizationResponse - (id, 조직명, 설정 요금, 조직 코드)
     *              recent7DaysElectricities - 최근 7일간 사용한 전력 사용량 정보 (time, kwh, bill)
     *              recent24HoursElectricites - 최근 24시간 사용한 전력 사용량 정보 (time, kwh, bill)
     */
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

            if (channelDtos != null && !channelDtos.isEmpty()) {
                for (ChannelDto channelDto : channelDtos) {
                    if ("main".equals(channelDto.getChannelName())) {
                        mainChannelDtos.add(channelDto);
                    }
                }
                placeDtoListMap.put(p.getId(), channelDtos);
            }
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
                int size = Math.min(mainDailyElectricityDtos.size(), dailyElectricityDtos.size());
                for (int i = 0; i < size; i++) {
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
                int size = Math.min(mainHourlyElectricityDtos.size(), hourlyElectricityDtos.size());
                for (int i = 0; i < size; i++) {
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

    /**
     * 채널별 지난 7일/24시간 동안 사용한 총 전력 사용량(kwh)과 요금 정보를 설정 합니다.
     * @param channelId 요금 정보를 검색할 채널 id 입니다.
     * @param model 검색한 요금 정보를 등록할 모델 입니다.
     * @return 일별/시간별 전력 정보가 저장된 리스트들의 튜플을 반환합니다.
     *         DailyElectricityDto - (time, kwh, bill)
     */
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
