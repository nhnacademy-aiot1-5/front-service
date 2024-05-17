package live.ioteatime.frontservice.adaptor;

import live.ioteatime.frontservice.dto.ChannelDto;
import live.ioteatime.frontservice.dto.request.ModbusSensorRequest;
import live.ioteatime.frontservice.dto.response.ModbusSensorResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Modbus 센서에 대한 기능을 수행하는 Adaptor 클래스입니다.
 */
@FeignClient(value = "gateway-service", contextId = "modbus-sensor-adaptor")
public interface ModbusSensorAdaptor {
    /**
     * 채널 리스트를 가져옵니다.
     * @param sensorId 체널리스트 조회시 필요한 센서의 아이디입니다.
     * @return (id, 채널 이름, 주소, 타입, fc, 센서 id, 장소 id, 장소 이름, ModbusSensorResponse, PlaceDto) 리스트를 반환합니다.
     *          ModbusSensorResponse - (id, 센서 이름, 모델 이름, ip, port, 채널 개수, Alive)
     *                                  Alive - (UP, DOWN)
     *          PlaceDto - (id, 장소 이름, 조직 id, OrganizationResponse)
     *                      OrganizationResponse - (id, 이름, 설정 요금, 조직 코드)
     */
    @GetMapping("/api/sensors/modbus/{sensorId}/channels")
    ResponseEntity<List<ChannelDto>> getChannels(@PathVariable("sensorId") int sensorId);

    /**
     * Modbus 센서 리스트를 가져옵니다.
     * @return (id, 센서 이름, 모델 이름, ip, port, 채널 수, Alive)를 리턴합니다.
     *          Alive - UP, DOWN
     */
    @GetMapping("/api/sensors/modbus/list")
    ResponseEntity<List<ModbusSensorResponse>> getSensors();

    /**
     * 장소 id로 채널 리스트를 검색합니다.
     * @param placeId 검색시 사용되는 place id입니다.
     * @return (id, 채널 이름, 주소, 타입, fc, 센서 id, 장소 id, 장소 이름, ModbusSensorResponse, PlaceDto) 리스트를 반환합니다.
     *          ModbusSensorResponse - (id, 센서 이름, 모델 이름, ip, port, 채널 개수, Alive)
     *                                  Alive - (UP, DOWN)
     *          PlaceDto - (id, 장소 이름, 조직 id, OrganizationResponse)
     *                      OrganizationResponse - (id, 이름, 설정 요금, 조직 코드)
     */
    @GetMapping("/api/sensors/modbus/channels/{placeId}")
    ResponseEntity<List<ChannelDto>> getChannelsByPlaceId(@PathVariable("placeId") int placeId);

    /**
     * 지원하는 Modbus 센서 리스트를 가져옵니다.
     * @return (id, 센서 이름, 모델 이름, ip, port, 채널 수, Alive)를 리턴합니다.
     *          Alive - (UP, DOWN)
     */
    @GetMapping("/api/sensors/modbus/supported")
    ResponseEntity<List<ModbusSensorResponse>> getSupportedModbusSensors();

    /**
     * 센서에 채널이 존재하는지 여부를 체크합니다.
     * @param sensorId 검색할 센서의 id 입니다.
     * @return 존재 여부를 Boolean 값으로 리턴합니다.
     */
    @GetMapping("/api/sensors/modbus/{sensorId}/exist-channels")
    ResponseEntity<Boolean> existChannelCheck(@PathVariable("sensorId")int sensorId);

    /**
     * Modbus 센서를 등록합니다.
     * @param modbusSensorRequest (모델 이름, 센서 이름, ip, port, 채널 개수, 상세) 등록할 Modbus 센서 정보를 받아옵니다.
     * @return 등록 결과를 반환합니다.
     */
    @PostMapping("api/sensors/modbus")
    ResponseEntity<String> createModbusSensor(ModbusSensorRequest modbusSensorRequest);

