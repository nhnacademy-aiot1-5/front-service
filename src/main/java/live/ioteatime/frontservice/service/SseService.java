package live.ioteatime.frontservice.service;

import live.ioteatime.frontservice.adaptor.OutlierAdaptor;
import live.ioteatime.frontservice.dto.Alert;
import live.ioteatime.frontservice.dto.DoorayMessageDto;
import live.ioteatime.frontservice.dto.OutlierDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SseService {
    private final OutlierAdaptor outlierAdaptor;

    public String getMessage(Alert alert) {
        return "경고: " + alert.getPlace() +
                "의 " +
                alert.getType() +
                "에서 이상치 " +
                alert.getOutlierValue() +
                "가 발생했습니다.";
    }

    public void saveAlert(Alert alert) {
        OutlierDto outlierDto = OutlierDto.builder()
                .organizationId(0)
                .type(alert.getType())
                .place(alert.getPlace())
                .outlierValue(alert.getOutlierValue())
                .flag(0)
                .build();
        outlierAdaptor.saveOutlier(outlierDto);
    }

    public List<OutlierDto> getOutliers(int organizationId) {
        List<OutlierDto> outlierDtos = Optional.ofNullable(outlierAdaptor.getOutliers(organizationId).getBody()).orElse(List.of());
        return outlierDtos.stream()
                .filter(outlierDto -> outlierDto.getFlag() == 0)
                .collect(Collectors.toList());
    }

    public void updateOutlier(int id, int flag) {
        outlierAdaptor.updateOutlier(id, flag);
    }

    public void turnSensorOn(String sensorName, String devEui) {
        outlierAdaptor.turnSensorOn(sensorName, devEui);
    }

    public void sendDoorayMessage(String botName, int id, String place, String type) {
        System.out.println("이거 실행됰");
        DoorayMessageDto doorayMessageDto = DoorayMessageDto.builder()
                .botName(botName)
                .text(id + ": " + place + " " + type + "resolved")
                .build();
        outlierAdaptor.sendDooray(doorayMessageDto);
    }

}
