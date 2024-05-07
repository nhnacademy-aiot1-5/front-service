package live.ioteatime.frontservice.controller;

import live.ioteatime.frontservice.adaptor.*;
import live.ioteatime.frontservice.dto.ChannelDto;
import live.ioteatime.frontservice.dto.PlaceDto;
import live.ioteatime.frontservice.dto.request.ModbusSensorRequest;
import live.ioteatime.frontservice.dto.request.MqttSensorRequest;
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
    private final MqttSensorAdaptor mqttSensorAdaptor;
    private final PlaceAdaptor placeAdaptor;


    /**
     * 센서 관리 페이지 기본페이지
     * @param model
     * @return
     */
    @GetMapping
    public String sensorPage(Model model) {
        //modbus센서 리스트 불러오기
        List<GetModbusSensorResponse> modbusSensorInfo = modbusSensorAdaptor.getSensors().getBody();
        model.addAttribute("modbusSensorInfo", modbusSensorInfo);

        return "/sensor/sensor-modbus";
    }

    /**
     * MODBUS 센서 관리 페이지
     * @param model
     * @return
     */
    @GetMapping("/modbus")
    public String getModbusSensors(Model model) {
        return sensorPage(model);
    }

    /**
     * MODBUS 센서의 채널들을 확인하는 페이지
     * @param model
     * @param sensorId 센서의 아이디
     * @return
     */
    @GetMapping("/modbus/{sensorId}")
    public String getModbusSensorDetail(Model model, @PathVariable("sensorId") int sensorId) {
        //modbus 센서 상세 채널 불러오기
        List<ChannelDto> modbusChannelInfo = modbusSensorAdaptor.getChannels(sensorId).getBody();
        model.addAttribute("modbusChannelInfo", modbusChannelInfo);
        return "/sensor/sensor-modbus-detail";
    }

    @GetMapping("/modbus/add")
    public String createModbusSensor(Model model) {
        List<GetModbusSensorResponse> supportedModbusSensorInfo = modbusSensorAdaptor.getSupportedModbusSensors().getBody();
        model.addAttribute("supportedModbusSensorInfo", supportedModbusSensorInfo);

        return"/sensor/modbus-add-form";
    }

    @PostMapping("/modbus")
    public String createModbusSensor(@ModelAttribute ModbusSensorRequest createModbusRequest){
        modbusSensorAdaptor.createModbusSensor(createModbusRequest);
        return "redirect:/sensors/modbus";
    }


    @PutMapping("/modbus/{channelId}/change-name")
    public String changeChannelName(@PathVariable("channelId") int channelId, @RequestParam String channelName){
        int sensorId = modbusSensorAdaptor.changeChannelName(channelId, channelName).getBody();
        return "redirect:/sensors/modbus/" + sensorId;
    }

    @PutMapping("/modbus/work/{sensorId}")
    public String changeWork(@PathVariable("sensorId") int sensorId){
        modbusSensorAdaptor.changeWork(sensorId);
        return "redirect:/sensors/modbus";
    }

    /**
     * Mqtt 센서 관리 페이지
     * @param model
     * @return
     */
    @GetMapping("/mqtt")
    public String getMqttSensorPage(Model model) {
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
        GetUserResponse userInfo = userAdaptor.getUser().getBody();

        List<GetMqttSensorResponse> supportedSensorList = mqttSensorAdaptor.getSupportedMqttSensors().getBody();
        model.addAttribute("supportedSensorList", supportedSensorList);

        List<PlaceDto> placeList = placeAdaptor.getPlaces(userInfo.getOrganization().getId()).getBody();
        model.addAttribute("placeList", placeList);

        return "/sensor/mqtt-add-form";
    }

    /**
     * Mqtt 센서 등록 요청
     * @param addMqttSensorRequest
     * @return
     */
    @PostMapping("/mqtt")
    public String addMqttSensor(@ModelAttribute MqttSensorRequest addMqttSensorRequest){
        mqttSensorAdaptor.addMqttSensor(addMqttSensorRequest);
        return "redirect:/sensors/mqtt";
    }

    /**
     * Mqtt 센서 상세 조회 및 수정, 토픽 관리 페이지
     * @param sensorId 센서아이디
     * @param model 센서정보, 토픽정보, 조직 장소 목록을 모델에 넣어줍니다
     * @return 센서 상세 페이지
     */
    @GetMapping("/mqtt/{sensorId}")
    public String mqttSensorDetailsPage(@PathVariable("sensorId") int sensorId, Model model){
        //사이드바 전용
        GetUserResponse userInfo = userAdaptor.getUser().getBody();

        List<PlaceDto> placeList = placeAdaptor.getPlaces(userInfo.getOrganization().getId()).getBody();
        model.addAttribute("placeList", placeList);
        GetMqttSensorResponse mqttSensor = mqttSensorAdaptor.getMqttSensor(sensorId).getBody();
        model.addAttribute("sensorInfo", mqttSensor);
        List<GetTopicResponse> topicList = mqttSensorAdaptor.getTopicsBySensorId(sensorId).getBody();
        model.addAttribute("topicList", topicList);

        return "sensor/sensor-mqtt-detail";
    }

    /**
     * Mqtt 센서 정보 수정
     * @param sensorId 센서아이디
     * @param request 수정 요청 폼 데이터
     * @return 센서 단일 조회 페이지 리다이렉트
     */
    @PutMapping("/mqtt/{sensorId}")
    public String updateMqttSensor(@PathVariable("sensorId") int sensorId, @ModelAttribute MqttSensorRequest request){
        mqttSensorAdaptor.updateMqttSensor(sensorId, request);
        return "redirect:/sensors/mqtt/" + sensorId;
    }

    /**
     * 샌서 삭제
     * @param sensorId 센서아이디
     * @return mqtt 센서 목록 페이지 리다이렉트
     */
    @DeleteMapping("/mqtt/{sensorId}")
    public String deleteMqttSensor(@PathVariable("sensorId") int sensorId){
        mqttSensorAdaptor.deleteMqttSensor(sensorId);
        return "redirect:/sensors/mqtt";
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
    @PutMapping("/mqtt/{sensorId}/topics/{topicId}")
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
        return "redirect:/sensors/mqtt/" + sensorId;
    }

}
