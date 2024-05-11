package live.ioteatime.frontservice.adaptor;

import live.ioteatime.frontservice.dto.ChannelDto;
import live.ioteatime.frontservice.dto.request.ModbusSensorRequest;
import live.ioteatime.frontservice.dto.response.ModbusSensorResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@FeignClient(value = "gateway-service", contextId = "modbus-sensor-adaptor")
public interface ModbusSensorAdaptor {
    @GetMapping("/api/sensors/modbus/{sensorId}/channels")
    ResponseEntity<List<ChannelDto>> getChannels(@PathVariable("sensorId") int sensorId);

    @GetMapping("/api/sensors/modbus/list")
    ResponseEntity<List<ModbusSensorResponse>> getSensors();

    @GetMapping("/api/sensors/modbus/channels/{placeId}")
    ResponseEntity<List<ChannelDto>> getChannelsByPlaceId(@PathVariable("placeId") int placeId);

    @GetMapping("/api/sensors/modbus/supported")
    ResponseEntity<List<ModbusSensorResponse>> getSupportedModbusSensors();

    @GetMapping("/api/sensors/modbus/{sensorId}/exist-channels")
    ResponseEntity<Boolean> existChannelCheck(@PathVariable("sensorId")int sensorId);

    @PostMapping("/api/sensors/modbus")
    ResponseEntity<String> createModbusSensor(ModbusSensorRequest modbusSensorRequest);

    @PostMapping("/api/sensors/modbus/{sensorId}/channels")
    ResponseEntity<Integer> createModbusChannel(@PathVariable("sensorId") int sensorId, @RequestBody ChannelDto channelDto);

    @PutMapping("/api/sensors/modbus/{sensorId}")
    ResponseEntity<String> updateModbusSensor(@PathVariable("sensorId") int sensorId, @RequestBody ModbusSensorRequest modbusSensorRequest);

    @PutMapping("/api/sensors/modbus/{channelId}/change-place")
    ResponseEntity<Integer> changeChannelPlace(@PathVariable("channelId") int channelId, @RequestParam String channelPlace);

    @PutMapping("/api/sensors/modbus/{channelId}/change-name")
    ResponseEntity<Integer> changeChannelName(@PathVariable("channelId") int channelId, @RequestParam String channelName);

    @PutMapping("/api/sensors/modbus/{channelId}/change-info")
    ResponseEntity<Integer> changeChannelInfo(@PathVariable("channelId") int channelId, @RequestBody ChannelDto channelDto);

    @PutMapping("/api/sensors/modbus/health")
    ResponseEntity<String> changeWork(@RequestParam int sensorId);

    @DeleteMapping("/api/sensors/modbus/{sensorId}")
    ResponseEntity<String> deleteSensor(@PathVariable("sensorId") int sensorId);

    @DeleteMapping("api/sensors/modbus/{sensorId}/channels/{channelId}")
    ResponseEntity<String> deleteChannel(@PathVariable("sensorId") int sensorId, @PathVariable("channelId") int channelId);
}
