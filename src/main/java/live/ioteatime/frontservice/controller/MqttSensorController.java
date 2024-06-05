package live.ioteatime.frontservice.controller;

import live.ioteatime.frontservice.adaptor.MqttSensorAdaptor;
import live.ioteatime.frontservice.adaptor.PlaceAdaptor;
import live.ioteatime.frontservice.adaptor.UserAdaptor;
import live.ioteatime.frontservice.dto.PlaceDto;
import live.ioteatime.frontservice.dto.request.MqttSensorRequest;
import live.ioteatime.frontservice.dto.request.TopicRequest;
import live.ioteatime.frontservice.dto.response.GetMqttSensorResponse;
import live.ioteatime.frontservice.dto.response.GetTopicResponse;
import live.ioteatime.frontservice.dto.response.GetUserResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/sensors/mqtt")
public class MqttSensorController {
    public static final String REDIRECT_SENSORS_MQTT = "redirect:/sensors/mqtt/";
    private final UserAdaptor userAdaptor;
    private final MqttSensorAdaptor mqttSensorAdaptor;
    private final PlaceAdaptor placeAdaptor;

    /**
     * Mqtt 센서 관리 페이지
     *
     * @param model
     * @return
     */
    @GetMapping
    public String getMqttSensorPage(Model model) {
        List<GetMqttSensorResponse> mqttSensorInfo = mqttSensorAdaptor.getUserOrganizationMqttSensors().getBody();
        model.addAttribute("mqttSensorInfo", mqttSensorInfo);
        return "sensor/sensor-mqtt";
    }

    /**
     * Mqtt 센서 등록 페이지
     *
     * @param model
     * @return
     */
    @GetMapping("/add")
    public String addMqttSensorPage(Model model) {
        GetUserResponse userInfo = userAdaptor.getUser().getBody();
        if (userInfo == null) {
            return "redirect:/login";
        }

        List<GetMqttSensorResponse> supportedSensorList = mqttSensorAdaptor.getSupportedMqttSensors().getBody();
        model.addAttribute("supportedSensorList", supportedSensorList);

        List<PlaceDto> placeList = placeAdaptor.getPlacesByOrganizationId(userInfo.getOrganization().getId()).getBody();
        model.addAttribute("placeList", placeList);
        return "sensor/mqtt-add-form";
    }

    /**
     * Mqtt 센서 등록 요청
     *
     * @param addMqttSensorRequest
     * @return
     */
    @PostMapping
    public String addMqttSensor(@ModelAttribute MqttSensorRequest addMqttSensorRequest) {
        mqttSensorAdaptor.addMqttSensor(addMqttSensorRequest);
        return "redirect:/sensors/mqtt";
    }

    /**
     * Mqtt 센서 상세 조회 및 수정, 토픽 관리 페이지
     *
     * @param sensorId 센서아이디
     * @param model    센서정보, 토픽정보, 조직 장소 목록을 모델에 넣어줍니다
     * @return 센서 상세 페이지
     */
    @GetMapping("/{sensorId}")
    public String mqttSensorDetailsPage(@PathVariable("sensorId") int sensorId, Model model) {
        GetUserResponse userInfo = userAdaptor.getUser().getBody();
        if (userInfo == null) {
            return "redirect:/login";
        }

        List<PlaceDto> placeList = placeAdaptor.getPlacesByOrganizationId(userInfo.getOrganization().getId()).getBody();
        model.addAttribute("placeList", placeList);
        GetMqttSensorResponse mqttSensor = mqttSensorAdaptor.getMqttSensor(sensorId).getBody();
        model.addAttribute("sensorInfo", mqttSensor);
        List<GetTopicResponse> topicList = mqttSensorAdaptor.getTopicsBySensorId(sensorId).getBody();
        model.addAttribute("topicList", topicList);

        return "sensor/sensor-mqtt-detail";
    }

    /**
     * Mqtt 센서 정보 수정
     *
     * @param sensorId 센서아이디
     * @param request  수정 요청 폼 데이터
     * @return 센서 단일 조회 페이지 리다이렉트
     */
    @PutMapping
    public String updateMqttSensor(@RequestParam("id") int sensorId, @ModelAttribute MqttSensorRequest request) {
        mqttSensorAdaptor.updateMqttSensor(sensorId, request);
        return REDIRECT_SENSORS_MQTT + sensorId;
    }

    /**
     * 샌서 삭제
     *
     * @param sensorId 센서아이디
     * @return mqtt 센서 목록 페이지 리다이렉트
     */
    @PostMapping("/delete")
    public String deleteMqttSensor(@RequestParam("id") int sensorId) {
        mqttSensorAdaptor.deleteMqttSensor(sensorId);
        return "redirect:/sensors/mqtt";
    }

    /**
     * 토픽 추가
     *
     * @param sensorId
     * @param topicRequest
     * @return
     */
    @PostMapping("/{sensorId}/topics")
    public String addTopic(@PathVariable("sensorId") int sensorId, @ModelAttribute TopicRequest topicRequest) {
        mqttSensorAdaptor.addTopic(sensorId, topicRequest);
        return REDIRECT_SENSORS_MQTT + sensorId;
    }

    /**
     * Mqtt 센서 토픽 수정
     *
     * @param sensorId 센서아이디
     * @param topicId  토픽아이디
     * @return 센서 상세 페이지
     */
    @PutMapping("/{sensorId}/topics/{topicId}")
    public String updateTopic(@PathVariable("sensorId") int sensorId, @PathVariable("topicId") int topicId,
                              @ModelAttribute TopicRequest request) {
        log.info("update topic request- topic:{}, description:{}", request.getTopic(), request.getDescription());
        mqttSensorAdaptor.updateTopic(sensorId, topicId, request);
        return REDIRECT_SENSORS_MQTT + sensorId;
    }

    /**
     * Mqtt 센서 토픽 삭제
     *
     * @param sensorId
     * @param topicId
     * @return
     */
    @DeleteMapping("/{sensorId}/topics/{topicId}")
    public String deleteTopic(@PathVariable("sensorId") int sensorId, @PathVariable("topicId") int topicId) {
        mqttSensorAdaptor.deleteTopic(sensorId, topicId);
        return REDIRECT_SENSORS_MQTT;
    }
}
