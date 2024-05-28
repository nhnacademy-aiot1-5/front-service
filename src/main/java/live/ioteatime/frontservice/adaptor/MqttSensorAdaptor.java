package live.ioteatime.frontservice.adaptor;

import live.ioteatime.frontservice.dto.request.MqttSensorRequest;
import live.ioteatime.frontservice.dto.request.TopicRequest;
import live.ioteatime.frontservice.dto.response.GetMqttSensorResponse;
import live.ioteatime.frontservice.dto.response.GetTopicResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Mqtt 센서에 관련된 기능을 수행하는 Adaptor 클래스 입니다.
 */
@FeignClient(value = "gateway-service", contextId = "mqtt-sensor-adaptor")
public interface MqttSensorAdaptor {
    /**
     * ioteatime 서비스가 지원하는 Mqtt 센서 리스트를 가져옵니다.
     * @return (id, 이름, 모델 이름, ip, port, Alive, PlaceDto) 리스트를 반환합니다.
     *          Alive - (UP, DOWN)
     *          PlaceDto - (장소 id, 장소 이름, 조직 id, OrganizationResponse)
     *                      OrganizationResponse - (id, name, 설정 요금, 조직 코드)
     */
    @GetMapping("/api/sensors/mqtt/supported")
    ResponseEntity<List<GetMqttSensorResponse>> getSupportedMqttSensors();

    /**
     * 사용자의 조직에 등록된 Mqtt 센서를 검색합니다.
     * @return (id, 이름, 모델 이름, ip, port, Alive, PlaceDto) 리스트를 반환합니다.
     *          Alive - (UP, DOWN)
     *          PlaceDto - (장소 id, 장소 이름, 조직 id, OrganizationResponse)
     *                      OrganizationResponse - (id, name, 설정 요금, 조직 코드)
     */
    @GetMapping("/api/sensors/mqtt/list")
    ResponseEntity<List<GetMqttSensorResponse>> getUserOrganizationMqttSensors();

    /**
     * 지정된 센서 id에 해당하는 Mqtt 센서를 검색합니다.
     * @param sensorId 검색할 센서의 id
     * @return (id, 이름, 모델 이름, ip, port, Alive, PlaceDto)를 반환합니다.
     *          Alive - (UP, DOWN)
     *          PlaceDto - (장소 id, 장소 이름, 조직 id, OrganizationResponse)
     *                      OrganizationResponse - (id, name, 설정 요금, 조직 코드)
     */
    @GetMapping("/api/sensors/mqtt/{sensorId}")
    ResponseEntity<GetMqttSensorResponse> getMqttSensor(@PathVariable("sensorId") int sensorId);

    /**
     * Mqtt 센서를 등록합니다.
     * @param addMqttSensorRequest 등록할 Mqtt 정보를 파라미터로 받아옵니다.
     *                             (모델명, 장소 id, 센서 이름, ip, port, 토픽, 상세)
     * @return 센서 등록 결과를 반환합니다.
     */
    @PostMapping("/api/sensors/mqtt")
    ResponseEntity<String> addMqttSensor(MqttSensorRequest addMqttSensorRequest);

    /**
     * Mqtt 센서를 업데이트 합니다.
     * @param sensorId 수정 하고 싶은 센서의 id 입니다.
     * @param request 수정할 센서의 정보 입니다. - (모델명, 장소 id, 센서 이름, ip, port, 토픽, 상세)
     * @return 업데이트 결과를 반환합니다.
     */
    @PutMapping("/api/sensors/mqtt/{sensorId}")
    ResponseEntity<String> updateMqttSensor(@PathVariable("sensorId") int sensorId, MqttSensorRequest request);

    /**
     * Mqtt 센서를 삭제합니다.
     * @param sensorId 삭제할 센서의 ID 입니다.
     * @return 삭제 결과를 반환합니다.
     */
    @DeleteMapping("/api/sensors/mqtt/{sensorId}")
    ResponseEntity<String> deleteMqttSensor(@PathVariable("sensorId") int sensorId);

    /**
     * 센서 아이디로 토픽 리스트를 검색합니다.
     * @param sensorId 토픽 리스트를 검색할 때 사용되는 센서 ID입니다.
     * @return 토픽 리스트를 반환합니다. - (id, topic, description)
     */
    @GetMapping("/api/sensors/mqtt/{sensorId}/topics")
    ResponseEntity<List<GetTopicResponse>> getTopicsBySensorId(@PathVariable("sensorId") int sensorId);

    /**
     *  토픽을 추가합니다.
     * @param sensorId 토픽을 추가할 센서의 id 입니다.
     * @param topicRequest 추가할 토픽 정보를 파라미터로 받아옵니다. - (topic, description)
     * @return 토픽 추가 결과를 리턴합니다.
     */
    @PostMapping("/api/sensors/mqtt/{sensorId}/topics")
    ResponseEntity<String> addTopic(@PathVariable("sensorId") int sensorId, @RequestBody TopicRequest topicRequest);

    /**
     * 토픽을 업데이트합니다.
     * @param sensorId 토픽을 업데이트할 센서의 아이디입니다.
     * @param topicId 업데이트할 토픽의 id 입니다.
     * @param topicRequest 업데이트할 토픽의 정보입니다. - (topic, description)
     * @return 업데이트 결과를 리턴합니다.
     */
    @PutMapping("/api/sensors/mqtt/{sensorId}/topics/{topicId}")
    ResponseEntity<String> updateTopic(@PathVariable("sensorId") int sensorId, @PathVariable("topicId") int topicId,
                                       @RequestBody TopicRequest topicRequest);

    /**
     * 토픽을 삭제합니다.
     * @param sensorId 삭제할 토픽이 등록된 센서의 id 입니다.
     * @param topicId 삭제할 토픽의 id 입니다.
     * @return 삭제 결과를 리턴합니다.
     */
    @DeleteMapping("/api/sensors/mqtt/{sensorId}/topics/{topicId}")
    ResponseEntity<String> deleteTopic(@PathVariable("sensorId") int sensorId, @PathVariable("topicId") int topicId);

}
