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

@Slf4j
@Service
@RequiredArgsConstructor
public class ElectricityService {
    private final ElectricityAdaptor electricityAdaptor;
    private final UserAdaptor userAdaptor;

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
