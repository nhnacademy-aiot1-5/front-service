package live.ioteatime.frontservice.controller;

import live.ioteatime.frontservice.adaptor.ModbusSensorAdaptor;
import live.ioteatime.frontservice.adaptor.MqttSensorAdaptor;
import live.ioteatime.frontservice.adaptor.UserAdaptor;
import live.ioteatime.frontservice.dto.response.GetModbusSensorResponse;
import live.ioteatime.frontservice.dto.response.GetUserResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/sensor")
public class SensorController {
    private final UserAdaptor userAdaptor;
    private final ModbusSensorAdaptor modbusSensorAdaptor;
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
    public String getModbusSensorDetail(Model model, @PathVariable("sensorId") String sensorId) {
        //사이드바 전용
        GetUserResponse userInfo = userAdaptor.getUser().getBody();
        model.addAttribute("userInfo", userInfo);
        log.info("userId: {}, userName: {}, userRole={}", userInfo.getId(), userInfo.getName(), userInfo.getRole());

        return "/sensor/sensor-modbus-detail";
    }

    @PutMapping("/modbus/work/{sensorId}")
    public String changeWork(@PathVariable("sensorId") int sensorId){
        modbusSensorAdaptor.changeWork(sensorId);
        return "redirect:/sensor/modbus";
    }
}
