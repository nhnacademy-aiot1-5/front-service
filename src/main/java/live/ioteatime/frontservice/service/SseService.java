package live.ioteatime.frontservice.service;

import live.ioteatime.frontservice.adaptor.OutlierAdaptor;
import live.ioteatime.frontservice.dto.Alert;
import live.ioteatime.frontservice.dto.OutlierDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SseService {
    private final OutlierAdaptor outlierAdaptor;

    public String getMessage(Alert alert) {
        return "경고: " + alert.getPlace() +
                "의 " +
                alert.getType() +
                "에서 이상치 " +
                alert.getValue() +
                "가 발생했습니다.";
    }

    public void saveAlert(Alert alert) {
        OutlierDto outlierDto = OutlierDto.builder()
                .organizationId(0)
                .type(alert.getType())
                .place(alert.getPlace())
                .outlierValue(alert.getValue())
                .flag(0)
                .build();
        outlierAdaptor.saveOutlier(outlierDto);
    }

    public List<OutlierDto> getOutliers(int organizationId) {
        return outlierAdaptor.getOutliers(organizationId).getBody();
    }

    public void updateOutlier(int id, int flag) {
        outlierAdaptor.updateOutlier(id, flag);
    }

    public void turnSensorOn(String sensorName, String devEui) {
        outlierAdaptor.turnSensorOn(sensorName, devEui);
    }

}
