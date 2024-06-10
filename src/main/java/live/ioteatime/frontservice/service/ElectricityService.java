package live.ioteatime.frontservice.service;

import live.ioteatime.frontservice.adaptor.ElectricityAdaptor;
import live.ioteatime.frontservice.adaptor.UserAdaptor;
import live.ioteatime.frontservice.dto.DailyElectricityDto;
import live.ioteatime.frontservice.dto.RealtimeElectricityResponseDto;
import live.ioteatime.frontservice.dto.response.ElectricityResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 전력(w) 관련 서비스를 제공하는 클래스 입니다.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class ElectricityService {
    private final ElectricityAdaptor electricityAdaptor;
    private final UserAdaptor userAdaptor;

    /**
     * 실시간 전력(w)이 가장 높은 채널 10개를 찾는 메서드 입니다.
     * @return (장소명, 채널명, w)들의 리스트를 반환합니다.
     */
    public List<RealtimeElectricityResponseDto> getTop10Electricity(){
        int organizationId = userAdaptor.getUser().getBody().getOrganization().getId();

        List<RealtimeElectricityResponseDto> response = electricityAdaptor.getRealtimeElectricity(organizationId).getBody();
        response = response.stream()
                .sorted(Comparator.comparing(RealtimeElectricityResponseDto::getW).reversed())
                .limit(10)
                .collect(Collectors.toList());
        return response;
    }

    public List<DailyElectricityDto> getCumulativeBills() {
        LocalDateTime time = LocalDateTime.now().withHour(0).withMinute(0).withSecond(0).withNano(0);
        List<DailyElectricityDto> electricityDtoList = userAdaptor.getDailyElectricities(time,-1).getBody();
        Long bill = 0L;
        List<DailyElectricityDto> cumulativeBillsUntilToday = new ArrayList<>();
        for(DailyElectricityDto e : electricityDtoList) {
            if(e.getTime().isBefore(time.withDayOfMonth(1).withHour(0).withMinute(0).withSecond(0).withNano(0))) {
                continue;
            }
            if (e.getTime().isEqual(time)) {
                break;
            }
            bill += e.getBill();
            e.setBill(bill);
            cumulativeBillsUntilToday.add(e);
        }
        return cumulativeBillsUntilToday;
    }

    public List<ElectricityResponse> getCumulativeBillPredictions() {
        LocalDateTime time = LocalDateTime.now().withHour(0).withMinute(0).withSecond(0).withNano(0);
        List<ElectricityResponse> electricityDtoList = electricityAdaptor
                .getMonthlyPredictedValues(1, time).getBody();

        Long bill = 0L;
        List<ElectricityResponse> result = new ArrayList<>();
        for(ElectricityResponse e : electricityDtoList) {
            bill += e.getBill();
            e.setBill(bill);
            result.add(e);
        }
        return result;
    }
}
