package live.ioteatime.frontservice.adaptor;

import live.ioteatime.frontservice.dto.request.AddMqttSensorRequest;
import live.ioteatime.frontservice.dto.response.GetMqttSensorResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;

@FeignClient(value = "gateway-service", contextId = "mqtt-sensor-adaptor")
public interface MqttSensorAdaptor {
    @GetMapping("/api/sensors/mqtt/list")
    ResponseEntity<List<GetMqttSensorResponse>> getUserOrganizationMqttSensors();

    @GetMapping("/api/sensors/mqtt/supported")
    ResponseEntity<List<GetMqttSensorResponse>> getSupportedMqttSensors();

    @PostMapping("/api/sensors/mqtt")
    ResponseEntity<String> addMqttSensor(AddMqttSensorRequest addMqttSensorRequest);

}
