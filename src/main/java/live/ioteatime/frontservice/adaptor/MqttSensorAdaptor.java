package live.ioteatime.frontservice.adaptor;

import live.ioteatime.frontservice.dto.request.MqttSensorRequest;
import live.ioteatime.frontservice.dto.request.TopicRequest;
import live.ioteatime.frontservice.dto.response.GetMqttSensorResponse;
import live.ioteatime.frontservice.dto.response.GetTopicResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@FeignClient(value = "gateway-service", contextId = "mqtt-sensor-adaptor")
public interface MqttSensorAdaptor {
    @GetMapping("/api/sensors/mqtt/supported")
    ResponseEntity<List<GetMqttSensorResponse>> getSupportedMqttSensors();

    @GetMapping("/api/sensors/mqtt/list")
    ResponseEntity<List<GetMqttSensorResponse>> getUserOrganizationMqttSensors();

    @GetMapping("/api/sensors/mqtt/{sensorId}")
    ResponseEntity<GetMqttSensorResponse> getMqttSensor(@PathVariable("sensorId") int sensorId);

    @PostMapping("/api/sensors/mqtt")
    ResponseEntity<String> addMqttSensor(MqttSensorRequest addMqttSensorRequest);

    @PutMapping("/api/sensors/mqtt/{sensorId}/update")
    ResponseEntity<String> updateMqttSensor(@PathVariable("sensorId") int sensorId, MqttSensorRequest request);

    @DeleteMapping("/api/sensors/mqtt/{sensorId}")
    ResponseEntity<String> deleteMqttSensor(@PathVariable("sensorId") int sensorId);

    @GetMapping("/api/sensors/mqtt/{sensorId}/topics")
    ResponseEntity<List<GetTopicResponse>> getTopicsBySensorId(@PathVariable("sensorId") int sensorId);

    @PostMapping("/api/sensors/mqtt/{sensorId}/topics")
    ResponseEntity<String> addTopic(@PathVariable("sensorId") int sensorId, @RequestBody TopicRequest topicRequest);

    @PutMapping("/api/sensors/mqtt/{sensorId}/topics/{topicId}/update")
    ResponseEntity<String> updateTopic(@PathVariable("sensorId") int sensorId, @PathVariable("topicId") int topicId,
                                       @RequestBody TopicRequest topicRequest);

    @DeleteMapping("/api/sensors/mqtt/{sensorId}/topics/{topicId}")
    ResponseEntity<String> deleteTopic(@PathVariable("sensorId") int sensorId, @PathVariable("topicId") int topicId);

}
