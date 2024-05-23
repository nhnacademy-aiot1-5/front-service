package live.ioteatime.frontservice.service;

import live.ioteatime.frontservice.adaptor.ElectricityAdaptor;
import live.ioteatime.frontservice.adaptor.UserAdaptor;
import live.ioteatime.frontservice.dto.RealtimeElectricityResponseDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

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
}
