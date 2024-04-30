package live.ioteatime.frontservice.controller;

import live.ioteatime.frontservice.adaptor.ModbusSensorAdaptor;
import live.ioteatime.frontservice.adaptor.MqttSensorAdaptor;
import live.ioteatime.frontservice.adaptor.UserAdaptor;
import live.ioteatime.frontservice.dto.request.AddMqttSensorRequest;
import live.ioteatime.frontservice.dto.response.GetModbusSensorResponse;
import live.ioteatime.frontservice.dto.response.GetMqttSensorResponse;
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
@RequestMapping("/sensor")
public class SensorController {
    private final UserAdaptor userAdaptor;
    private final ModbusSensorAdaptor modbusSensorAdaptor;
    private final ChannelAdaptor channelAdaptor;
    private final MqttSensorAdaptor mqttSensorAdaptor;


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

        return "sensor/mqtt-add-form";
    }


    @PostMapping("/mqtt")
    public String addMqttSensor(@ModelAttribute AddMqttSensorRequest addMqttSensorRequest){

        mqttSensorAdaptor.addMqttSensor(addMqttSensorRequest);
        return null;
    }
}
