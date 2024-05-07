package live.ioteatime.frontservice.adaptor;

import live.ioteatime.frontservice.dto.ChannelDto;
import live.ioteatime.frontservice.dto.request.ModbusSensorRequest;
import live.ioteatime.frontservice.dto.response.GetModbusSensorResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@FeignClient(value = "gateway-service", contextId = "modbus-sensor-adaptor")
public interface ModbusSensorAdaptor {
    @GetMapping("/api/sensors/modbus/{sensorId}/channels")
    ResponseEntity<List<ChannelDto>> getChannels(@PathVariable("sensorId") int sensorId);

    @GetMapping("/api/sensors/modbus/list")
    ResponseEntity<List<GetModbusSensorResponse>> getSensors();

    @GetMapping("api/sensors/modbus/supported")
    ResponseEntity<List<GetModbusSensorResponse>> getSupportedModbusSensors();

    @PostMapping("api/sensors/modbus")
    ResponseEntity<String> createModbusSensor(ModbusSensorRequest modbusSensorRequest);

    @PutMapping("/api/sensors/modbus/health")
    ResponseEntity<String> changeWork(@RequestParam int sensorId);

    @PutMapping("/api/sensors/modbus/{channelId}/change-name")
    ResponseEntity<Integer> changeChannelName(@PathVariable("channelId") int channelId, @RequestParam String channelName);
}
