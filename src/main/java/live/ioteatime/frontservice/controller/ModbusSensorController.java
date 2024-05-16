package live.ioteatime.frontservice.controller;

import live.ioteatime.frontservice.adaptor.ModbusSensorAdaptor;
import live.ioteatime.frontservice.adaptor.PlaceAdaptor;
import live.ioteatime.frontservice.adaptor.UserAdaptor;
import live.ioteatime.frontservice.dto.ChannelDto;
import live.ioteatime.frontservice.dto.PlaceDto;
import live.ioteatime.frontservice.dto.request.ModbusSensorRequest;
import live.ioteatime.frontservice.dto.response.GetUserResponse;
import live.ioteatime.frontservice.dto.response.ModbusSensorResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/sensors/modbus")
public class ModbusSensorController {
    public static final String REDIRECT_SENSORS_MODBUS = "redirect:/sensors/modbus/";
    private final UserAdaptor userAdaptor;
    private final ModbusSensorAdaptor modbusSensorAdaptor;
    private final PlaceAdaptor placeAdaptor;

    /**
     * 센서 관리 페이지 기본페이지 (MODBUS 센서 관리 페이지)
     *
     * @param model
     * @return
     */
    @GetMapping
    public String sensorPage(Model model) {
        List<ModbusSensorResponse> modbusSensorInfo = modbusSensorAdaptor.getSensors().getBody();
        model.addAttribute("modbusSensorInfo", modbusSensorInfo);

        return "sensor/sensor-modbus";
    }

    /**
     * MODBUS 센서의 채널들을 확인하는 페이지
     *
     * @param model
     * @param sensorId 센서의 아이디
     * @return
     */
    @GetMapping("/{sensorId}")
    public String getModbusSensorDetail(Model model, @PathVariable("sensorId") int sensorId) {
        GetUserResponse userInfo = userAdaptor.getUser().getBody();
        List<PlaceDto> placeList = placeAdaptor.getPlacesByOrganizationId(userInfo.getOrganization().getId()).getBody();
        model.addAttribute("placeList", placeList);

        //modbus 센서 상세 채널 불러오기
        List<ChannelDto> modbusChannelInfo = modbusSensorAdaptor.getChannels(sensorId).getBody();
        model.addAttribute("modbusChannelInfo", modbusChannelInfo);

        model.addAttribute("sensorId", sensorId);
        return "sensor/sensor-modbus-detail";
    }

    /**
     * 장소에 따른 채널들을 불러옵니다.
     *
     * @param model
     * @param placeId 장소 아이디입니다.
     * @return
     */
    @GetMapping("/channels/{placeId}")
    public String getChannelsByPlace(Model model, @PathVariable("placeId") int placeId) {
        List<ChannelDto> modbusChannelInfo = modbusSensorAdaptor.getChannelsByPlaceId(placeId).getBody();

        model.addAttribute("modbusChannelInfo", modbusChannelInfo);

        return "sensor/sensor-modbus-place-channel";
    }

    /**
     * MODBUS 센서를 추가하는 페이지를 불러옵니다.
     *
     * @param model
     * @return
     */
    @GetMapping("/add")
    public String createModbusSensor(Model model) {
        List<ModbusSensorResponse> supportedModbusSensorInfo = modbusSensorAdaptor.getSupportedModbusSensors().getBody();
        model.addAttribute("supportedModbusSensorInfo", supportedModbusSensorInfo);

        return "sensor/modbus-add-form";
    }

    /**
     * MODBUS 센서의 채널을 추가하는 페이지를 로딩합니다.
     *
     * @param model
     * @param sensorId
     * @return
     */
    @GetMapping("/{sensorId}/add")
    public String createModbusSensorChannel(Model model, @PathVariable("sensorId") int sensorId) {
        GetUserResponse userInfo = userAdaptor.getUser().getBody();
        List<PlaceDto> placeList = placeAdaptor.getPlacesByOrganizationId(userInfo.getOrganization().getId()).getBody();
        model.addAttribute("placeList", placeList);

        model.addAttribute("sensorId", sensorId);
        return "sensor/modbus-channel-add-form";
    }

    /**
     * MODBUS 센서를 추가합니다.
     *
     * @param createModbusRequest 센서의 정보를 담는 리퀘스트입니다.
     * @return
     */
    @PostMapping
    public String createModbusSensor(@ModelAttribute ModbusSensorRequest createModbusRequest) {
        modbusSensorAdaptor.createModbusSensor(createModbusRequest);
        return REDIRECT_SENSORS_MODBUS;
    }

