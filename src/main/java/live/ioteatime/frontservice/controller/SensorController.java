package live.ioteatime.frontservice.controller;

import live.ioteatime.frontservice.adaptor.*;
import live.ioteatime.frontservice.dto.request.AddMqttSensorRequest;
import live.ioteatime.frontservice.dto.request.TopicRequest;
import live.ioteatime.frontservice.dto.response.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/sensors")
public class SensorController {
    private final UserAdaptor userAdaptor;
    private final ModbusSensorAdaptor modbusSensorAdaptor;
    private final ChannelAdaptor channelAdaptor;
    private final MqttSensorAdaptor mqttSensorAdaptor;
    private final PlaceAdaptor placeAdaptor;


    @GetMapping
    public String sensorPage(Model model) {
        //사이드바 전용
        GetUserResponse userInfo = userAdaptor.getUser().getBody();
        model.addAttribute("userInfo", userInfo);
        log.info("userId: {}, userName: {}, userRole={}", userInfo.getId(), userInfo.getName(), userInfo.getRole());

        //modbus센서 리스트 불러오기
        List<GetModbusSensorResponse> modbusSensorInfo = modbusSensorAdaptor.getSensors().getBody();
        model.addAttribute("modbusSensorInfo", modbusSensorInfo);

        return "/sensor/sensor-modbus";
    }

    @GetMapping("/modbus")
    public String getModbusSensors(Model model) {
        return sensorPage(model);
    }

    @GetMapping("/modbus/{sensorId}")
    public String getModbusSensorDetail(Model model, @PathVariable("sensorId") int sensorId) {
        //사이드바 전용
        GetUserResponse userInfo = userAdaptor.getUser().getBody();
        model.addAttribute("userInfo", userInfo);
        log.info("userId: {}, userName: {}, userRole={}", userInfo.getId(), userInfo.getName(), userInfo.getRole());

        //modbus 센서 상세 채널 불러오기
        List<GetModbusSensorChannelResponse> modbusChannelInfo = channelAdaptor.getChannels(sensorId).getBody();
        model.addAttribute("modbusChannelInfo", modbusChannelInfo);
        return "/sensor/sensor-modbus-detail";
    }

    @PutMapping("/modbus/work/{sensorId}")
    public String changeWork(@PathVariable("sensorId") int sensorId){
        modbusSensorAdaptor.changeWork(sensorId);
        return "redirect:/sensor/modbus";
    }

    /**
     * Mqtt 센서 관리 페이지
     * @param model
     * @return
     */
    @GetMapping("/mqtt")
    public String getMqttSensorPage(Model model) {
        //사이드바 전용
        GetUserResponse userInfo = userAdaptor.getUser().getBody();
        model.addAttribute("userInfo", userInfo);
        log.info("userId: {}, userName: {}, userRole={}", userInfo.getId(), userInfo.getName(), userInfo.getRole());

        List<GetMqttSensorResponse> mqttSensorInfo = mqttSensorAdaptor.getUserOrganizationMqttSensors().getBody();
        model.addAttribute("mqttSensorInfo", mqttSensorInfo);

        return "/sensor/sensor-mqtt";
    }

    /**
     * Mqtt 센서 등록 페이지
     * @param model
     * @return
     */
    @GetMapping("/mqtt/add")
    public String addMqttSensorPage(Model model) {
        //사이드바 전용
        GetUserResponse userInfo = userAdaptor.getUser().getBody();
        model.addAttribute("userInfo", userInfo);
        log.info("userId: {}, userName: {}, userRole={}", userInfo.getId(), userInfo.getName(), userInfo.getRole());

        List<GetMqttSensorResponse> supportedSensorList = mqttSensorAdaptor.getSupportedMqttSensors().getBody();
        model.addAttribute("supportedSensorList", supportedSensorList);

        List<GetPlaceResponse> placeList = placeAdaptor.getPlaces(userInfo.getOrganization().getId()).getBody();
        model.addAttribute("placeList", placeList);

        return "/sensor/mqtt-add-form";
    }

    /**
     * Mqtt 센서 등록 요청
     * @param addMqttSensorRequest
     * @return
     */
    @PostMapping("/mqtt")
    public String addMqttSensor(@ModelAttribute AddMqttSensorRequest addMqttSensorRequest){
        mqttSensorAdaptor.addMqttSensor(addMqttSensorRequest);
        return "redirect:/sensors/mqtt";
    }

    /**
     * Mqtt 센서 상세 조회 및 수정, 토픽 관리 페이지
     * @param sensorId
     * @param model
     * @return
     */
    @GetMapping("/mqtt/{sensorId}")
    public String mqttSensorDetailsPage(@PathVariable("sensorId") int sensorId, Model model){
        //사이드바 전용
        GetUserResponse userInfo = userAdaptor.getUser().getBody();
        model.addAttribute("userInfo", userInfo);
        log.info("userId: {}, userName: {}, userRole={}", userInfo.getId(), userInfo.getName(), userInfo.getRole());

        List<GetPlaceResponse> placeList = placeAdaptor.getPlaces(userInfo.getOrganization().getId()).getBody();
        model.addAttribute("placeList", placeList);
        GetMqttSensorResponse mqttSensor = mqttSensorAdaptor.getMqttSensor(sensorId).getBody();
        model.addAttribute("sensorInfo", mqttSensor);
        List<GetTopicResponse> topicList = mqttSensorAdaptor.getTopicsBySensorId(sensorId).getBody();
        model.addAttribute("topicList", topicList);

        return "sensor/sensor-mqtt-detail";
    }

    /**
     * 토픽 추가
     * @param sensorId
     * @param topicRequest
     * @return
     */
    @PostMapping("/mqtt/{sensorId}/topics")
    public String addTopic(@PathVariable("sensorId") int sensorId, @ModelAttribute TopicRequest topicRequest) {
        mqttSensorAdaptor.addTopic(sensorId, topicRequest);
        return "redirect:/sensors/mqtt/" + sensorId;
    }


    /**
     * Mqtt 센서 토픽 수정
     * @param sensorId 센서아이디
     * @param topicId 토픽아이디
     * @return 센서 상세 페이지
     */
    @PutMapping("/mqtt/{sensorId}/topics/{topicId}/update")
    public String updateTopic(@PathVariable("sensorId") int sensorId, @PathVariable("topicId") int topicId,
                              @ModelAttribute TopicRequest request){
        log.info("update topic request- topic:{}, description:{}", request.getTopic(), request.getDescription());
        mqttSensorAdaptor.updateTopic(sensorId, topicId, request);
        return "redirect:/sensors/mqtt/" + sensorId;
    }

    /**
     * Mqtt 센서 토픽 삭제
     * @param sensorId
     * @param topicId
     * @return
     */
    @DeleteMapping("/mqtt/{sensorId}/topics/{topicId}")
    public String deleteTopic(@PathVariable("sensorId") int sensorId, @PathVariable("topicId") int topicId){
        mqttSensorAdaptor.deleteTopic(sensorId,topicId);
        return "redirect:/sensors/mqtt";
    }

}