    /**
     * 해당 센서에 Modbus 채널을 등록합니다.
     * @param sensorId 채널을 등록할 센서의 id입니다.
     * @param channelDto 등록할 채널에 대한 정보입니다. (id, 채널 이름, 주소, 타입, fc, 센서 id, 장소 id, 장소 이름, ModbusSensorResponse, PlaceDto)
     *                   ModbusSensorResponse - (id, 센서 이름, 모델 이름, ip, port, 채널 개수, Alive)
     *                                          Alive - (UP, DOWN)
     *                  PlaceDto - (id, 장소 이름, 조직 id, OrganizationResponse)
     *                              OrganizationResponse - (id, 이름, 설정 요금, 조직 코드)
     * @return 채널 등록 결과를 반환합니다.
     */
    @PostMapping("/api/sensors/modbus/{sensorId}/channels")
    ResponseEntity<Integer> createModbusChannel(@PathVariable("sensorId") int sensorId, @RequestBody ChannelDto channelDto);

    /**
     * Modbus 센서를 업데이트 합니다.
     * @param sensorId 업데이트 할 센서의 id 입니다.
     * @param modbusSensorRequest 업데이트 요청입니다. (id, 센서 이름, 모델 이름, ip, port, 채널 개수, Alive)
     *                            Alive - (UP, DOWN)
     * @return 업데이트 결과를 반환합니다.
     */
    @PutMapping("/api/sensors/modbus/{sensorId}")
    ResponseEntity<String> updateModbusSensor(@PathVariable("sensorId") int sensorId, @RequestBody ModbusSensorRequest modbusSensorRequest);

    /**
     * 채널이 등록된 장소를 변경합니다.
     * @param channelId 장소를 변경할 채널의 id 입니다.
     * @param channelPlace 변경하고 싶은 장소 입니다.
     * @return 변경 결과를 반환합니다.
     */
    @PutMapping("/api/sensors/modbus/{channelId}/change-place")
    ResponseEntity<Integer> changeChannelPlace(@PathVariable("channelId") int channelId, @RequestParam String channelPlace);

    /**
     * Work를 변경합니다.
     * 센서 동작 상태가 UP이면 DOWN으로, DOWN이면 UP 상태로 변경합니다.
     * @param sensorId 변경할 센서 id를 받아옵니다.
     * @return 변경 결과를 반환합니다.
     */
    @PutMapping("/api/sensors/modbus/health")
    ResponseEntity<String> changeHealth(@RequestParam int sensorId);

    /**
     * 채널 이름을 변경합니다.
     * @param channelId 이름을 변경 하고 싶은 채널의 id를 받아 옵니다.
     * @param channelName 변경할 채널의 이름을 받아 옵니다.
     * @return 변경 결과를 반환합니다.
     */
    @PutMapping("/api/sensors/modbus/{channelId}/change-name")
    ResponseEntity<Integer> changeChannelName(@PathVariable("channelId") int channelId, @RequestParam String channelName);

    /**
     * 채널 정보를 수정합니다.
     * @param channelId 수정하고싶은 채널의 id 입니다.
     * @param channelDto 수정할 채널 정보입니다. (id, 채널 이름, 주소, 타입, fc, 센서 id, 장소 id, 장소 이름, ModbusSensorResponse, PlaceDto)
     *                         ModbusSensorResponse - (id, 센서 이름, 모델 이름, ip, port, 채널 개수, Alive)
     *                                                Alive - (UP, DOWN)
     *                        PlaceDto - (id, 장소 이름, 조직 id, OrganizationResponse)
     *                                    OrganizationResponse - (id, 이름, 설정 요금, 조직 코드)
     * @return 수정 결과를 반환합니다.
     */
    @PutMapping("/api/sensors/modbus/{channelId}/change-info")
    ResponseEntity<Integer> changeChannelInfo(@PathVariable("channelId") int channelId, @RequestBody ChannelDto channelDto);

    /**
     * 등록된 센서를 삭제합니다.
     * @param sensorId 삭제 할 센서의 id 입니다.
     * @return 삭제 결과를 반환합니다.
     */
    @DeleteMapping("/api/sensors/modbus/{sensorId}")
    ResponseEntity<String> deleteSensor(@PathVariable("sensorId") int sensorId);

    /**
     * 등록된 채널을 삭제합니다.
     * @param sensorId 삭제할 채널이 속해있는 센서의 id 입니다.
     * @param channelId 삭제할 채널의 id 입니다.
     * @return 삭제 결과를 반환합니다.
     */
    @DeleteMapping("api/sensors/modbus/{sensorId}/channels/{channelId}")
    ResponseEntity<String> deleteChannel(@PathVariable("sensorId") int sensorId, @PathVariable("channelId") int channelId);
}