    /**
     * MODBUS 센서 아이디에 해당하는 센서에 채널을 추가합니다.
     *
     * @param sensorId   채널을 추가할 센서 아이디입니다.
     * @param channelDto 추가할 채널의 정보입니다.
     * @return
     */
    @PostMapping("/{sensorId}/channels")
    public String createModbusChannel(@PathVariable("sensorId") int sensorId, @ModelAttribute ChannelDto channelDto) {
        modbusSensorAdaptor.createModbusChannel(sensorId, channelDto);
        return REDIRECT_SENSORS_MODBUS + sensorId;
    }

    /**
     * MODBUS 센서의 정보를 수정합니다.
     *
     * @param sensorId            수정할 센서 아이디입니다.
     * @param updateModbusRequest 수정할 정보가 담겨있는 Request입니다.
     * @return
     */
    @PutMapping("/{sensorId}")
    public String updateModbusSensor(@PathVariable("sensorId") int sensorId, @ModelAttribute ModbusSensorRequest updateModbusRequest) {
        modbusSensorAdaptor.updateModbusSensor(sensorId, updateModbusRequest);
        return REDIRECT_SENSORS_MODBUS;
    }

    /**
     * MODBUS 채널의 장소를 변경합니다.
     *
     * @param channelId
     * @param channelPlace
     * @return
     */
    @PutMapping("/{channelId}/change-place")
    public String changeChannelPlace(@PathVariable("channelId") int channelId, @RequestParam String channelPlace) {
        int sensorId = modbusSensorAdaptor.changeChannelPlace(channelId, channelPlace).getBody();
        return REDIRECT_SENSORS_MODBUS + sensorId;
    }


//    /**
//     * MODBUS의 채널의 이름을 변경합니다.
//     *
//     * @param channelId   변경할 채널의 ID입니다.
//     * @param channelName 변경할 채널의 이름입니다.
//     * @return
//     */
//    @PutMapping("/{channelId}/change-name")
//    public String changeChannelName(@PathVariable("channelId") int channelId, @RequestParam String channelName) {
//        int sensorId = modbusSensorAdaptor.changeChannelName(channelId, channelName).getBody();
//        return REDIRECT_SENSORS_MODBUS + sensorId;
//    }

    /**
     * MODBUS 채널의 Address, Quantity, Function-Code를 변경합니다.
     *
     * @param channelId  변경할 채널의 ID입니다.
     * @param channelDto Address, Quantity, Function-Code 값이 있는 DTO입니다.
     * @return
     */
    @PutMapping("/{channelId}/change-info")
    public String changeChannelInfo(@PathVariable("channelId") int channelId, @ModelAttribute ChannelDto channelDto) {
        int sensorId = modbusSensorAdaptor.changeChannelInfo(channelId, channelDto).getBody();
        return REDIRECT_SENSORS_MODBUS + sensorId;
    }

    /**
     * Modbus 채널의 동작 상태를 변경합니다.
     *
     * @param sensorId 동작 상태를 변경할 센서의 아이디입니다.
     * @return
     */
    @PutMapping("/work/{sensorId}")
    public String changeWork(@PathVariable("sensorId") int sensorId) {
        modbusSensorAdaptor.changeWork(sensorId);
        return REDIRECT_SENSORS_MODBUS;
    }


    @DeleteMapping("/{sensorId}")
    public String deleteSensor(@PathVariable("sensorId") int sensorId, RedirectAttributes redirectAttributes) {
        boolean exist = modbusSensorAdaptor.existChannelCheck(sensorId).getBody();
        if (exist) {
            redirectAttributes.addFlashAttribute("alertMessage", "센서에 소속된 채널이 있어 센서를 삭제할 수 없습니다.");
            return REDIRECT_SENSORS_MODBUS;
        } else {
            modbusSensorAdaptor.deleteSensor(sensorId);
            redirectAttributes.addFlashAttribute("alertMessage", "센서를 삭제했습니다.");
            return REDIRECT_SENSORS_MODBUS;
        }
    }

    /**
     * 아이디에 해당하는 센서의 채널번호에 해당하는 채널을 지우는 컨트롤러입니다.
     *
     * @param sensorId
     * @param channelId
     * @return
     */
    @DeleteMapping("/{sensorId}/channels/{channelId}")
    public String deleteChannel(@PathVariable("sensorId") int sensorId, @PathVariable("channelId") int channelId) {
        modbusSensorAdaptor.deleteChannel(sensorId, channelId);
        return REDIRECT_SENSORS_MODBUS + sensorId;
    }
}
