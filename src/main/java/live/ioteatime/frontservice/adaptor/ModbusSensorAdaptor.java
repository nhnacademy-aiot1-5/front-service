package live.ioteatime.frontservice.adaptor;

import live.ioteatime.frontservice.dto.response.GetModbusSensorResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@FeignClient(value = "gateway-service", contextId = "modbus-sensor-adaptor")
public interface ModbusSensorAdaptor {
    @GetMapping("api/sensors/modbus/list")
    ResponseEntity<List<GetModbusSensorResponse>> getSensors();

    @PutMapping("/api/sensor/modbus/work")
    ResponseEntity<String> changeWork(@RequestParam Integer id);
}
